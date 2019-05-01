package com.lifeinide.rest.filter.test.hibernate.jpa;

import com.lifeinide.rest.filter.dto.BaseRestFilter;
import com.lifeinide.rest.filter.impl.jpa.JpaFilterQueryBuilder;
import com.lifeinide.rest.filter.intr.PageableResult;
import com.lifeinide.rest.filter.test.hibernate.BaseHibernateJpaTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.function.Consumer;

/**
 * @author Lukasz Frankowski
 */
public class JpaQueryBuilderTest extends BaseHibernateJpaTest {

	@BeforeAll
	public static void populateData() {
		doWithEntityManager(em -> {
			for (int i = 1; i <=100; i++) {
				JpaEntity entity = new JpaEntity(
					(long) i,
					"",
					(long) i,
					new BigDecimal(i),
					LocalDate.of(2018, Month.JANUARY, 1).plusDays(i-1),
					JpaEntityEnum.values()[i % JpaEntityEnum.values().length]);
				em.persist(entity);
			}
		});
	}

	protected void doTest(Consumer<JpaFilterQueryBuilder<JpaEntity>> c) {
		doWithEntityManager(em -> c.accept(new JpaFilterQueryBuilder<>(em, JpaEntity.class)));
	}

	@Test
	public void testUnpaged() {
		doTest(qb -> {
			PageableResult<JpaEntity> res = qb.list(BaseRestFilter.ofUnpaged());
			Assertions.assertEquals(res.getCount(), 100);
			Assertions.assertEquals(res.getData().size(), 100);
			Assertions.assertEquals(res.getPageSize(), 0);
			Assertions.assertEquals(res.getPagesCount(), 0);
			Assertions.assertEquals(res.getPage(), 1);
		});
	}

	@Test
	public void testPaged() {
		doTest(qb -> {
			PageableResult<JpaEntity> res = qb.list(BaseRestFilter.ofDefault().withPageSize(20));
			Assertions.assertEquals(res.getCount(), 100);
			Assertions.assertEquals(res.getData().size(), 20);
			Assertions.assertEquals(res.getPageSize(), 20);
			Assertions.assertEquals(res.getPagesCount(), 5);
			Assertions.assertEquals(res.getPage(), 1);
		});
	}

}
