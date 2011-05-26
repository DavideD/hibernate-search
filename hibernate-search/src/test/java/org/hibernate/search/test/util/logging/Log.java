/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009 Red Hat Inc. and/or its affiliates and other
 * contributors as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.search.test.util.logging;

import static org.jboss.logging.Logger.Level.INFO;

import org.jboss.logging.LogMessage;
import org.jboss.logging.Message;
import org.jboss.logging.MessageLogger;

/**
 * Log abstraction for test in hibernate search.
 *
 * @author Davide D'Alto
 * @since 4.0
 */
@MessageLogger(projectCode = "HSEARCH")
public interface Log extends org.hibernate.search.util.logging.Log
{
   @LogMessage(level = INFO)
   @Message(value = "Using %s as test directory.", id = 45)
   void usingTestDirectory(String absolutePath);

   @LogMessage(level = INFO)
   @Message(value = "Deleting test directory {} ", id = 46)
   void deletingTestDirectory(String absolutePath);
}
