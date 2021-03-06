# This project is no longer supported and the code is moved to https://github.com/json-ql

A collection of DTO frames that can be used to create data filters through REST/GraphQL interface containing the abstraction to fetch underlying data from any data source (sql/nosql database, lucene index, etc) and some concrete implementations.

The goal of this library is to provide uniform queries to REST/GraphQL with pagination and sorting support regardless the underlying data source .

This library can be installed from Github Releases using [jitpack.io](https://jitpack.io/#l0co/rest-data-filter).

## Default filters provided

Following default filter frames are provided by this lib.

### [`SingleValueQueryFilter`](src/main/java/com/lifeinide/rest/filter/filters/SingleValueQueryFilter.java)

Filters any single value with one of predefined [conditions](src/main/java/com/lifeinide/rest/filter/enums/QueryCondition.java):

```json
{
  "value": "a"
}

{
  "condition": "eq",
  "value": "a"
}

{
  "condition": "gt",
  "value": 10
}

{
  "condition": "notNull"
}
``` 

### [`EntityQueryFilter`](src/main/java/com/lifeinide/rest/filter/filters/EntityQueryFilter.java)

Special kind of single value `QueryFilter` assumed to be working with entity ID.

```json
{
  "value": "b9a103d6-a9dd-4371-9d2b-1b008bf88327"
}
``` 

### [`DateRangeQueryFilter`](src/main/java/com/lifeinide/rest/filter/filters/DateRangeQueryFilter.java) 

Filters date by from-to range.

```json
{
  "from": "2018-01-01",
  "to": "2018-03-03"
}

{
  "range": "LAST_30_DAYS"
}

{
  "range": "PREVIOUS_MONTH"
}
``` 

### [`ValueRangeQueryFilter`](src/main/java/com/lifeinide/rest/filter/filters/ValueRangeQueryFilter.java).

Filters numeric value by from-to range.

```json
{
  "from": 10,
  "to": 20
}
``` 

### [`ListQueryFilter`](src/main/java/com/lifeinide/rest/filter/filters/ListQueryFilter.java)

Combines multiple filters with and/or conjunction.

```json
{
  "filters": [
    {
      "value": "a"
    },
    {
      "value": "b"
    }
  ]
}

{
  "conjunction": "and",
  "filters": [
    {
      "condition": "gt",
      "value": 10
    },
    {
      "conditions": "ne",
      "value": 100
    }
  ]
}
```

## Query builder abstraction

A query builder is an abstraction used to fetch paginated data from the persistence storage depending on the incoming request comprised of the combination of filters. Before you get the data you need to implement you own custom frame, for example:

```java
class UserFilter extends BaseRestFilter {

	protected SingleValueQueryFilter<String> name;
	protected SingleValueQueryFilter<Boolean> admin;
  
	public SingleValueQueryFilter<String> getName() {
		return name;
	}

	public void setName(SingleValueQueryFilter<String> name) {
		this.name = name;
	}

	public SingleValueQueryFilter<Boolean> getAdmin() {
		return admin;
	}

	public void setAdmin(SingleValueQueryFilter<Boolean> admin) {
		this.admin = admin;
	}
	
}
```

Because `BaseRestFilter` supports pagination and sort out of the box we can now query for list of users using following example request:

```json
{
  "admin": {
    "value": "true"
  },
  "name": {
    "condition": "notNull"
  },
  "pageSize": 20,
  "page": 3,
  "sort": [{
  	"sortDirection": "asc",
  	"sortField": "name"
  }]
}
```

Now, in your controller you can simply get the data in the following way (this example uses `JpaFilterQueryBuilder` but there are some other options listed below):

```java
public PageableResult<User> listUsers(UserFilter req) {
	new JpaFilterQueryBuilder<User>()
		.add("name", req.getName())
		.add("admin", req.isAdmin())
		.list(req);
}
```

### Query builder implementations

This lib provides following query builders implementation, ready to be used with default filters:

| Class | Description | 
|------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------|
| [JpaFilterQueryBuilder](src/main/java/com/lifeinide/rest/filter/impl/jpa/JpaFilterQueryBuilder.java) | Provides query builder for JPA criteria API. |
| [HibernateSearchFilterQueryBuilder](src/main/java/com/lifeinide/rest/filter/impl/hibernate/HibernateSearchFilterQueryBuilder.java) | Provides query builder for Hibernate Search. |

Note, that the underlying **dependencies are not included by default by this lib** and to enable given persistence storage support you need to add dependencies described in the specific query builder class header to your project.

## How to implement custom filter

To implement custom filter implement [`QueryFilter`](src/main/java/com/lifeinide/rest/filter/intr/QueryFilter.java) interface in your custom filtering frame.

Now, each `FilterQueryBuilder` implementation contains following not implemented method:

```java
@Override
public SELF add(String field, QueryFilter filter) {
	throw new IllegalStateException(String.format("Support for filter: %s in builder: %s is not implemented",
		filter.getClass().getSimpleName(), getClass().getSimpleName()));
}
```

So, you can just extend `FilterQueryBuilder` implementation and implement this method with your custom filters support. 

In case you want to delegate custom query builder execution to some external class, you can always pass to it `FilterQueryBuilder` instance which implements `context()` method that can be used to join to current query building process.

## Project supporters

Thanks for supporting this project to [Two Fish Software](https://twofishsoftware.com/).
