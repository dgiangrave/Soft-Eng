DROP TABLE EDGE;
DROP TABLE PROLOCATION;
DROP TABLE PROFESSIONAL;
DROP TABLE SERVICE;
DROP TABLE NODE;
DROP TABLE ADMIN;


CREATE TABLE NODE(
  XPOS INT,
  YPOS INT,
  FLOOR INT,
  ISHIDDEN BOOLEAN,
  ENABLED BOOLEAN DEFAULT TRUE,
  TYPE VARCHAR(100),
  NAME VARCHAR(100),
  ROOMNUM VARCHAR(100),
  PRIMARY KEY(XPOS, YPOS, FLOOR)
);

CREATE TABLE EDGE(
  XPOS1 INT,
  YPOS1 INT,
  FLOOR1 INT,
  XPOS2 INT,
  YPOS2 INT,
  FLOOR2 INT,
  CONSTRAINT fkXPOS1 FOREIGN KEY (XPOS1, YPOS1, FLOOR1) REFERENCES NODE(XPOS, YPOS, FLOOR),
  CONSTRAINT fkXPOS2 FOREIGN KEY (XPOS2, YPOS2, FLOOR2) REFERENCES NODE(XPOS, YPOS, FLOOR)
);

CREATE TABLE PROFESSIONAL(
  ID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  FIRSTNAME VARCHAR(100),
  LASTNAME VARCHAR(100),
  TYPE VARCHAR(100),
  DEPARTMENT VARCHAR(100)
);

CREATE TABLE PROLOCATION(
  PROID INT,
  XPOS INT,
  YPOS INT,
  FLOOR INT,
  CONSTRAINT fkProID FOREIGN KEY (PROID) REFERENCES PROFESSIONAL(ID),
  CONSTRAINT fkLoc FOREIGN KEY (XPOS, YPOS, FLOOR) REFERENCES NODE(XPOS, YPOS, FLOOR),
  PRIMARY KEY (PROID, XPOS, YPOS, FLOOR)
);

CREATE TABLE ADMIN(
  ID INTEGER PRIMARY KEY,
  FIRSTNAME VARCHAR(100),
  LASTNAME VARCHAR(100),
  USERNAME VARCHAR(100),
  PASSWORD VARCHAR(200)
);

CREATE TABLE SERVICE(
  ID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  NAME VARCHAR(100),
  TYPE VARCHAR(100),
  XPOS INT,
  YPOS INT,
  FLOOR INT,
  CONSTRAINT fkSerLoc FOREIGN KEY (XPOS, YPOS, FLOOR) REFERENCES NODE(XPOS, YPOS, FLOOR)
);

-- Filling in PROFESSIONAL
INSERT INTO PROFESSIONAL (FIRSTNAME, LASTNAME, TYPE, DEPARTMENT) VALUES ('Mario', 'Zyla', 'Doctor', 'Cardiology');
INSERT INTO PROFESSIONAL (FIRSTNAME, LASTNAME, TYPE, DEPARTMENT) VALUES ('Augusto', 'Wong', 'Nurse', 'Cardiology');
INSERT INTO PROFESSIONAL (FIRSTNAME, LASTNAME, TYPE, DEPARTMENT) VALUES ('Griffin', 'Tabor', 'Doctor', 'Allergic Diseases');
INSERT INTO PROFESSIONAL (FIRSTNAME, LASTNAME, TYPE, DEPARTMENT) VALUES ('Alex', 'Ibro', 'Doctor', 'Neurology');

-- Filling in NODE
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (384, 75, 4, false, true, 'Restroom', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (370, 51, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (406, 56, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (336, 138, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (431, 88, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (431, 122, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (395, 171, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (374, 191, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (371, 166, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (402, 217, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (371, 225, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (399, 257, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (398, 286, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (369, 340, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (361, 404, 4, false, true, 'Restroom', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (410, 401, 4, false, true, 'Restroom', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (267, 439, 4, false, true, 'Restroom', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (195, 447, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (110, 438, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (88, 407, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (12, 428, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (499, 439, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (574, 447, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (754, 424, 4, false, true, 'Doctor''s Office', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (730, 424, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (564, 422, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (499, 423, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (385, 423, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (267, 422, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (201, 423, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (109, 424, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (88, 425, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (33, 427, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (384, 384, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (384, 340, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (386, 287, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (385, 257, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (386, 226, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (385, 192, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (384, 164, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (383, 144, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (350, 139, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (421, 137, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (421, 79, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (351, 79, 4, true, true, 'Food Service', '1', '1');
INSERT INTO APP.NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM) VALUES (386, 452, 4, false, true, 'Doctor''s Office', 'Smith', '211');

-- Filling in EDGE
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (12, 428, 4, 33, 427, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (33, 427, 4, 88, 425, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (88, 407, 4, 88, 425, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (88, 425, 4, 109, 424, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (109, 424, 4, 110, 438, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (109, 424, 4, 201, 423, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (195, 447, 4, 201, 423, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (201, 423, 4, 267, 422, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (267, 422, 4, 267, 439, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (267, 422, 4, 385, 423, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (385, 423, 4, 499, 423, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (499, 423, 4, 499, 439, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (499, 423, 4, 564, 422, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (564, 422, 4, 574, 447, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (564, 422, 4, 730, 424, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (730, 424, 4, 754, 424, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (385, 423, 4, 410, 401, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (361, 404, 4, 385, 423, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (384, 384, 4, 410, 401, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (361, 404, 4, 384, 384, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (384, 384, 4, 385, 423, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (384, 340, 4, 384, 384, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (369, 340, 4, 384, 340, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (384, 340, 4, 386, 287, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (386, 287, 4, 398, 286, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (385, 257, 4, 386, 287, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (385, 257, 4, 399, 257, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (385, 257, 4, 386, 226, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (371, 225, 4, 386, 226, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (386, 226, 4, 402, 217, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (385, 192, 4, 386, 226, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (384, 164, 4, 385, 192, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (374, 191, 4, 385, 192, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (384, 164, 4, 395, 171, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (371, 166, 4, 384, 164, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (383, 144, 4, 384, 164, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (383, 144, 4, 421, 137, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (350, 139, 4, 383, 144, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (336, 138, 4, 350, 139, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (350, 139, 4, 351, 79, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (421, 137, 4, 431, 122, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (431, 88, 4, 431, 122, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (421, 79, 4, 431, 88, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (406, 56, 4, 421, 79, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (384, 75, 4, 406, 56, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (370, 51, 4, 384, 75, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (351, 79, 4, 370, 51, 4);
INSERT INTO APP.EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2) VALUES (351, 79, 4, 384, 75, 4);


-- Filling in ADMIN
INSERT INTO ADMIN (ID, FIRSTNAME, LASTNAME, USERNAME, PASSWORD) VALUES (1, 'Jason', 'Ashton', 'Jason', '$2a$10$MyXFwAYccDvY6B8zXDYDrOldaF0kBrPI/ybyUXCOhC0.i7o7qaCBu');