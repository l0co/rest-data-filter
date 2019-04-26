package com.lifeinide.rest.filter.dto;

import com.lifeinide.rest.filter.enums.SortDirection;
import com.lifeinide.rest.filter.intr.SortField;

import java.io.Serializable;

/**
 * @author Lukasz Frankowski
 */
public class Sort implements Serializable, SortField {

	protected String sortField;
	protected SortDirection sortDirection;

	public Sort() {
	}

	public Sort(String sortField, SortDirection sortDirection) {
		this.sortField = sortField;
		this.sortDirection = sortDirection;
	}

	@Override
	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	@Override
	public SortDirection getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(SortDirection sortDirection) {
		this.sortDirection = sortDirection;
	}
}
