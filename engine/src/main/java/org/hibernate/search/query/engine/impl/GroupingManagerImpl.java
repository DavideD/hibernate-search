/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.query.engine.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.hibernate.search.exception.SearchException;
import org.hibernate.search.query.engine.spi.DocumentExtractor;
import org.hibernate.search.query.engine.spi.EntityInfo;
import org.hibernate.search.query.engine.spi.GroupingManager;
import org.hibernate.search.query.grouping.Group;
import org.hibernate.search.query.grouping.GroupingRequest;
import org.hibernate.search.query.grouping.GroupingResult;

/**
 * Default {@link org.hibernate.search.query.engine.spi.GroupingManager} implementation.
 *
 * @author Sascha Grebe
 */
public class GroupingManagerImpl implements GroupingManager {

	private GroupingRequest groupingRequest;

	private GroupingResult groupingResult;

	/**
	 * The query from which this manager was retrieved
	 */
	private final HSQueryImpl query;

	GroupingManagerImpl(HSQueryImpl query) {
		this.query = query;
	}

	@Override
	public GroupingManager enableGrouping(GroupingRequest grouping) {
		this.groupingRequest = grouping;
		return this;
	}

	public GroupingRequest getGroupingRequest() {
		return groupingRequest;
	}

	public void setGroupingResult(GroupingResult groupingResult) {
		this.groupingResult = groupingResult;
	}

	@Override
	public Map<Group, List<EntityInfo>> getGroupHits() {
		DocumentExtractor extractor = query.queryDocumentExtractor();
		try {
			return createGroupHits( extractor );
		}
		finally {
			extractor.close();
		}
	}

	@Override
	public List<Group> getGroups() {
		return groupingResult.getGroups();
	}

	private Map<Group, List<EntityInfo>> createGroupHits(DocumentExtractor extractor) {
		if ( groupingResult != null ) {
			Map<Group, List<EntityInfo>> groupHits = new HashMap<Group, List<EntityInfo>>( groupingResult.getTotalGroupedHitCount() );
			for ( Group group : groupingResult.getGroups() ) {
				List<EntityInfo> hits = exctractHits( extractor, group );
				groupHits.put( group, hits );
			}
			return groupHits;
		}
		return Collections.emptyMap();
	}

	private List<EntityInfo> exctractHits(DocumentExtractor extractor, Group group) {
		try {
			List<EntityInfo> hits = new ArrayList<EntityInfo>( group.getTotalHits() );
			for ( ScoreDoc nextScoreDoc : group.getScoreDocs() ) {
				final EntityInfo info = extractEntityInfo( extractor, nextScoreDoc );
				hits.add( info );
			}
			return hits;
		}
		catch (IOException e) {
			throw new SearchException( "Unable to extract document from index", e );
		}
	}

	// FIXME I don't think this is the right way to extract the entity infos
	private EntityInfo extractEntityInfo(DocumentExtractor extractor, ScoreDoc nextScoreDoc) throws IOException {
		final int index = index( extractor.getTopDocs(), nextScoreDoc );
		final EntityInfo info = extractor.extract( index );
		return info;
	}

	private int index(TopDocs topDocs, ScoreDoc scoreDoc) {
		for ( int i = 0; i < topDocs.scoreDocs.length; i++ ) {
			if ( topDocs.scoreDocs[i].doc == scoreDoc.doc ) {
				return i;
			}
		}
		return -1;
	}

}
