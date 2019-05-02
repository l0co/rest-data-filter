package com.lifeinide.rest.filter.impl.spring;

import com.lifeinide.rest.filter.BaseFilterQueryBuilder;
import com.lifeinide.rest.filter.enums.QueryConjunction;
import com.lifeinide.rest.filter.filters.*;
import com.lifeinide.rest.filter.intr.FilterQueryBuilder;
import com.lifeinide.rest.filter.intr.PageableResult;
import com.lifeinide.rest.filter.intr.PageableSortable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.support.Repositories;
import org.springframework.util.ReflectionUtils;

import javax.persistence.criteria.Predicate;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Implementation of {@link FilterQueryBuilder} with Spring Data. Use with dependency:
 *
 * <pre>{@code compile group: 'org.springframework.data', name: 'spring-data-jpa', version: '2.0.7.RELEASE'}</pre>
 *
 * @author Lukasz Frankowski
 */
public class SpringDataJpaFilterQueryBuilder<E>
extends BaseFilterQueryBuilder<E, Specification<E>, SpringDataJpaQueryBuilderContext, SpringDataJpaFilterQueryBuilder<E>> {

	protected SpringDataJpaQueryBuilderContext<E> context;
	protected Repositories repositories;

	/**
	 * @param repositories Used to find the {@code entityClass}-related repository. In Spring create it with
	 *                     {@code new Repositories(applicationContext)}, passing the current application context bean.
	 */
	public SpringDataJpaFilterQueryBuilder(Repositories repositories, Class<E> entityClass, QueryConjunction conjunction) {
		this.repositories = repositories;
		this.context = new SpringDataJpaQueryBuilderContext<>(entityClass, conjunction, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SpringDataJpaFilterQueryBuilder<E> add(String field, DateRangeQueryFilter filter) {
		if (filter!=null)
			addSpecification((Specification<E>) (root, query, criteriaBuilder) -> {
				Predicate fromPredicate = null;
				Predicate toPredicate = null;

				LocalDate from = filter.calculateFrom();
				LocalDate to = filter.calculateTo();

				Field reflectField = ReflectionUtils.findField(context.getEntityClass(), field);
				Comparable fromObject = (Comparable) filter.convert(from, reflectField);
				Comparable toObject = (Comparable) filter.convert(to, reflectField);

				// convert to instant if required
				if (Instant.class.isAssignableFrom(ReflectionUtils.findField(context.getEntityClass(), field).getType())) {
					fromObject = from.atStartOfDay(ZoneId.systemDefault()).toInstant();
					toObject = to.atStartOfDay(ZoneId.systemDefault()).toInstant();
				}

				if (from!=null)
					fromPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get(field), fromObject);
				if (to!=null)
					toPredicate = criteriaBuilder.lessThan(root.get(field), toObject);

				if (fromPredicate!=null && toPredicate!=null)
					return criteriaBuilder.and(fromPredicate, toPredicate);

				return fromPredicate!=null ? fromPredicate : toPredicate;
			});

		return this;
	}

	@Override
	public SpringDataJpaFilterQueryBuilder<E> add(String field, EntityQueryFilter filter) {
		if (filter!=null)
			addSpecification((Specification<E>) (root, query, criteriaBuilder) -> SpringDataJpaCriteriaBuilderHelper.INSTANCE.buildCriteria(
				filter.getCondition(), criteriaBuilder, root.get(field).get("id"), filter.getValue()));

		return this;
	}

	@Override
	public SpringDataJpaFilterQueryBuilder<E> add(String field, ListQueryFilter<?> filter) {
		if (filter!=null) {
			SpringDataJpaFilterQueryBuilder<E> internalBuilder =
				new SpringDataJpaFilterQueryBuilder<>(this.repositories, context.getEntityClass(), filter.getConjunction());

			filter.getFilters().forEach(it -> it.accept(internalBuilder, field));
			Specification<E> internalSpecification = context.getSpecification();
			if (internalSpecification!=null)
				addSpecification(internalSpecification);
		}

		return this;
	}

	@Override
	public SpringDataJpaFilterQueryBuilder<E> add(String field, SingleValueQueryFilter filter) {
		if (filter!=null)
			addSpecification((Specification<E>) (root, query, criteriaBuilder) -> SpringDataJpaCriteriaBuilderHelper.INSTANCE.buildCriteria(
				filter.getCondition(), criteriaBuilder, root.get(field), filter.getValue()));

		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SpringDataJpaFilterQueryBuilder<E> add(String field, ValueRangeQueryFilter filter) {
		if (filter!=null) {
			addSpecification((Specification<E>) (root, query, criteriaBuilder) -> {
				Predicate fromPredicate = null;
				Predicate toPredicate = null;

				if (filter.getFrom()!=null)
					fromPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get(field), (Comparable) filter.getFrom());
				if (filter.getTo()!=null)
					toPredicate = criteriaBuilder.lessThanOrEqualTo(root.get(field), (Comparable) filter.getTo());

				if (fromPredicate!=null && toPredicate!=null)
					return criteriaBuilder.and(fromPredicate, toPredicate);

				return fromPredicate!=null ? fromPredicate : toPredicate;
			});
		}

		return this;
	}

	@Override
	public Specification<E> build() {
		return context.getSpecification();
	}

	@Override
	public PageableResult<E> list(PageableSortable req) {
		return SpringPageableConverter.springPageToApplication(this, 
			findExecutor().findAll(build(), SpringPageableConverter.applicationPageableToSpring(req)));
	}

	@Override
	public SpringDataJpaQueryBuilderContext context() {
		return context;
	}

	@SuppressWarnings("unchecked")
	public JpaSpecificationExecutor<E> findExecutor() {
		Object repository = this.repositories.getRepositoryFor(context.getEntityClass()).orElseThrow(
			() -> new IllegalStateException(String.format("No repository found for: %s", context.getEntityClass().getSimpleName())));
		if (!(repository instanceof JpaSpecificationExecutor))
			throw new IllegalStateException(String.format("Found repository: %s for: %s is not JpaSpecificationExecutor",
				repository.getClass().getSimpleName(), context.getEntityClass().getSimpleName()));
		return (JpaSpecificationExecutor<E>) repository;
	}

	public void addSpecification(Specification<E> specification) {
		if (context.getSpecification()==null)
			context.setSpecification(specification);
		else
			context.setSpecification(SpringDataJpaCriteriaBuilderHelper.INSTANCE
				.conjunctCriteria(context.getConjunction(), context.getSpecification(), specification));
	}


}
