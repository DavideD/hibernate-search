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
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * @author Davide D'Alto
 */
@Entity
public class EntityB {

	@Id
	@GeneratedValue
	public int id;

	@OneToOne(mappedBy = "b")
	@ContainedIn
	public EntityA a;

	@OneToOne(mappedBy = "b2")
	@ContainedIn
	public EntityA a2;

	@OneToOne(mappedBy = "b3")
	@ContainedIn
	public EntityA a3;

	@OneToOne(mappedBy = "b4")
	@ContainedIn
	public EntityA a4;

	@OneToOne
	@IndexedEmbedded
	public EntityC c;

	@OneToOne
	@IndexedEmbedded
	public EntityC skipped;

	@OneToOne
	@IndexedEmbedded
	public EntityC overridden;

	public EntityB() {
	}

	public EntityB(EntityC c, EntityC skipped) {
		this.c = c;
		c.b = this;

		if ( skipped != null) {
			this.skipped = skipped;
			skipped.b = this;
		}
	}

}
