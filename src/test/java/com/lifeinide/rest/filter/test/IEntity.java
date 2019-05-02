package com.lifeinide.rest.filter.test;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @param <A> Associated entity type.
 * @author Lukasz Frankowski
 */
public interface IEntity<A extends IBaseEntity> extends IBaseEntity {

	String getStringVal();

	void setStringVal(String stringVal);

	Long getLongVal();

	void setLongVal(Long longVal);

	BigDecimal getDecimalVal();

	void setDecimalVal(BigDecimal decimalVal);

	LocalDate getDateVal();

	void setDateVal(LocalDate dateVal);

	EntityEnum getEnumVal();

	void setEnumVal(EntityEnum enumVal);

	A getEntityVal();

	void setEntityVal(A entityVal);
	
}
