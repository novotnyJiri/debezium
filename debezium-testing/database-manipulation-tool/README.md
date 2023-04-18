# database-manipulation-tool

## Working features

This is currently in aplha state so there are not many features completed and the ones that are finished aren't tested properly.

| Database/feature        	 | Postgres 	 | Mongo 	 | Mysql 	 | Oracle 	 | SqlServer 	 | Db2 	 |
|---------------------------|------------|---------|---------|----------|-------------|-------|
| Create table            	 | X    	     | X   	   | X   	   | 	        | 	           | 	     |
| Insert                  	 | X    	     | 	       | X   	   | 	        | 	           | 	     |
| Create table and insert 	 | 	          | 	       | 	       | 	        | 	           | 	     |
| Create table and upsert 	 | 	          | X	      | X	      | 	        | 	           | 	     |
| Update                  	 | 	          | 	       | 	       | 	        | 	           | 	     |
| Upsert                  	 | 	          | X   	   | X   	   | 	        | 	           | 	     |
| Delete                  	 | 	          | 	       | 	       | 	        | 	           | 	     |
| Selects and Gets        	 | 	          | 	       | 	       | 	        | 	           | 	     |

There is some error handling and debug logging, but it is not complete. 
## Json insertion schema
```
{
	"databases": ["db2", "postgresql", "mysql", "mongo"],
	"table": "pepici",
	"primary": "name",
	"payload": [{
            "name": "name",
            "dataType": "VarChar(255)",
            "value": "PepaZDepa"
        },
        {
            "name": "age",
            "dataType": "Integer",
            "value": "20"
        }
    ]
}
```

It is imperative to use the primary key so that the created tables can have primary key and can be correctly updated.
Primary keys must always be unique and once set cannot be changed. Therefore, I recommend using GUID or other system that ensures these properties.

When Creating tables, the `payload` value determines the columns and their types.

You can enable/disable different databases with the `enabled` property. The database insertion filtering based on json key `databases` is not implemented yet. Every enabled database will try to execute the command.

## Current REST endpoints

 <summary><code>POST</code> <code><b>/Main/Insert</b></code> <code>(Inserts json into all enabled databases)</code></summary>
<summary><code>POST</code> <code><b>/Main/CreateTable</b></code> <code>(Creates table/collection in every enabled database)</code></summary>
<summary><code>POST</code> <code><b>/Main/Upsert</b></code> <code>(Upserts json into all databases)</code></summary>
<summary><code>POST</code> <code><b>/Main/CreateTableAndUpsert</b></code> <code>(Upserts json into all databases and creates tables if they did not exist)</code></summary>


## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/database-manipulation-tool-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
