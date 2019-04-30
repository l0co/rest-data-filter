package com.lifeinide.rest.filter.impl.spring;

import com.lifeinide.rest.filter.enums.QueryCondition;
import com.lifeinide.rest.filter.enums.QueryConjunction;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * @author Lukasz Frankowski
 */
public class SpringDataMongoCriteriaBuilderHelper {

	public static final SpringDataMongoCriteriaBuilderHelper INSTANCE = new SpringDataMongoCriteriaBuilderHelper();

	private SpringDataMongoCriteriaBuilderHelper() {
	}

	public Criteria buildCriteria(QueryCondition condition, String field, Object y) {
		switch (condition) {

			case eq:
				return Criteria.where(field).is(y);

			case ne:
				return Criteria.where(field).ne(y);

			case gt:
				return Criteria.where(field).gt(y);

			case ge:
				return Criteria.where(field).gte(y);

			case lt:
				return Criteria.where(field).lt(y);

			case le:
				return Criteria.where(field).lte(y);

			case isNull:
				return Criteria.where(field).exists(false);

			case notNull:
				return Criteria.where(field).exists(true);

			default:
				throw new UnsupportedOperationException(String.format("Criterion for: %s is not implemented yet", condition));
		}
	}

	public Criteria conjunctCriteria(QueryConjunction conjunction, Criteria ... criterias) {
		switch (conjunction) {

			case and:
				return new Criteria().andOperator(criterias);

			case or:
				return new Criteria().orOperator(criterias);

			default:
				throw new UnsupportedOperationException(String.format("Conjunction for: %s is not implemented yet", conjunction));
		}
	}


}
