# SecuritySlice

Displays the Average Monthly Open and Close prices for a given set of securities.  
Max daily profit, busy day, and biggest loser extra features are implemented too.

Utilizes the Quandl API(https://www.quandl.com/product/WIKIP/documentation/about) for retrieving securities information. 

Design
---
Maven and JDK8 are needed to build the securities application.  
The application is built on the dropwizard framework.  
Google guava cache is incorporated to cache the Quandl API results to improve performance and prevent redundant calls being made.
The business logic is contained within QuandlSecurityProvider.java.

How to run tests
---
* Run `mvn clean test` to run unit test
* Run `mvn clean integration-test` to run unit + integration tests(Quandl API integration)

How to start the SecuritySlice application
---
1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/security-slice-1.0-SNAPSHOT.jar server config.yml`


Webservice endpoints
---
* Get avg monthly open/close prices - [GET] `http://localhost:8080/api/securities`
* Get max daily profit - [GET] `http://localhost:8080/api/securities/max-daily-profit`
* Get busy days - [GET] `http://localhost:8080/api/securities/busy-days`
* Get biggest loser - [GET] `http://localhost:8080/api/securities/biggest-loser`


Misc
---
* config.yml - application configuration settings
* SecuritySliceApplication.java - Main application class