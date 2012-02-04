/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
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

package org.hibernate.search.test.embedded.path;

import java.util.List;

import junit.framework.Assert;

import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchException;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.test.SearchTestCase;

/**
 * @author Davide D'Alto
 */
public class PathEmbeddedTest extends SearchTestCase {

	private Session s = null;
	private EntityA entityA = null;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		EntityC entityC = new EntityC( "C1" );
		EntityC prefixed = new EntityC( "prefixed" );
		EntityC skipped = new EntityC( "skipped" );
		EntityC renamed = new EntityC();
		renamed.wrongName = "renamed";
		EntityC renamedAndPrefixed = new EntityC();
		renamedAndPrefixed.wrongName = "renamedAndPrefixed";

		EntityB b1 = new EntityB( entityC, skipped );
		EntityB b2 = new EntityB( prefixed, null );
		EntityB b3 = new EntityB();
		b3.overridden = renamed;
		renamed.b3 = b3;
		b3.a3 = entityA;

		EntityB b4 = new EntityB();
		b4.overridden = renamedAndPrefixed;
		renamedAndPrefixed.b4 = b4;
		b4.a4 = entityA;

		entityA = new EntityA( b1, b2 );
		entityA.b3 = b3;
		entityA.b4 = b4;

		s = openSession();
		persistEntity( s, entityC, skipped, prefixed, renamed, renamedAndPrefixed, b1, b2, b3, b4, entityA );
	}

	@Override
	public void tearDown() throws Exception {
		s.clear();

		deleteAll( s, EntityA.class, EntityB.class, EntityC.class );
		s.close();
		super.tearDown();
	}

	public void testPathWithOverriddenFieldIsIndexed() throws Exception {
		List<EntityA> result = search( s, "b3.overridden.overridden", "renamed" );

		Assert.assertEquals( 1, result.size() );
		Assert.assertEquals( entityA.id, result.get( 0 ).id );
	}

	public void testPathIsIndexed() throws Exception {
		List<EntityA> result = search( s, "b.c.indexed", "C1" );

		Assert.assertEquals( 1, result.size() );
		Assert.assertEquals( entityA.id, result.get( 0 ).id );
	}

	public void testPathIsIndexedWithPrefixAndFieldRenamed() throws Exception {
		List<EntityA> result = search( s, "px_overridden.overridden", "renamedAndPrefixed" );

		Assert.assertEquals( 1, result.size() );
		Assert.assertEquals( entityA.id, result.get( 0 ).id );
	}

	public void testPathIsIndexedWithPrefix() throws Exception {
		List<EntityA> result = search( s, "prefixedc.indexed", "prefixed" );

		Assert.assertEquals( 1, result.size() );
		Assert.assertEquals( entityA.id, result.get( 0 ).id );
	}

	public void testMultiFieldsAreIndexedIfInPath() throws Exception {
		List<EntityA> result = search( s, "b.c.fieldOne", "indexed twice" );

		Assert.assertEquals( 1, result.size() );
		Assert.assertEquals( entityA.id, result.get( 0 ).id );
	}

	public void testEmbeddedNotIndexedIfNotInPath() throws Exception {
		try {
			search( s, "b.skipped.indexed", "skipped" );
			fail( "Should not index embedded property if not in path and not in depth limit" );
		}
		catch ( SearchException e ) {
		}
	}

	public void testFieldNotIndexedIfNotInPath() throws Exception {
		try {
			search( s, "b.c.indexedNot", "skipped" );
			fail( "Should not index field if not included in path and over depth limit" );
		}
		catch ( SearchException e ) {
		}
	}

	private List<EntityA> search(Session s, String field, String value) {
		FullTextSession session = Search.getFullTextSession( s );
		QueryBuilder queryBuilder = session.getSearchFactory().buildQueryBuilder().forEntity( EntityA.class ).get();
		Query query = queryBuilder.keyword().onField( field ).matching( value ).createQuery();
		@SuppressWarnings("unchecked")
		List<EntityA> result = session.createFullTextQuery( query ).list();
		return result;
	}

	private void deleteAll(Session s, Class<?>... classes) {
		Transaction tx = s.beginTransaction();
		for ( Class<?> each : classes ) {
			List<?> list = s.createCriteria( each ).list();
			for ( Object object : list ) {
				s.delete( object );
			}
		}
		tx.commit();
	}

	private void persistEntity(Session s, Object... entities) {
		Transaction tx = s.beginTransaction();
		for ( Object entity : entities ) {
			s.persist( entity );
		}
		tx.commit();
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { EntityA.class, EntityB.class, EntityC.class };
	}
}
