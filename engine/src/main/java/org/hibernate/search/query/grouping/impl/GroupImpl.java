/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.query.grouping.impl;

import org.apache.lucene.search.ScoreDoc;
import org.hibernate.search.query.grouping.Group;

/**
 * @author Sascha Grebe
 */
public class GroupImpl implements Group {

	private final int totalHits;

	private final String value;

	private final ScoreDoc[] scoreDocs;

	public GroupImpl(String value, int totalHits, ScoreDoc[] scoreDocs) {
		this.totalHits = totalHits;
		this.value = value;
		this.scoreDocs = scoreDocs;
	}

	@Override
	public int getTotalHits() {
		return totalHits;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public ScoreDoc[] getScoreDocs() {
		return scoreDocs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + totalHits;
		result = prime * result + ( ( value == null ) ? 0 : value.hashCode() );
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		GroupImpl other = (GroupImpl) obj;
		if ( totalHits != other.totalHits )
			return false;
		if ( value == null ) {
			if ( other.value != null )
				return false;
		}
		else if ( !value.equals( other.value ) )
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append( "[" );
		builder.append( value );
		builder.append( ", totalHits==" );
		builder.append( totalHits );
		builder.append( "]" );
		return builder.toString();
	}
}
