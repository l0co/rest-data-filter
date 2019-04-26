package com.lifeinide.rest.filter.intr;

import com.lifeinide.rest.filter.enums.SortDirection;

/**
 * @author Lukasz Frankowski
 */
public interface SortField {

	String getSortField();
	SortDirection getSortDirection();

}
