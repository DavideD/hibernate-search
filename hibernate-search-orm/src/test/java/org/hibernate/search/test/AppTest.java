/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * JBoss, Home of Professional Open Source
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
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
package org.hibernate.search.test;

import static org.fest.assertions.Assertions.assertThat;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.Test;

public class AppTest extends SearchTestCase {

	private void populateDb() throws InterruptedException {
		Session session = openSession();
		Transaction tx = session.beginTransaction();
		session.persist( new BestPractices( '1', "", Date.valueOf( "2013-12-27" ), Date.valueOf( "2013-12-29" ), Date.valueOf( "2013-12-28" ), "12445", "Develop Realistic Expectations", "It is important to maintain realistic expectations when seeking a \"best practice\" and to be wary of \"internal validity problems\".", "bla bla bla" ) );
		session.persist( new BestPractices( '2', "", Date.valueOf( "2011-01-22" ), Date.valueOf( "2012-12-12" ), Date.valueOf( "2013-07-21" ), "124", "Analyze Smart Practices", "In policy analysis and the implementation of smart practices, administering a free lunch demonstrates a greater value in something at a minimum or very low risk.", "bla bla bla" ) );
		session.persist( new BestPractices( '3', "", Date.valueOf( "2000-10-27" ), Date.valueOf( "2001-12-29" ), Date.valueOf( "2002-12-28" ), "13242445", " Observe the Practice", "When adapting smart practices for other sites, it is important to identify the core essence of the practice while allowing flexibility for how it is implemented so it remains sensitive to local conditions", "bla bla bla" ) );
		session.persist( new BestPractices( '4', "", Date.valueOf( "2011-12-22" ), Date.valueOf( "2012-12-12" ), Date.valueOf( "2013-07-21" ), "124324", " Describe Generic Vulnerabilities", "In addition to the reasons why a smart practice might succeed, an analyst should describe potential vulnerabilities that could lead a smart practice to fail—these weaknesses are \"generic vulnerabilities\".", "bla bla bla" ) );
		tx.commit();
		session.clear();
		session.close();
	}

	private void indexDB() throws InterruptedException {
		Session session = openSession();
		FullTextSession fullTextSession = Search.getFullTextSession( session );
		fullTextSession.createIndexer().startAndWait();
		fullTextSession.close();
	}

	private List<BestPractices> search(String queryString) {
		Session session = openSession();
		FullTextSession fullTextSession = Search.getFullTextSession( session );

		QueryBuilder queryBuilder = fullTextSession.getSearchFactory()
				.buildQueryBuilder()
					.forEntity( BestPractices.class )
					.get();

		org.apache.lucene.search.Query luceneQuery = queryBuilder
				.keyword()
					.onFields( "title" ).matching( queryString )
					.createQuery();

		// wrap Lucene query in a javax.persistence.Query
		org.hibernate.Query fullTextQuery = fullTextSession.createFullTextQuery( luceneQuery, BestPractices.class );

		@SuppressWarnings("unchecked")
		List<BestPractices> bestPracticesList = fullTextQuery.list();

		fullTextSession.close();

		return bestPracticesList;
	}

	@Test
	public void testname() throws Exception {
		populateDb();
		indexDB();
		List<BestPractices> result = search( "smart" );

		assertThat( result ).hasSize( 1 );
		assertThat( result.get( 0 ).getTitle() ).isEqualTo( "Analyze Smart Practices" );
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { BestPractices.class };
	}
}
