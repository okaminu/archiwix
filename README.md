![Alt text](logo.png?raw=true)
# ArchiWix 

ArchiWix is a web server that stores and provides Records. Storing of the Record is perfomed with POST request,
while retrieval of Record is performed with a GET request with a query parameter.

### Workflow
* Store Record with POST request
* If Record with given ID already exists, POST request will overwrite existing Record
* Retrieve Record with GET request with query parameter
* Since Records are stored in memory, they are removed once a web server is stopped.

### Key features
* Written in Java and is compatible with other JVM languages including Kotlin, Scala, Groovy.
* Separation of Concerns for Query parsing and evalutation logic being kept separate from web layer,
allowing for easier maintainability
* Written with TDD in mind, and providing 100% code coverage with Jacoco
* Extensible implementation, allowing to easily add new expressions without modifying existing code
* Nested expression evaluation in query, giving possibility to parse unlimited nesting levels

### Usage
* Endpoint "GET /store?query=" Takes query as input and returns matching entries. 
Example: GET /store?query=EQUAL(id,"abc")
* Endpoint "POST /store" Take entity and stores it. ID remains unique. 
If record with given ID already exists, it is overwritten.

### Record
Record consists of the following fields, which can be used in queries.

id​ - text
title​ - text
content​ - text
views​ - integer number
timestamp​ - integer number

### Query
* EQUAL(property,value) - Filters only values which have matching property value. 
Example - EQUAL(id,"first-post")
* AND(a,b) - Filters only values for which both ​ a ​ and ​ b ​ are true.
Example - AND(EQUAL(id,"first-post"),EQUAL(views,100))
* OR(a,b) - Filters only values for which either ​ a ​ or b is true (or both).
Example - OR(EQUAL(id,"first-post"),EQUAL(id,"second-post"))
* NOT(a) - Filters only values for which ​ a ​ is false.
Example - NOT(EQUAL(id,"first-post"))
* GREATER_THAN(property,value) - Filters only values for which property is greater than the given value. 
Valid only for number values. Example - GREATER_THAN(views,100)
* LESS_THAN(property,value) - Filters only values for which property is less than the given value. 
Valid only for number values. Example - LESS_THAN(views,100)

### License
This library is licensed under MIT. Full license text is available in [LICENSE](https://github.com/boldadmin-com/Crowbar/blob/dev/LICENSE.txt).