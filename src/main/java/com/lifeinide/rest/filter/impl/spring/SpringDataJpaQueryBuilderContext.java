package com.lifeinide.rest.filter.impl.spring;

import com.lifeinide.rest.filter.BaseQueryBuilderContext;
import com.lifeinide.rest.filter.enums.QueryConjunction;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author Lukasz Frankowski
 */
public class SpringDataJpaQueryBuilderContext<E> extends BaseQueryBuilderContext {

	protected Class<E> entityClass;
	protected QueryConjunction conjunction;
	protected Specification<E> specification = null;

	public SpringDataJpaQueryBuilderContext(Class<E> entityClass, QueryConjunction conjunction, Specification<E> specification) {
		this.entityClass = entityClass;
		this.conjunction = conjunction;
		this.specification = specification;
	}

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public QueryConjunction getConjunction() {
		return conjunction;
	}

	public Specification<E> getSpecification() {
		return specification;
	}

	public void setSpecification(Specification<E> specification) {
		this.specification = specification;
	}
}
