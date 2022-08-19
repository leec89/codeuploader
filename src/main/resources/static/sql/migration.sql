DROP DATABASE IF EXISTS capstone_db;

CREATE DATABASE IF NOT EXISTS capstone_db;

USE capstone_db;


DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS resources;
DROP TABLE IF EXISTS curriculum_topic;
DROP TABLE IF EXISTS docs;

CREATE TABLE users (
                       id INT UNSIGNED NOT NULL AUTO_INCREMENT,
                       username VARCHAR(240) NOT NULL,
                       email VARCHAR(240) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE curriculum_topic
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    title VARCHAR(240) NOT NULL ,
    description VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE resources (
                     id INT UNSIGNED NOT NULL AUTO_INCREMENT,
                     user_id INT UNSIGNED NOT NULL,
                     curriculum_topic_id INT UNSIGNED NOT NULL,
                     title VARCHAR(240) NOT NULL,
                     description TEXT NOT NULL,
                     link VARCHAR(240) NULL,
                     PRIMARY KEY (id),
                     created_at DATETIME,
                     FOREIGN KEY (user_id) REFERENCES users(id),
                     FOREIGN KEY (curriculum_topic_id) REFERENCES curriculum_topic(id)
                         ON DELETE CASCADE
);

CREATE TABLE favorites(
    user_id INT UNSIGNED NOT NULL,
    resource_id INT UNSIGNED NOT NULL,

    KEY FK_1 (resource_id),
    CONSTRAINT FK_1 FOREIGN KEY FK_1 (resource_id) REFERENCES users(id),
    KEY FK_2 (user_id),
    CONSTRAINT FK_2 FOREIGN KEY FK_2 (user_id) REFERENCES resources(id)
);


CREATE TABLE `documents`
(
    `id`          INT UNSIGNED NOT NULL ,
    `resource_id` INT UNSIGNED NOT NULL ,
    `data`        longblob NOT NULL ,
    `doc_name`    varchar(45) NOT NULL ,
    `doc_type`    varchar(45) NOT NULL ,


    PRIMARY KEY (`id`),
    KEY `FK_1` (`resource_id`),
    CONSTRAINT `FK_5` FOREIGN KEY `FK_1` (`resource_id`) REFERENCES `Resources` (`id`)


);





