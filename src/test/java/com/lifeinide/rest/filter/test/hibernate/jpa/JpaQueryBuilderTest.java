package com.lifeinide.rest.filter.test.hibernate.jpa;

import com.lifeinide.rest.filter.dto.Page;
import com.lifeinide.rest.filter.impl.jpa.JpaFilterQueryBuilder;
import com.lifeinide.rest.filter.test.hibernate.BaseHibernateJpaTest;
import org.junit.jupiter.api.BeforeAll;

import javax.persistence.EntityManager;
import java.util.function.BiConsumer;

/**
 * @author Lukasz Frankowski
 */
public class JpaQueryBuilderTest extends BaseHibernateJpaTest<Long, JpaAssociatedEntity, JpaEntity,
JpaFilterQueryBuilder<JpaEntity, Page<JpaEntity>>> {

	@BeforeAll
	public void populateData() {
		doWithEntityManager(em -> populateData(em::persist));
	}

	@Override
	protected JpaEntity buildEntity(Long previousId) {
		return new JpaEntity(previousId==null ? 1L : previousId+1);
	}

	@Override
	protected JpaAssociatedEntity buildAssociatedEntity() {
		return new JpaAssociatedEntity(1L);
	}

	@Override
	protected void doTest(BiConsumer<EntityManager, JpaFilterQueryBuilder<JpaEntity, Page<JpaEntity>>> c) {
		doWithEntityManager(em -> c.accept(em, new JpaFilterQueryBuilder<>(em, JpaEntity.class)));
	}
	
}
