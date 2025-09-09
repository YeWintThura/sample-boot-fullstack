 Web Lab - Spring Data Relationship
 ====================
In this lab, we will develop a web application that support concert ticket's reservation.  We will use Spring Data to manage the data that have relationships. We will use H2 as a database for this lab.

# Develop Backend
We will develop Model that has 2 entity classes. The concert can have many seats. A seat can belong to a concert.

### Concert
 `Concert`  will represent a concert. It should have proper annotation for JPA such as `@Entity`. The class will have the following attributes:
* `id`: This attribute will store the unique id of the concert. This attribute should have `@Id` and `@GeneratedValue` that has `GenerationType.AUTO`.
* `title`: This attribute will store the name of the concert.
* `date`: This attribute will store the date of the concert.
* `performer`: This attribute will store the performer of the concert.

### Seat
The class `Seat` will represent a seat. It should have proper annotation for JPA such as `@Entity`. The class will have the following attributes:
* `id`: This attribute will store the unique id of the seat. This attribute should have `@Id` and `@GeneratedValue` that has `GenerationType.AUTO`.
* `number`: This attribute will store the seat number of the seat.
* `zone`: This attribute will store the price of the seat.

### Relationship
The relationship between the entities are as follows:

* `Seat` has a relationship with `Concert`. (Make this relationship in the `Seat` class) This is a many-to-one relationship. Many seats can belong to a concert. Please use `CascadeType.MERGE` for this relationship, as we want to save the seat to an existing concert.

There is `data.sql`, which initializes the database with some data. You can use this data to test your application.


## Develop Repository
- Develop a repository class `ConcertRepository` that will manage the data. The repository will extend `CrudRepository` interface. Currently, we do not need any method in the repository. We will use the methods provided by `CrudRepository` interface.
* `findAll`: This method gives all concert as `List<Concert>`

- develop a repository class `SeatRepository` that will manage the data. The repository will extend `CrudRepository` interface. Currently, we do not need any method in the repository. Add the following derived method
  * `findByConcertId`: This method will find all seats that belong to a concert with the given id.
  * `deleteByConcertId`: This method will delete all seats that belong to a concert with the given id.


We will use H2 as a database for this lab. Add the following to the `application.properties` file under `src/main/resources` folder:
```
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.show-sql = true
spring.jpa.defer-datasource-initialization=true
spring.jpa.hibernate.ddl-auto=create
spring.datasource.initialization-mode=always
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
```

## Develop Controller
We will develop a controller class `ConcertController` that will handle the HTTP requests. 

- The controller must have a contructor that takes `ConcertRepository` and `SeatRepository` as a parameters. 

- The controller will have the following methods:
* **`GET /concerts`**
    * Lists all concerts.
    * Returns a 200 OK response with a collection of `Concert` objects.
* **`POST /concerts`**
    * Creates a new concert.
    * Expects a `Concert` object in the request body.
    * Returns a 201 CREATED response with a message "concert saved".
* **`DELETE /concerts/{id}`**
    * Deletes a concert by ID.
    * Returns a 204 NO CONTENT response if successful.
    * Returns a 404 NOT FOUND response if the concert does not exist.
* **`GET /concerts/{concertId}/seats`**
    * Lists all seats for a given concert.
    * Returns a 200 OK response with a collection of `Seat` objects.
    * Returns a 404 NOT FOUND response if the concert does not exist.
* **`POST /concerts/{concertId}/seats`**
    * Creates a new seat for a given concert.
    * Expects a `Seat` object in the request body.
    * Returns a 201 CREATED response with a message "seat saved".
    * Returns a 404 NOT FOUND response if the concert does not exist.
* **`POST /concerts/{concertId}/seats/{seatId}/book`**
    * Books a specific seat for a given concert.
    * Expects a `Seat` object in the request body containing the attendee name.
    * Find the seat by given id and set attendee name.
    * Sets the `booked` status of the seat to `true`.
    * Returns a 200 OK response with a message "seat booked".
    * Returns a 404 NOT FOUND response if the concert or seat does not exist.


## Test API using Postman

Run the application using App.java class. The application will start on port 8100. You can test all the API endpoints using Postman.

### Base URL
```
http://localhost:8100
```

### API Endpoints

#### 1. Get All Concerts
- **Method:** GET
- **URL:** `http://localhost:8100/concerts`
- **Description:** Lists all available concerts

#### 2. Create a New Concert
- **Method:** POST
- **URL:** `http://localhost:8100/concerts`
- **Headers:** `Content-Type: application/json`
- **Body (JSON):**
```json
{
    "title": "Winter Music Festival",
    "date": "2024-07-15",
    "performer": "The Rock Band"
}
```

#### 3. Delete a Concert
- **Method:** DELETE
- **URL:** `http://localhost:8100/concerts/{id}`
- **Description:** Replace `{id}` with the actual concert ID (can be found in data.sql)

#### 4. Get Seats for a Concert
- **Method:** GET
- **URL:** `http://localhost:8100/concerts/{concertId}/seats`
- **Description:** Replace `{concertId}` with the actual concert ID (can be found in data.sql)

#### 5. Create a New Seat
- **Method:** POST
- **URL:** `http://localhost:8100/concerts/{concertId}/seats`
- **Description:** Replace `{concertId}` with the actual concert ID (can be found in data.sql)
- **Headers:** `Content-Type: application/json`
- **Body (JSON):**
```json
{
    "number": "A1",
    "zone": "VIP"
}
```

#### 6. Book a Seat (Reserve)
- **Method:** POST
- **URL:** `http://localhost:8100/concerts/{concertId}/seats/{seatId}/book`
- **Description:** Replace `{concertId}` with the actual concert ID and seat ID (can be found in data.sql)
- **Headers:** `Content-Type: application/json`
- **Body (JSON):**
```json
{
    "attendee": "John Doe"
}
```

#### 7. List Seats
- **Method:** GET
- **URL:** `http://localhost:8100/concerts/{concertId}/seats`
- **Description:** Replace `{concertId}` with the actual concert ID (can be found in data.sql)

## Unit Test
Run the unit tests in `ConcertControllerTest` class to test the service. You can run the tests using the following command:
```
mvn verify
```
