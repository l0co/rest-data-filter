package com.lifeinide.rest.filter.enums;

import com.lifeinide.rest.filter.base.QueryFilter;

/**
 * Condition for {@link QueryFilter}.
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
