CREATE TABLE `gift_certificate` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(45) NOT NULL,
  `price` double NOT NULL,
  `create_day` varchar(45) NOT NULL,
  `last_update_date` varchar(45) NOT NULL,
  `duration` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;


CREATE TABLE `tag` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

CREATE TABLE `gift_tag` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `gift_certificate_id` int(10) unsigned NOT NULL,
  `tag_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_gift_tag_tag_idx` (`gift_certificate_id`),
  KEY `fk_gift_tag_gift_certificate1_idx` (`tag_id`),
  CONSTRAINT `fk_gift_tag_gift_certificate1` FOREIGN KEY (`gift_certificate_id`) REFERENCES `gift_certificate` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_gift_tag_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

CREATE TABLE `order` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `date` varchar(45) NOT NULL,
  `cost` double NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `gift_certificate_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_user1_idx` (`user_id`),
  KEY `fk_order_gift_certificate1_idx` (`gift_certificate_id`),
  CONSTRAINT `fk_order_gift_certificate1` FOREIGN KEY (`gift_certificate_id`) REFERENCES `gift_certificate` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;