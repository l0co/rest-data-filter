package com.lifeinide.rest.filter.impl.hibernate;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.engine.spi.QueryDescriptor;

import javax.persistence.EntityManager;

/**
 * Hibernate search helper.
 *
 * @author Lukasz Frankowski
 */
public class HibernateSearch {

	/**
	 * A common field for storing text content.
	 * <p>
	 * This kind of field is appropriate to index <strong>case-insensitively all natural-language fields</strong> and should be analyzed
	 * with {@link #FIELD_ID_ANALYZER}. The indexed entity field has has usually the following definition:
	 * <pre>{@code
	 *@literal @Field(name = HibernateSearch.FIELD_TEXT)
	 *@literal @Analyzer(definition = HibernateSearch.FIELD_TEXT_ANALYZER)
	 * protected String myfield;
	 * }</pre>
	 * This way text context is tokenized with elasticsearch
	 * <a href="https://www.elastic.co/guide/en/elasticsearch/reference/5.4/analysis-standard-tokenizer.html">standard tokenizer</a> and
	 * <a href="https://www.elastic.co/guide/en/elasticsearch/reference/5.4/analysis-lowercase-tokenfilter.html">lowercase token filter</a> with
	 * <a href="https://www.elastic.co/guide/en/elasticsearch/reference/5.4/analysis-htmlstrip-charfilter.html">html_strip characters filter</a>.
	 * </p>
	 **/
	public static final String FIELD_TEXT = "text";
	public static final String FIELD_TEXT_ANALYZER = "simple";

	/**
	 * A common field for storing text indentificator, like document numbers {@code FV/2016/12/223412}.
	 * <p>
	 * This kind of field is appropriate to index <strong>case-insensitively all fields tokenized only with whitespaces</strong> and
	 * prevents from tokenizing using usual document number separators like slash or dash. This kind of field should be analyzed
	 * with {@link #FIELD_ID_ANALYZER}. The indexed entity field has has usually the following definition:
	 * <pre>{@code
	 *@literal @Field(name = HibernateSearch.FIELD_ID)
	 *@literal @Analyzer(definition = HibernateSearch.FIELD_ID_ANALYZER)
	 * protected String myfield;
	 * }</pre>
	 * This way text context is tokenized with elasticsearch
	 * <a href="https://www.elastic.co/guide/en/elasticsearch/reference/5.4/analysis-whitespace-tokenizer.html">whitespace tokenizer</a> and
	 * <a href="https://www.elastic.co/guide/en/elasticsearch/reference/5.4/analysis-lowercase-tokenfilter.html">lowercase token filter</a>.
	 * </p>
	 **/
	public static final String FIELD_ID = "textid";
	public static final String FIELD_ID_ANALYZER = "lowercase";

	public static class FieldAnalyzer {
		public final String field;
		public final String analyzer;

		public FieldAnalyzer(String field, String analyzer) {
			this.field = field;
			this.analyzer = analyzer;
		}
	}

	public static final FieldAnalyzer[] ALL_FIELDS = new FieldAnalyzer[] {
		new FieldAnalyzer(FIELD_TEXT, FIELD_TEXT_ANALYZER),
		new FieldAnalyzer(FIELD_ID, FIELD_ID_ANALYZER)
	};

	protected EntityManager entityManager;

	public HibernateSearch(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public FullTextEntityManager fullTextEntityManager() {
		return Search.getFullTextEntityManager(entityManager);
	}

	public QueryBuilder queryBuilder(Class entityClass) {
		return Search.getFullTextEntityManager(entityManager).getSearchFactory().buildQueryBuilder().forEntity(entityClass).get();
	}

	public FullTextQuery buildQuery(Query query, Class entityClass) {
		return fullTextEntityManager().createFullTextQuery(query, entityClass);
	}

	public String buildStandardQueryString(String q) {
		return String.format("%s:%s %s:%s", FIELD_TEXT, q, FIELD_ID, q);
	}

	public FullTextQuery buildQuery(QueryDescriptor descriptor, Class entityClass) {
		return fullTextEntityManager().createFullTextQuery(descriptor, entityClass);
	}

}
