/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 *  Copyright (c) 2011, Red Hat, Inc. and/or its affiliates or third-party contributors as
 *  indicated by the @author tags or express copyright attribution
 *  statements applied by the authors.  All third-party contributions are
 *  distributed under license by Red Hat, Inc.
 *
 *  This copyrighted material is made available to anyone wishing to use, modify,
 *  copy, or redistribute it subject to the terms and conditions of the GNU
 *  Lesser General Public License, as published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this distribution; if not, write to:
 *  Free Software Foundation, Inc.
 *  51 Franklin Street, Fifth Floor
 *  Boston, MA  02110-1301  USA
 */
package org.hibernate.search.test.integration.jbossas7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import org.hibernate.search.test.integration.jbossas7.controller.MemberRegistration;
import org.hibernate.search.test.integration.jbossas7.model.Member;
import org.hibernate.search.test.integration.jbossas7.util.Resources;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.jpa.persistence.PersistenceDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Example of an integration test using JBoss AS 7 and Arquillian.
 *
 * @author Davide D'Alto
 * @author Sanne Grinovero
 */
@RunWith(Arquillian.class)
public class MemberRegistrationIT {

	@Deployment
	public static Archive<?> createTestArchive() {
		WebArchive archive = ShrinkWrap
				.create( WebArchive.class, MemberRegistrationIT.class.getSimpleName() + ".war" )
				.addClasses( Member.class, MemberRegistration.class, Resources.class )
				.addAsResource( persistenceXml(), "META-INF/persistence.xml" )
				.addAsLibraries( dependencies() )
				.addAsWebInfResource( EmptyAsset.INSTANCE, "beans.xml" );
		// To debug dependencies, have it dump a zip export:
		//archive.as( ZipExporter.class ).exportTo( new File("test-app.war"), true );
		return archive;
	}

	private static File[] dependencies() {
		String dependenciesFolderPath = properties().getProperty( "war.dependencies.folder" );
		File dependencyFolder = new File( dependenciesFolderPath );
		File[] dependencyPaths = new File[dependencyFolder.list().length];
		int i = 0;
		for ( String file : dependencyFolder.list() ) {
			dependencyPaths[i++] = new File( dependenciesFolderPath + File.separator + file );
		}
		return dependencyPaths;
	}

	private static Properties properties() {
		try {
			Properties properties = new Properties();
			InputStream inStream = Thread
					.currentThread()
					.getContextClassLoader()
					.getResourceAsStream( "integration-test.properties" );
			properties.load( inStream );
			return properties;
		}
		catch ( IOException e ) {
			throw new AssertionError( "Properties file for test should exists" );
		}
	}

	private static Asset persistenceXml() {
		String persistenceXml = Descriptors.create( PersistenceDescriptor.class )
			.version( "2.0" )
			.persistenceUnit( "primary" )
			.jtaDataSource( "java:jboss/datasources/ExampleDS" )
			.property( "hibernate.hbm2ddl.auto", "create-drop" )
			.property( "hibernate.search.default.directory_provider", "ram" )
			.property( "hibernate.search.lucene_version", "LUCENE_CURRENT")
			.exportAsString();
		return new StringAsset( persistenceXml );
	}

	@Inject
	MemberRegistration memberRegistration;

	@Test
	public void testRegister() throws Exception {
		Member newMember = memberRegistration.getNewMember();
		newMember.setName( "Davide D'Alto" );
		newMember.setEmail( "davide@mailinator.com" );
		newMember.setPhoneNumber( "2125551234" );
		memberRegistration.register();

		assertNotNull( newMember.getId() );
	}

	@Test
	public void testNewMemberSearch() throws Exception {
		Member newMember = memberRegistration.getNewMember();
		newMember.setName( "Peter O'Tall" );
		newMember.setEmail( "peter@mailinator.com" );
		newMember.setPhoneNumber( "4643646643" );
		memberRegistration.register();

		List<Member> search = memberRegistration.search( "Peter" );

		assertFalse( "Expected at least one result after the indexing", search.isEmpty() );
		assertEquals( "Search hasn't found a new member", newMember.getName(), search.get( 0 ).getName() );
	}

	@Test
	public void testUnexistingMember() throws Exception {
		List<Member> search = memberRegistration.search( "TotallyInventedName" );

		assertNotNull( "Search should never return null", search );
		assertTrue( "Search results should be empty", search.isEmpty() );
	}
}
