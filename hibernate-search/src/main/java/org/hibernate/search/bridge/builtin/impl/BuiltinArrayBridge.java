/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hibernate.search.bridge.builtin.impl;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.StringBridge;
import org.hibernate.search.bridge.builtin.ArrayBridge;

/**
 * @author Davide D'Alto
 */
public class BuiltinArrayBridge extends ArrayBridge implements StringBridge {

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
	 * @see org.hibernate.search.bridge.builtin.CollectionBridge#indexNotNullEntry(java.lang.String, java.lang.Object, org.apache.lucene.document.Document, org.hibernate.search.bridge.LuceneOptions)
	 */
	@Override
	protected void indexNotNullEntry(String name, Object entry, Document document, LuceneOptions luceneOptions) {
		luceneOptions.addFieldToDocument( name, objectToString( entry ), document );
	}

}
