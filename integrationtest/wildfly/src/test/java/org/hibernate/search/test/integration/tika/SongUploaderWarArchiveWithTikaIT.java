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
package org.hibernate.search.test.integration.tika;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URISyntaxException;

import javax.inject.Inject;

import org.hibernate.search.test.integration.VersionTestHelper;
import org.hibernate.search.test.integration.tika.controller.SongUploader;
import org.hibernate.search.test.integration.tika.model.Mp3TikaMetadataProcessor;
import org.hibernate.search.test.integration.tika.model.Song;
import org.hibernate.search.test.integration.tika.util.Resources;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.persistence20.PersistenceDescriptor;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Example of an integration test using JBoss AS 7 and Arquillian.
 *
 * @author Davide D'Alto
 */
@RunWith(Arquillian.class)
public class SongUploaderWarArchiveWithTikaIT {

	private static final String TEST_MP3_DOCUMENT = "/org/hibernate/search/test/bridge/tika/mysong.mp3";
	private static final String PATH_TO_TEST_MP3;

	static {
		try {
			File mp3File = new File( SongUploaderWarArchiveWithTikaIT.class.getResource( TEST_MP3_DOCUMENT ).toURI() );
			PATH_TO_TEST_MP3 = mp3File.getAbsolutePath();
		}
		catch (URISyntaxException e) {
			throw new RuntimeException( "Unable to determine file path for test document" );
		}
	}

	@Deployment
	public static Archive<?> createTestArchive() {
		WebArchive war = ShrinkWrap
				.create( WebArchive.class, SongUploaderWarArchiveWithTikaIT.class.getSimpleName() + ".war" )
				.addClasses( SongUploaderWarArchiveWithTikaIT.class, Song.class, SongUploader.class, Resources.class, Mp3TikaMetadataProcessor.class )
				.addAsResource( persistenceXml(), "META-INF/persistence.xml" )
				.addAsResource( VersionTestHelper.moduleDependencyManifest(), "META-INF/MANIFEST.MF" )
				.addAsResource( new FileAsset( new File( PATH_TO_TEST_MP3 ) ), TEST_MP3_DOCUMENT )
				.addAsLibraries( tikaLibraries() )
				.addAsWebInfResource( EmptyAsset.INSTANCE, "beans.xml" )
				;
		return war;
	}

	private static JavaArchive[] tikaLibraries() {
		JavaArchive[] tikaLibraries = Maven
				.resolver()
				.resolve( "org.apache.tika:tika-core:" + VersionTestHelper.getDependencyVersionTika(), "org.apache.tika:tika-parsers:" + VersionTestHelper.getDependencyVersionTika() )
						.withoutTransitivity()
						.as( JavaArchive.class );

		return tikaLibraries;
	}

	private static Asset persistenceXml() {
		String persistenceXml = Descriptors.create( PersistenceDescriptor.class )
			.version( "2.0" )
			.createPersistenceUnit()
				.name( "primary" )
				.jtaDataSource( "java:jboss/datasources/ExampleDS" )
				.getOrCreateProperties()
					.createProperty().name( "hibernate.hbm2ddl.auto" ).value( "create-drop" ).up()
					.createProperty().name( "hibernate.search.default.lucene_version" ).value( "LUCENE_CURRENT" ).up()
					.createProperty().name( "hibernate.search.default.directory_provider" ).value( "ram" ).up()
				.up().up()
			.exportAsString();
		return new StringAsset( persistenceXml );
	}

	@Inject
	SongUploader songUploader;

	@Test
	public void testSongUpload() throws Exception {
		Song newSong = songUploader.getNewSong();
		newSong.setMp3FileName( PATH_TO_TEST_MP3 );
		songUploader.upload();

		assertNotNull( newSong.getId() );
	}
}
