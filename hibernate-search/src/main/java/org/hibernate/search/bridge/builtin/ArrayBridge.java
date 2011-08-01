/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.hibernate.search.bridge.builtin;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

/**
 * Stores each entry of an array calling the method {@link ArrayBridge#indexNotNullEntry(String, Object, Document, LuceneOptions)}. 
 *
 * The bridge manages {@code null} elements or a {@code null} array in accordance to the {@link LuceneOptions#indexNullAs()} value.
 *
 * @author Davide D'Alto
 */
public abstract class ArrayBridge implements FieldBridge {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.hibernate.search.bridge.FieldBridge#set(java.lang.String, java.lang.Object, org.apache.lucene.document.Document, org.hibernate.search.bridge.LuceneOptions)
	 */
	@Override
	public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
		if ( value == null ) {
			manageNullValue( name, document, luceneOptions );
		} else {
			indexNotNullArray( name, value, document, luceneOptions );
		}
	}

	private void indexNotNullArray(String name, Object value, Document document, LuceneOptions luceneOptions) {
		Object[] collection = (Object[]) value;
		for ( Object entry : collection ) {
			indexEntry( name, entry, document, luceneOptions );
		}
	}

	private void indexEntry(String name, Object entry, Document document, LuceneOptions luceneOptions) {
		if ( entry == null ) {
			manageNullValue( name, document, luceneOptions );
		}
		else {
			indexNotNullEntry( name, entry, document, luceneOptions );
		}
	}

	private void manageNullValue(String name, Document document, LuceneOptions luceneOptions) {
		if ( luceneOptions.indexNullAs() != null )
			luceneOptions.addFieldToDocument( name, luceneOptions.indexNullAs(), document );
	}

	/**
	 * Index the given not {@code null} entry from an array.
	 *
	 * @param name The field to add to the Lucene document
	 * @param entry Element of the collection to index
	 * @param document The Lucene document into which we want to index the entry.
	 * @param luceneOptions Contains the parameters used for adding the {@code entry} to
	 * the Lucene document.
	 */
	protected abstract void indexNotNullEntry(String fieldName, Object entry, Document document, LuceneOptions luceneOptions);

}
