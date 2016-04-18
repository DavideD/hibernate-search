/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.backend.elasticsearch.test.id;

import org.junit.Test;

/**
 * Related to HSEARCH-1050: check we deal nicely with weird DocumentId
 * configurations on Elasticsearch.
 *
 * @author Sanne Grinovero (C) 2012 Red Hat Inc.
 */
public class EmbeddedIdWithDocumentIdTest extends org.hibernate.search.test.id.EmbeddedIdWithDocumentIdTest {

	@Test
	@Override
	public void testFieldBridge() throws Exception {
		testFieldBridge( "id" );
	}
}
