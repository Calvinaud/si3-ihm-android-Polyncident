DROP TABLE IF EXISTS assignations;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS followers;
DROP TABLE IF EXISTS incidents;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS types;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS users;

--==== Creation of the tables ====

CREATE TABLE users (
    userId INTEGER PRIMARY KEY,
    username varchar(30),
    password varchar(100),
    roles VARCHAR(20) NOT NULL CHECK (roles IN ('UTILISATEUR','ADMINISTRATEUR', 'TECHNICIEN'))
);

CREATE TABLE locations (
    locationId INTEGER PRIMARY KEY,
    name VARCHAR(30),
    description TEXT
);

CREATE TABLE types(
    typeId INTEGER PRIMARY KEY,
    name VARCHAR(30),
    description TEXT
);

CREATE TABLE incidents (
    incidentId INTEGER PRIMARY KEY,
    reporterId INTEGER REFERENCES users,
    locationId INTEGER REFERENCES locations,
    typeId INTEGER REFERENCES types,
    importance INTEGER,
    urlPhoto VARCHAR(100),
    title VARCHAR(30),
    description TEXT
);

CREATE TABLE subscriptions (
    incidentId INTEGER REFERENCES incidents,
    userId INTEGER REFERENCES users,
    PRIMARY KEY(incidentId, userId)
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

INSERT INTO locations (locationId, name) VALUES('1', 'E130');
INSERT INTO locations (locationId, name) VALUES('2', 'E131');
INSERT INTO locations (locationId, name) VALUES('3','E132');

INSERT INTO locations (locationId, name) VALUES('4','O108');
INSERT INTO locations (locationId, name) VALUES('5','0109');
INSERT INTO locations (locationId, name) VALUES('6','0110');

INSERT INTO locations (locationId, name) VALUES('7','Parking');

--categories

INSERT INTO types (typeid, name) VALUES('1','Fournitures');
INSERT INTO types (typeid, name) VALUES('2','Matériel cassé');
INSERT INTO types (typeid, name) VALUES('3','Autres');

--users
INSERT INTO users (userId, username, password, roles) VALUES (1,'Mathieu', 'Mathieu', 'UTILISATEUR');
INSERT INTO users (userId, username, password, roles) VALUES (2, 'Bob', 'Bob', 'TECHNICIEN');

--incident
INSERT INTO incidents (incidentId, reporterId, locationId, typeId, importance, title) VALUES (1, 1, 3, 2, 3, 'Incident 1');
INSERT INTO incidents (incidentId, reporterId, locationId, typeId, importance, title) VALUES (2, 1, 5, 1, 3, 'Incident 2');

--assignation
INSERT INTO assignations (userId, incidentId, startDate, endDate) VALUES (2, 1, '2018-05-19 15:30:00', '2018-05-19 18:30:00');
INSERT INTO assignations (userId, incidentId, startDate, endDate) VALUES (2, 2, '2018-05-19 11:00:00', '2018-05-19 13:00:00');

SELECT incidentId FROM assignations WHERE userId=2 AND startDate>='2018-05-19 00:00:00' AND endDate<'2018-05-19 23:59:59';

SELECT i.title, a.startDate, a.endDate, l.name FROM assignations AS a, incidents AS i, locations AS l WHERE l.locationId=i.locationId AND i.incidentId=a.incidentId AND a.userId=2;