package com.lifeinide.rest.filter;

/**
 * Default interface for list filter. The filter gets the data from the client (usually in JSON) and then is converted to database query
 * using appropriate {@link IFilterQueryBuilder}.
 *
 * @author Lukasz Frankowski
 */
public interface IQueryFilter {

	default void accept(IFilterQueryBuilder builder, String field) {
		builder.add(field, this);
	}

}
