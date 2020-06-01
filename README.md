# Customers Integrator
*Spring Boot REST API for Customers management*<br>

### Get Started

* Clone this repository using either SSH or HTTP on https://github.com/claytonrm/customers-integrator
* Install requirements

### Requirements
- [Java 8](https://www.oracle.com/java/technologies/javase-downloads.html#JDK8)
- [Lombok](https://projectlombok.org/download)
- [Maven](https://maven.apache.org/download.cgi)
- [Docker](https://www.docker.com)

### Setup
Set your docker up following the steps below to create a postgres instance:

##### Download postgres
 ```shell 
 docker pull postgres
 ```
##### Setup postgres
 ```shell 
docker run --name customers-integrator-postgres -e "POSTGRES_PASSWORD=CI2020" -p 5432:5432 -d postgres
 ```
##### Creating a database called *customersintegratordb*
 ```shell 
docker exec customers-integrator-postgres psql -U postgres -c "CREATE DATABASE customersintegratordb" postgres
 ```

### Running App

* Open the project on your IDE and run CustomersIntegratorApplication main class or in your bash (project root) `mvn spring-boot:run` (Ctrl + C to stop running it)
* Check the URL `http://localhost:8080/swagger-ui.html`

#### Available operations
* You can try it out each of operations available in above URL or via [Postman](https://www.getpostman.com/)

**Troubleshooting** <br>
For PATCH operations you must include `Content-Type: application/json-patch+json` as a header. 

### Running Tests
```shell
mvn test
```

### Sonar

You can check loads of stats about main codes with Sonar.

* In your terminal type `docker pull sonarqube` to download sonar image. <br>
* Run `docker run -d --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube` to create a docker container <br>

Now you are able to generate test files and check your code out.
```shell script
mvn clean package sonar:sonar
```

After that, you can open http://localhost:9000/projects
