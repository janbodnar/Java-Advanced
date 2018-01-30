-- SQL for the Cars table

CREATE TABLE Cars(Id BIGINT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(150),
    Price INTEGER);
    
INSERT INTO Cars(Name, Price) VALUES('Audi', 52642);
INSERT INTO Cars(Name, Price) VALUES('Mercedes', 57127);
INSERT INTO Cars(Name, Price) VALUES('Skoda', 9000);
INSERT INTO Cars(Name, Price) VALUES('Volvo', 29000);
INSERT INTO Cars(Name, Price) VALUES('Bentley', 350000);
INSERT INTO Cars(Name, Price) VALUES('Citroen', 21000);
INSERT INTO Cars(Name, Price) VALUES('Hummer', 41400);
INSERT INTO Cars(Name, Price) VALUES('Volkswagen', 21600);
