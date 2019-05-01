package com.lifeinide.rest.filter.test.hibernate.jpa;

import com.lifeinide.rest.filter.test.EntityEnum;
import com.lifeinide.rest.filter.test.IEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Lukasz Frankowski
 */
@Entity
public class JpaEntity implements IEntity {

	@Id
	private Long id;

	protected String stringVal;

	protected Long longVal;

	protected BigDecimal decimalVal;

	protected LocalDate dateVal;

	@Enumerated(EnumType.STRING)
	protected EntityEnum enumVal;

	public JpaEntity() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getStringVal() {
		return stringVal;
	}

	@Override
	public void setStringVal(String stringVal) {
		this.stringVal = stringVal;
	}

	@Override
	public Long getLongVal() {
		return longVal;
	}

	@Override
	public void setLongVal(Long longVal) {
		this.longVal = longVal;
	}

	@Override
	public BigDecimal getDecimalVal() {
		return decimalVal;
	}

	@Override
	public void setDecimalVal(BigDecimal decimalVal) {
		this.decimalVal = decimalVal;
	}

	@Override
	public LocalDate getDateVal() {
		return dateVal;
	}

	@Override
	public void setDateVal(LocalDate dateVal) {
		this.dateVal = dateVal;
	}

	@Override
	public EntityEnum getEnumVal() {
		return enumVal;
	}

	@Override
	public void setEnumVal(EntityEnum enumVal) {
		this.enumVal = enumVal;
	}

}
