package com.lifeinide.rest.filter.impl.hibernate;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import javax.persistence.EntityManager;

/**
 * Hibernate search helper.
 *
 * <h2>Searchable fields implementation</h2>
 *
 * This {@link HibernateSearchFilterQueryBuilder} can search entities of given type for a text contained in searchable fields with
 * additional filtering support by custom fields. We support following types of searchable fields in entities:
 *
 * <ol>
 *     <li>{@link #FIELD_TEXT} containing all arbitrary texts, which should searchable, but previously be tokenized, analyzed, etc</li>
 *     <li>{@link #FIELD_ID} string-like ids, which we don't want to analyze and tokenize, but made searchable as-is. For example
 *     	   invoice number "INV201012333" shouldn't be analyzer nor tokenized, but we want to be able to search such number as-is,
 *     	   possibly using wildcard search.</li>
 * </ol>
 *
 * Check the corresponsing constants description to see how to apply them on entity fields.
 *
 * <h2>Filtering fields implementation</h2>
 *
 * Besides above two kinds of searchable fields we also may want to filter the results by some other fields, like in {@code where} clause
 * in SQL. For example we may want to search all articles containing some phrase, but <strong>only</strong> written by some author. In
 * such a scenario we need both to apply search and filtering using Lucene query.
 *
 * Such filter fields should have preserved custom names (excluding {@link #FIELD_TEXT} and {@link #FIELD_ID} names) and should usually
 * be defined without analyzers, tokenizers, etc:
 *
 * <pre>{@code
 * @Field(analyze = Analyze.NO, norms = Norms.NO)
 * protected String myField;
 * }</pre>
 *
 * Using such definition you may now filter the results using this field with {@link HibernateSearchFilterQueryBuilder}:
 *
 * <pre>{@code
 * new HibernateSearchFilterQueryBuilder(...).add("myField", SingleValueQueryFilter.of(...)).list(...);
 * }</pre>
 *
 * To check examples of types of fields we support with filtering, please refer {@code HibernateSearchEntity} from test package.
 *
 * <h3>Field bridge for numbers</h3>
 *
 * <p>
 * By default Lucene performs text searchs even for ranges, so fox example "2" > "10" for Lucene. To assert proper numbers filtering
 * for Lucene we prepend all number with zeros, so that "*0002" > "*0010" and we can apply filtering properly. This is why the filtering
 * fields of {@link Number} type should be defined with {@link RangeNumberBridge}:
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
 * <h3>Field bridge for entities</h3>
 *
 * In case we want to store in the index the to-one relation, we first need to provide a bridge extending {@link BaseDomainFieldBridge}
 * so that Hibernate Search can convert this entity to and from String representation. For example:
 *
 * <pre>{@code
 * public class DomainFieldBridge extends BaseDomainFieldBridge<IBaseEntity<Long>> {
 *
 *    @Override
 *    public String getEntityIdAsString(IBaseEntity<Long> entity) {
 * 		return String.valueOf(entity.getId());
 *    }
 *
 *    @Override
 *    public boolean isEntity(Object entity) {
 * 		return entity instanceof IBaseEntity;
 *    }
 *
 * }
 * }</pre>
 *
 * Having this field bridge we can use it to store to-one relations in the full text index:
 *
 * <pre>{@code
 * @ManyToOne
 * @Field(analyze = Analyze.NO, norms = Norms.NO)
 * @FieldBridge(impl = DomainFieldBridge.class)
 * protected MyEntity entity;
 * }</pre>
 *
 * @author Lukasz Frankowski
 */
public class HibernateSearch {

	/**
	 * A common field for storing text content.
	 * <p>
	 * This kind of field is appropriate to index <strong>case-insensitively all natural-language fields</strong> and should be analyzed
	 * with some analyzer. The indexed entity field has has usually the following definition:
	 * <pre>{@code
	 * @Field(name = HibernateSearch.FIELD_TEXT)
	 * @Analyzer(impl = ... , definition = ...)
	 * protected String myfield;
	 * }</pre>
	 * </p>
	 **/
	public static final String FIELD_TEXT = "text";

	/**
	 * A common field for storing text indentificator, like document numbers {@code FV/2016/12/223412}.
	 * <p>
	 * This kind of field is appropriate to index <strong>case-insensitively all fields tokenized only with whitespaces</strong> and
	 * prevents from tokenizing using usual document number separators like slash or dash. This kind of field should be analyzed
	 * with no analyzer nor tokenizer. The indexed entity field has has usually the following definition:
	 * <pre>{@code
	 * @Field(name = HibernateSearch.FIELD_ID, analyze = Analyze.NO, norms = Norms.NO)
	 * protected String myfield;
	 * }</pre>
	 * </p>
	 **/
	public static final String FIELD_ID = "textid";

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

}
