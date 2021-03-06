/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.backend.lucene.types.projection.impl;

import org.hibernate.search.backend.lucene.types.codec.impl.LuceneFieldCodec;
import org.hibernate.search.backend.lucene.types.converter.impl.LuceneFieldConverter;
import org.hibernate.search.backend.lucene.types.predicate.impl.LuceneFieldPredicateBuilderFactory;
import org.hibernate.search.engine.search.projection.spi.DistanceToFieldSearchProjectionBuilder;
import org.hibernate.search.engine.search.projection.spi.FieldSearchProjectionBuilder;
import org.hibernate.search.engine.spatial.GeoPoint;

public interface LuceneFieldProjectionBuilderFactory {

	<U> FieldSearchProjectionBuilder<U> createFieldValueProjectionBuilder(String absoluteFieldPath,
			Class<U> expectedType);

	DistanceToFieldSearchProjectionBuilder createDistanceProjectionBuilder(String absoluteFieldPath, GeoPoint center);

	/**
	 * Determine whether another projection builder factory is DSL-compatible with this one,
	 * i.e. whether it creates builders that behave the same way.
	 *
	 * @see LuceneFieldConverter#isDslCompatibleWith(LuceneFieldConverter)
	 * @see LuceneFieldCodec#isCompatibleWith(LuceneFieldCodec)
	 *
	 * @param other Another {@link LuceneFieldPredicateBuilderFactory}, never {@code null}.
	 * @return {@code true} if the given predicate builder factory is DSL-compatible.
	 * {@code false} otherwise, or when in doubt.
	 */
	boolean isDslCompatibleWith(LuceneFieldProjectionBuilderFactory other);
}
