package com.lifeinide.rest.filter.test;

import com.lifeinide.rest.filter.dto.BaseRestFilter;
import com.lifeinide.rest.filter.intr.FilterQueryBuilder;
import com.lifeinide.rest.filter.intr.PageableResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Lukasz Frankowski
 */
@TestInstance(Lifecycle.PER_CLASS)
public abstract class BaseQueryBuilderTest<E extends IEntity, F extends FilterQueryBuilder<E, ?, F>> {

	/**
	 * Builds empty entity object
	 */
	protected abstract E buildEntity();

	/**
	 * Executes the filter query builder test
	 */
	protected abstract void doTest(Consumer<F> c);

	/**
	 * Should be executed in {@link BeforeAll} method to populate entities to the db
	 * @param save The consumer executing entity save
	 */
	protected void populateData(Consumer<IEntity> save) {
		StringGen sg = new StringGen();
		for (int i = 1; i <=100; i++) {
			IEntity entity = buildEntity();
			entity.setId((long) i);
			entity.setStringVal(sg.nextStr());
			entity.setLongVal((long) i);
			entity.setDecimalVal(new BigDecimal(i));
			entity.setDateVal(LocalDate.of(2018, Month.JANUARY, 1).plusDays(i-1));
			entity.setEnumVal(EntityEnum.values()[i % EntityEnum.values().length]);
			save.accept(entity);
		}
	}

	@Test
	public void testUnpaged() {
		doTest(qb -> {
			PageableResult<E> res = qb.list(BaseRestFilter.ofUnpaged());
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
			PageableResult<E> res = qb.list(BaseRestFilter.ofDefault().withPageSize(20));
			assertEquals(100, res.getCount());
			assertEquals(20, res.getData().size());
			assertEquals(20, res.getPageSize());
			assertEquals(5, res.getPagesCount());
			assertEquals(1, res.getPage());

			PageableResult<E> res2 = qb.list(BaseRestFilter.ofDefault().withPageSize(20).withPage(2));
			assertEquals(100, res2.getCount());
			assertEquals(20, res2.getData().size());
			assertEquals(20, res2.getPageSize());
			assertEquals(5, res2.getPagesCount());
			assertEquals(2, res2.getPage());

			for (E e: res2.getData()) {
				assertFalse(res.getData().contains(e));
			}
		});
	}


}
