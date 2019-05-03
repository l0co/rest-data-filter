package com.lifeinide.rest.filter.intr;

/**
 * @author lukasz.frankowski@gmail.com
 */
public interface PageableSortable<S extends SortField> extends Pageable, Sortable<S> {
}
