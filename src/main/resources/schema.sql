--Schema Definition
CREATE SCHEMA IF NOT EXISTS minilinkDB;
--Table definition
CREATE TABLE minilinkDB.link_store (
  actualLink VARCHAR(100) PRIMARY KEY,
  mini_link VARCHAR(100)
);