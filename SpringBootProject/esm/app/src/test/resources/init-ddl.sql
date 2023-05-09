CREATE TABLE `gift_certificate` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `create_day` varchar(45) DEFAULT NULL,
  `last_update_date` varchar(45) DEFAULT NULL,
  `duration` int NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `tag` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `gift_tag` (
  `id` int NOT NULL AUTO_INCREMENT,
  `gift_certificate_id` int NOT NULL,
  `tag_id` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_gift_tag_gift_certificate1` FOREIGN KEY (`gift_certificate_id`) REFERENCES `gift_certificate` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_gift_tag_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);


