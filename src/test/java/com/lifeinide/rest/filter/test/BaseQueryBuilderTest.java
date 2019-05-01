package com.lifeinide.rest.filter.test;

import com.lifeinide.rest.filter.dto.BaseRestFilter;
import com.lifeinide.rest.filter.filters.ListQueryFilter;
import com.lifeinide.rest.filter.filters.SingleValueQueryFilter;
import com.lifeinide.rest.filter.filters.ValueRangeQueryFilter;
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

import static org.junit.jupiter.api.Assertions.*;

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

	@Test
	public void testStringFilter() {
		doTest(qb -> {
			PageableResult<E> res = qb
				.add("stringVal", SingleValueQueryFilter.of("aa"))
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(1, res.getCount());
			assertEquals("aa", res.getData().iterator().next().getStringVal());
		});

		doTest(qb -> {
			PageableResult<E> res = qb
				.add("stringVal", SingleValueQueryFilter.of("aa").ne())
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(99, res.getCount());
		});

		doTest(qb -> {
			PageableResult<E> res = qb
				.add("stringVal", SingleValueQueryFilter.of("aa").ge())
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(100, res.getCount());
		});

		doTest(qb -> {
			PageableResult<E> res = qb
				.add("stringVal", SingleValueQueryFilter.of("aa").gt())
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(99, res.getCount());
		});

		doTest(qb -> {
			PageableResult<E> res = qb
				.add("stringVal", SingleValueQueryFilter.of("ba").le())
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(27, res.getCount());
		});

		doTest(qb -> {
			PageableResult<E> res = qb
				.add("stringVal", SingleValueQueryFilter.of("ba").lt())
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(26, res.getCount());
		});

		doTest(qb -> {
			PageableResult<E> res = qb
				.add("stringVal", SingleValueQueryFilter.ofNotNull())
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(100, res.getCount());
		});

		doTest(qb -> {
			PageableResult<E> res = qb
				.add("stringVal", SingleValueQueryFilter.ofNull())
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(0, res.getCount());
		});

		doTest(qb -> {
			PageableResult<E> res = qb
				.add("stringVal", ListQueryFilter.of(SingleValueQueryFilter.of("aa"), SingleValueQueryFilter.of("ab")))
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(2, res.getCount());
		});

		doTest(qb -> {
			PageableResult<E> res = qb
				.add("stringVal", ListQueryFilter.of(SingleValueQueryFilter.of("aa"), SingleValueQueryFilter.of("ab")).and())
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(0, res.getCount());
		});
	}

	@Test
	public void testEnumFilter() {
		doTest(qb -> {
			PageableResult<E> res = qb
				.add("enumVal", SingleValueQueryFilter.of(EntityEnum.A))
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(33, res.getCount());
			for (E e: res)
				assertEquals(EntityEnum.A, e.getEnumVal());
		});

		doTest(qb -> {
			PageableResult<E> res = qb
				.add("enumVal", SingleValueQueryFilter.of(EntityEnum.A).ne())
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(67, res.getCount());
			for (E e: res)
				assertNotEquals(EntityEnum.A, e.getEnumVal());
		});

		doTest(qb -> {
			PageableResult<E> res = qb
				.add("enumVal", ListQueryFilter.of(SingleValueQueryFilter.of(EntityEnum.A), SingleValueQueryFilter.of(EntityEnum.B)))
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(67, res.getCount());
			for (E e: res)
				assertNotEquals(EntityEnum.C, e.getEnumVal());
		});
	}

	@Test
	public void testLongFilter() {
		doTest(qb -> {
			PageableResult<E> res = qb
				.add("longVal", SingleValueQueryFilter.of(1L))
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(1, res.getCount());
			assertEquals(1L, (long) res.iterator().next().getLongVal());
		});

		doTest(qb -> {
			PageableResult<E> res = qb
				.add("longVal", SingleValueQueryFilter.of(1L).ge())
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(100, res.getCount());
		});

		doTest(qb -> {
			PageableResult<E> res = qb
				.add("longVal", SingleValueQueryFilter.of(1L).gt())
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(99, res.getCount());
		});

		doTest(qb -> {
			PageableResult<E> res = qb
				.add("longVal", ValueRangeQueryFilter.ofFrom(10L))
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(91, res.getCount());
		});

		doTest(qb -> {
			PageableResult<E> res = qb
				.add("longVal", ValueRangeQueryFilter.ofTo(10L))
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(10, res.getCount());
		});

		doTest(qb -> {
			PageableResult<E> res = qb
				.add("longVal", ValueRangeQueryFilter.of(10L, 20L))
				.list(BaseRestFilter.ofUnpaged());
			assertEquals(11, res.getCount());
		});
	}

	@Test
	public void testDecimalFilter() {
		// TODOLF implement BaseQueryBuilderTest.testDecimalFilter
		doTest(qb -> {
			PageableResult<E> res = qb.list(BaseRestFilter.ofUnpaged());
		});
	}

	@Test
	public void testDateFilter() {
		// TODOLF implement BaseQueryBuilderTest.testDateFilter
		doTest(qb -> {
			PageableResult<E> res = qb.list(BaseRestFilter.ofUnpaged());
		});
	}

	@Test
	public void testAndListFilter() {
		// TODOLF implement BaseQueryBuilderTest.testAndListFilter
		doTest(qb -> {
			PageableResult<E> res = qb.list(BaseRestFilter.ofUnpaged());
		});
	}

	@Test
	public void testOrListFilter() {
		// TODOLF implement BaseQueryBuilderTest.testOrListFilter
		doTest(qb -> {
			PageableResult<E> res = qb.list(BaseRestFilter.ofUnpaged());
		});
	}

	@Test
	public void testMultipleFilters() {
		// TODOLF implement BaseQueryBuilderTest.testOrListFilter
		doTest(qb -> {
			PageableResult<E> res = qb.list(BaseRestFilter.ofUnpaged());
		});
	}

	// TODOLF entity test

}
