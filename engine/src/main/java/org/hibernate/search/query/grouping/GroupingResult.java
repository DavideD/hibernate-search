/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.query.grouping;

import java.util.List;

/**
 * Contains the results requested by the grouping request.
 *
 * @author Sascha Grebe
 */
public interface GroupingResult {

	/**
	 * @return The total count of found groups or null if the total number of groups is not computed
	 */
	Integer getTotalGroupCount();

	/**
	 * @return The found groups limited by the requested max result size.
	 */
	List<Group> getGroups();

	/**
	 * @return Number of documents grouped into the top groups.
	 */
	int getTotalGroupedHitCount();

	/**
	 * @return Number of documents matching the search.
	 */
	int getTotalHitCount();
}
