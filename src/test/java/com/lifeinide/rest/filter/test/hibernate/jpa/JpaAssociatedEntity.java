package com.lifeinide.rest.filter.test.hibernate.jpa;

import com.lifeinide.rest.filter.test.IBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Lukasz Frankowski
 */
@Entity
public class JpaAssociatedEntity implements IBaseEntity<Long> {

	@Id Long id;

	public JpaAssociatedEntity() {
	}

	public JpaAssociatedEntity(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
}
