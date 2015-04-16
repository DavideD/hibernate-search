/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.query.grouping.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.search.ScoreDoc;
import org.hibernate.search.query.engine.spi.EntityInfo;
import org.hibernate.search.query.grouping.Group;

/**
 * @author Sascha Grebe
 */
public class GroupImpl implements Group {

	private final int totalHits;

	private final String value;

	private final ScoreDoc[] scoreDocs;

	private List<EntityInfo> hits = new LinkedList<>();

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
	public List<EntityInfo> getHits() {
		return hits;
	}

	@Override
	public void setHits(List<EntityInfo> hits) {
		this.hits = hits;
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
