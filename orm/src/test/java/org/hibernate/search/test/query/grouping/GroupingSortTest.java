/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.test.query.grouping;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.query.engine.spi.EntityInfo;
import org.hibernate.search.query.grouping.Group;
import org.hibernate.search.query.grouping.GroupingRequest;
import org.hibernate.search.query.grouping.GroupingResult;
import org.junit.Test;

/**
 * Test the grouping API support
 *
 * @author Sascha Grebe
 * @author Davide D'Alto
 */
public class GroupingSortTest extends AbstractGroupingTest {

	private static final String COLOR_FIELD = "color";

	private static final boolean ASC = false;
	private static final boolean DESC = true;

	@Test
	public void testSortGroupsByColor() throws Exception {
		final Sort colorSorting = new Sort( new SortField( COLOR_FIELD, Type.STRING, ASC ) );
		final GroupingRequest request = queryBuilder( Car.class )
				.group()
					.onField( COLOR_FIELD )
						.topGroupCount( 10 )
						.groupSort( colorSorting )
						.createGroupingRequest();

		final GroupingResult groups = queryHonda().getGroupingManager().enableGrouping( request ).getGroupingResult();
		assertGroupSorting( groups, ASC );
	}

	@Test
	public void testSortGroupsByColorReversed() throws Exception {
		final Sort colorSorting = new Sort( new SortField( COLOR_FIELD, Type.STRING, DESC ) );
		final GroupingRequest request = queryBuilder( Car.class )
				.group()
					.onField( COLOR_FIELD )
						.topGroupCount( 10 )
						.groupSort( colorSorting )
						.createGroupingRequest();

		final GroupingResult groups = queryHonda().getGroupingManager().enableGrouping( request ).getGroupingResult();
		assertGroupSorting( groups, DESC );
	}

	private static void assertGroupSorting(GroupingResult groups, boolean descending) {
		Group lastGroup = null;
		for ( Group nextGroup : groups.getGroups() ) {
			if ( lastGroup != null ) {
				if ( descending ) {
					assertThat( lastGroup.getValue().compareTo( nextGroup.getValue() ) )
						.as( "The groups are not sorted in descending order: " + groups )
						.isGreaterThan( 0 );
				}
				else {
					assertThat( lastGroup.getValue().compareTo( nextGroup.getValue() ) )
						.as( "The groups are not sorted in ascending order: " + groups )
						.isLessThan( 0 );
				}
			}
			lastGroup = nextGroup;
		}
	}

	@Test
	public void testSortEntitiesInGroupByCubicCapacity() throws Exception {
		final Sort cubicCapcitySort = new Sort( new SortField( "cubicCapacity", Type.INT, ASC ) );
		final GroupingRequest request = queryBuilder( Car.class )
				.group()
					.onField( COLOR_FIELD )
						.topGroupCount( 10 )
						.maxDocsPerGroup( 10 )
						.withinGroupSort( cubicCapcitySort )
						.createGroupingRequest();

		final GroupingResult groups = queryHonda().getGroupingManager().enableGrouping( request ).getGroupingResult();

		// check entities in groups sorted by cubic capacity
		for ( Group nextGroup : groups.getGroups() ) {
			final Session session = this.getSession();
			Car lastCar = null;
			for ( EntityInfo nextEntityInfo : nextGroup.getHits() ) {
				final Car car = (Car) session.load( nextEntityInfo.getClazz(), nextEntityInfo.getId() );
				if ( lastCar != null ) {
					assertThat( lastCar.getCubicCapacity() )
						.as( "The documents in the group are not sorted in ascending order" )
						.isLessThan( car.getCubicCapacity() );
				}
				lastCar = car;
			}
		}
	}

	@Test
	public void testSortEntitiesInGroupByCubicCapacityInvert() throws Exception {
		final Sort cubicCapcitySort = new Sort( new SortField( "cubicCapacity", Type.INT, DESC) );
		final GroupingRequest request = queryBuilder( Car.class )
				.group()
					.onField( COLOR_FIELD )
						.topGroupCount( 10 )
						.maxDocsPerGroup( 10 )
						.withinGroupSort( cubicCapcitySort )
						.createGroupingRequest();

		final GroupingResult groups = queryHonda().getGroupingManager().enableGrouping( request ).getGroupingResult();

		// check entities in groups sorted by cubic capacity
		for ( Group nextGroup : groups.getGroups() ) {
			final Session session = this.getSession();
			Car lastCar = null;
			for ( EntityInfo nextEntityInfo : nextGroup.getHits() ) {
				final Car car = (Car) session.load( nextEntityInfo.getClazz(), nextEntityInfo.getId() );
				if ( lastCar != null ) {
					assertThat( lastCar.getCubicCapacity() )
						.as( "The documents in the group are not sorted in ascending order" )
						.isGreaterThan( car.getCubicCapacity() );
				}
				lastCar = car;
			}
		}
	}

	private FullTextQuery queryHonda() {
		Query luceneQuery = queryBuilder( Car.class ).keyword().onField( "make" ).matching( "Honda" ).createQuery();
		FullTextQuery query = fullTextSession.createFullTextQuery( luceneQuery, Car.class );
		assertEquals( "Wrong number of query matches", 13, query.getResultSize() );
		return query;
	}

	@Override
	public void loadTestData(Session session) {
		Transaction tx = session.beginTransaction();
		for ( String make : makes ) {
			for ( String color : colors ) {
				for ( int cc : ccs ) {
					Car car = new Car( make, color, cc );
					session.save( car );
				}
			}
		}

		Car car = new Car( "Honda", "yellow", 2407 );
		session.save( car );

		car = new Car( "Ford", "yellow", 2500 );
		session.save( car );
		tx.commit();
		session.clear();
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { Car.class };
	}
}
