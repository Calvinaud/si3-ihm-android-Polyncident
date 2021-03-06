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
    telephoneNumber VARCHAR(20),
    emailAddress VARCHAR(50),
    img BLOB,
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
    img BLOB,
    declarationDate DATETIME,
    title VARCHAR(30),
    description TEXT,
    status INTEGER
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
INSERT INTO users (userId, username, emailAddress, password, roles, telephoneNumber) VALUES (0,'Mathieu', 'Mat@gmail.com', 'Mathieu', 'UTILISATEUR', "0645128214");
INSERT INTO users (userId, username, emailAddress, password, roles) VALUES (1, 'Roger', 'Roger@gmail.com','Roger', 'TECHNICIEN');
INSERT INTO users (userId, username, emailAddress, password, roles) VALUES (2, 'Bernard', 'Bernard@gmail.com','Roger', 'TECHNICIEN');
INSERT INTO users (userId, username, emailAddress, password, roles, telephoneNumber) VALUES (3, 'Claudine', 'Claudine@gmail.com', 'Claudine', 'ADMINISTRATEUR','0678128214');

--incident
INSERT INTO incidents (incidentId, reporterId, locationId, typeId, importance, title, status, declarationDate, description) VALUES (0, 0, 3, 1, 1, 'Plus de feuilles',0, '2018-05-22 11:15:00', "description");
INSERT INTO incidents (incidentId, reporterId, locationId, typeId, importance, title, status, declarationDate) VALUES (1, 1, 5, 1, 3, 'Plus de stylos',2, '2018-05-24 12:00:00');
INSERT INTO incidents (incidentId, reporterId, locationId, typeId, importance, title, status, declarationDate) VALUES (2, 1, 5, 2, 3, 'Panne rétroprojecteur 3',2, '2018-05-21 15:00:00');

--assignation
INSERT INTO assignations (userId, incidentId, startDate, endDate) VALUES (1, 1, '2018-05-25 15:30:00', '2018-05-25 18:30:00');
INSERT INTO assignations (userId, incidentId, startDate, endDate) VALUES (1, 2, '2018-05-26 14:00:00', '2018-05-26 15:30:00');
INSERT INTO assignations (userId, incidentId, startDate, endDate) VALUES (1, 3, '2018-05-25 16:00:00', '2018-05-25 17:30:00');

--subs
INSERT INTO subscriptions (userId, incidentId) VALUES (0,1);

--comment
INSERT INTO comments (commentId, userId, incidentId, date, comment) VALUES (0, 3, 0, '2018-05-23 12:00:00', 'Veuillez me contactez')
