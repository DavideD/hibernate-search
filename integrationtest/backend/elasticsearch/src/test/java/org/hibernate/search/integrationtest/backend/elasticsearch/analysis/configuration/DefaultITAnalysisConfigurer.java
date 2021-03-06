/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.integrationtest.backend.elasticsearch.analysis.configuration;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;
import org.hibernate.search.backend.elasticsearch.analysis.model.dsl.ElasticsearchAnalysisDefinitionContainerContext;
import org.hibernate.search.integrationtest.backend.tck.configuration.DefaultAnalysisDefinitions;

public class DefaultITAnalysisConfigurer implements ElasticsearchAnalysisConfigurer {
	@Override
	public void configure(ElasticsearchAnalysisDefinitionContainerContext context) {
		context.analyzer( DefaultAnalysisDefinitions.ANALYZER_STANDARD.name ).type( "standard" );
		context.normalizer( DefaultAnalysisDefinitions.NORMALIZER_LOWERCASE.name ).custom()
				.withTokenFilters( "lowercase" );
	}
}
