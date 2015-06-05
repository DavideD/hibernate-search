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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TikaBridge;

/**
 * @author Davide D'Alto
 */
@Entity
@Indexed
public class Song {

	@Id
	@GeneratedValue
	long id;

	@Field
	@TikaBridge(metadataProcessor = Mp3TikaMetadataProcessor.class)
	String mp3FileName;

	public Song(String mp3FileName) {
		this.mp3FileName = mp3FileName;
	}

	public Song() {
	}

	public Long getId() {
		return id;
	}

	public String getMp3FileName() {
		return mp3FileName;
	}

	public void setMp3FileName(String mp3FileName) {
		this.mp3FileName = mp3FileName;
	}
}
