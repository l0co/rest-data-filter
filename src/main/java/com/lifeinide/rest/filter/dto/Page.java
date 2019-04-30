package com.lifeinide.rest.filter.dto;

import com.lifeinide.rest.filter.intr.Pageable;
import com.lifeinide.rest.filter.intr.PageableResult;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Lukasz Frankowski
 */
public class Page<T> implements Serializable, PageableResult<T>, Iterable<T> {

	protected int pageSize;
	protected int page;
	protected long count;
	protected int pagesCount;
	protected List<T> data;

	public Page() {
	}

	public Page(int pageSize, int page, long count, List<T> data) {
		this.pageSize = pageSize;
		this.page = page;
		this.count = count;
		this.data = data;
		this.pagesCount = (int) (this.count / pageSize + (this.count % pageSize == 0 ? 0 : 1));
	}

	public Page(Pageable pageable, long count, List<T> data) {
		this(pageable.getPageSize(), pageable.getPage(), count, data);
	}

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
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	@Override
	public int getPagesCount() {
		return pagesCount;
	}

	public void setPagesCount(int pagesCount) {
		this.pagesCount = pagesCount;
	}

	@Override
	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	/**********************************************************************************************************
	 * Iterable
	 **********************************************************************************************************/

	@Override
	public Iterator<T> iterator() {
		return data.iterator();
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		data.forEach(action);
	}

	@Override
	public Spliterator<T> spliterator() {
		return data.spliterator();
	}

}
