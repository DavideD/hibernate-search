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
package org.hibernate.search.test.bridge;

import static org.hibernate.search.test.bridge.Foreigner.Language.ENGLISH;
import static org.hibernate.search.test.bridge.Foreigner.Language.ITALIAN;
import static org.hibernate.search.test.bridge.Foreigner.Language.KLINGON;

import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.bridge.util.impl.NumericFieldUtils;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.test.SearchTestCase;
import org.hibernate.search.test.bridge.Foreigner.Language;

/**
 * Test indexing of {@link javax.persistence.ElementCollection} annotated elements.
 *
 * @author Davide D'Alto
 */
public class ElementCollectionTest extends SearchTestCase {

	private FullTextSession fullTextSession;
	private Foreigner italian;
	private Foreigner klingon;
	private Foreigner mute;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Session session = openSession();
		fullTextSession = Search.getFullTextSession( session );
		prepareData();
	}

	@Override
	protected void tearDown() throws Exception {
		cleanData();
		assertTrue( indexIsEmpty() );
		super.tearDown();
	}

	private void prepareData() {
		Transaction tx = fullTextSession.beginTransaction();

		// Entity without nulls
		italian = persistEntity( fullTextSession, "Davide D'Alto", ITALIAN, ENGLISH );
		italian.addPhoneNumber( 391 );
		italian.addPhoneNumber( 392 );
		italian.addAddress( "Address 1" );
		italian.addAddress( "Address 2" );
		italian.setPrimitives( new Long[] { 1L, 2L } );

		// Entity with NULL entries in the collection
		klingon = persistEntity( fullTextSession, "Worf", ENGLISH, KLINGON );
		klingon.addPhoneNumber( (Integer) null );
		klingon.addAddress( null );
		klingon.setChildren( new String[] { "Klingon Son" } );

		// Entity with NULL collections
		mute = persistEntity( fullTextSession, "Bernardo" );
		mute.setLanguages( null );
		mute.setAddresses( null );
		mute.setChildren( new String[] { "Mute child", "Mute Son" } );
		tx.commit();
	}

	public void testIndexingOfCollections() throws Exception {
		Transaction tx = fullTextSession.beginTransaction();
		{
			List<Foreigner> results = findSpeakers( fullTextSession, KLINGON );

			assertNotNull( "No result found for an indexed collection", results );
			assertEquals( "Wrong number of results returned for an indexed collection", 1, results.size() );
			assertEquals( "Wrong result returned from an indexed collection", klingon.getName(), results.get( 0 ).getName() );
		}
		{
			List<Foreigner> results = findSpeakers( fullTextSession, ITALIAN );

			assertNotNull( "No result found for an indexed collection", results );
			assertEquals( "Wrong number of results returned for an indexed collection", 1, results.size() );
			assertEquals( "Wrong result returned from an indexed collection", italian.getName(), results.get( 0 ).getName() );
		}
		{
			List<Foreigner> results = findSpeakers( fullTextSession, ENGLISH );

			assertNotNull( "No result found for an indexed collection", results );
			assertEquals( "Wrong number of results returned for an indexed collection", 2, results.size() );
		}

		tx.commit();
	}

	public void testIndexingOfNumericCollections() throws Exception {
		Transaction tx = fullTextSession.beginTransaction();
		List<Foreigner> results = findNumericResultsNumbers( fullTextSession, "phoneNumbers", 391 );

		assertNotNull( "No result found for a NULL indexed collection", results );
		assertEquals( "Wrong number of results returned for an indexed collection", 1, results.size() );
		assertEquals( "Wrong result returned from an indexed NULL collection", italian.getName(), results.get( 0 ).getName() );

		tx.commit();
	}

	public void testIndexingOfNullCollections() throws Exception {
		Transaction tx = fullTextSession.beginTransaction();
		List<Foreigner> results = findResults( fullTextSession, "languages", "mute" );

		assertNotNull( "No result found for a NULL indexed collection", results );
		assertEquals( "Wrong number of results returned for an indexed collection", 1, results.size() );
		assertEquals( "Wrong result returned from an indexed NULL collection", mute.getName(), results.get( 0 ).getName() );

		tx.commit();
	}

	public void testIndexingOfCollectionsWithNullEntries() throws Exception {
		Transaction tx = fullTextSession.beginTransaction();
		List<Foreigner> results = findResults( fullTextSession, "phoneNumbers", "none" );

		assertNotNull( "No result found for a NULL indexed collection", results );
		assertEquals( "Wrong number of results returned from a collection with NULL entries", 1, results.size() );
		assertEquals( "Wrong result returned from a collection with NULL entries", klingon.getName(), results.get( 0 ).getName() );

		tx.commit();
	}

	public void testIndexingOfCollectionsWithoutIndexAsNullProperty() throws Exception {
		Transaction tx = fullTextSession.beginTransaction();
		List<Foreigner> results = findResults( fullTextSession, "addresses", "Address 1" );

		assertNotNull( "No result found for indexed collection", results );
		assertEquals( "Wrong number of results returned from a collection entries", 1, results.size() );
		assertEquals( "Wrong result returned from a collection entries", italian.getName(), results.get( 0 ).getName() );

		tx.commit();
	}

	public void testIndexingOfArrays() throws Exception {
		Transaction tx = fullTextSession.beginTransaction();
		List<Foreigner> results = findResults( fullTextSession, "children", "Klingon" );

		assertNotNull( "No result found for indexed collection", results );
		assertEquals( "Wrong number of results returned from a collection entries", 1, results.size() );
		assertEquals( "Wrong result returned from a collection entries", klingon.getName(), results.get( 0 ).getName() );

		tx.commit();
	}
	
	public void testIndexingOfNumericArrays() throws Exception {
		Transaction tx = fullTextSession.beginTransaction();
		List<Foreigner> results = findNumericResultsNumbers( fullTextSession, "primitives", 1L );

		assertNotNull( "No result found for indexed collection", results );
		assertEquals( "Wrong number of results returned from a collection entries", 1, results.size() );
		assertEquals( "Wrong result returned from a collection entries", italian.getName(), results.get( 0 ).getName() );

		tx.commit();
	}

	private List<Foreigner> findSpeakers(Session s, Language language) throws ParseException {
		return findResults( s, "languages", language.toString() );
	}

	@SuppressWarnings("unchecked")
	private List<Foreigner> findResults(Session s, String fieldName, String language) throws ParseException {
		QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity( Foreigner.class ).get();
		Query query = queryBuilder.keyword().onField( fieldName ).matching( language ).createQuery();
		return fullTextSession.createFullTextQuery( query, Foreigner.class ).list();
	}

	@SuppressWarnings("unchecked")
	private List<Foreigner> findNumericResultsNumbers(Session s, String fieldName, Object number) throws ParseException {
		Query query = NumericFieldUtils.createNumericRangeQuery( fieldName, number, number, true, true );
		return fullTextSession.createFullTextQuery( query, Foreigner.class ).list();
	}

	private Foreigner persistEntity(Session s, String name, Language... languages) {
		Foreigner boy = new Foreigner();
		boy.setName( name );
		for ( Language language : languages ) {
			boy.addLanguage( language );
		}
		s.persist( boy );
		return boy;
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { Foreigner.class, };
	}

	private void cleanData() {
		Transaction tx = fullTextSession.beginTransaction();
		@SuppressWarnings("unchecked")
		List<Foreigner> locations = fullTextSession.createCriteria( Foreigner.class ).list();
		for ( Foreigner location : locations ) {
			fullTextSession.delete( location );
		}
		tx.commit();
		fullTextSession.close();
	}

	private boolean indexIsEmpty() {
		int numDocsForeigner = countSizeForType( Foreigner.class );
		return numDocsForeigner == 0;
	}

	private int countSizeForType(Class<?> type) {
		SearchFactory searchFactory = fullTextSession.getSearchFactory();
		int numDocs = -1; // to have it fail in case of errors
		IndexReader locationIndexReader = searchFactory.openIndexReader( type );
		try {
			numDocs = locationIndexReader.numDocs();
		}
		finally {
			searchFactory.closeIndexReader( locationIndexReader );
		}
		return numDocs;
	}
}
