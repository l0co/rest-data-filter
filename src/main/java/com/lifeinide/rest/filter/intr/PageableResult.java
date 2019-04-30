package com.lifeinide.rest.filter.intr;

import java.util.List;

/**
 * @author Lukasz Frankowski
 */
public interface PageableResult<T> extends Pageable {

	long getCount();
	int getPagesCount();
	List<T> getData();

}
