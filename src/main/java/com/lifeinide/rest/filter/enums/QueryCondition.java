package com.lifeinide.rest.filter.enums;

import com.lifeinide.rest.filter.filters.SingleValueQueryFilter;

/**
 * Condition for {@link SingleValueQueryFilter}.
 *
 * @author Lukasz Frankowski
 */
public enum QueryCondition {

	/** Equals **/
	eq,

	/** Not equals **/
	ne,

	/** Greater than **/
	gt,

	/** Greater or equal than **/
	ge,

	/** Lower than **/
	lt,

	/** Lower or equal than **/
	le,

	/** Is null **/
	isNull,

	/** Is not null **/
	notNull

}
