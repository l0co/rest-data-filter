package com.lifeinide.rest.filter.test.hibernate.jpa;

import com.lifeinide.rest.filter.dto.BaseRestFilter;
import com.lifeinide.rest.filter.impl.jpa.JpaFilterQueryBuilder;
import com.lifeinide.rest.filter.intr.PageableResult;
import com.lifeinide.rest.filter.test.StringGen;
import com.lifeinide.rest.filter.test.hibernate.BaseHibernateJpaTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Lukasz Frankowski
 */
public class JpaQueryBuilderTest extends BaseHibernateJpaTest {

	@BeforeAll
	public static void populateData() {
		doWithEntityManager(em -> {
			StringGen sg = new StringGen();
			for (int i = 1; i <=100; i++) {
				JpaEntity entity = new JpaEntity(
					(long) i,
					sg.nextStr(),
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
			assertEquals(res.getCount(), 100);
			assertEquals(res.getData().size(), 100);
			assertEquals(res.getPageSize(), 0);
			assertEquals(res.getPagesCount(), 0);
			assertEquals(res.getPage(), 1);
		});
	}

	@Test
	public void testPaged() {
		doTest(qb -> {
			PageableResult<JpaEntity> res = qb.list(BaseRestFilter.ofDefault().withPageSize(20));
			assertEquals(100, res.getCount());
			assertEquals(20, res.getData().size());
			assertEquals(20, res.getPageSize());
			assertEquals(5, res.getPagesCount());
			assertEquals(1, res.getPage());

			PageableResult<JpaEntity> res2 = qb.list(BaseRestFilter.ofDefault().withPageSize(20).withPage(2));
			assertEquals(100, res2.getCount());
			assertEquals(20, res2.getData().size());
			assertEquals(20, res2.getPageSize());
			assertEquals(5, res2.getPagesCount());
			assertEquals(2, res2.getPage());

			for (JpaEntity e: res2.getData()) {
				assertFalse(res.getData().contains(e));
			}
		});
	}

}
