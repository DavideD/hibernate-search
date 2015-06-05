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
package org.hibernate.search.test.integration.tika.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.test.integration.tika.model.Song;

/**
 * @author Davide D'Alto
 */
@Stateful
@Model
public class SongUploader {

	@Inject
	private FullTextEntityManager em;

	private Song newSong;

	@Produces
	@Named
	public Song getNewSong() {
		return newSong;
	}

	public void upload() throws Exception {
		upload( newSong );
	}

	public void upload(Song song) throws Exception {
		em.persist( song );
		initNewSong();
	}

	@SuppressWarnings("unchecked")
	public List<Song> search(String name) {
		Query luceneQuery = em.getSearchFactory().buildQueryBuilder().forEntity( Song.class ).get().keyword().onField( "mp3FileName" ).matching( name )
				.createQuery();

		return em.createFullTextQuery( luceneQuery ).getResultList();
	}

	@PostConstruct
	public void initNewSong() {
		newSong = new Song();
	}

}
