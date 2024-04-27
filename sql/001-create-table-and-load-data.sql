DROP TABLE IF EXISTS music;

CREATE TABLE music (
  id int unsigned AUTO_INCREMENT,
  title VARCHAR(100) NOT NULL,
  artist VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);

INSERT INTO music (title, artist) VALUES ("jaded", "Aero Smith");
INSERT INTO music (title, artist) VALUES ("Runaway Baby", "Bruno Mars");
INSERT INTO music (title, artist) VALUES ("WHY", "Avril Lavigne");
INSERT INTO music (title, artist) VALUES ("It's My Life", "Bon Jovi");
INSERT INTO music (title, artist) VALUES ("You're Young", "Fun");
INSERT INTO music (title, artist) VALUES ("The Way I am", "Ana Johnson");
