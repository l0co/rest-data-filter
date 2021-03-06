package com.lifeinide.rest.filter.dto;

import com.lifeinide.rest.filter.enums.SortDirection;
import com.lifeinide.rest.filter.intr.SortField;

import java.io.Serializable;

/**
 * @author Lukasz Frankowski
 */
public class Sort implements Serializable, SortField {

	protected String sortField;
	protected SortDirection sortDirection = SortDirection.ASC;

	@Override
	public String getSortField() {
		return sortField;
	}

	public void setField(String sortField) {
		this.sortField = sortField;
	}

	@Override
	public SortDirection getSortDirection() {
		return sortDirection;
	}

	public void setDirection(SortDirection sortDirection) {
		this.sortDirection = sortDirection;
	}

	public Sort with(String sortField) {
		setField(sortField);
		return this;
	}

	public Sort asc() {
		setDirection(SortDirection.ASC);
		return this;
	}

	public Sort desc() {
		setDirection(SortDirection.DESC);
		return this;
	}

	public static Sort of(String sortField) {
		return new Sort().with(sortField);
	}

	public static Sort ofAsc(String sortField) {
		return of(sortField);
	}

	public static Sort ofDesc(String sortField) {
		return of(sortField).desc();
	}

}
