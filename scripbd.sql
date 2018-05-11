DROP TABLE IF EXISTS incidents;
DROP TABLE IF EXISTS location;
DROP TABLE IF EXISTS types;
DROP TABLE IF EXISTS follows;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS commentaires;
DROP TABLE IF EXISTS affectations;

CREATE TABLE locations (
locationId INTEGER PRIMARY KEY AUTOINCREMENT,
name VARCHAR(30)
description TEXT);

CREATE TABLE incidents (
incidentId INTEGER PRIMARY KEY AUTOINCREMENT,
reporterId INTEGER REFERENCES users,
locationId INTEGER REFERENCES locations,
categorieId INETEGER REFERENCES categories,
importance INTEGER,
urlPhoto VARCHAR(100),
title VARCHAR(30),
description TEXT);

CREATE TABLE follows (
incidentId INTEGER REFERENCES incidents,
userId INTEGER REFERENCES users,
PRIMARY KEY(incidentId, userId));

CREATE TABLE categories(
categorieId INTEGER PRIMARY KEY AUTOINCREMENT,
name VARCHAR(30),
description TEXT);

CREATE TABLE users (
userId INTEGER PRIMARY KEY AUTOINCREMENT,
username varchar(30) UNIQUE,
password varchar(100),
roles VARCHAR(20) NOT NULL CHECK (roles IN ('UTILISATEUR','ADMINISTRATEUR', 'TECHNICIEN')));

CREATE TABLE commentaires(
commentid INTEGER PRIMARY KEY AUTOINCREMENT,
userId INTEGER REFERENCES users,
incidentId INTEGER REFERENCES incidents,
date DATE,
comment TEXT);

CREATE TABLE affectation (
userId INTEGER REFERENCES users,
incidentId INTEGER REFERENCES incidents,
dateDebut DATE,
dateFin DATE,
comment TEXT,
PRIMARY KEY(userId, incidentId));