package com.lifeinide.rest.filter.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Base common request for filtering.
 *
 * @author Lukasz Frankowski
 */
public class BaseFilterDTO implements Pageable {

	protected int page;
	protected int size;
	protected Sort sort = Sort.unsorted();
	protected String query;

	@SuppressWarnings("deprecation")
	public BaseFilterDTO() {
		this.page = 0;
		this.size = 20;
	}

	public BaseFilterDTO(int page, int size) {
		this.page = page;
		this.size = size;
	}

	public BaseFilterDTO(int page, int size, Sort sort) {
		this(page, size);
		this.sort = sort;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	@NonNull
	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	public void setBindableSort(List<String> list) {
		sort = Sort.by(list.stream()
			.map(it -> it.trim().split(" "))
			.map(it -> it.length==1 || "asc".equals(it[1].toLowerCase()) ? Order.asc(it[0]) : Order.desc(it[0]))
			.collect(Collectors.toList())
		);
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public boolean isFullTextQuery() {
		return !isEmpty(query);
	}

	/**********************************************************************************************************
	 * Pageable implementation
	 **********************************************************************************************************/

	@Override
	public int getPageSize() {
		return size;
	}

	@Override
	public int getPageNumber() {
		return page;
	}

	@Override
	public long getOffset() {
		return page * size;
	}

	@Override
	public boolean hasPrevious() {
		return page > 0;
	}

	@Override
	@NonNull
	public Pageable previousOrFirst() {
		return hasPrevious() ? previous() : first();
	}

	@Override
	@NonNull
	public Pageable next() {
		return new BaseFilterDTO(getPageNumber() + 1, getPageSize(), getSort());
	}

	@NonNull
	public Pageable previous() {
		return getPageNumber() == 0 ? this : new BaseFilterDTO(getPageNumber() - 1, getPageSize(), getSort());
	}

	@Override
	@NonNull
	public Pageable first() {
		return new BaseFilterDTO(0, getPageSize(), getSort());
	}

	public PageRequest toPageRequest() {
		return PageRequest.of(page, size, sort);
	}

}
