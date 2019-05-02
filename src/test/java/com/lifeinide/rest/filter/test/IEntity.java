package com.lifeinide.rest.filter.test;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Lukasz Frankowski
 */
public interface IEntity {

	String getId();

	void setId(String id);

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
	
}
