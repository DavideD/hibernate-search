/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.backend.lucene.search.projection.impl;

import java.util.Set;

import org.hibernate.search.backend.lucene.search.extraction.impl.LuceneResult;
import org.hibernate.search.backend.lucene.search.extraction.impl.LuceneCollectorsBuilder;
import org.hibernate.search.backend.lucene.search.extraction.impl.ObjectHitExtractor;
import org.hibernate.search.engine.search.query.spi.ProjectionHitCollector;

class ObjectSearchProjectionImpl implements LuceneSearchProjection<Object> {

	private static final ObjectSearchProjectionImpl INSTANCE = new ObjectSearchProjectionImpl();

	static ObjectSearchProjectionImpl get() {
		return INSTANCE;
	}

	private ObjectSearchProjectionImpl() {
	}

	@Override
	public void contributeCollectors(LuceneCollectorsBuilder luceneCollectorBuilder) {
		ObjectHitExtractor.get().contributeCollectors( luceneCollectorBuilder );
	}

	@Override
	public void contributeFields(Set<String> absoluteFieldPaths) {
		ObjectHitExtractor.get().contributeFields( absoluteFieldPaths );
	}

	@Override
	public void extract(ProjectionHitCollector collector, LuceneResult documentResult) {
		ObjectHitExtractor.get().extract( collector, documentResult );
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
