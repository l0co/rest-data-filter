package com.lifeinide.rest.filter.test.hibernate.search;

import com.lifeinide.rest.filter.test.IBaseEntity;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Lukasz Frankowski
 */
@Entity
@Indexed
public class HibernateSearchAssociatedEntity implements IBaseEntity<Long> {

	@Id Long id;

	public HibernateSearchAssociatedEntity() {
	}

	public HibernateSearchAssociatedEntity(Long id) {
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
