/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.exception.impl;

import org.hibernate.search.exception.ErrorContext;
import org.hibernate.search.exception.ErrorHandler;

/**
 * A template for {@link ErrorHandler}s.
 * <p>
 * This class is meant to be extended.
 *
 * @since 5.0
 * @author Davide D'Alto
 */
public abstract class ErrorHandlerBaseImpl implements ErrorHandler {

	@Override
	public void handle(ErrorContext context) {
	}

	@Override
	public void handleException(String errorMsg, Throwable exception) {
	}
}
