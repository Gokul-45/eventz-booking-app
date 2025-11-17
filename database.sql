CREATE DATABASE eventdb;
USE eventdb;

CREATE TABLE event (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    date VARCHAR(20),
    location VARCHAR(100),
    description VARCHAR(255),
    price DOUBLE,
    totalSeats INT,
    bookedSeats INT
);

CREATE TABLE participant (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100),
    rsvp BOOLEAN,
    cancelled BOOLEAN,
    event_id INT,
    FOREIGN KEY (event_id) REFERENCES event(id)
);
