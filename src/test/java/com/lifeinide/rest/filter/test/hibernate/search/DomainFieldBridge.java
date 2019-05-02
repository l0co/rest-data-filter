package com.lifeinide.rest.filter.test.hibernate.search;

import com.lifeinide.rest.filter.impl.hibernate.BaseDomainFieldBridge;
import com.lifeinide.rest.filter.test.IBaseEntity;

/**
 * @author Lukasz Frankowski
 */
public class DomainFieldBridge extends BaseDomainFieldBridge<IBaseEntity<Long>> {

	@Override
	public String getEntityIdAsString(IBaseEntity<Long> entity) {
		return String.valueOf(entity.getId());
	}

	@Override
	public boolean isEntity(Object entity) {
		return entity instanceof IBaseEntity;
	}
	
}
