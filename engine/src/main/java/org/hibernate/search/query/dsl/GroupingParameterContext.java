/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.query.dsl;

import org.apache.lucene.search.Sort;

/**
 * @author Sascha Grebe
 */
public interface GroupingParameterContext extends GroupingTermination {

	/**
	 * The sorting of the groups.
	 */
	GroupingParameterContext groupSort(Sort groupSort);

	/**
	 * The sorting within the group.
	 */
	GroupingParameterContext withinGroupSort(Sort withinGroupSort);

	/**
	 * The maimum number of fetched documents per group. Needs to be greater than 0. The default is 1.
	 */
	GroupingParameterContext maxDocsPerGroup(int maxDocsPerGroup);

	/**
	 * The offset of the first group (skip the first n groups). Used for pagination.
	 */
	GroupingParameterContext groupOffset(int groupOffset);

	/**
	 * Don't calculate the totale number of groups.
	 */
	GroupingParameterContext disableTotalGroupCount();
}
