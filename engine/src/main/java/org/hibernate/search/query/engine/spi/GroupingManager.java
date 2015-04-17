/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.query.engine.spi;

import java.util.List;
import java.util.Map;

import org.hibernate.search.query.grouping.Group;
import org.hibernate.search.query.grouping.GroupingRequest;

/**
 * The manager used for all grouping related operation.
 *
 * @author Sascha Grebe
 */
public interface GroupingManager {

	/**
	 * Request the search result to be grouped.
	 *
	 * @param grouping
	 */
	GroupingManager enableGrouping(GroupingRequest grouping);

	/**
	 * @return The grouped search result.
	 */
	Map<Group, List<EntityInfo>> getGroupHits();

	List<Group> getGroups();
}
