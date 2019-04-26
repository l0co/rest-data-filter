package com.lifeinide.rest.filter.enums;

import com.lifeinide.rest.filter.base.DateRangeQueryFilter;

import java.time.LocalDate;

/**
 * Constants for {@link DateRangeQueryFilter}.
 *
 * @author Lukasz Frankowski
 */
public enum DateRange {

	CUSTOM,

	LAST_30_DAYS {

		@Override
		public LocalDate getFrom() {
			return LocalDate.now().atStartOfDay().minusDays(30).toLocalDate();
		}

		@Override
		public LocalDate getTo() {
			return LocalDate.now().atStartOfDay().toLocalDate();
		}
		
	},

	LAST_90_DAYS {

		@Override
		public LocalDate getFrom() {
			return LocalDate.now().atStartOfDay().minusDays(90).toLocalDate();
		}

		@Override
		public LocalDate getTo() {
			return LocalDate.now().atStartOfDay().toLocalDate();
		}

	},

	CURRENT_MONTH {

		@Override
		public LocalDate getFrom() {
			return LocalDate.now().atStartOfDay().withDayOfMonth(1).toLocalDate();
		}

		@Override
		public LocalDate getTo() {
			return getFrom().plusMonths(1).minusDays(1);
		}
		
	},

	PREVIOUS_MONTH {

		@Override
		public LocalDate getFrom() {
			return CURRENT_MONTH.getFrom().minusMonths(1);
		}

		@Override
		public LocalDate getTo() {
			return getFrom().plusMonths(1).minusDays(1);
		}

	},

	CURRENT_YEAR {

		@Override
		public LocalDate getFrom() {
			return CURRENT_MONTH.getFrom().withMonth(1);
		}

		@Override
		public LocalDate getTo() {
			return getFrom().plusYears(1).minusDays(1);
		}

	},

	PREVIOUS_YEAR {

		@Override
		public LocalDate getFrom() {
			return CURRENT_YEAR.getFrom().minusYears(1);
		}

		@Override
		public LocalDate getTo() {
			return CURRENT_YEAR.getTo().minusYears(1);
		}

	};

	public LocalDate getFrom() {
		return null;
	}

	public LocalDate getTo() {
		return null;
	}

}
