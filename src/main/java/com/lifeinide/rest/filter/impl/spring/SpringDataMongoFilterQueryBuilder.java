package com.lifeinide.rest.filter.impl.spring;

import com.lifeinide.rest.filter.BaseFilterQueryBuilder;
import com.lifeinide.rest.filter.dto.BaseRestFilter;
import com.lifeinide.rest.filter.enums.QueryConjunction;
import com.lifeinide.rest.filter.filters.*;
import com.lifeinide.rest.filter.intr.FilterQueryBuilder;
import com.lifeinide.rest.filter.intr.PageableResult;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Implementation of {@link FilterQueryBuilder} with MongoDB. Use with dependency:
 *
 * <pre>{@code compile group: 'org.springframework.data', name: 'spring-data-mongodb', version: '2.0.7.RELEASE'}</pre>
 *
 * @author Lukasz Frankowski
 */
public class SpringDataMongoFilterQueryBuilder<E>
extends BaseFilterQueryBuilder<E, Criteria, SpringDataMongoQueryBuilderContext, SpringDataMongoFilterQueryBuilder<E>> {

	protected MongoTemplate mongoTemplate;
	protected String fullTextQuery;
	protected SpringDataMongoQueryBuilderContext<E> context;

	/**
	 * @param fullTextQuery If provided, executes mongo full text query against this text instead of normal query.
	 */
	public SpringDataMongoFilterQueryBuilder(MongoTemplate mongoTemplate, Class<E> entityClass, QueryConjunction conjunction,
											 String fullTextQuery) {
		this.mongoTemplate = mongoTemplate;
		this.fullTextQuery = fullTextQuery;
		this.context = new SpringDataMongoQueryBuilderContext<>(entityClass, conjunction);
	}

	@Override
	public SpringDataMongoFilterQueryBuilder<E> add(String field, DateRangeQueryFilter filter) {
		if (filter!=null) {
			Criteria fromCriteria = null;
			Criteria toCriteria = null;

			LocalDate from = filter.calculateFrom();
			LocalDate to = filter.calculateTo();

			if (from!=null)
				fromCriteria = Criteria.where(field).gte(from);
			if (to!=null)
				toCriteria = Criteria.where(field).lt(to);

			addRangeCriteria(fromCriteria, toCriteria);

		}

		return this;
	}

	@Override
	public SpringDataMongoFilterQueryBuilder<E> add(String field, EntityQueryFilter filter) {
		if (filter!=null)
			addCriteria(SpringDataMongoCriteriaBuilderHelper.INSTANCE
				.buildCriteria(filter.getCondition(), field+".id", filter.getValue()));

		return this;
	}

	@Override
	public SpringDataMongoFilterQueryBuilder<E> add(String field, ListQueryFilter<?> filter) {
		if (filter!=null) {
			SpringDataMongoFilterQueryBuilder<E> internalBuilder =
				new SpringDataMongoFilterQueryBuilder<>(this.mongoTemplate, context.getEntityClass(), filter.getConjunction(), null);
			filter.getFilters().forEach(it -> it.accept(internalBuilder, field));
			Optional.ofNullable(internalBuilder.build()).ifPresent(this::addCriteria);
		}

		return this;
	}

	@Override
	public SpringDataMongoFilterQueryBuilder<E> add(String field, QueryFilter filter) {
		if (filter!=null)
			addCriteria(SpringDataMongoCriteriaBuilderHelper.INSTANCE.buildCriteria(filter.getCondition(), field, filter.getValue()));

		return this;
	}

	@Override
	public SpringDataMongoFilterQueryBuilder<E> add(String field, ValueRangeQueryFilter filter) {
		if (filter!=null) {
			Criteria fromCriteria = null;
			Criteria toCriteria = null;

			if (filter.getFrom()!=null)
				fromCriteria = Criteria.where(field).gte(filter.getFrom());
			if (filter.getTo()!=null)
				toCriteria = Criteria.where(field).lte(filter.getTo());

			addRangeCriteria(fromCriteria, toCriteria);

		}

		return this;
	}

	@Override
	public SpringDataMongoQueryBuilderContext context() {
		return context;
	}

	@Override
	public Criteria build() {
		if (context.getCriterias().isEmpty())
			return null;

		return SpringDataMongoCriteriaBuilderHelper.INSTANCE
			.conjunctCriteria(context.getConjunction(), context.getCriterias().toArray(new Criteria[0]));
	}

	@Override
	public PageableResult<E> list(BaseRestFilter req) {
		Criteria criteria = build();
		Pageable springPageable = SpringPageableConverter.applicationPageableToSpring(req);

		Query query;
		if (fullTextQuery==null) {

			// normal query execution
			query = new Query().restrict(context.getEntityClass()).with(springPageable);

		} else {

			// full text query execution
			// TODO no class restrictions yet
			query = new TextQuery(new TextCriteria().matchingAny(fullTextQuery)).sortByScore().with(springPageable);

		}

		if (criteria!=null)
			query.addCriteria(criteria);
		return SpringPageableConverter.springPageToApplication(
			new PageImpl<>(mongoTemplate.find(query, context.getEntityClass()), springPageable,
				mongoTemplate.count(query, context.getEntityClass()))
		);
	}

	public void addRangeCriteria(Criteria fromCriteria, Criteria toCriteria) {
		if (fromCriteria!=null && toCriteria!=null)
			addCriteria(new Criteria().andOperator(fromCriteria, toCriteria));
		else if (fromCriteria!=null)
			addCriteria(fromCriteria);
		else if (toCriteria!=null)
			addCriteria(toCriteria);
	}

	public SpringDataMongoFilterQueryBuilder<E> addCriteria(Criteria criteria) {
		context.getCriterias().add(criteria);
		return this;
	}

}
