/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.mapper.pojo.bridge.builtin.impl;

import org.hibernate.search.mapper.pojo.bridge.IdentifierBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.IdentifierBridgeBindingContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.IdentifierBridgeFromDocumentIdentifierContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.IdentifierBridgeToDocumentIdentifierContext;

public final class DefaultEnumIdentifierBridge<T extends Enum<T>> implements IdentifierBridge<T> {

	private Class<T> enumType;

	@Override
	@SuppressWarnings("unchecked") // The bridge resolver performs the checks using reflection
	public void bind(IdentifierBridgeBindingContext<T> context) {
		this.enumType = (Class<T>) context.getBridgedElement().getRawType();
	}

	@Override
	public String toDocumentIdentifier(T propertyValue,
			IdentifierBridgeToDocumentIdentifierContext context) {
		return propertyValue.name();
	}

	@Override
	public T fromDocumentIdentifier(String documentIdentifier, IdentifierBridgeFromDocumentIdentifierContext context) {
		return Enum.valueOf( enumType, documentIdentifier );
	}

	@Override
	public boolean isCompatibleWith(IdentifierBridge<?> bridge) {
		return false;
	}

}