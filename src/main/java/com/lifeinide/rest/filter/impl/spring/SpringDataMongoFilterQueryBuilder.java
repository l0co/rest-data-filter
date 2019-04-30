package com.lifeinide.rest.filter.impl.spring;

import com.lifeinide.rest.filter.BaseFilterQueryBuilder;
import com.lifeinide.rest.filter.intr.FilterQueryBuilder;

import javax.persistence.criteria.CriteriaQuery;

/**
 * Implementation of {@link FilterQueryBuilder} with MongoDB. Use with dependency:
 *
 * <pre>{@code compile group: 'org.springframework.data', name: 'spring-data-mongodb', version: '2.0.7.RELEASE'}</pre>
 *
 * // TODOLF implement MongoFilterQueryBuilder.
 *
 * @author Lukasz Frankowski
 */
public abstract class SpringDataMongoFilterQueryBuilder<E>
extends BaseFilterQueryBuilder<E, CriteriaQuery<E>, SpringDataMongoQueryBuilderContext, SpringDataMongoFilterQueryBuilder<E>> {

}
