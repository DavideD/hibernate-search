/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.backend.elasticsearch.search.sort.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.function.Consumer;

import org.hibernate.search.backend.elasticsearch.document.model.impl.ElasticsearchIndexSchemaFieldNode;
import org.hibernate.search.backend.elasticsearch.logging.impl.Log;
import org.hibernate.search.backend.elasticsearch.search.impl.ElasticsearchSearchQueryElementCollector;
import org.hibernate.search.backend.elasticsearch.search.impl.ElasticsearchSearchTargetModel;
import org.hibernate.search.backend.elasticsearch.search.impl.IndexSchemaFieldNodeComponentRetrievalStrategy;
import org.hibernate.search.backend.elasticsearch.types.sort.impl.ElasticsearchFieldSortBuilderFactory;
import org.hibernate.search.engine.search.SearchSort;
import org.hibernate.search.engine.search.sort.spi.DistanceSortBuilder;
import org.hibernate.search.engine.search.sort.spi.FieldSortBuilder;
import org.hibernate.search.engine.search.sort.spi.ScoreSortBuilder;
import org.hibernate.search.engine.spatial.GeoPoint;
import org.hibernate.search.util.EventContext;
import org.hibernate.search.util.SearchException;
import org.hibernate.search.util.impl.common.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * @author Yoann Rodiere
 */
// TODO have one version of the factory per dialect, if necessary
public class SearchSortFactoryImpl implements ElasticsearchSearchSortFactory {

	private static final Log log = LoggerFactory.make( Log.class, MethodHandles.lookup() );

	private static final Gson GSON = new GsonBuilder().create();

	private static final SortBuilderFactoryRetrievalStrategy SORT_BUILDER_FACTORY_RETRIEVAL_STRATEGY =
			new SortBuilderFactoryRetrievalStrategy();

	private final ElasticsearchSearchTargetModel searchTargetModel;

	public SearchSortFactoryImpl(ElasticsearchSearchTargetModel searchTargetModel) {
		this.searchTargetModel = searchTargetModel;
	}

	@Override
	public SearchSort toSearchSort(List<ElasticsearchSearchSortBuilder> builders) {
		ElasticsearchSearchQueryElementCollector collector = new ElasticsearchSearchQueryElementCollector();
		for ( ElasticsearchSearchSortBuilder builder : builders ) {
			builder.buildAndAddTo( collector );
		}
		return new ElasticsearchSearchSort( collector.toJsonSort() );
	}

	@Override
	public void toImplementation(SearchSort sort, Consumer<ElasticsearchSearchSortBuilder> implementationConsumer) {
		if ( !( sort instanceof ElasticsearchSearchSort ) ) {
			throw log.cannotMixElasticsearchSearchSortWithOtherSorts( sort );
		}
		implementationConsumer.accept( (ElasticsearchSearchSort) sort );
	}

	@Override
	public void contribute(ElasticsearchSearchSortCollector collector, List<ElasticsearchSearchSortBuilder> builders) {
		for ( ElasticsearchSearchSortBuilder builder : builders ) {
			builder.buildAndAddTo( collector );
		}
	}

	@Override
	public ScoreSortBuilder<ElasticsearchSearchSortBuilder> score() {
		return new ScoreSortBuilderImpl();
	}

	@Override
	public FieldSortBuilder<ElasticsearchSearchSortBuilder> field(String absoluteFieldPath) {
		return searchTargetModel
				.getSchemaNodeComponent( absoluteFieldPath, SORT_BUILDER_FACTORY_RETRIEVAL_STRATEGY )
				.createFieldSortBuilder( absoluteFieldPath );
	}

	@Override
	public DistanceSortBuilder<ElasticsearchSearchSortBuilder> distance(String absoluteFieldPath, GeoPoint location) {
		return searchTargetModel
				.getSchemaNodeComponent( absoluteFieldPath, SORT_BUILDER_FACTORY_RETRIEVAL_STRATEGY )
				.createDistanceSortBuilder( absoluteFieldPath, location );
	}

	@Override
	public ElasticsearchSearchSortBuilder indexOrder() {
		return IndexOrderSortContributor.INSTANCE;
	}

	@Override
	public ElasticsearchSearchSortBuilder fromJsonString(String jsonString) {
		return new UserProvidedJsonSortContributor( GSON.fromJson( jsonString, JsonObject.class ) );
	}

	private static class SortBuilderFactoryRetrievalStrategy
			implements IndexSchemaFieldNodeComponentRetrievalStrategy<ElasticsearchFieldSortBuilderFactory> {

		@Override
		public ElasticsearchFieldSortBuilderFactory extractComponent(ElasticsearchIndexSchemaFieldNode<?> schemaNode) {
			return schemaNode.getSortBuilderFactory();
		}

		@Override
		public boolean areCompatible(ElasticsearchFieldSortBuilderFactory component1,
				ElasticsearchFieldSortBuilderFactory component2) {
			return component1.isDslCompatibleWith( component2 );
		}

		@Override
		public SearchException createCompatibilityException(String absoluteFieldPath,
				ElasticsearchFieldSortBuilderFactory component1, ElasticsearchFieldSortBuilderFactory component2,
				EventContext context) {
			return log.conflictingFieldTypesForSort( absoluteFieldPath, component1, component2, context );
		}
	}
}
