package com.lifeinide.rest.filter.test.hibernate;

import com.lifeinide.rest.filter.intr.FilterQueryBuilder;
import com.lifeinide.rest.filter.test.BaseQueryBuilderTest;
import com.lifeinide.rest.filter.test.IEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Consumer;

/**
 * @author Lukasz Frankowski
 */
public abstract class BaseHibernateJpaTest<E extends IEntity, F extends FilterQueryBuilder<E, ?, F>> extends BaseQueryBuilderTest<E, F> {

	public static final String PERSISTENCE_UNIT_NAME = "test-jpa";

	protected EntityManagerFactory entityManagerFactory;

	@BeforeAll
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	}

	@AfterAll
	public void done() {
		if (entityManagerFactory!=null)
			entityManagerFactory.close();
	}

	protected void doWithEntityManager(Consumer<EntityManager> c) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		try {
			c.accept(entityManager);
		} finally {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}

}
