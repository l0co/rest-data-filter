package com.lifeinide.rest.filter.intr;

import java.util.Iterator;
import java.util.List;

/**
 * @author Lukasz Frankowski
 */
public interface PageableResult<T> extends Pageable, Iterable<T> {

	@FunctionalInterface
	interface Builder<E> {
		PageableResult<E> buildPageableResult(int pageSize, int page, long count, List<E> data);
	}

	long getCount();
	int getPagesCount();
	List<T> getData();

	@Override
	default Iterator<T> iterator() {
		return getData().iterator();
	}

}
