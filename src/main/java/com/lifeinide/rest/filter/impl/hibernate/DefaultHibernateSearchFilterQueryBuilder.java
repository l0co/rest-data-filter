package com.lifeinide.rest.filter.impl.hibernate;

import com.lifeinide.rest.filter.dto.Page;

/**
 * @author lukasz.frankowski@gmail.com
 */
public class DefaultHibernateSearchFilterQueryBuilder<E>
extends HibernateSearchFilterQueryBuilder<E, Page<E>> {

	public DefaultHibernateSearchFilterQueryBuilder(HibernateSearch hibernateSearch, Class<E> entityClass, String q) {
		super(hibernateSearch, entityClass, q);
	}
	
}
