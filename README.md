# SecuritySlice

Displays the Average Monthly Open and Close prices for a given set of securities

TODO what it does and where it gets it's data
---


TODO add tech
---
mvn
java
dropwizard
bootstrap

TODO add application design
---

How to run tests
---
* Run `mvn clean test` to run unit test
* Run `mvn clean integration-test` to run unit + integration tests(Quandl API integration)
TODO add mvn verify?

TODO outline config
---

How to start the SecuritySlice application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/security-slice-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`


TODO endpoints
---

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
