package com.lifeinide.rest.filter.impl.spring;

import com.lifeinide.rest.filter.BaseFilterQueryBuilder;
import com.lifeinide.rest.filter.intr.FilterQueryBuilder;

import javax.persistence.criteria.CriteriaQuery;

/**
 * Implementation of {@link FilterQueryBuilder} with Spring Data. Use with dependency:
 *
 * // TODOLF complement dependency
 * <pre>{@code compile group: 'org.hibernate', name: 'hibernate-core', version: '5.4.2.Final'}</pre>
 *
 * // TODOLF implement SpringDataFilterQueryBuilder.
 *
 * @author Lukasz Frankowski
 */
public abstract class SpringDataFilterQueryBuilder<E>
extends BaseFilterQueryBuilder<E, CriteriaQuery<E>, SpringDataQueryBuilderContext, SpringDataFilterQueryBuilder<E>> {

}
