package com.lifeinide.rest.filter.test;

import com.lifeinide.rest.filter.enums.QueryCondition;

/**
 * @author Lukasz Frankowski
 */
public enum QueryBuilderTestFeature {

	/**
	 * Whether supports {@code >} and {@code <} inequalites, i.e. {@link QueryCondition#ge} and {@link QueryCondition#le}. For example
	 * lucene queries don't support them. They support only non-strict equalites {@code >=} and {@code <=}.
	 */
	STRICT_INEQUALITIES,

	/**
	 * Whether isNull / notNull queries are supported. Unsupported for example for Lucene queries.
	 */
	NULLS

}
