package com.lifeinide.rest.filter.impl.hibernate;

import com.lifeinide.rest.filter.BaseFilterQueryBuilder;
import com.lifeinide.rest.filter.intr.FilterQueryBuilder;

import javax.persistence.criteria.CriteriaQuery;

/**
 * Implementation of {@link FilterQueryBuilder} with Elastic Search. Use with dependency:
 *
 * // TODOLF complement dependency
 * <pre>{@code compile group: 'org.hibernate', name: 'hibernate-core', version: '5.4.2.Final'}</pre>
 *
 * // TODOLF implement ElasticSearchFilterQueryBuilder.
 *
 * @author Lukasz Frankowski
 */
public abstract class HibernateSearchFilterQueryBuilder<E>
extends BaseFilterQueryBuilder<E, CriteriaQuery<E>, HibernateSearchQueryBuilderContext, HibernateSearchFilterQueryBuilder<E>> {

}
