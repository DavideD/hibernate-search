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

package org.hibernate.search.test.embedded.path;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;

/**
 * @author Davide D'Alto
 */
@Entity
public class EntityC {

	@Id
	@GeneratedValue
	public int id;

	@OneToOne(mappedBy = "c")
	@ContainedIn
	public EntityB b;

	@OneToOne(mappedBy = "skipped")
	@ContainedIn
	public EntityB b2;

	@OneToOne(mappedBy = "overridden")
	@ContainedIn
	public EntityB b3;

	@OneToOne(mappedBy = "overridden")
	@ContainedIn
	public EntityB b4;

	@Field
	public String indexed;

	@Field
	public String indexedNot = "notIndexed";

	@Field(name = "overridden")
	public String wrongName;

	@Fields({ @Field(name = "fieldOne"), @Field(name = "fieldTwo") })
	public String multiFieldsIndexed = "indexed twice";

	public EntityC() {
	}

	public EntityC(String indexed) {
		this.indexed = indexed;
	}

}
