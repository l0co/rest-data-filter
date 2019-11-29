package com.lifeinide.rest.filter.test.hibernate.search;

import com.lifeinide.rest.filter.impl.hibernate.HibernateSearch;
import com.lifeinide.rest.filter.test.IBaseEntity;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Lukasz Frankowski
 */
@Entity
@Indexed
public class HibernateSearchAssociatedEntity implements IBaseEntity<Long> {

	@Id Long id;

	@Field(name = HibernateSearch.FIELD_TEXT)
	@Analyzer(impl = EnglishAnalyzer.class)
	protected String q = HibernateSearchQueryBuilderTest.SEARCHABLE_STRING;

	public HibernateSearchAssociatedEntity() {
	}

	public HibernateSearchAssociatedEntity(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}
	
}
