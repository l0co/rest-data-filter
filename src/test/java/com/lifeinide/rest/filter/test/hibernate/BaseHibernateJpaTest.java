package com.lifeinide.rest.filter.test.hibernate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Lukasz Frankowski
 */
public abstract class BaseHibernateJpaTest {

	public static final String PERSISTENCE_UNIT_NAME = "test-jpa";

	protected static EntityManagerFactory entityManagerFactory;

	@BeforeAll
	public static void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	}

	@AfterAll
	public static void done() {
		if (entityManagerFactory!=null)
			entityManagerFactory.close();
	}

}
