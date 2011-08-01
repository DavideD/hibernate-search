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

package org.hibernate.search.bridge.builtin.impl;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.StringBridge;
import org.hibernate.search.bridge.builtin.IterableBridge;

/**
 * @author Davide D'Alto
 */
public class BuiltinIterableBridge extends IterableBridge implements StringBridge {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.hibernate.search.bridge.StringBridge#objectToString(java.lang.Object)
	 */
	@Override
	public String objectToString(Object object) {
		return String.valueOf( object );
	}

	/*
	 * (non-Javadoc)
	 * @see org.hibernate.search.bridge.builtin.IterableBridge#indexNotNullEntry(java.lang.String, java.lang.Object, org.apache.lucene.document.Document, org.hibernate.search.bridge.LuceneOptions)
	 */
	@Override
	protected void indexNotNullEntry(String name, Object entry, Document document, LuceneOptions luceneOptions) {
		luceneOptions.addFieldToDocument( name, objectToString( entry ), document );
	}

}
