## Antaeus

Antaeus (/ænˈtiːəs/), in Greek mythology, a giant of Libya, the son of the sea god Poseidon and the Earth goddess Gaia. He compelled all strangers who were passing through the country to wrestle with him. Whenever Antaeus touched the Earth (his mother), his strength was renewed, so that even if thrown to the ground, he was invincible. Heracles, in combat with him, discovered the source of his strength and, lifting him up from Earth, crushed him to death.

Welcome to our challenge.

## The challenge

As most "Software as a Service" (SaaS) companies, Pleo needs to charge a subscription fee every month. Our database contains a few invoices for the different markets in which we operate. Your task is to build the logic that will schedule payment of those invoices on the first of the month. While this may seem simple, there is space for some decisions to be taken and you will be expected to justify them.

## Instructions

Fork this repo with your solution. Ideally, we'd like to see your progression through commits, and don't forget to update the README.md to explain your thought process.

Please let us know how long the challenge takes you. We're not looking for how speedy or lengthy you are. It's just really to give us a clearer idea of what you've produced in the time you decided to take. Feel free to go as big or as small as you want.

## Developing

Requirements:
- \>= Java 11 environment

Open the project using your favorite text editor. If you are using IntelliJ, you can open the `build.gradle.kts` file and it is gonna setup the project in the IDE for you.

### Building

```
./gradlew build
```

### Running

There are 2 options for running Anteus. You either need libsqlite3 or docker. Docker is easier but requires some docker knowledge. We do recommend docker though.

*Running Natively*

Native java with sqlite (requires libsqlite3):

If you use homebrew on MacOS `brew install sqlite`.

```
./gradlew run
```

*Running through docker*

Install docker for your platform

```
docker build -t antaeus
docker run antaeus
```

### App Structure
The code given is structured as follows. Feel free however to modify the structure to fit your needs.
```
├── buildSrc
|  | gradle build scripts and project wide dependency declarations
|  └ src/main/kotlin/utils.kt 
|      Dependencies
|
├── pleo-antaeus-app
|       main() & initialization
|
├── pleo-antaeus-core
|       This is probably where you will introduce most of your new code.
|       Pay attention to the PaymentProvider and BillingService class.
|
├── pleo-antaeus-data
|       Module interfacing with the database. Contains the database 
|       models, mappings and access layer.
|
├── pleo-antaeus-models
|       Definition of the Internal and API models used throughout the
|       application.
|
└── pleo-antaeus-rest
        Entry point for HTTP REST API. This is where the routes are defined.
```

### Main Libraries and dependencies
* [Exposed](https://github.com/JetBrains/Exposed) - DSL for type-safe SQL
* [Javalin](https://javalin.io/) - Simple web framework (for REST)
* [kotlin-logging](https://github.com/MicroUtils/kotlin-logging) - Simple logging framework for Kotlin
* [JUnit 5](https://junit.org/junit5/) - Testing framework
* [Mockk](https://mockk.io/) - Mocking library
* [Sqlite3](https://sqlite.org/index.html) - Database storage engine

Happy hacking 😁!


### BillingService

1. Things to consider:
   1. "Schedule payment of those invoices on the first of the month"
      1. what is the location based on which 'first of the month' should be scheduled? We live in the word of many timezones, first of the month in Europe is not exactly the first of the month in America.
         1. Based on the service location? @Scheduled annotation
         2. Based on client location? e.g. extension of CustomerTable, new column location + dynamic background task 
         3. Location could be obtained based on client' currency... it's problematic in case of regional currencies like EUR 
   2. The client' bank might be closed on the 1st because of a holiday / technical break. PaymentProvider::charge method is blocking. How long charge request might take? is asynchronous call a need?
   3. Charging method (assumption: millions of clients):
         1. sequentially: overall request&response time might take very long (sum of all separate requests&responses). Some clients might be charged on 2nd day of a month.
         2. parallel: if we charge all the client in parallel than memory&cpu consumption of the service will be low majority of the time with rare peaks - performance issues possible
         3. some kind of client bucketing seems to be a way e.g. per region/country/timezone
   4. Monitoring:
      1. Do we need/want to have an option to monitor state of currently running bank transactions?
      2. Listing of invoices filtered by state might be useful feature, protected endpoint only for people with Admin role.
   5. Failure handling:
      1. Let's imagine that payment request was sent and service crashed without receiving a response. Client' account was charged but service still thinks that payment state is PENDING.
         1. More payment state?
         2. auto-recovery mechanism?
      2. Let's imagine we bucket clients per country, there are 100 clients. What happens when 50 clients were charged and then service crashes? 
         1. auto-recovery mechanism?
      3. what happens when client cannot pay?
         1. do(when) we block this account? e.g. on the 1st of a next month?
         2. how many attempts to debit the account should we take?
         3. how to notify a client about payment failure? email?
