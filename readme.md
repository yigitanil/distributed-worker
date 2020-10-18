### **Distributed-Worker**

**Prerequisites:**

 ~~~
 Java 11
 Maven
 PostgreSql
 RabbitMq
~~~

Docker commands:

 ~~~
RabbitMq:
docker run -p 5672:5672 -p 15672:15672 -d --hostname my-rabbit --name some-rabbit rabbitmq:3-management

PostgreSql:
docker run -p 5432:5432 --name my-postgres -e POSTGRES_PASSWORD=123456 -d postgres
~~~

Table script:
 ~~~
create table url_status
(
	id serial not null
		constraint url_status_pk
			primary key,
	url varchar(4000) not null,
	status varchar(20),
	http_code varchar(3)
);
~~~




##### **Run:**

 ~~~
 mvn clean install
~~~
Run 1 Instance for manager with respective parameters
~~~
java -jar -DDB_URL=jdbc:postgresql://localhost:5432/postgres -DDB_USERNAME=postgres  -DDB_PASSWORD=123456  -DDB_SCHEMA=public   -DRABBIT_HOST=localhost   -DRABBIT_PORT=5672   -DRABBIT_USERNAME=guest  -DRABBIT_PASSWORD=guest ./manager/target/manager-0.0.1-SNAPSHOT.jar 
~~~

Run 1-N Instances for worker with respective parameters
~~~
java -jar -DDB_URL=jdbc:postgresql://localhost:5432/postgres -DDB_USERNAME=postgres  -DDB_PASSWORD=123456  -DDB_SCHEMA=public   -DRABBIT_HOST=localhost   -DRABBIT_PORT=5672   -DRABBIT_USERNAME=guest  -DRABBIT_PASSWORD=guest ./worker/target/worker-0.0.1-SNAPSHOT.jar 
~~~