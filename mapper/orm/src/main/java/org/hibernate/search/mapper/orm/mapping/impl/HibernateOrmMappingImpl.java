/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.mapper.orm.mapping.impl;

import java.lang.invoke.MethodHandles;
import javax.persistence.EntityManager;

import org.hibernate.Hibernate;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.search.mapper.orm.logging.impl.Log;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmMapping;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchManager;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchManagerBuilder;
import org.hibernate.search.mapper.pojo.mapping.spi.PojoMappingDelegate;
import org.hibernate.search.mapper.pojo.mapping.spi.PojoMappingImpl;
import org.hibernate.search.util.impl.common.LoggerFactory;

public class HibernateOrmMappingImpl extends PojoMappingImpl<HibernateOrmMapping>
		implements HibernateOrmMapping {

	private static final Log log = LoggerFactory.make( Log.class, MethodHandles.lookup() );

	private final SessionFactoryImplementor sessionFactoryImplementor;

	HibernateOrmMappingImpl(PojoMappingDelegate mappingDelegate, SessionFactoryImplementor sessionFactoryImplementor) {
		super( mappingDelegate );
		this.sessionFactoryImplementor = sessionFactoryImplementor;
	}

	@Override
	public HibernateOrmMapping toAPI() {
		return this;
	}

	@Override
	public HibernateOrmSearchManager createSearchManager(EntityManager entityManager) {
		return createSearchManagerBuilder( entityManager ).build();
	}

	@Override
	public HibernateOrmSearchManagerBuilder createSearchManagerWithOptions(EntityManager entityManager) {
		return createSearchManagerBuilder( entityManager );
	}

	@Override
	public boolean isWorkable(Object entity) {
		return isWorkable( Hibernate.getClass( entity ) );
	}

	private HibernateOrmSearchManagerBuilder createSearchManagerBuilder(EntityManager entityManager) {
		SessionImplementor sessionImplementor = entityManager.unwrap( SessionImplementor.class );
		SessionFactoryImplementor givenSessionFactory = sessionImplementor.getSessionFactory();

		if ( !givenSessionFactory.equals( sessionFactoryImplementor ) ) {
			throw log.usingDifferentSessionFactories( sessionFactoryImplementor, givenSessionFactory );
		}

		return new HibernateOrmSearchManagerImpl.Builder( getDelegate(), sessionImplementor );
	}
}
