/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.backend.lucene.logging.impl;

import static org.jboss.logging.Logger.Level.WARN;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.hibernate.search.backend.lucene.cfg.SearchBackendLuceneSettings;
import org.hibernate.search.backend.lucene.index.LuceneIndexManager;
import org.hibernate.search.backend.lucene.types.predicate.impl.LuceneFieldPredicateBuilderFactory;
import org.hibernate.search.backend.lucene.types.projection.impl.LuceneFieldProjectionBuilderFactory;
import org.hibernate.search.backend.lucene.types.sort.impl.LuceneFieldSortBuilderFactory;
import org.hibernate.search.engine.backend.index.spi.IndexSearchTargetBuilder;
import org.hibernate.search.engine.search.SearchPredicate;
import org.hibernate.search.engine.search.SearchProjection;
import org.hibernate.search.engine.search.SearchSort;
import org.hibernate.search.util.EventContext;
import org.hibernate.search.util.SearchException;
import org.hibernate.search.util.impl.common.MessageConstants;
import org.hibernate.search.util.impl.common.logging.ClassFormatter;
import org.hibernate.search.util.impl.common.logging.EventContextFormatter;
import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger.Level;
import org.jboss.logging.annotations.Cause;
import org.jboss.logging.annotations.FormatWith;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageLogger;
import org.jboss.logging.annotations.Param;
import org.jboss.logging.annotations.ValidIdRange;
import org.jboss.logging.annotations.ValidIdRanges;

@MessageLogger(projectCode = MessageConstants.PROJECT_CODE)
@ValidIdRanges({
		@ValidIdRange(min = MessageConstants.BACKEND_LUCENE_ID_RANGE_MIN, max = MessageConstants.BACKEND_LUCENE_ID_RANGE_MAX),
		// Exceptions for legacy messages from Search 5
		@ValidIdRange(min = 35, max = 35),
		@ValidIdRange(min = 55, max = 55),
		@ValidIdRange(min = 75, max = 75),
		@ValidIdRange(min = 114, max = 114),
		@ValidIdRange(min = 228, max = 228),
		@ValidIdRange(min = 284, max = 284),
		@ValidIdRange(min = 320, max = 320),
		@ValidIdRange(min = 321, max = 321),
		@ValidIdRange(min = 329, max = 329),
		@ValidIdRange(min = 330, max = 330),
		@ValidIdRange(min = 337, max = 337),
		@ValidIdRange(min = 341, max = 341),
		@ValidIdRange(min = 342, max = 342),
		@ValidIdRange(min = 344, max = 344),
		@ValidIdRange(min = 345, max = 345),
		@ValidIdRange(min = 353, max = 353)
		// TODO HSEARCH-3308 add exceptions here for legacy messages from Search 5.
})
public interface Log extends BasicLogger {

	// -----------------------------------
	// Pre-existing messages from Search 5
	// DO NOT ADD ANY NEW MESSAGES HERE
	// -----------------------------------
	int ID_OFFSET_1 = MessageConstants.ENGINE_ID_RANGE_MIN;

	@LogMessage(level = Level.WARN)
	@Message(id = ID_OFFSET_1 + 35,
			value = "Could not close resource.")
	void couldNotCloseResource(@Cause Exception e);

	@LogMessage(level = Level.WARN)
	@Message(id = ID_OFFSET_1 + 55,
			value = "Unable to close the index reader. %1$s")
	void unableToCloseIndexReader(@FormatWith(EventContextFormatter.class) EventContext context, @Cause Exception e);

	@LogMessage(level = WARN)
	@Message(id = ID_OFFSET_1 + 75,
			value = "Configuration setting " + SearchBackendLuceneSettings.LUCENE_VERSION
			+ " was not specified: using LATEST (currently '%1$s'). %2$s")
	void recommendConfiguringLuceneVersion(Version latest, @FormatWith(EventContextFormatter.class) EventContext context);

	@Message(id = ID_OFFSET_1 + 114,
			value = "Could not load resource: '%1$s'")
	SearchException unableToLoadResource(String fileName);

	@Message(id = ID_OFFSET_1 + 228,
			value = "Property " + SearchBackendLuceneSettings.LUCENE_VERSION
			+ " set to value '%s' is not in a valid format to express a Lucene version: %s" )
	SearchException illegalLuceneVersionFormat(String property, String luceneErrorMessage, @Cause Exception e);

	@Message(id = ID_OFFSET_1 + 284,
			value = "An IOException happened while opening multiple indexes." )
	SearchException ioExceptionOnMultiReaderRefresh(@Param EventContext context, @Cause IOException e);

	@Message(id = ID_OFFSET_1 + 320,
			value = "Could not normalize value for field '%1$s'.")
	SearchException couldNotNormalizeField(String absoluteFieldPath, @Cause Exception cause);

	@LogMessage(level = Level.WARN)
	@Message(id = ID_OFFSET_1 + 321,
			value = "The analysis of field '%1$s' produced multiple tokens. Tokenization or term generation"
			+ " (synonyms) should not be used on sortable fields or range queries. Only the first token will be considered.")
	void multipleTermsDetectedDuringNormalization(String absoluteFieldPath);

	@Message(id = ID_OFFSET_1 + 329,
			value = "Error while applying analysis configuration: %1$s")
	SearchException unableToApplyAnalysisConfiguration(String errorMessage, @Param EventContext context, @Cause Exception e);

	@Message(id = ID_OFFSET_1 + 330,
			value = "Multiple analyzer definitions with the same name: '%1$s'. The analyzer names must be unique." )
	SearchException analyzerDefinitionNamingConflict(String analyzerDefinitionName);

	@Message(id = ID_OFFSET_1 + 337,
			value = "Multiple parameters with the same name: '%1$s'. Can't assign both value '%2$s' and '%3$s'" )
	SearchException analysisComponentParameterConflict(String name, String value1, String value2);

	@Message(id = ID_OFFSET_1 + 341,
			value = "Multiple normalizer definitions with the same name: '%1$s'. The normalizer names must be unique." )
	SearchException normalizerDefinitionNamingConflict(String normalizerDefinitionName);

	@Message(id = ID_OFFSET_1 + 342,
			value = "Cannot apply both an analyzer and a normalizer. Analyzer: '%1$s', normalizer: '%2$s'.")
	SearchException cannotApplyAnalyzerAndNormalizer(String analyzerName, String normalizerName, @Param EventContext context);

	@LogMessage(level = Level.WARN)
	@Message(id = ID_OFFSET_1 + 344,
			value = "The normalizer for definition '%s' produced %d tokens."
			+ " Normalizers should never produce more than one token."
			+ " The tokens have been concatenated by Hibernate Search,"
			+ " but you should fix your normalizer definition." )
	void normalizerProducedMultipleTokens(String normalizerName, int token);

	@Message(id = ID_OFFSET_1 + 345,
			value = "Cannot apply an analyzer on a sortable field. Use a normalizer instead. Analyzer: '%1$s'."
			+ " If an actual analyzer (with tokenization) is necessary, define two separate fields:"
			+ " one with an analyzer that is not sortable, and one with a normalizer that is sortable.")
	SearchException cannotUseAnalyzerOnSortableField(String analyzerName, @Param EventContext context);

	@Message(id = ID_OFFSET_1 + 353,
			value = "Unknown analyzer: '%1$s'. Make sure you defined this analyzer.")
	SearchException unknownAnalyzer(String analyzerName, @Param EventContext context);

	// TODO HSEARCH-3308 migrate relevant messages from Search 5 here

	// -----------------------------------
	// New messages from Search 6 onwards
	// -----------------------------------
	int ID_OFFSET_2 = MessageConstants.BACKEND_LUCENE_ID_RANGE_MIN;

	@Message(id = ID_OFFSET_2 + 0,
			value = "Unknown field '%1$s'.")
	SearchException unknownFieldForSearch(String absoluteFieldPath, @Param EventContext context);

	@Message(id = ID_OFFSET_2 + 1,
			value = "Root directory '%1$s' exists but is not a writable directory.")
	SearchException localDirectoryBackendRootDirectoryNotWritableDirectory(Path rootDirectory,
			@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 2,
			value = "Unable to create root directory '%1$s'.")
	SearchException unableToCreateRootDirectoryForLocalDirectoryBackend(Path rootDirectory,
			@Param EventContext context, @Cause Exception e);

	@Message(id = ID_OFFSET_2 + 3,
			value = "Undefined Lucene directory provider.")
	SearchException undefinedLuceneDirectoryProvider(@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 4,
			value = "Unrecognized Lucene directory provider '%1$s'.")
	SearchException unrecognizedLuceneDirectoryProvider(String directoryProvider, @Param EventContext context);

	@Message(id = ID_OFFSET_2 + 5,
			value = "The Lucene extension can only be applied to objects"
			+ " derived from the Lucene backend. Was applied to '%1$s' instead.")
	SearchException luceneExtensionOnUnknownType(Object context);

	@Message(id = ID_OFFSET_2 + 10,
			value = "A Lucene query cannot include search predicates built using a non-Lucene search target."
			+ " Given predicate was: '%1$s'")
	SearchException cannotMixLuceneSearchQueryWithOtherPredicates(SearchPredicate predicate);

	@Message(id = ID_OFFSET_2 + 12,
			value = "Field '%1$s' is not an object field.")
	SearchException nonObjectFieldForNestedQuery(String absoluteFieldPath, @Param EventContext context);

	@Message(id = ID_OFFSET_2 + 13,
			value = "Object field '%1$s' is not stored as nested.")
	SearchException nonNestedFieldForNestedQuery(String absoluteFieldPath, @Param EventContext context);

	@Message(id = ID_OFFSET_2 + 14,
			value = "A Lucene query cannot include search sorts built using a non-Lucene search target."
			+ " Given sort was: '%1$s'")
	SearchException cannotMixLuceneSearchSortWithOtherSorts(SearchSort sort);

	@Message(id = ID_OFFSET_2 + 15,
			value = "Unable to create the IndexWriter.")
	SearchException unableToCreateIndexWriter(@Param EventContext context, @Cause Exception e);

	@Message(id = ID_OFFSET_2 + 16, value = "Unable to index entry '%2$s' with tenant identifier '%1$s'.")
	SearchException unableToIndexEntry(String tenantId, String id,
			@Param EventContext context, @Cause Exception e);

	@Message(id = ID_OFFSET_2 + 17,
			value = "Unable to delete entry '%2$s' with tenant identifier '%1$s'.")
	SearchException unableToDeleteEntryFromIndex(String tenantId, String id,
			@Param EventContext context, @Cause Exception e);

	@Message(id = ID_OFFSET_2 + 18,
			value = "Unable to flush.")
	SearchException unableToFlushIndex(@Param EventContext context, @Cause Exception e);

	@Message(id = ID_OFFSET_2 + 19,
			value = "Unable to commit.")
	SearchException unableToCommitIndex(@Param EventContext context, @Cause Exception e);

	@Message(id = ID_OFFSET_2 + 20,
			value = "Index directory '%1$s' exists but is not a writable directory.")
	SearchException localDirectoryIndexRootDirectoryNotWritableDirectory(Path indexDirectory,
			@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 21,
			value = "Unable to create index root directory '%1$s'.")
	SearchException unableToCreateIndexRootDirectoryForLocalDirectoryBackend(Path indexDirectory,
			@Param EventContext context, @Cause Exception e);

	@Message(id = ID_OFFSET_2 + 22,
			value = "Could not open an index reader.")
	SearchException unableToCreateIndexReader(@Param EventContext context, @Cause Exception e);

	@Message(id = ID_OFFSET_2 + 24,
			value = "A search query cannot target both a Lucene index and other types of index."
					+ " First target was: '%1$s', other target was: '%2$s'")
	SearchException cannotMixLuceneSearchTargetWithOtherType(IndexSearchTargetBuilder firstTarget,
			LuceneIndexManager otherTarget, @Param EventContext context);

	@Message(id = ID_OFFSET_2 + 25,
			value = "A search query cannot target multiple Lucene backends."
					+ " First target was: '%1$s', other target was: '%2$s'")
	SearchException cannotMixLuceneSearchTargetWithOtherBackend(IndexSearchTargetBuilder firstTarget,
			LuceneIndexManager otherTarget, @Param EventContext context);

	@Message(id = ID_OFFSET_2 + 27,
			value = "An IOException happened while executing the query '%1$s'.")
	SearchException ioExceptionOnQueryExecution(Query luceneQuery, @Param EventContext context, @Cause IOException e);

	@Message(id = ID_OFFSET_2 + 29,
			value = "Index '%1$s' requires multi-tenancy but the backend does not support it in its current configuration.")
	SearchException multiTenancyRequiredButNotSupportedByBackend(String indexName, @Param EventContext context);

	@Message(id = ID_OFFSET_2 + 30,
			value = "Unknown multi-tenancy strategy '%1$s'.")
	SearchException unknownMultiTenancyStrategyConfiguration(String multiTenancyStrategy);

	@Message(id = ID_OFFSET_2 + 31,
			value = "Tenant identifier '%1$s' is provided, but multi-tenancy is disabled for this backend.")
	SearchException tenantIdProvidedButMultiTenancyDisabled(String tenantId, @Param EventContext context);

	@Message(id = ID_OFFSET_2 + 32,
			value = "Backend has multi-tenancy enabled, but no tenant identifier is provided.")
	SearchException multiTenancyEnabledButNoTenantIdProvided(@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 33,
			value = "Attempt to unwrap a Lucene backend to '%1$s',"
					+ " but this backend can only be unwrapped to '%2$s'.")
	SearchException backendUnwrappingWithUnknownType(@FormatWith(ClassFormatter.class) Class<?> requestedClass,
			@FormatWith(ClassFormatter.class) Class<?> actualClass,
			@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 34,
			value = "The index schema node '%1$s' was added twice."
					+ " Multiple bridges may be trying to access the same index field, "
					+ " or two indexed-embeddeds may have prefixes that lead to conflicting field names,"
					+ " or you may have declared multiple conflicting mappings."
					+ " In any case, there is something wrong with your mapping and you should fix it.")
	SearchException indexSchemaNodeNameConflict(String relativeFieldName,
			@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 37,
			value = "Range predicates are not supported by the GeoPoint field type, use spatial predicates instead.")
	SearchException rangePredicatesNotSupportedByGeoPoint(@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 38,
			value = "Match predicates are not supported by the GeoPoint field type, use spatial predicates instead.")
	SearchException matchPredicatesNotSupportedByGeoPoint(@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 39,
			value = "Invalid parent object for this field accessor; expected path '%1$s', got '%2$s'.")
	SearchException invalidParentDocumentObjectState(String expectedPath, String actualPath);

	@Message(id = ID_OFFSET_2 + 40,
			value = "Spatial predicates are not supported by this field's type.")
	SearchException spatialPredicatesNotSupportedByFieldType(@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 41,
			value = "Distance related operations are not supported by this field's type.")
	SearchException distanceOperationsNotSupportedByFieldType(@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 42,
			value = "Traditional sorting operations are not supported by the GeoPoint field type, use distance sorting instead.")
	SearchException traditionalSortNotSupportedByGeoPoint(@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 43,
			value = "Descending order is not supported for distance sort.")
	SearchException descendingOrderNotSupportedByDistanceSort(@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 44,
			value = "Computed minimum for minimumShouldMatch constraint is out of bounds:"
					+ " expected a number between 1 and '%1$s', got '%2$s'.")
	SearchException minimumShouldMatchMinimumOutOfBounds(int minimum, int totalShouldClauseNumber);

	@Message(id = ID_OFFSET_2 + 45,
			value = "Multiple conflicting minimumShouldMatch constraints for ceiling '%1$s'")
	SearchException minimumShouldMatchConflictingConstraints(int ignoreConstraintCeiling);

	@Message(id = ID_OFFSET_2 + 46,
			value = "Native fields do not support defining predicates with the DSL: use the Lucene extension and a native query.")
	SearchException unsupportedDSLPredicates(@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 47,
			value = "Native fields do not support defining sorts with the DSL: use the Lucene extension and a native sort.")
	SearchException unsupportedDSLSorts(@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 48,
			value = "This native field does not support projection.")
	SearchException unsupportedProjection(@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 49,
			value = "Invalid field path; expected path '%1$s', got '%2$s'.")
	SearchException invalidFieldPath(String expectedPath, String actualPath);

	@Message(id = ID_OFFSET_2 + 50,
			value = "Unable to convert DSL parameter: %1$s")
	SearchException cannotConvertDslParameter(String errorMessage, @Cause Exception cause, @Param EventContext context);

	@Message(id = ID_OFFSET_2 + 51,
			value = "Attempt to unwrap a Lucene index manager to '%1$s',"
					+ " but this index manager can only be unwrapped to '%2$s'.")
	SearchException indexManagerUnwrappingWithUnknownType(@FormatWith(ClassFormatter.class) Class<?> requestedClass,
			@FormatWith(ClassFormatter.class) Class<?> actualClass,
			@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 52,
			value = "Unable to create analyzer for name '%1$s'.")
	SearchException unableToCreateAnalyzer(String name, @Cause Exception e);

	@Message(id = ID_OFFSET_2 + 53,
			value = "Unable to create normalizer for name '%1$s'.")
	SearchException unableToCreateNormalizer(String name, @Cause Exception e);

	@Message(id = ID_OFFSET_2 + 54,
			value = "Unknown normalizer: '%1$s'. Make sure you defined this normalizer.")
	SearchException unknownNormalizer(String normalizerName, @Param EventContext context);

	@Message(id = ID_OFFSET_2 + 55,
			value = "A Lucene query cannot include search projections built using a non-Lucene search target."
			+ " Given projection was: '%1$s'")
	SearchException cannotMixLuceneSearchQueryWithOtherProjections(SearchProjection<?> projection);

	@Message(id = ID_OFFSET_2 + 56, value = "Invalid type '%2$s' for projection on field '%1$s'.")
	SearchException invalidProjectionInvalidType(String absoluteFieldPath,
			@FormatWith(ClassFormatter.class) Class<?> type,
			@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 57, value = "This field does not support projections.")
	SearchException unsupportedDSLProjections(@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 58,
			value = "Multiple conflicting types to build a predicate for field '%1$s': '%2$s' vs. '%3$s'.")
	SearchException conflictingFieldTypesForPredicate(String absoluteFieldPath,
			LuceneFieldPredicateBuilderFactory component1, LuceneFieldPredicateBuilderFactory component2,
			@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 59,
			value = "Multiple conflicting types to build a sort for field '%1$s': '%2$s' vs. '%3$s'.")
	SearchException conflictingFieldTypesForSort(String absoluteFieldPath,
			LuceneFieldSortBuilderFactory component1, LuceneFieldSortBuilderFactory component2,
			@Param EventContext context);

	@Message(id = ID_OFFSET_2 + 60,
			value = "Multiple conflicting types to build a projection for field '%1$s': '%2$s' vs. '%3$s'.")
	SearchException conflictingFieldTypesForProjection(String absoluteFieldPath,
			LuceneFieldProjectionBuilderFactory component1, LuceneFieldProjectionBuilderFactory component2,
			@Param EventContext context);
}
