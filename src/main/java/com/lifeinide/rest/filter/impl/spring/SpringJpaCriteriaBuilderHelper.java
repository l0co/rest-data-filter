package com.lifeinide.rest.filter.impl.spring;

import com.lifeinide.rest.filter.enums.QueryConjunction;
import com.lifeinide.rest.filter.impl.jpa.JpaCriteriaBuilderHelper;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author Lukasz Frankowski
 */
public class SpringJpaCriteriaBuilderHelper extends JpaCriteriaBuilderHelper {

	public static final SpringJpaCriteriaBuilderHelper INSTANCE = new SpringJpaCriteriaBuilderHelper();

	public <E> Specification<E> conjunctCriteria(QueryConjunction conjunction, Specification<E> left, Specification<E> right) {
		switch (conjunction) {

			case and:
				return left.and(right);

			case or:
				return left.or(right);

			default:
				throw new UnsupportedOperationException(String.format("Conjunction for: %s is not implemented yet", conjunction));
		}
	}

}
