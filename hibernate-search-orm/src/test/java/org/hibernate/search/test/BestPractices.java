/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * JBoss, Home of Professional Open Source
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
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
package org.hibernate.search.test;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

/**
 * The persistent class for the best_practices__kav database table.
 * 
 */
@Entity
@Indexed
@Table(name = "best_practices__kav")
public class BestPractices {
	private char id;
	private String articletype;
	private Date firstpublisheddate;
	private Date lastpublisheddate;
	private Date lastmodifieddate;
	private String articlenumber;
	private String title;
	private String summary;
	private String best_practices__c;

	public BestPractices() {

	}

	public BestPractices(char id, String articletype, Date firstpublisheddate, Date lastpublisheddate,
			Date lastmodifieddate, String articlenumber, String title, String summary, String best_practices__c) {
		this.id = id;
		this.articletype = articletype;
		this.firstpublisheddate = firstpublisheddate;
		this.lastpublisheddate = lastpublisheddate;
		this.lastmodifieddate = lastmodifieddate;
		this.articlenumber = articlenumber;
		this.title = title;
		this.summary = summary;
		this.best_practices__c = best_practices__c;
	}

	@Id
	@Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
	public char getId() {
		return this.id;
	}

	public void setId(char id) {
		this.id = id;
	}

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	public String getArticleType() {
		return this.articletype;
	}

	public void setArticleType(String articletype) {
		this.articletype = articletype;
	}

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	public String getArticleNumber() {
		return this.articlenumber;
	}

	public void setArticleNumber(String articlenumber) {
		this.articlenumber = articlenumber;
	}

	@Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
	@DateBridge(resolution = Resolution.MINUTE)
	public Date getFirstPublishedDate() {
		return firstpublisheddate;
	}

	public void setFirstPublishedDate(Date firstpublisheddate) {
		this.firstpublisheddate = firstpublisheddate;
	}

	@Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
	@DateBridge(resolution = Resolution.MINUTE)
	public Date getLastPublishedDate() {
		return lastpublisheddate;
	}

	public void setLastPublishedDate(Date lastpublisheddate) {
		this.lastpublisheddate = lastpublisheddate;
	}

	@Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
	@DateBridge(resolution = Resolution.MINUTE)
	public Date getLastModifiedDate() {
		return lastmodifieddate;
	}

	public void setLastModifiedDate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	public String getBest_Practices__c() {
		return this.best_practices__c;
	}

	public void setBest_Practices__c(String best_practices__c) {
		this.best_practices__c = best_practices__c;
	}

	@Override
	public String toString() {
		return "BestPractices [id=" + id + ", articletype=" + articletype + ", firstpublisheddate="
				+ firstpublisheddate + ", lastpublisheddate=" + lastpublisheddate + ", lastmodifieddate="
				+ lastmodifieddate + ", articlenumber=" + articlenumber + ", title=" + title + ", summary=" + summary
				+ ", best_practices__c=" + best_practices__c + "]";
	}

}
