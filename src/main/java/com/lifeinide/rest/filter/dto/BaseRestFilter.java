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

	protected Integer pageSize;

	protected Integer page = 1;

	protected List<S> sort = new ArrayList<>();

	@Override
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public BaseRestFilter withPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	@Override
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public BaseRestFilter withPage(Integer page) {
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
		return ofDefault();
	}

}
