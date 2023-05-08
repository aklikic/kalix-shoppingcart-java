# Kalix Demo - Loan application - Spring
Not supported by Lightbend in any conceivable way, not open for contributions.<br>
## Prerequisite
Java 17<br>
Apache Maven 3.6 or higher<br>
[Kalix CLI](https://docs.kalix.io/kalix/install-kalix.html) <br>
Docker 20.10.8 or higher (client and daemon)<br>
Container registry with public access (like Docker Hub)<br>
Access to the `gcr.io/kalix-public` container registry<br>
cURL<br>
IDE / editor<br>

## Create kickstart maven project

```
mvn \
archetype:generate \
-DarchetypeGroupId=io.kalix \
-DarchetypeArtifactId=kalix-spring-boot-archetype \
-DarchetypeVersion=1.1.7
```
Define value for property 'groupId': `com.example`<br>
Define value for property 'artifactId': `shoppingcart-spring`<br>
Define value for property 'version' 1.0-SNAPSHOT: :<br>
Define value for property 'package' com.example: : `com.example.shoppingcart`<br>

## Import generated project in your IDE/editor

## Update dockerId in pom.xml
In `pom.xml` in `<dockerImage>my-docker-repo/${project.artifactId}</dockerImage>` replace `my-docker-repo` with the right `dockerId`

## ACL
In `com.example.loanapp.Main` class `ACL` annotation using: <br>
`@Acl(allow = @Acl.Matcher(principal = Acl.Principal.ALL))`

# Shopping cart service

## Define persistence (domain)
1. Create package `com.example.loanapp.doman`
2. Create interface `LoanAppDomainEvent`
3. Make interface a `sealed` interface
4. Add events using: `evts` (place cursor inside of interface definition)
5. Add annotations using: `evtsanno` (place cursor before class definition)
6. Create enum `LoanAppDomainStatus`
7. Add values using: `status`
8. Create record `LoanAppDomainState`
9. Add record parameters using: `stateparams`
10. Add helper methods using: `statemethods`

## Define API data structure and endpoints
1. Create package `com.example.loanapp.api`
2. Create interface `LoanAppApi`
3. Make interface a `sealed` interface
4. Add requests and responses data structures using: `apimodel`
5. Create class `LoanAppService`
6. Class extending `EventSourcedEntity<LoanAppDomainState>`
7. Add class level annotations using: `srvann` (place cursor before class definition)
8. Add all methods: `srvall` (place cursor inside of class definition)


## Implement unit test
1. Create  folder `src/test/java`
2. Create  package `com.example.loanapp`
3. Create  `com.example.loanapp.LoanAppServiceTest` class
4. Add imports using: `utimport`
5. Add content using: `ut`

## Run unit test
```
mvn test
```
## Implement integration test
1. Edit `com.example.loanapp.IntegrationTest` class
2. Add imports using: `itimport`
3. Add content using: `it`

## Run integration test
```
mvn -Pit verify
```

<i><b>Note</b></i>: Integration tests uses [TestContainers](https://www.testcontainers.org/) to span integration environment so it could require some time to download required containers.
Also make sure docker is running.


## Package & Deploy

<i><b>Note</b></i>: Make sure you have updated `dockerImage` in your `pom.xml` and that your local docker is authenticated with your docker container registry

```
mvn deploy
```


## Expose service
```
kalix services expose loan-application-spring
```
## Test service in production
Submit loan application:
```
curl -XPOST -d '{
  "clientId": "12345",
  "clientMonthlyIncomeCents": 60000,
  "loanAmountCents": 20000,
  "loanDurationMonths": 12
}' https://<somehost>.kalix.app/loanapp/1/submit -H "Content-Type: application/json"
```
Get loan application:
```
curl -XGET https://<somehost>.kalix.app/loanapp/1 -H "Content-Type: application/json"
```
Approve:
```
curl -XPOST https://<somehost>.kalix.app/loanapp/1/approve -H "Content-Type: application/json"
```

## Copy-paste list

```
mvn \
archetype:generate \
-DarchetypeGroupId=io.kalix \
-DarchetypeArtifactId=kalix-spring-boot-archetype \
-DarchetypeVersion=LATEST
```
```
com.example
```
```
loan-application-spring
```
```
com.example.loanapp
```
```
domain package
```
```
LoanAppDomainEvent
```
```
LoanAppDomainStatus
```
```
LoanAppDomainState
```
```
api package
```
```
LoanAppApi
```
```
LoanAppService
```
```
EventSourcedEntity
```
```
src/test/java folder
```
```
com.example.loanapp package
```
```
LoanAppServiceTest
```
```
curl -XPOST -d '{
  "clientId": "12345",
  "clientMonthlyIncomeCents": 60000,
  "loanAmountCents": 20000,
  "loanDurationMonths": 12
}' https://fancy-king-1984.us-east1.kalix.app/loanapp/1/submit -H "Content-Type: application/json"
```
```
curl -XGET https://fancy-king-1984.us-east1.kalix.app/loanapp/1 -H "Content-Type: application/json"
```
```
curl -XPOST https://fancy-king-1984.us-east1.kalix.app/loanapp/1/approve -H "Content-Type: application/json"
```