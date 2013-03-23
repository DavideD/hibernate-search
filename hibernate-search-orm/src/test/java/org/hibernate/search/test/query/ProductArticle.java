package org.hibernate.search.test.query;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.apache.solr.analysis.HTMLStripCharFilterFactory;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.SnowballPorterFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.apache.solr.analysis.SynonymFilterFactory;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.CharFilterDef;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Spatial;
import org.hibernate.search.annotations.SpatialMode;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.hibernate.search.spatial.Coordinates;

@Entity
@Spatial(spatialMode = SpatialMode.GRID, name = "location")
@Indexed(index = "ProductArticle")
@Analyzer(impl = org.apache.lucene.analysis.standard.StandardAnalyzer.class)
@AnalyzerDef(name = "customanalyzer", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class), filters = {
		@TokenFilterDef(factory = LowerCaseFilterFactory.class),
		@TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = { @Parameter(name = "language", value = "English"), }),
		@TokenFilterDef(factory = SynonymFilterFactory.class, params = {
				@Parameter(name = "ignoreCase", value = "true"), @Parameter(name = "expand", value = "true"),
				@Parameter(name = "synonyms", value = "synonyms.txt") }), }, charFilters = { @CharFilterDef(factory = HTMLStripCharFilterFactory.class) })
public class ProductArticle implements Serializable, Coordinates, Comparable<ProductArticle> {

	@Id
	@Basic(optional = false)
	@Column(name = "article_id")
	private Integer articleId;

	@Lob
	@Column(name = "a_desc")
	@Analyzer(definition = "customanalyzer")
	@Field(index = Index.YES, store = Store.YES)
	private String aDesc;

	@Column(name = "header")
	@Field(index = Index.YES, store = Store.YES)
	@Analyzer(definition = "customanalyzer")
	private String header;

	@Column(name = "parent_category_name")
	@Analyzer(definition = "customanalyzer")
	@Field(index = Index.YES, store = Store.YES)
	private String parentCategoryName;

	@Column(name = "sub_category_name")
	@Analyzer(definition = "customanalyzer")
	@Field(index = Index.YES, store = Store.YES)
	private String subCategoryName;

	@Column(name = "state")
	@Analyzer(definition = "customanalyzer")
	@Field(index = Index.YES, store = Store.YES)
	private String state;

	@Column(name = "suburb")
	@Analyzer(definition = "customanalyzer")
	@Field(index = Index.YES, store = Store.YES)
	private String suburb;

	@Column(name = "postcode")
	@Analyzer(definition = "customanalyzer")
	@Field(index = Index.YES, store = Store.YES)
	private String postcode;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "creation_date")
	@Field(analyze = Analyze.NO)
	@DateBridge(resolution = Resolution.DAY)
	private Date creationDate;

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public String getaDesc() {
		return aDesc;
	}

	public void setaDesc(String aDesc) {
		this.aDesc = aDesc;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getParentCategoryName() {
		return parentCategoryName;
	}

	public void setParentCategoryName(String parentCategoryName) {
		this.parentCategoryName = parentCategoryName;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@Override
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Override
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public int compareTo(ProductArticle article) {
		return article.getCreationDate().compareTo( creationDate );
	}

	@Override
	public String toString() {
		return "[" + articleId + ", " + header + ", " + creationDate + "]";
	}

	
}
