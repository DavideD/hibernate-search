/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.integrationtest.backend.tck.search;

import java.util.function.Function;

import org.hibernate.search.engine.search.DocumentReference;

/**
 * The only purpose of this class is to avoid unchecked cast warnings when creating mocks.
 */
interface StubDocumentReferenceTransformer
		extends Function<DocumentReference, StubTransformedReference> {
}
