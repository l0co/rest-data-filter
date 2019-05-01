package com.lifeinide.rest.filter.test.hibernate.jpa;

import com.lifeinide.rest.filter.impl.jpa.JpaFilterQueryBuilder;
import com.lifeinide.rest.filter.test.hibernate.BaseHibernateJpaTest;
import org.junit.jupiter.api.BeforeAll;

import java.util.function.Consumer;

/**
 * @author Lukasz Frankowski
 */
public class JpaQueryBuilderTest extends BaseHibernateJpaTest<JpaEntity, JpaFilterQueryBuilder<JpaEntity>> {

	@BeforeAll
	public void populateData() {
		doWithEntityManager(em -> populateData(em::persist));
	}

	@Override
	protected JpaEntity buildEntity() {
		return new JpaEntity();
	}

	@Override
	protected void doTest(Consumer<JpaFilterQueryBuilder<JpaEntity>> c) {
		doWithEntityManager(em -> c.accept(new JpaFilterQueryBuilder<>(em, JpaEntity.class)));
	}
	
}
