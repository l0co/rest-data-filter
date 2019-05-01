package com.lifeinide.rest.filter.test.hibernate.jpa;

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
public class JpaEntity {

	@Id
	private Long id;

	protected String s;

	protected Long l;

	protected BigDecimal b;

	protected LocalDate d;

	@Enumerated(EnumType.STRING)
	protected JpaEntityEnum e;

	public JpaEntity() {
	}

	public JpaEntity(long id, String s, Long l, BigDecimal b, LocalDate d, JpaEntityEnum e) {
		this.id = id;
		this.s = s;
		this.l = l;
		this.b = b;
		this.d = d;
		this.e = e;
	}

	public Long getId() {
		return id;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public Long getL() {
		return l;
	}

	public void setL(Long l) {
		this.l = l;
	}

	public BigDecimal getB() {
		return b;
	}

	public void setB(BigDecimal b) {
		this.b = b;
	}

	public LocalDate getD() {
		return d;
	}

	public void setD(LocalDate d) {
		this.d = d;
	}

	public JpaEntityEnum getE() {
		return e;
	}

	public void setE(JpaEntityEnum e) {
		this.e = e;
	}

}
