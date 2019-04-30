package com.lifeinide.rest.filter.impl.hibernate;

import com.lifeinide.rest.filter.BaseFilterQueryBuilder;
import com.lifeinide.rest.filter.intr.FilterQueryBuilder;
import org.hibernate.Criteria;

import javax.persistence.criteria.CriteriaQuery;

/**
 * Implementation of {@link FilterQueryBuilder} with Hibernate5 {@link Criteria} API. Use with dependency:
 * <pre>{@code compile group: 'org.hibernate', name: 'hibernate-core', version: '5.4.2.Final'}</pre>
 *
 * // TODOLF implement Hibernate5FilterQueryBuilder.
 *
 * @author Lukasz Frankowski
 */
public abstract class Hibernate5FilterQueryBuilder<E>
extends BaseFilterQueryBuilder<E, CriteriaQuery<E>, Hibernate5QueryBuilderContext, Hibernate5FilterQueryBuilder<E>> {

}
