# rest-data-filter

A collection of DTO frames that can be used to create data filters through REST/GraphQL interface containing the abstraction to fetch underlying data from any data source (sql/nosql database, lucene index, etc) and concrete implementation for JPA.

The goal of this library is to provide uniform queries to REST/GraphQL regardless the underlying data source. 

Following default filters are provided here:

## [`QueryFilter`](src/main/java/com/lifeinide/rest/filter/filters/QueryFilter.java)

Filters any single value with one of predefined [`QueryCondition`](src/main/java/com/lifeinide/rest/filter/base/QueryFilter.java).

```json
{
  value: "a"
}

{
  condition: "eq",
  value: "a"
}

{
  condition: "eq",
  value: "a"
}

{
  condition: "gt",
  value: 10
}

{
  condition: "notNull"
}
``` 

## [`EntityQueryFilter`](src/main/java/com/lifeinide/rest/filter/filters/EntityQueryFilter.java)

Special kind of single value `QueryFilter` assumed to be working with entity ID.

```json
{
  value: "b9a103d6-a9dd-4371-9d2b-1b008bf88327"
}
``` 

## [`DateRangeQueryFilter`](src/main/java/com/lifeinide/rest/filter/filters/DateRangeQueryFilter.java) 

Filters date by from-to range.

```json
{
  from: "2018-01-01",
  to: "2018-03-03"
}

{
  range: "LAST_30_DAYS"
}

{
  range: "PREVIOUS_MONTH"
}
``` 

## [`ValueRangeQueryFilter`](src/main/java/com/lifeinide/rest/filter/filters/ValueRangeQueryFilter.java).

Filters numeric value by from-to range.

```json
{
  from: 10,
  to: 20
}
``` 

## [`ListQueryFilter`](src/main/java/com/lifeinide/rest/filter/filters/ListQueryFilter.java)

Combines multiple filters with and/or conjunction.

```json
{
  filters: [
    {
      value: "a"
    },
    {
      value: "b"
    }
  ]
}

{
  conjunction: "and"
  filters: [
    {
      condition: "gt"
      value: 10
    },
    {
      conditions: "ne"
      value: 100
    }
  ]
}
```
