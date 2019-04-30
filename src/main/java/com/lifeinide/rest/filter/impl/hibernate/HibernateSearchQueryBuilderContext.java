package com.lifeinide.rest.filter.impl.hibernate;

import com.lifeinide.rest.filter.BaseQueryBuilderContext;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;

/**
 * @author Lukasz Frankowski
 */
public class HibernateSearchQueryBuilderContext<E> extends BaseQueryBuilderContext {

	protected String query;
	protected Class<E> entityClass;
	protected HibernateSearch hibernateSearch;
	protected QueryBuilder queryBuilder;
	protected BooleanJunction booleanJunction;

	public HibernateSearchQueryBuilderContext(String query, Class<E> entityClass, HibernateSearch hibernateSearch,
											  QueryBuilder queryBuilder, BooleanJunction booleanJunction) {
		this.query = query;
		this.entityClass = entityClass;
		this.hibernateSearch = hibernateSearch;
		this.queryBuilder = queryBuilder;
		this.booleanJunction = booleanJunction;
	}

	public String getQuery() {
		return query;
	}

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public HibernateSearch getHibernateSearch() {
		return hibernateSearch;
	}

	public QueryBuilder getQueryBuilder() {
		return queryBuilder;
	}

	public BooleanJunction getBooleanJunction() {
		return booleanJunction;
	}
	
}
