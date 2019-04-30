package com.lifeinide.rest.filter.impl.mongo;

import com.lifeinide.rest.filter.BaseFilterQueryBuilder;
import com.lifeinide.rest.filter.intr.FilterQueryBuilder;

import javax.persistence.criteria.CriteriaQuery;

/**
 * Implementation of {@link FilterQueryBuilder} with MongoDB. Use with dependency:
 *
 * // TODOLF complement dependency
 * <pre>{@code compile group: 'org.hibernate', name: 'hibernate-core', version: '5.4.2.Final'}</pre>
 *
 * // TODOLF implement MongoFilterQueryBuilder.
 *
 * @author Lukasz Frankowski
 */
public abstract class MongoFilterQueryBuilder<E>
extends BaseFilterQueryBuilder<E, CriteriaQuery<E>, MongoQueryBuilderContext, MongoFilterQueryBuilder<E>> {

}
