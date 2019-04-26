# rest-data-filter

A collection of DTO frames that can be used to create data filters through REST/GraphQL interface conaining the abstraction to fetch underlying data from any data source (sql/nosql database, lucene index, etc).

This project has a dependency on `spring-data-commons` providing `Pageable` and `Page` abstraction.

Following default filters are provided here:

1. [`QueryFilter`](src/main/java/com/lifeinide/rest/filter/base/QueryFilter.java) - filters any single value with one of predefined [`QueryCondition`](src/main/java/com/lifeinide/rest/filter/base/QueryFilter.java). 
1. [`EntityQueryFilter`](src/main/java/com/lifeinide/rest/filter/base/EntityQueryFilter.java) - special kind of single value `QueryFilter` assumed to be working with entity ID. 
1. [`DateRangeQueryFilter`](src/main/java/com/lifeinide/rest/filter/base/DateRangeQueryFilter.java) - filters date by from-to range. 
1. [`ValueRangeQueryFilter`](src/main/java/com/lifeinide/rest/filter/base/ValueRangeQueryFilter.java) - filters numeric value by from-to range. 
1. [`ListQueryFilter`](src/main/java/com/lifeinide/rest/filter/base/ListQueryFilter.java) - combines multiple filters with and/or conjunction. 
