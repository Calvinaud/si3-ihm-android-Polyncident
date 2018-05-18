DROP TABLE IF EXISTS incidents;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS types;
DROP TABLE IF EXISTS followers;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS assignations;

/* ==== Creation of the tables ==== */

CREATE TABLE locations (
    locationId INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30),
    description TEXT)
;

CREATE TABLE incidents (
    incidentId INTEGER PRIMARY KEY AUTO_INCREMENT,
    reporterId INTEGER REFERENCES users,
    locationId INTEGER REFERENCES locations,
    categoryId INTEGER REFERENCES categories,
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
    typeId INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30),
    description TEXT
);

CREATE TABLE users (
    userId INTEGER PRIMARY KEY AUTO_INCREMENT,
    username varchar(30) UNIQUE,
    password varchar(100),
    roles VARCHAR(20) NOT NULL CHECK (roles IN ('UTILISATEUR','ADMINISTRATEUR', 'TECHNICIEN'))
);

CREATE TABLE comments(
    commentId INTEGER PRIMARY KEY AUTO_INCREMENT,
    userId INTEGER REFERENCES users,
    incidentId INTEGER REFERENCES incidents,
    date DATE,
    comment TEXT
);

CREATE TABLE assignations (
    userId INTEGER REFERENCES users,
    incidentId INTEGER REFERENCES incidents,
    startDate DATE,
    endDate DATE,
    comment TEXT,
    PRIMARY KEY(userId, incidentId)
);


/* ==== Adding of the test data ==== */

/*locations*/

INSERT INTO locations (name) VALUES('E130');
INSERT INTO locations (name) VALUES('E131');
INSERT INTO locations (name) VALUES('E132');

INSERT INTO locations (name) VALUES('O108');
INSERT INTO locations (name) VALUES('0109');
INSERT INTO locations (name) VALUES('0110');

INSERT INTO locations (name) VALUES('Parking');

/*categories*/

INSERT INTO types (name) VALUES('Fournitures');
INSERT INTO types (name) VALUES('Matériel cassé');
INSERT INTO types (name) VALUES('Autres');

/*users*/

INSERT INTO users

INSERT INTO incidents (reporterId, locationId, categoryId) VALUES ();