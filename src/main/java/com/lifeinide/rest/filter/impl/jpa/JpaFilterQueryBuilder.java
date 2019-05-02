package com.lifeinide.rest.filter.impl.jpa;

import com.lifeinide.rest.filter.BaseFilterQueryBuilder;
import com.lifeinide.rest.filter.dto.BaseRestFilter;
import com.lifeinide.rest.filter.enums.QueryConjunction;
import com.lifeinide.rest.filter.filters.*;
import com.lifeinide.rest.filter.intr.FilterQueryBuilder;
import com.lifeinide.rest.filter.intr.PageableResult;
import org.hibernate.query.criteria.internal.compile.CriteriaQueryTypeQueryAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@link FilterQueryBuilder} with JPA {@link CriteriaBuilder}. Use with dependency:
 *
 * <pre>{@code compile group: 'javax.persistence', name:'javax.persistence-api', version: '2.2'}</pre>
 *
 * @author Lukasz Frankowski
 */
public class JpaFilterQueryBuilder<E>
extends BaseFilterQueryBuilder<E, CriteriaQuery<E>, JpaQueryBuilderContext, JpaFilterQueryBuilder<E>> {

	public static final Logger logger = LoggerFactory.getLogger(JpaFilterQueryBuilder.class);

	protected JpaQueryBuilderContext<E> context;
	protected QueryConjunction conjunction = QueryConjunction.and;

	protected JpaFilterQueryBuilder(EntityManager entityManager) {
		this.context = new JpaQueryBuilderContext<>(entityManager, entityManager.getCriteriaBuilder());
	}

	public JpaFilterQueryBuilder(EntityManager entityManager, CriteriaQuery<E> query, Root<E> root) {
		this(entityManager);
		init(query, root);
	}

	public JpaFilterQueryBuilder(EntityManager entityManager, Class<E> rootClass) {
		this(entityManager);
		CriteriaQuery<E> query = context.getCb().createQuery(rootClass);
		init(query, query.from(rootClass));
	}

	protected void init(CriteriaQuery<E> query, Root<E> root) {
		context.setQuery(query);
		context.setRoot(root);

		CriteriaQuery<Long> countQuery = context.getEntityManager().getCriteriaBuilder().createQuery(Long.class);
		countQuery.select(context.getCb().count(query.getRoots().iterator().next()));
		for(Root<?> fromRoot : query.getRoots())
			countQuery.getRoots().add(fromRoot);
	}

	public JpaFilterQueryBuilder<E> withOrConjunction() {
		conjunction = QueryConjunction.or;
		return this;
	}

	@Override
	public JpaQueryBuilderContext context() {
		return context;
	}

	@Override
	public JpaFilterQueryBuilder<E> add(String field, DateRangeQueryFilter filter) {
		LocalDate from = filter.calculateFrom();
		LocalDate to = filter.calculateTo();

		Predicate predicate = from==null ? null : context.getCb().greaterThanOrEqualTo(context.getRoot().get(field), from);
		Predicate toPredicate = to==null ? null : context.getCb().lessThan(context.getRoot().get(field), to);

		predicate = (predicate!=null && toPredicate!=null)
			? context.getCb().and(predicate, toPredicate)
			: predicate != null ? predicate : toPredicate;

		if (predicate!=null)
			context.getPredicates().add(predicate);

		return this;
	}

	@Override
	public JpaFilterQueryBuilder<E> add(String field, EntityQueryFilter filter) {
		// discover id field name of the associated entity
		Class<?> associatedEntityJavaType = context.getRoot().get(field).getJavaType();
		EntityType<?> associatedEntityType = context.getEntityManager().getEntityManagerFactory().getMetamodel().entity(associatedEntityJavaType);
		Class<?> idJavaType = associatedEntityType.getIdType().getJavaType();
		SingularAttribute<?, ?> id = associatedEntityType.getId(idJavaType);

		context.getPredicates().add(JpaCriteriaBuilderHelper.INSTANCE.buildCriteria(filter.getCondition(), context.getCb(),
			context.getRoot().get(field).get(id.getName()), filter.getValue()));

		return this;
	}

	@Override
	public JpaFilterQueryBuilder<E> add(String field, ListQueryFilter<?> filter) {
		JpaFilterQueryBuilder<E> internalBuilder =
			new JpaFilterQueryBuilder<>(context.getEntityManager(), context.getQuery(), context.getRoot());

		if (QueryConjunction.or.equals(filter.getConjunction()))
			internalBuilder.withOrConjunction();

		filter.getFilters().forEach(f -> f.accept(internalBuilder, field));
		internalBuilder.buildPredicate().ifPresent(predicate -> context.getPredicates().add(predicate));

		return this;
	}

	@Override
	public JpaFilterQueryBuilder<E> add(String field, SingleValueQueryFilter filter) {
		context.getPredicates().add(JpaCriteriaBuilderHelper.INSTANCE.buildCriteria(filter.getCondition(),
			context.getCb(), context.getRoot().get(field), filter.getValue()));
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JpaFilterQueryBuilder<E> add(String field, ValueRangeQueryFilter filter) {
		Number from = filter.getFrom();
		Number to = filter.getTo();

		Predicate predicate = from==null ? null : context.getCb().greaterThanOrEqualTo(context.getRoot().get(field), (Comparable) from);
		Predicate toPredicate = to==null ? null : context.getCb().lessThanOrEqualTo(context.getRoot().get(field), (Comparable) to);

		predicate = (predicate!=null && toPredicate!=null)
			? context.getCb().and(predicate, toPredicate)
			: predicate != null ? predicate : toPredicate;

		if (predicate!=null)
			context.getPredicates().add(predicate);

		return this;
	}

	@Override
	public CriteriaQuery<E> build() {
		return context.getQuery();
	}

	protected Optional<Predicate> buildPredicate() {
		if (!context.getPredicates().isEmpty()) {
			if (QueryConjunction.and.equals(conjunction)) {
				return Optional.of(context.getCb().and(context.getPredicates().toArray(new Predicate[]{})));
			} else {
				return Optional.of(context.getCb().or(context.getPredicates().toArray(new Predicate[]{})));
			}
		}

		return Optional.empty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageableResult<E> list(BaseRestFilter req) {
		// apply predicates
		buildPredicate().ifPresent(predicate -> {
			context.getQuery().where(predicate);
		});

		// first we calulate count
		Selection<E> selection = context.getQuery().getSelection();
		CriteriaQuery countQuery = context.getQuery();
		countQuery.select(context.getCb().count(context.getRoot()));
		var cq = context.getEntityManager().createQuery(countQuery);
		if (logger.isTraceEnabled())
			logger.trace("Executing JPA query: {}", ((CriteriaQueryTypeQueryAdapter) cq).getQueryString());
		Long count = (Long) cq.getSingleResult();
		context.getQuery().select(selection); // restore selection afterwards

		// apply orders
		var orders = req.getSort().stream()
			.map(sort -> sort.isAsc()
				? context.getCb().asc(context.getRoot().get(sort.getSortField()))
				: context.getCb().desc(context.getRoot().get(sort.getSortField())))
			.collect(Collectors.toList());
		if (!orders.isEmpty())
			context.getQuery().orderBy(orders);

		// apply pagination
		var q = context.getEntityManager().createQuery(context.getQuery());
		if (req.isPaged())
			q.setFirstResult(req.getOffset()).setMaxResults(req.getPageSize());
		if (logger.isTraceEnabled())
			logger.trace("Executing JPA query: {}", ((CriteriaQueryTypeQueryAdapter<E>) q).getQueryString());

		// create and execute main query
		return buildPageableResult(req.getPageSize(), req.getPage(), count, q.getResultList());
	}
	
}
