package com.lifeinide.rest.filter.intr;

import java.util.List;

/**
 * @author Lukasz Frankowski
 */
public interface PageableResultBuilder<T> {

	PageableResult<T> buildPageableResult(Pageable pageable, int count, List<T> data);

}
