DROP TABLE IF EXISTS music;

CREATE TABLE music (
  id int unsigned AUTO_INCREMENT,
  title VARCHAR(100) NOT NULL,
  artist VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);

INSERT INTO music (title, artist) VALUES ("jaded", "Aero Smith");
INSERT INTO music (title, artist) VALUES ("Runaway Baby", "Bruno Mars");
