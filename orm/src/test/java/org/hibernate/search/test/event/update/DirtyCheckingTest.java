/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.test.event.update;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Davide D'Alto
 */
public class DirtyCheckingTest {

	private SessionFactory sessionFactory;

	@Before
	public void setUp() throws Exception {
		Properties properties = new Properties();
		properties.load( DirtyCheckingTest.class.getResourceAsStream( "/hibernate.properties" ) );
		properties.setProperty( Environment.HBM2DDL_AUTO, "create-drop" );

		Configuration cfg = new Configuration().addProperties( properties ).addClass( CheeseRollingCompetitor.class );

		sessionFactory = cfg.buildSessionFactory();
	}

	@Test
	public void testName() throws Exception {
		Session s = sessionFactory.openSession();

		try {
			Transaction tx = s.beginTransaction();
			s.save( new CheeseRollingCompetitor( 1, "Jimmy Fontina" ) );
			tx.commit();
			s.clear();

			tx = s.beginTransaction();
			CheeseRollingCompetitor johnny = (CheeseRollingCompetitor) s.load( CheeseRollingCompetitor.class, 1 );
			johnny.setNickname( "Johnny Fontina" );
			tx.commit();
			s.clear();

			tx = s.beginTransaction();
			s.delete( johnny );
			tx.commit();
		}
		finally {
			s.close();
		}
	}

	@After
	public void tearDown() {
		if ( sessionFactory != null ) {
			sessionFactory.close();
		}
	}
}
