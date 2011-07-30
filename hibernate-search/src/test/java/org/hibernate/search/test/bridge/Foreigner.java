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
package org.hibernate.search.test.bridge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Store;

/**
 * @author Davide D'Alto
 */
@Entity
@Indexed
@Table(name = "foreigner")
public class Foreigner {

	private Long id;
	private String name;
	private Set<Language> languages = new HashSet<Language>();
	private Set<Integer> phoneNumbers = new HashSet<Integer>();
	private List<String> addresses = new ArrayList<String>();
	private String[] children = new String[0];
	private Long[] primitives = new Long[0];

	public enum Language {
		ITALIAN, ENGLISH, PIRATE, KLINGON
	}

	@Id
	@GeneratedValue
	@Column(name = "foreigner_id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name")
	@Field(index = Index.TOKENIZED, store = Store.YES)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Field(index = Index.TOKENIZED, store = Store.YES, indexNullAs = "mute")
	@ElementCollection
	@CollectionTable(name = "Languages", joinColumns = @JoinColumn(name = "foreigner_id"))
	@Column(name = "language")
	public Set<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(Set<Language> languages) {
		this.languages = languages;
	}

	public void addLanguage(Language language) {
		this.languages.add( language );
	}

	@Field(index = Index.TOKENIZED, store = Store.YES, indexNullAs = "none")
	@NumericField
	@ElementCollection
	@CollectionTable(name = "PhoneNumbers", joinColumns = @JoinColumn(name = "foreigner_id"))
	@Column(name = "phoneNumbers")
	public Set<Integer> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(Set<Integer> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public void addPhoneNumber(Integer number) {
		this.phoneNumbers.add( number );
	}

	@Field(index = Index.TOKENIZED, store = Store.YES)
	@ElementCollection
	@CollectionTable(name = "Addresses", joinColumns = @JoinColumn(name = "foreigner_id"))
	@Column(name = "addresses")
	public List<String> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<String> addresses) {
		this.addresses = addresses;
	}

	public void addAddress(String address) {
		this.addresses.add( address );
	}

	@ElementCollection
	@CollectionTable(name = "Children", joinColumns = @JoinColumn(name = "foreigner_id"))
	@Column(name = "children")
	@Field(index = Index.TOKENIZED, store = Store.YES)
	@OrderColumn
	public String[] getChildren() {
		return children;
	}

	public void setChildren(String[] children) {
		this.children = children;
	}

	@ElementCollection
	@NumericField
	@CollectionTable(name = "primitives", joinColumns = @JoinColumn(name = "foreigner_id"))
	@Column(name = "primitives")
	@Field(index = Index.TOKENIZED, store = Store.YES)
	@OrderColumn
	public Long[] getPrimitives() {
		return primitives;
	}

	public void setPrimitives(Long[] primitives) {
		this.primitives = primitives;
	}

	@Override
	public String toString() {
		return "Foreigner [id=" + id + ", name=" + name + "]";
	}

}
