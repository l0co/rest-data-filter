package com.lifeinide.rest.filter.dto;

import com.lifeinide.rest.filter.intr.PageableSortable;
import com.lifeinide.rest.filter.intr.SortField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Base common request for filtering.
 *
 * @author Lukasz Frankowski
 */
public class BaseRestFilter<S extends SortField> implements Serializable, PageableSortable<S> {

	protected int pageSize = 20;

	protected int page = 1;

	protected List<S> sort = new ArrayList<>();

	@Override
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public BaseRestFilter withPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	@Override
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public BaseRestFilter withPage(int page) {
		this.page = page;
		return this;
	}

	@Override
	public List<S> getSort() {
		return sort;
	}

	public void setSort(List<S> sort) {
		this.sort = sort;
	}

	public BaseRestFilter withSort(S sort) {
		getSort().add(sort);
		return this;
	}

	public static BaseRestFilter ofDefault() {
		return new BaseRestFilter();
	}

	public static BaseRestFilter ofUnpaged() {
		return ofDefault().withPageSize(0);
	}

}
