/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009 Red Hat Inc. and/or its affiliates and other
 * contributors as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.search.util.logging;

import static org.jboss.logging.Logger.Level.ERROR;
import static org.jboss.logging.Logger.Level.INFO;
import static org.jboss.logging.Logger.Level.WARN;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Cause;
import org.jboss.logging.LogMessage;
import org.jboss.logging.Message;
import org.jboss.logging.MessageLogger;

/**
 * Hibernate Search's log abstraction layer on top of JBoss Logging.
 * <p/>
 * It contains explicit methods for all INFO or above levels so that they can
 * be internationalized. For the core module, message ids ranging from 00001
 * to 2000 inclusively have been reserved.
 * <p/>
 * <code> Log log = LogFactory.getLog( getClass() ); </code> The above will get
 * you an instance of <tt>Log</tt>, which can be used to generate log messages
 * either via JBoss Logging which then can delegate to Log4J (if the libraries
 * are present) or (if not) the built-in JDK logger.
 * <p/>
 * In addition to the 6 log levels available, this framework also supports
 * parameter interpolation, similar to the JDKs {@link String#format(String, Object...)}
 * method.  What this means is, that the following block:
 * <code> if (log.isTraceEnabled()) { log.trace("This is a message " + message + " and some other value is " + value); }
 * </code>
 * <p/>
 * ... could be replaced with ...
 * <p/>
 * <code> if (log.isTraceEnabled()) log.tracef("This is a message %s and some other value is %s", message, value);
 * </code>
 * <p/>
 * This greatly enhances code readability.
 * <p/>
 * If you are passing a <tt>Throwable</tt>, note that this should be passed in
 * <i>before</i> the vararg parameter list.
 * <p/>
 *
 * @author Davide D'Alto
 * @since 4.0
 */
@MessageLogger(projectCode = "HSEARCH")
public interface Log extends BasicLogger
{

   @LogMessage(level = WARN)
   @Message(value = "initialized \"blackhole\" backend. Index changes will be prepared but discarded!", id = 1)
   void initializedBlackholeBackend();

   @LogMessage(level = INFO)
   @Message(value = "closed \"blackhole\" backend.", id = 2)
   void closedBlackholeBackend();

   @LogMessage(level = WARN)
   @Message(value = "update DirectoryProviders \"blackhole\" backend. Index changes will be prepared but discarded!", id = 3)
   void updatedDirectoryProviders();

   @LogMessage(level = ERROR)
   @Message(value = "Exception attempting to instantiate Similarity '%s' set for %s", id = 4)
   void similarityInstantiationException(String similarityName, String beanXClassName);

   @LogMessage(level = INFO)
   @Message(value = "Starting JGroups Channel", id = 5)
   void jGroupsStartingChannel();

   @LogMessage(level = INFO)
   @Message(value = "Connected to cluster [ %s ]. The node address is $s", id = 6)
   void jGroupsConnectedToCluster(String clusterName, Object address);

   @LogMessage(level = WARN)
   @Message(value = "FLUSH is not present in your JGroups stack!  FLUSH is needed to ensure messages are not dropped while new nodes join the cluster.  Will proceed, but inconsistencies may arise!", id = 7)
   void jGroupsFlushNotPresentInStack();

   @LogMessage(level = ERROR)
   @Message(value = "Error while trying to create a channel using config files: %s", id = 8)
   void jGroupsChannelCreationUsingFileError(String configuration);

   @LogMessage(level = ERROR)
   @Message(value = "Error while trying to create a channel using config XML: %s", id = 9)
   void jGroupsChannelCreationUsingXmlError(String configuration);

   @LogMessage(level = ERROR)
   @Message(value = "Error while trying to create a channel using config string: %s", id = 10)
   void jGroupsChannelCreationFromStringError(String configuration);

   @LogMessage(level = INFO)
   @Message(value = "Unable to use any JGroups configuration mechanisms provided in properties %s. Using default JGroups configuration file!", id = 11)
   void jGroupsConfigurationNotFoundInProperties(Properties props);

   @LogMessage(level = WARN)
   @Message(value = "Default JGroups configuration file was not found. Attempt to start JGroups channel with default configuration!", id = 12)
   void jGroupsDefaultConfigurationFileNotFound();

   @LogMessage(level = INFO)
   @Message(value = "Disconnecting and closing JGroups Channel", id = 13)
   void jGroupsDisconnectingAndClosingChannel();

   @LogMessage(level = ERROR)
   @Message(value = "Problem closing channel; setting it to null", id = 14)
   void jGroupsClosingChannelError(@Cause Exception toLog);

   @LogMessage(level = INFO)
   @Message(value = "Received new cluster view: %s", id = 15)
   void jGroupsReceivedNewClusterView(Object view);

   @LogMessage(level = ERROR)
   @Message(value = "Incorrect message type: %s", id = 16)
   void incorrectMessageType(Class<?> class1);

   @LogMessage(level = ERROR)
   @Message(value = "Work discarded, thread was interrupted while waiting for space to schedule: %s", id = 17)
   void interruptedWokError(Runnable r);

   @LogMessage(level = INFO)
   @Message(value = "Skipping directory synchronization, previous work still in progress: %s", id = 18)
   void skippingDirectorySynchronization(String indexName);

   @LogMessage(level = WARN)
   @Message(value = "Unable to remove previous marker file from source of %s", id = 19)
   void unableToRemovePreviousMarket(String indexName);

   @LogMessage(level = WARN)
   @Message(value = "Unable to create current marker in source of %s", id = 20)
   void unableToCreateCurrentMarker(String indexName, @Cause IOException e);

   @LogMessage(level = ERROR)
   @Message(value = "Unable to synchronize source of %s", id = 21)
   void unableToSynchronizeSource(String indexName, @Cause IOException e);

   @LogMessage(level = WARN)
   @Message(value = "Unable to determine current in source directory, will try again during the next synchronization", id = 22)
   void unableToDetermineCurrentInSourceDirectory();

   @LogMessage(level = ERROR)
   @Message(value = "Unable to compare %s with %s.", id = 23)
   void unableToCompareSourceWithDestinationDirectory(String source, String destination);

   @LogMessage(level = WARN)
   @Message(value = "Unable to reindex entity on collection change, id cannot be extracted: %s", id = 24)
   void idCannotBeExtracted(String affectedOwnerEntityName);

   @LogMessage(level = WARN)
   @Message(value = "Service provider has been used but not released: %s", id = 25)
   void serviceProviderNotReleased(Class<?> class1);

   @LogMessage(level = ERROR)
   @Message(value = "Fail to properly stop service: %s", id = 26)
   void stopServiceFailed(Class<?> class1, @Cause Exception e);

   @LogMessage(level = INFO)
   @Message(value = "Going to reindex %d entities", id = 27)
   void indexingEntities(long count);

   @LogMessage(level = INFO)
   @Message(value = "Reindexed %d entities", id = 28)
   void indexingEntitiesCompleted(long l);

   @LogMessage(level = INFO)
   @Message(value = "Indexing completed. Reindexed %d entities. Unregistering MBean from server", id = 29)
   void indexingCompletedAndMBeanUnregistered(long l);

   @LogMessage(level = INFO)
   @Message(value = "%d documents indexed in %d ms", id = 30)
   void indexingDocumentsCompleted(long doneCount, long elapsedMs);

   @LogMessage(level = INFO)
   @Message(value = "Indexing speed: %f documents/second; progress: %f%", id = 31)
   void indexingSpeed(float estimateSpeed, float estimatePercentileComplete);

   @LogMessage(level = ERROR)
   @Message(value = "Could not delete %s", id = 32)
   void notDeleted(File file);

   @LogMessage(level = WARN)
   @Message(value = "Could not change timestamp for %s. Index synchronization may be slow.", id = 33)
   void notChangeTimestamp(File destFile);

   @LogMessage(level = INFO)
   @Message(value = "Hibernate Search %s", id = 34)
   void version(String versionString);

}
