# FileMgmt

This project was generated with [Spring boot](http://docs.spring.io/spring-boot/docs/1.5.2.RELEASE/api/) version 1.5.2.RELEASE.

## Further help

To get more help on the `spring boot` use (https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)

## Configuration

Open file /src/main/resources/application.properties  
Change the below properties as per your need.  

* DATA SOURCE  
spring.datasource.url = jdbc:oracle:thin:@localhost:1521:orcl  
spring.datasource.username = AdityaBirla  
spring.datasource.password = 123456  
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.OracleDialect  

* tomcat  
server.port=8800  

* mail  
spring.mail.host=smtp.gmail.com  
spring.mail.port=587  
spring.mail.username=xyz@gmail.com  
spring.mail.password=xyz123  
spring.mail.properties.mail.smtp.starttls.enable=true  
spring.mail.properties.mail.smtp.starttls.required=true  
spring.mail.properties.mail.smtp.auth=true  
spring.mail.properties.mail.smtp.connectiontimeout=5000  
spring.mail.properties.mail.smtp.timeout=5000  
spring.mail.properties.mail.smtp.writetimeout=5000  

* folder  
adityaBirla.folder=F:\\files\\    --- (Documents to be uploaded in this folder)  

* angular app adddress  
adityaBirla.angularAppAddress=http://192.168.0.103:4200  --- (FrontEnd app running ip and port number) 

## How to run the project ? 
Open file src\main\java\com\adityaBirla\UserFrontApplication.java  
Run as java application


