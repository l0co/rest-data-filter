package com.lifeinide.rest.filter.test.hibernate.search;

import com.lifeinide.rest.filter.impl.hibernate.HibernateSearch;
import com.lifeinide.rest.filter.impl.hibernate.HibernateSearchFilterQueryBuilder;
import com.lifeinide.rest.filter.test.hibernate.BaseHibernateJpaTest;
import org.junit.jupiter.api.BeforeAll;

import javax.persistence.EntityManager;
import java.util.function.BiConsumer;

/**
 * @author Lukasz Frankowski
 */
public class HibernateSearchQueryBuilderTest extends BaseHibernateJpaTest<Long, HibernateSearchAssociatedEntity, HibernateSearchEntity, HibernateSearchFilterQueryBuilder<HibernateSearchEntity>> {

	public static final String SEARCHABLE_STRING = "in the middle of";

	@BeforeAll
	public void populateData() {
		doWithEntityManager(em -> populateData(em::persist));
	}

	@Override
	protected HibernateSearchEntity buildEntity(Long previousId) {
		return new HibernateSearchEntity(previousId==null ? 1L : previousId+1);
	}

	@Override
	protected HibernateSearchAssociatedEntity buildAssociatedEntity() {
		return new HibernateSearchAssociatedEntity(1L);
	}

	@Override
	protected void doTest(BiConsumer<EntityManager, HibernateSearchFilterQueryBuilder<HibernateSearchEntity>> c) {
		doWithEntityManager(em -> c.accept(em,
			new HibernateSearchFilterQueryBuilder<>(new HibernateSearch(em), HibernateSearchEntity.class, SEARCHABLE_STRING)));
	}
	
}
