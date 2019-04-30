package com.lifeinide.rest.filter.impl.jpa;

import com.lifeinide.rest.filter.BaseQueryBuilderContext;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Frankowski
 */
public class JpaQueryBuilderContext<E> extends BaseQueryBuilderContext {

	protected EntityManager entityManager;
	protected CriteriaBuilder cb;
	protected Root<E> root;
	protected CriteriaQuery<E> query;
	List<Predicate> predicates = new ArrayList<>();

	public JpaQueryBuilderContext(EntityManager entityManager, CriteriaBuilder cb) {
		this.entityManager = entityManager;
		this.cb = cb;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public CriteriaBuilder getCb() {
		return cb;
	}

	public Root<E> getRoot() {
		return root;
	}

	public void setRoot(Root<E> root) {
		this.root = root;
	}

	public CriteriaQuery<E> getQuery() {
		return query;
	}

	public void setQuery(CriteriaQuery<E> query) {
		this.query = query;
	}

	public List<Predicate> getPredicates() {
		return predicates;
	}
	
}
