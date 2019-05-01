package com.lifeinide.rest.filter.filters;

import com.lifeinide.rest.filter.intr.FilterQueryBuilder;

import java.io.Serializable;

/**
 * Filter for entities. Special kind of {@link SingleValueQueryFilter} carrying the entity ID.
 *
 * @author Lukasz Frankowski
 */
public class EntityQueryFilter<ID extends Serializable> extends SingleValueQueryFilter<ID> {

	public EntityQueryFilter() {
	}

	@Override
	public void accept(FilterQueryBuilder builder, String field) {
		builder.add(field, this);
	}

}
