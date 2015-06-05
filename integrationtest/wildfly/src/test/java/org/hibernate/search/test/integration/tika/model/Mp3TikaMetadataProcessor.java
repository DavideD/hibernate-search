/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2015, Red Hat, Inc. and/or its affiliates or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat, Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.search.test.integration.tika.model;

import org.apache.lucene.document.Document;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.XMPDM;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TikaMetadataProcessor;

/**
 * @author Davide D'Alto
 */
public class Mp3TikaMetadataProcessor implements TikaMetadataProcessor {

	@Override
	public Metadata prepareMetadata() {
		return new Metadata();
	}

	@Override
	public void set(String name, Object value, Document document, LuceneOptions luceneOptions, Metadata metadata) {
		luceneOptions.addFieldToDocument( XMPDM.ARTIST.getName(), metadata.get( XMPDM.ARTIST ), document );
	}
}
