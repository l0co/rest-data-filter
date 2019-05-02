package com.lifeinide.rest.filter.impl.hibernate;

import com.lifeinide.rest.filter.filters.SingleValueQueryFilter;
import com.lifeinide.rest.filter.filters.ValueRangeQueryFilter;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.builtin.NumberBridge;

/**
 * A {@link FieldBridge} used to store {@link Number} values so that they are searchable using {@link SingleValueQueryFilter} and
 * {@link ValueRangeQueryFilter}.
 *
 * <p>
 * By default Lucene performs text searchs even for ranges, so fox example "2" > "10" for Lucene. To assert proper numbers filtering
 * for Lucene we prepend all number with zeros, so that "*0002" > "*0010" and we can apply filtering properly.
 * </p>
 *
 * Usage:
 * <pre>{@code
 * @Field(analyze = Analyze.NO, norms = Norms.NO)
 * @FieldBridge(impl = RangeNumberBridge.class)
 * protected Long longVal;
 *
 * @Field(analyze = Analyze.NO, norms = Norms.NO)
 * @FieldBridge(impl = RangeNumberBridge.class)
 * protected BigDecimal decimalVal;
 * }</pre>
 *
 * @author Lukasz Frankowski
 */
public class RangeNumberBridge extends NumberBridge {

	public static final int NUMBER_SIZE = 20;
	public static final String ZEROS_PAD_TEMPLATE = "00000000000000000000";

	protected String padZeros(String s) {
		s = ZEROS_PAD_TEMPLATE + s;
		if (s.length() - NUMBER_SIZE > 0)
			return s.substring(s.length() - NUMBER_SIZE);
		return s;
	}

	@Override
	public String objectToString(Object object) {
		if (object==null)
			return null;

		return padZeros(super.objectToString(object));
	}

	@Override
	public Object stringToObject(String stringValue) {
		if (stringValue==null)
			return null;

		return Long.valueOf(stringValue);
	}
	
}
