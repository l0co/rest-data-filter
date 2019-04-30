package com.lifeinide.rest.filter.impl.spring;

import com.lifeinide.rest.filter.BaseQueryBuilderContext;
import com.lifeinide.rest.filter.enums.QueryConjunction;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Frankowski
 */
public class SpringDataMongoQueryBuilderContext<E> extends BaseQueryBuilderContext {

	protected Class<E> entityClass;
	protected QueryConjunction conjunction;
	protected List<Criteria> criterias = new ArrayList<>();

	public SpringDataMongoQueryBuilderContext(Class<E> entityClass, QueryConjunction conjunction) {
		this.entityClass = entityClass;
		this.conjunction = conjunction;
	}

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public QueryConjunction getConjunction() {
		return conjunction;
	}

	public List<Criteria> getCriterias() {
		return criterias;
	}
	
}
