/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * JBoss, Home of Professional Open Source
 * Copyright 2012 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.hibernate.search.infinispan.impl.indexmanager;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Lock;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.hibernate.search.backend.BackendFactory;
import org.hibernate.search.backend.IndexingMonitor;
import org.hibernate.search.backend.LuceneWork;
import org.hibernate.search.backend.spi.BackendQueueProcessor;
import org.hibernate.search.indexes.impl.DirectoryBasedIndexManager;
import org.hibernate.search.infinispan.logging.impl.Log;
import org.hibernate.search.spi.WorkerBuildContext;
import org.hibernate.search.store.DirectoryProvider;
import org.hibernate.search.util.logging.impl.LoggerFactory;
import org.infinispan.AdvancedCache;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;
import org.infinispan.remoting.transport.Address;
import org.infinispan.remoting.transport.jgroups.SuspectException;

/**
 * @author Sanne Grinovero <sanne@hibernate.org> (C) 2012 Red Hat Inc.
 */
@Listener
public class RoutingArbiter implements BackendQueueProcessor {

	private static final Log log = LoggerFactory.make( Log.class );

	private final String indexName;
	private final InfinispanIndexManager infinispanIndexManager;
	private final Address localAddress;
	private final AdvancedCache channeledCache;
	private final int maxRetryAttempts;

	private WorkerBuildContext context;

	private volatile BackendQueueProcessor currentBackend;
	private volatile boolean localProcessor;

	//Guarded by synchronized on this
	private Address lastSeenMasterNode;
	private Properties cfg;

	public RoutingArbiter(InfinispanIndexManager infinispanIndexManager,
			Properties cfg, String indexName, Cache channeledCache, int maxRetryAttempts) {
		this.infinispanIndexManager = infinispanIndexManager;
		this.indexName = indexName;
		this.maxRetryAttempts = maxRetryAttempts;
		this.channeledCache = channeledCache.getAdvancedCache();
		EmbeddedCacheManager cacheManager = infinispanIndexManager.getCacheManager();
		this.localAddress = cacheManager.getTransport().getAddress();
	}

	@ViewChanged
	public void onViewChange(ViewChangedEvent event) {
		if ( ! localProcessor ) {
			//See if we need to failover to ourselves
			try {
				failoverCheck( event.getNewMembers() );
			}
			catch ( SecurityException e ) {
				//FIXME implement me
			}
			catch ( IllegalStateException e ) {
				//FIXME implement me
			}
			catch ( NotSupportedException e ) {
				//FIXME implement me
			}
			catch ( SystemException e ) {
				//FIXME implement me
			}
			catch ( RollbackException e ) {
				//FIXME implement me
			}
			catch ( HeuristicMixedException e ) {
				//FIXME implement me
			}
			catch ( HeuristicRollbackException e ) {
				//FIXME implement me
			}
		}
	}

	private synchronized void failoverCheck(List<Address> newMembers) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		if ( ! newMembers.contains( lastSeenMasterNode ) ) {
			log.warn( "Previous master " + lastSeenMasterNode + " is dead: electing new master among " + newMembers );
			//failover needed
			final OwnerDefiningKey key = new OwnerDefiningKey( indexName );
			final boolean selfElected;
			channeledCache.getTransactionManager().begin();
			try {
				channeledCache.lock( key );
				selfElected = channeledCache.replace( key, lastSeenMasterNode, localAddress );
			}
			finally {
				channeledCache.getTransactionManager().commit();
			}
			if ( selfElected ) {
				log.warn( "Self elected node " + localAddress + " !");
				makeLocalMaster();
			}
			else {
				final Address registeredMaster = (Address) channeledCache.get( key );
				log.warn( "Other node elected: " + registeredMaster + " !");
				makeMaster( registeredMaster );
			}
		}
	}

	private synchronized void makeMaster(Address masterNode) {
		BackendQueueProcessor old = currentBackend;
		this.lastSeenMasterNode = masterNode;
		InfinispanCommandsBackend remoteMaster = new InfinispanCommandsBackend( masterNode, channeledCache );
		remoteMaster.initialize( cfg, context, infinispanIndexManager );
		localProcessor = false;
		currentBackend = remoteMaster;
		if ( old != null ) old.close();
	}

	private synchronized void makeLocalMaster() {
		forceIndexUnlock(); // !
		BackendQueueProcessor old = currentBackend;
		BackendQueueProcessor localMaster = BackendFactory.createBackend( infinispanIndexManager, context, cfg );
		localMaster.initialize( cfg, context, infinispanIndexManager );
		localProcessor = true;
		currentBackend = localMaster;
		if ( old != null ) old.close();
	}

	private void forceIndexUnlock() {
		final DirectoryProvider directoryProvider = infinispanIndexManager.getDirectoryProvider();
		final Directory directory = directoryProvider.getDirectory();
		try {
			if ( IndexWriter.isLocked( directory ) ) {
				log.forcingReleaseIndexWriterLock();
				IndexWriter.unlock( directory );
			}
		}
		catch ( IOException e ) {
			log.unexpectedErrorInLuceneBackend( e );
		}
	}

	@Override
	public synchronized void initialize(Properties cfg, WorkerBuildContext context, DirectoryBasedIndexManager indexManager) {
		this.cfg = cfg;
		this.context = context;//would be better if we could avoid holding on the startup context but we need it
		EmbeddedCacheManager cacheManager = infinispanIndexManager.getCacheManager();
		cacheManager.addListener( this );
		Address masterNode = defineMasterNode();
		if ( localAddress.equals( masterNode ) ) {
			makeLocalMaster();
		}
		else {
			makeMaster( masterNode );
		}
	}

	private Address defineMasterNode() {
		OwnerDefiningKey key = new OwnerDefiningKey( indexName );
		Object previous = channeledCache.putIfAbsent( key, localAddress );
		if ( previous == null ) {
			return localAddress;
		}
		else {
			return (Address) previous;
		}
	}

	@Override
	public synchronized void close() {
		if ( currentBackend != null ) {
			currentBackend.close();
		}
	}

	@Override
	public void applyWork(final List<LuceneWork> workList, final IndexingMonitor monitor) {
		applyWorkRetry(workList, monitor, 0);
	}

	private void applyWorkRetry(final List<LuceneWork> workList, final IndexingMonitor monitor, final int retry) {
		final BackendQueueProcessor backend = currentBackend;
		try {
			backend.applyWork( workList, monitor );
		}
		catch (SuspectException se) {
			if ( retry == maxRetryAttempts ) {
				throw log.tooManySuspectExceptionsOnWorkRemoting( se, maxRetryAttempts );
			}
			else {
				try {
					Thread.sleep( 200 );
				}
				catch ( InterruptedException e ) {
					Thread.currentThread().interrupt();
					throw log.interruptedDuringRemoting();
				}
				applyWorkRetry( workList, monitor, retry + 1 );
			}
		}
	}

	@Override
	public void applyStreamWork(LuceneWork singleOperation, IndexingMonitor monitor) {
		currentBackend.applyStreamWork( singleOperation, monitor );
	}

	@Override
	public Lock getExclusiveWriteLock() {
		return currentBackend.getExclusiveWriteLock();
	}

	@Override
	public void indexMappingChanged() {
		currentBackend.indexMappingChanged();
	}

}
