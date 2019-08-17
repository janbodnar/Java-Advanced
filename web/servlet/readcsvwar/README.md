This is a simple web application which returns country names from a CSV file located 
within a WAR. We use Opencsv library to load the data and JSP/JSTL to display it. 

Project structure:
```
pom.xml
src
├───main
│   ├───java
│   │   └───com
│   │       └───zetcode
│   │           ├───bean
│   │           │       Country.java
│   │           ├───service
│   │           │       CountryService.java
│   │           └───web
│   │                   ReadCountries.java
│   ├───resources
│   │       countries.csv
│   └───webapp
│           index.jsp
│           listCountries.jsp
│           showError.jsp
└───test
    └───java
```
