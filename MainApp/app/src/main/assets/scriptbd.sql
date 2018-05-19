DROP TABLE IF EXISTS incidents;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS types;
DROP TABLE IF EXISTS followers;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS assignations;

--==== Creation of the tables ====

CREATE TABLE locations (
    locationId INTEGER PRIMARY KEY,
    name VARCHAR(30),
    description TEXT
);

CREATE TABLE incidents (
    incidentId INTEGER PRIMARY KEY,
    reporterId INTEGER REFERENCES users,
    locationId INTEGER REFERENCES locations,
    typeId INTEGER REFERENCES categories,
    importance INTEGER,
    urlPhoto VARCHAR(100),
    title VARCHAR(30),
    description TEXT
);

CREATE TABLE followers (
    incidentId INTEGER REFERENCES incidents,
    userId INTEGER REFERENCES users,
    PRIMARY KEY(incidentId, userId)
);

CREATE TABLE types(
    typeId INTEGER PRIMARY KEY,
    name VARCHAR(30),
    description TEXT
);

CREATE TABLE users (
    userId INTEGER PRIMARY KEY,
    username varchar(30) UNIQUE,
    password varchar(100),
    role VARCHAR(20) NOT NULL CHECK (roles IN ('UTILISATEUR','ADMINISTRATEUR', 'TECHNICIEN'))
);

CREATE TABLE comments(
    commentId INTEGER PRIMARY KEY,
    userId INTEGER REFERENCES users,
    incidentId INTEGER REFERENCES incidents,
    date DATETIME,
    comment TEXT
);

CREATE TABLE assignations (
    userId INTEGER REFERENCES users,
    incidentId INTEGER REFERENCES incidents,
    startDate DATETIME,
    endDate DATETIME,
    comment TEXT,
    PRIMARY KEY(userId, incidentId)
);


--==== Adding of the test data ====

--locations

INSERT INTO locations (name) VALUES('E130');
INSERT INTO locations (name) VALUES('E131');
INSERT INTO locations (name) VALUES('E132');

INSERT INTO locations (name) VALUES('O108');
INSERT INTO locations (name) VALUES('0109');
INSERT INTO locations (name) VALUES('0110');

INSERT INTO locations (name) VALUES('Parking');

--categories

INSERT INTO types (name) VALUES('Fournitures');
INSERT INTO types (name) VALUES('Matériel cassé');
INSERT INTO types (name) VALUES('Autres');

--users
INSERT INTO users (userId, username, password, role) VALUES ("11","Mathieu", "Mathieu", "UTILISATEUR");
INSERT INTO users (userId, username, password, role) VALUES ("42", "Bob", "Bob", "TECHNICIEN");

--incident
INSERT INTO incident (incidentId) VALUE ("1");
INSERT INTO incident (incidentId) VALUE ("2");

--assignation
INSERT INTO assignation (userId, incidentId, startDate, endDate) VALUES ("42", "1", "2018-05-19 15:30:00", "2018-05-20 18:30:00");
INSERT INTO assignation (userId, incidentId, startDate, endDate) VALUES ("42", "2", "2018-05-19 11:00:00", "2018-05-20 13:00:00");