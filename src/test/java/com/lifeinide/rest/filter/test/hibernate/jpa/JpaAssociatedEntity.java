package com.lifeinide.rest.filter.test.hibernate.jpa;

import com.lifeinide.rest.filter.test.IBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Lukasz Frankowski
 */
@Entity
public class JpaAssociatedEntity implements IBaseEntity {

	@Id String id;

	public JpaAssociatedEntity() {
	}

	public JpaAssociatedEntity(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
	
}
