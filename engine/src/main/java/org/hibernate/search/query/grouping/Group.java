/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.query.grouping;

import org.apache.lucene.search.ScoreDoc;

/**
 * Contains the values of a single group.
 *
 * @author Sascha Grebe
 */
public interface Group {

	/**
	 * @return The total number of elements in the group.
	 */
	int getTotalHits();

	/**
	 * @return The value which is used for grouping.
	 */
	String getValue();

	ScoreDoc[] getScoreDocs();
}
