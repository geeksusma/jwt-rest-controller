# # jwt-rest-controller

  This is an example about how to implement a Login Filter based on Spring Security, but in this case, handling a Jason Web Token (JWT) so that, we could see how a Stateless Filter can manage the Authentication and also the Authorization.

  ## How it works

  To check how this example works, just start the app (mvn clean package spring-boot:run) and do a POST against your localhost:8080/login with the next JSON:
  {
  	"username" : "test@geeksusma.es",
  	"password": "password"
  }

  You can use PostMan, curl or just open the code in your favourite IDE and checking the "test-integration" folder. There you can find some Spring Boot Tests to check the app. For example, the curl command is as follows:
  curl --header "Content-Type: application/json"  --request POST  --data '{"username":"test@geeksusma.es","password":"password"}'  http://localhost:8080/login

  ### Tech

  This exmaple uses a number of open source projects to work properly:

  * [Lombok] - Please enable Lombok in your favorite IDE for coding:
  * [Spring Boot]
  * [H2SQL]
  * [Maven]
  * [IO-Jwt]

  ### Installation

  This examples requires Maven to build.
  Also requires a JDK 1.8 to run
  The H2 database is a in-memory instance with only one user as default (test@geeksusma.es / password)

  ### Time to play!

  Don't hesitate of adding more users to the data.sql file (used by spring-boot by default to populate data in the h2 database) even you could start adding more endpoints to check later how the authorization works.

  The only you have to bear in mind is the algorithm to cipher the password, have a look at the configuration yml to let you know which algorithm is used and the secret key.

  The last thing you have to know is, maybe in a little bit paranoid, but I always like to save the password ciphered and as a binary data in the database just for security reason (if your database is attacked and accessed, the password is not readable at all!) so if you are interested in adding your own set of users, you'd have to convert your string to a ciphered text and later that text to a byte[]


