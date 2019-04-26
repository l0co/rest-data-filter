package com.lifeinide.rest.filter.dto;

import com.lifeinide.rest.filter.intr.Pageable;
import com.lifeinide.rest.filter.intr.SortField;
import com.lifeinide.rest.filter.intr.Sortable;

import java.io.Serializable;
import java.util.List;

/**
 * Base common request for filtering.
 *
 * @author Lukasz Frankowski
 */
public class BaseRestFilter implements Serializable, Sortable, Pageable {

	protected int pageSize = 20;

	protected int page = 1;

	protected List<SortField> sort;

	@Override
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	@Override
	public List<SortField> getSort() {
		return sort;
	}

	public void setSort(List<SortField> sort) {
		this.sort = sort;
	}
	
}
