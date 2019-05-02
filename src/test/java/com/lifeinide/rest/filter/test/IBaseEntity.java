package com.lifeinide.rest.filter.test;

import java.io.Serializable;

/**
 * @author Lukasz Frankowski
 */
public interface IBaseEntity<ID extends Serializable> {

	ID getId();

	void setId(ID id);
	
}
