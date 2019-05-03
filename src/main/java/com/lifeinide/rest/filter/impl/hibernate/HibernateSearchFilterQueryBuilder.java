package com.lifeinide.rest.filter.impl.hibernate;

import com.lifeinide.rest.filter.BaseFilterQueryBuilder;
import com.lifeinide.rest.filter.enums.QueryCondition;
import com.lifeinide.rest.filter.enums.QueryConjunction;
import com.lifeinide.rest.filter.filters.*;
import com.lifeinide.rest.filter.intr.*;
import org.hibernate.search.exception.SearchException;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import static org.hibernate.search.util.StringHelper.isEmpty;

/**
 * Implementation of {@link FilterQueryBuilder} for Hibernate Search. Use with dependency:
 *
 * <pre>{@code
 * compile group: 'org.hibernate', name: 'hibernate-search-engine', version: '5.8.2.Final'
 * compileOnly group: 'org.hibernate', name: 'hibernate-search-orm', version: '5.8.2.Final'
 * }</pre>
 *
 * @see HibernateSearch How to define searchable fields on entities
 * @author Lukasz Frankowski
 */
public class HibernateSearchFilterQueryBuilder<E>
extends BaseFilterQueryBuilder<E, FullTextQuery, HibernateSearchQueryBuilderContext, HibernateSearchFilterQueryBuilder<E>> {

	public static final Logger logger = LoggerFactory.getLogger(HibernateSearchFilterQueryBuilder.class);

	protected HibernateSearchQueryBuilderContext<E> context;

	public HibernateSearchFilterQueryBuilder(HibernateSearch hibernateSearch, Class<E> entityClass, String q) {
		var queryBuilder = hibernateSearch.queryBuilder(entityClass);
		var booleanJunction = queryBuilder.bool();
		this.context = new HibernateSearchQueryBuilderContext<>(q, entityClass, hibernateSearch, queryBuilder, booleanJunction);

		BooleanJunction fullTextQuery = queryBuilder.bool();

		boolean fieldFound = false;

		// FIELD_TEXT phrase search

		try {
			fullTextQuery.should(
				queryBuilder
					.phrase()
					.onField(HibernateSearch.FIELD_TEXT)
					.sentence(q).createQuery())
				.createQuery();
			fieldFound = true;
		} catch (Exception e) {
			// silently, this means that some of our full text fields don't exists in the entity
		}

		// FIELD_ID wildcard search

		try {
			fullTextQuery.should(
				queryBuilder
					.keyword()
					.wildcard()
					.onField(HibernateSearch.FIELD_ID)
					.matching(makeWild(q))
					.createQuery());
			fieldFound = true;
		} catch (Exception e) {
			// silently, this means that some of our full text fields don't exists in the entity
		}

		if (!fieldFound)
			throw new SearchException(String.format("No fulltext fields found for: %s", entityClass.getSimpleName()));

		booleanJunction.must(fullTextQuery.createQuery());

//			try {
//				for (FieldAnalyzer field: HibernateSearch.ALL_FIELDS) {
//
//					if (field.analyzer == null) {
//
//						// field without analyze
//						fullTextQuery.should(queryBuilder.keyword().wildcard().onField(HibernateSearch.FIELD_ID)
//							.matching(makeWild(q)).createQuery());
//
//					} else {
//
//						// fields with analyzer
//						TokenStream tokenStream = hibernateSearch.fullTextEntityManager().getSearchFactory().getAnalyzer(field.analyzer)
//							.tokenStream("", new StringReader(query));
//						tokenStream.reset();
//
//						OffsetAttribute offsetAttribute = tokenStream.getAttribute(OffsetAttribute.class);
//						TermAttribute termAttribute = tokenStream.getAttribute(TermAttribute.class);
//
//						while (tokenStream.incrementToken()) {
//							offsetAttribute.startOffset();
//							offsetAttribute.endOffset();
//							String term = termAttribute.term();
//
//							queryBool.should(this.queryBuilder.keyword().wildcard().onField(field)
//								.matching(makeWild(term)).createQuery());
//						}
//
//					}
//
//				}
//
//			} catch (IOException e) {
//				throw new RuntimeException(String.format("Can't analyze query: %s", query), e);
//			}

	}

	@Override
	public HibernateSearchFilterQueryBuilder<E> add(String field, DateRangeQueryFilter filter) {
		if (filter!=null) {
			LocalDate from = filter.calculateFrom();
			LocalDate to = filter.calculateTo();

			try {
				Field reflectField = context.getEntityClass().getDeclaredField(field);
				Comparable fromObject = (Comparable) filter.convert(from, reflectField);
				Comparable toObject = (Comparable) filter.convert(to, reflectField);

				if (from!=null)
					context.getBooleanJunction().must(context.getQueryBuilder().range().onField(field).above(fromObject).createQuery());
				if (to!=null)
					context.getBooleanJunction().must(context.getQueryBuilder().range().onField(field).below(toObject).createQuery());
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			}
		}

		return this;
	}

	@Override
	public HibernateSearchFilterQueryBuilder<E> add(String field, EntityQueryFilter filter) {
		return add(field, (SingleValueQueryFilter) filter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateSearchFilterQueryBuilder<E> add(String field, ListQueryFilter filter) {
		if (filter!=null) {

			List<QueryFilter> filters = filter.getFilters();
			BooleanJunction localJunction = context.getQueryBuilder().bool();

			if (filters!=null && !filters.isEmpty()) {
				for (QueryFilter qf1: filters) {
					if (!(qf1 instanceof SingleValueQueryFilter))
						throw new UnsupportedOperationException("Only QueryFilter is supported with ListQueryFilter for full text search");
					SingleValueQueryFilter qf = (SingleValueQueryFilter) qf1;
					if (QueryConjunction.or.equals(filter.getConjunction()) && filters.size()>1) {
						if (QueryCondition.eq.equals(qf.getCondition()))
							should(localJunction, field, qf.getValue(), true);
						else if (QueryCondition.ge.equals(qf.getCondition()))
							localJunction.should(context.getQueryBuilder().range().onField(field).above(qf.getValue()).createQuery());
						else if (QueryCondition.le.equals(qf.getCondition()))
							localJunction.should(context.getQueryBuilder().range().onField(field).below(qf.getValue()).createQuery());
						else
							throw new UnsupportedOperationException(String.format(
								"Condition: %s is not supported with ListQueryFilter using or conjunction", qf.getCondition()));
					} else {
						if (QueryCondition.eq.equals(qf.getCondition()))
							must(localJunction, field, qf.getValue(), true);
						else if (QueryCondition.ne.equals(qf.getCondition()))
							mustNot(localJunction, field, qf.getValue(), true);
						else if (QueryCondition.ge.equals(qf.getCondition()))
							localJunction.must(context.getQueryBuilder().range().onField(field).above(qf.getValue()).createQuery());
						else if (QueryCondition.le.equals(qf.getCondition()))
							localJunction.must(context.getQueryBuilder().range().onField(field).below(qf.getValue()).createQuery());
						else
							throw new UnsupportedOperationException(String.format(
								"Condition: %s is not supported with ListQueryFilter", qf.getCondition()));
					}
				}
			}

			context.getBooleanJunction().must(localJunction.createQuery());

		}

		return this;

	}

	@Override
	public HibernateSearchFilterQueryBuilder<E> add(String field, SingleValueQueryFilter filter) {
		if (filter!=null) {
			if (QueryCondition.eq.equals(filter.getCondition()))
				must(context.getBooleanJunction(), field, filter.getValue(), true);
			else if (QueryCondition.ne.equals(filter.getCondition()))
				mustNot(context.getBooleanJunction(), field, filter.getValue(), true);
			else if (QueryCondition.ge.equals(filter.getCondition()))
				context.getBooleanJunction().must(context.getQueryBuilder().range().onField(field).above(filter.getValue()).createQuery());
			else if (QueryCondition.le.equals(filter.getCondition()))
				context.getBooleanJunction().must(context.getQueryBuilder().range().onField(field).below(filter.getValue()).createQuery());
			else
				throw new IllegalArgumentException(
					String.format("Condition: %s not supported for HibernateSearchFilterQueryBuilder", filter.getCondition()));
		}

		return this;
	}

	@Override
	public HibernateSearchFilterQueryBuilder<E> add(String field, ValueRangeQueryFilter filter) {
		if (filter!=null) {
			if (filter.getFrom()!=null)
				context.getBooleanJunction().must(context.getQueryBuilder().range().onField(field).above(filter.getFrom()).createQuery());
			if (filter.getTo()!=null)
				context.getBooleanJunction().must(context.getQueryBuilder().range().onField(field).below(filter.getTo()).createQuery());
		}

		return this;

	}

	public HibernateSearchFilterQueryBuilder<E> must(BooleanJunction booleanJunction, String fieldName, Object expression) {
		must(booleanJunction, fieldName, expression, false);
		return this;
	}

	public HibernateSearchFilterQueryBuilder<E> must(BooleanJunction booleanJunction, String fieldName, Object expression, boolean ignoreAnalyzer) {
		if (ignoreAnalyzer) {
			booleanJunction.must(context.getQueryBuilder().keyword().onField(fieldName).ignoreAnalyzer().
				matching(expression).createQuery());
		} else {
			booleanJunction.must(context.getQueryBuilder().keyword().onField(fieldName).matching(expression).createQuery());
		}
		return this;
	}

	public HibernateSearchFilterQueryBuilder<E> should(BooleanJunction booleanJunction, String fieldName, Object expression) {
		return should(booleanJunction, fieldName, expression, false);
	}

	public HibernateSearchFilterQueryBuilder<E> should(BooleanJunction booleanJunction, String fieldName, Object expression, boolean ignoreAnalyzer) {
		if (ignoreAnalyzer) {
			booleanJunction.should(context.getQueryBuilder().keyword().onField(fieldName).ignoreAnalyzer().
				matching(expression).createQuery());
		} else {
			booleanJunction.should(context.getQueryBuilder().keyword().onField(fieldName).matching(expression).createQuery());
		}
		return this;
	}

	public HibernateSearchFilterQueryBuilder<E> mustNot(BooleanJunction booleanJunction, String fieldName, Object expression) {
		return mustNot(booleanJunction, fieldName, expression, false);
	}

	/**
	 * Helper method to add "must not" field constraint and showing how to work with {@code booleanJunction} and
	 * {@code queryBuilder}.
	 *
	 * @param fieldName      Field name
	 * @param expression     The must not expression
	 * @param ignoreAnalyzer if to ignore analyzer. If analyzer is ignored fields will not be sliced into tokens.
	 */
	public HibernateSearchFilterQueryBuilder<E> mustNot(BooleanJunction booleanJunction, String fieldName, Object expression, boolean ignoreAnalyzer) {
		if (ignoreAnalyzer) {
			booleanJunction.must(context.getQueryBuilder().keyword().onField(fieldName).ignoreAnalyzer().
				matching(expression).createQuery()).not().createQuery();
		} else {
			booleanJunction.must(context.getQueryBuilder().keyword().onField(fieldName).matching(expression).createQuery()).not().createQuery();
		}
		return this;
	}

	protected String makeWild(String s) {
		if (isEmpty(s))
			return s;
		if (s.endsWith("*"))
			return s;
		return s+"*";
	}

	@Override
	public HibernateSearchQueryBuilderContext context() {
		return context;
	}

	@Override
	public FullTextQuery build() {
		return context.getHibernateSearch().buildQuery(context.getBooleanJunction().createQuery(), context.getEntityClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageableResult<E> list(Pageable pageable, Sortable sortable) {
		FullTextQuery fullTextQuery = build();

		if (logger.isTraceEnabled())
			logger.trace("Executing lucene query: {}", fullTextQuery.toString());

		long count = fullTextQuery.getResultSize();

		if (pageable.isPaged()) {
			fullTextQuery.setFirstResult(pageable.getOffset());
			fullTextQuery.setMaxResults(pageable.getPageSize());
		}

		return buildPageableResult(pageable.getPageSize(), pageable.getPage(), count, fullTextQuery.getResultList());
	}
	
}
