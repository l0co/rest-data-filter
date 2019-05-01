package com.lifeinide.rest.filter.impl.spring;

import com.lifeinide.rest.filter.dto.BaseRestFilter;
import com.lifeinide.rest.filter.enums.SortDirection;
import com.lifeinide.rest.filter.intr.PageableResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.stream.Collectors;

/**
 * @author Lukasz Frankowski
 */
public class SpringPageableConverter {

	public static PageRequest applicationPageableToSpring(BaseRestFilter pageable) {
		return PageRequest.of(pageable.getPage(), pageable.getPageSize(), Sort.by(
			pageable.getSort().stream()
				.map(sortField -> SortDirection.ASC.equals(sortField.getSortDirection())
					? Order.asc(sortField.getSortField())
					: Order.desc(sortField.getSortField()))
				.collect(Collectors.toList())
		));
	}

	public static <E> PageableResult<E> springPageToApplication(PageableResult.Builder<E> builder,
																org.springframework.data.domain.Page<E> page) {
		return builder.buildPageableResult(page.getPageable().getPageSize(), page.getPageable().getPageNumber(),
			page.getTotalElements(), page.getContent());
	}

}