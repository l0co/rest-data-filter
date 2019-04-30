package com.lifeinide.rest.filter.intr;

import java.util.List;

/**
 * @author Lukasz Frankowski
 */
public interface PageableResult<T> extends Pageable {

	@FunctionalInterface
	public interface Builder<E> {
		PageableResult<E> buildPageableResult(int pageSize, int page, long count, List<E> data);
	}

	long getCount();
	int getPagesCount();
	List<T> getData();

}
