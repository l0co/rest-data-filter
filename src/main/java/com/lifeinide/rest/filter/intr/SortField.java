package com.lifeinide.rest.filter.intr;

import com.lifeinide.rest.filter.enums.SortDirection;

/**
 * @author Lukasz Frankowski
 */
public interface SortField {

	String getSortField();
	SortDirection getSortDirection();

	default boolean isAsc() {
		return SortDirection.ASC.equals(getSortDirection());
	}

	default boolean isDesc() {
		return !isAsc();
	}

}
