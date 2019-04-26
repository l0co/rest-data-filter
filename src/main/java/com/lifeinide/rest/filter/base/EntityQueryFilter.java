package com.lifeinide.rest.filter.base;

import com.lifeinide.rest.filter.IFilterQueryBuilder;
import com.lifeinide.rest.filter.enums.QueryCondition;

import java.io.Serializable;

/**
 * Filter for entities. Special kind of {@link QueryFilter} carrying the entity ID.
 *
 * @author Lukasz Frankowski
 */
public class EntityQueryFilter<ID extends Serializable> extends QueryFilter<ID> {

	public EntityQueryFilter() {
	}

	public EntityQueryFilter(ID value) {
		super(value);
	}

	public EntityQueryFilter(ID value, QueryCondition condition) {
		super(value, condition);
	}

	@Override
	public void accept(IFilterQueryBuilder builder, String field) {
		builder.add(field, this);
	}

}
