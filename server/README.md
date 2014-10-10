# Instructions

## IDE setup 

For the usage inside an IDE do the following:

1. Make sure you have an Eclipse with m2e installed (preferably [STS](http://spring.io/sts)).
2. Install [Lombok](http://projectlombok.org).
   1. Download it from the [project page](http://projectlombok.org/download.html).
   2. Run the JAR (double click or `java -jar …`).
   3. Point it to your Eclipse installation, run the install.
3. Install checkstyle 5.8 plugin
   3.1 Set up your ide to use the project checkstyle located at build-config/checkstyle
4. Restart Eclipse.

## Start the server
- gradlew **bootRun**

## Browse the server
- browse the server in: [http://localhost:8080](http://localhost:8080)

## Befor pushing code
- gradlew **check** 
