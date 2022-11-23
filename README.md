# Sharedpay

## Project description
* The project is a rest api directed to share payments of a bill.
* The api is fully made in java using spring boot.
* It uses a postgres database in a docker container.

Content Table
=================
<!--ts-->
* [Tecnologies](#tecnologies)
* [How to use](#how-to-use)
    * [Prerequisites](#prerequisites)
    * [Executing](#executing)
    * [Executing Tests](#executing-tests)
* [Additional Resources](#additional-resources)
    * [Pgadmin](#pgadmin)
    * [Interactive Documentation](#executing)
* [Business Flux](#business-flux)
* [API Use Examples](#)
* [API Architecture](#api-architecture)
  * [Components](#components)
  * [Exception Handling](#exception-handling)
<!--te-->

---
### Tecnologies
The following tecnologies have been used in this Project:
* [JDK 8](https://www.oracle.com/br/java/technologies/javase/javase8-archive-downloads.html)
* [SpringBoot 2.7.5](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) (Starters abaixo)
  * Data JPA (Uses Hibernate)
  * Web
  * Validation
  * Postgres
  * Lombok
* [Postgres Docker Image:latest](https://hub.docker.com/_/postgres)
* [Docker 20.10.17](https://www.docker.com/)
* [springdoc-openapi ](https://springdoc.org/)
* [Maven 3.8.6](https://maven.apache.org/)

---
### How to use

#### Prerequisites
Before beginning, it needs the following application:
* [Docker ( > = version with compose)](https://docs.docker.com/get-docker/)

#### Executing
Before trying to execute the app, see if your system has ports 8080 (spring), 5051 (pgadmin) and 5432 (postgres) available!

```bash
# Clone this repository
$ git clone https://github.com/murilofarias/sharedpay.git

# Change the working directory to the cloned project.
$ cd sharedpay

# Do the build of the application skipping tests
$ ./mvnw package

# Execute docker compose file
$ docker compose up
```
#### Executing Tests

```bash
# Execute unit tests
$ ./mvnw test

# Execute integration tests
$ ./mvnw verify
```
---
### Additional Resources

#### Pgadmin
You can interact with the database using a web interface in http://localhost:5051

#### Interactive Documentation
You can interact with the spring application using swagger in http://localhost:8080/swagger-ui/index.html or the  OpenAPI descriptions in http://localhost:8080/v3/api-docs/


---
### Business Flux
The main idea of the app is for a user use his internal credits to pay shared bills and, after that, share this payment with his friends in an organized and fair manner. These payments are shared by links to picpay payment system. After the payments are fulfilled, the user that payed for the bill is recashed by the exact amount paid back. The following steps explain the business flux:

1. First the user, the one that will register the bill, needs to be registered in the system, so to do that the endpoint "POST /users" must be called with the user information.

2. The user now needs to add some credit to his user account. This part is only simulated, you can add any value of credit you would like calling the endpoint "PUT /users/{id}/add-credit". If you are not sure about the id, you can check in "GET /users" that returns the list of all users.

3. 
* With sufficient credit, the user can register a reasonable bill by "POST /bills". 
* This use case receives the new bill, charges the user by the total value of the bill, generates the respective payments from the bill and saves them.
* If the app is running, you can check the interactive doc for this endpoint here http://localhost:8080/swagger-ui/index.html#/bill-controller/registerBill.
* The owner of the bill must be a user, but the rest doesn't have to be.

4. The payments can be requested now by "PUT /bills/{id}/request-payments". It returns the links to the picpay payment system.

5. The user shares the links with his friends and then they can pay him back. If the status of the payments changes, a notification is expected to be made by the picpay api to "Post /confirm-payment". When the notification is received, the application checks the status of the payment calling the picpay api and then, depending on the answer, sets the new payment status. The picpay api really can notify the api, but, as this api is not in a public site, this behavior doesn't happen. You can simulate partially calling the endpoint.

---
### API Use Examples
   1. POST /users
   * request body:
      {
        "firstName": "Murilo",
        "lastName": "Farias",
        "cpf": "00293966206"
      }
      
   * response body: 
      {
        "id": 2,
        "firstName": "Murilo",
        "credit": 0,
        "lastName": "Farias",
        "cpf": "00293966206"
      }
      
   2. PUT /users/2/add-credit
   * request body:
      {
         "value": 200.00
      }
      
   * response body: 
      {
        "id": 2,
        "firstName": "Murilo",
        "credit": 200,
        "lastName": "Farias",
        "cpf": "00293966206"
      } 
      
    3. POST /bills
    * request body:
      {
        "additionals": 8.00,
        "discounts": 20.00,
        "hasWaiterService": false,
        "includeOwnerPayment":false,
        "individualSpendings": [
          {
            "value": 42.00,
            "person": {
              "firstName": "Eduardo",
              "lastName": "Monteiro",
              "cpf": "01942539290"
            }
          },
          {
            "value": 8.00,
            "person": {
              "firstName": "Alan",
              "lastName": "Pera",
              "cpf": "00293966206"
            }
          }
        ]
      }
 ```     
   * response body: 
     {
         "id": 4
        "additionals": 8.00,
        "discounts": 20.00,
        "hasWaiterService": false,
        "includeOwnerPayment":false,
        "individualSpendings": [
          {
            "value": 42.00,
            "person": {
              "firstName": "Eduardo",
              "lastName": "Monteiro",
              "cpf": "01942539290"
            }
          },
          {
            "value": 8.00,
            "person": {
              "firstName": "Alan",
              "lastName": "Pera",
              "cpf": "00293966206"
            }
          }
        ],
        "payments": [
          {
            "id": 7,
            "status": "NON_REQUESTED",
            "debtor": {
              "firstName": "Eduardo",
              "lastName": "Monteiro",
              "cpf": "01942539290"
            },
            "value": 31.92
          }
        ]
     }
  ``` 
---
### API Architecture
The api architecture is a simplification of a plug and adapter architecture. There is only one primary adapter and it resides in the subpackage "controller". The other adapters are secondary adapters and they are located in the subpackage "adapter". 

#### Components

1. **(Rest) Controller** :
  Responsible for receiving https requests and answering responses.
  
  
2. **Core**:
  There are applications services ( BillService, UserService, BillNotificationService) that implements the use cases of the application, api exceptions and   the core model where most of the business logic resides.
  
  
3. **Plug**:
  It is part of the "Core" and connects the low level abstraction logic to the business logic using Inversion of Control. Each element is an interface that   must be implemented by some corresponding adapter.
  
  
4. **Adapter**:
  It implements low level logic important for the application use cases like communicating using rest clients or database clients.
   
#### Exception Handling
 The main Component to handle Exception is the RestExceptionHandler inside subpackage controller. It can respond to some usual api exceptions and some custom ones from the core application itself :

* **DomainException**:
  It is thrown when some business rule is violated.
  

* **ResourceNotFoundException**:
  Thrown when repository can't find any resource that matches the parameters given.
  
  
* **PaymentServiceException**:
  This is an Exception thrown by the PicpayService adapter when there is a problem with the http request.
  

##### - class ApiError > error > dto > controller
It defines a standard structure for the api to answer requests when some exception is thrown.
