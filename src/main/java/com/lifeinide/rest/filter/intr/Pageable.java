package com.lifeinide.rest.filter.intr;

/**
 * @author Lukasz Frankowski
 */
public interface Pageable {

	int getPageSize();
	int getPage();

	default int getOffset() {
		return (getPage()-1) * getPageSize();
	}

	default boolean isPaged() {
		return getPageSize() > 0;
	}


}
