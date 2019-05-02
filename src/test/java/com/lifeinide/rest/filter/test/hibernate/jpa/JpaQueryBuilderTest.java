package com.lifeinide.rest.filter.test.hibernate.jpa;

import com.lifeinide.rest.filter.impl.jpa.JpaFilterQueryBuilder;
import com.lifeinide.rest.filter.test.hibernate.BaseHibernateJpaTest;
import org.junit.jupiter.api.BeforeAll;

import javax.persistence.EntityManager;
import java.util.function.BiConsumer;

/**
 * @author Lukasz Frankowski
 */
public class JpaQueryBuilderTest extends BaseHibernateJpaTest<JpaAssociatedEntity, JpaEntity, JpaFilterQueryBuilder<JpaEntity>> {

	@BeforeAll
	public void populateData() {
		doWithEntityManager(em -> populateData(em::persist));
	}

	@Override
	protected JpaEntity buildEntity() {
		return new JpaEntity();
	}

	@Override
	protected JpaAssociatedEntity buildAssociatedEntity() {
		return new JpaAssociatedEntity("1");
	}

	@Override
	protected void doTest(BiConsumer<EntityManager, JpaFilterQueryBuilder<JpaEntity>> c) {
		doWithEntityManager(em -> c.accept(em, new JpaFilterQueryBuilder<>(em, JpaEntity.class)));
	}
	
}
