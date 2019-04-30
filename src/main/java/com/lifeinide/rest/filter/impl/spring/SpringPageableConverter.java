package com.lifeinide.rest.filter.impl.spring;

import com.lifeinide.rest.filter.dto.Page;
import com.lifeinide.rest.filter.intr.Pageable;
import org.springframework.data.domain.PageRequest;

/**
 * @author Lukasz Frankowski
 */
public class SpringPageableConverter {

	public static PageRequest applicationPageableToSpring(Pageable pageable) {
		// TODOLF implement SpringPageableConverter.applicationPageableToSpring
		return null;
	}

	// TODOLF add builder
	public static <E> Page<E> springPageToApplication(org.springframework.data.domain.Page<E> page) {
		// TODOLF implement SpringPageableConverter.springPageToApplication
		return null;
	}

}
