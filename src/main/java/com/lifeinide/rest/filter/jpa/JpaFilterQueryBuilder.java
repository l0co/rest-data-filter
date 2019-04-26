package com.lifeinide.rest.filter.jpa;

import com.lifeinide.rest.filter.intr.FilterQueryBuilder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Implementation of {@link FilterQueryBuilder} with JPA {@link CriteriaBuilder}.
 * // TODOLF impl
 *
 * @author Lukasz Frankowski
 */
public abstract class JpaFilterQueryBuilder<E> implements FilterQueryBuilder<E, CriteriaQuery<E>, JpaFilterQueryBuilder<E>> {

	protected EntityManager entityManager;
	protected CriteriaBuilder cb;
	protected CriteriaQuery<E> query;
	protected CriteriaQuery<Long> countQuery;
	protected Root<E> root;

	public JpaFilterQueryBuilder(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.cb = cb = entityManager.getCriteriaBuilder();
	}

	public JpaFilterQueryBuilder(EntityManager entityManager, CriteriaQuery<E> query, Root<E> root) {
		this(entityManager);
		this.query = query;
		this.root = root;

		this.countQuery = cb.createQuery(Long.class).select(cb.count(countQuery.from(root.getModel())));
	}

	public JpaFilterQueryBuilder(EntityManager entityManager, Class<E> rootClass) {
		this(entityManager);
		this.query = cb.createQuery(rootClass);
		this.root = query.from(rootClass);

		this.countQuery = cb.createQuery(Long.class).select(cb.count(countQuery.from(rootClass)));
	}
	
}
