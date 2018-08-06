CREATE DATABASE example_db;

CREATE TABLE person(
	id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name varchar(150) NOT NULL,
	phone varchar(10),
	email varchar(150) NOT null,	
	salary decimal(12,2) NOT NULL DEFAULT '0.00',
	status tinyint(4) NOT NULL DEFAULT '1',
	created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	created_by int(11) NOT NULL,
	updated_at datetime DEFAULT NULL,
	updated_by int(11) DEFAULT NULL
);

CREATE TABLE pet(
	id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name varchar(150) NOT NULL,
	person_id int NOT NULL,
	status tinyint(4) NOT NULL DEFAULT '1',
	created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	created_by int(11) NOT NULL,
	updated_at datetime DEFAULT NULL,
	updated_by int(11) DEFAULT NULL,
	CONSTRAINT fk_pet_person FOREIGN KEY (person_id) REFERENCES person(id)
);




