CREATE USER discover WITH PASSWORD 'discover@123';
CREATE DATABASE discover;
GRANT ALL PRIVILEGES ON DATABASE discover TO discover;

CREATE TABLE Tags(
  Id    serial primary key,
  Name        VARCHAR(30) not null,
  Description        VARCHAR(80) not null,
  Time_Added        DATE
);