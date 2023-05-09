
INSERT INTO Gift_Certificate (name, description, price, create_day, last_update_date) values ( 'name_1', 'description_1', 4, '2022-01-22T16:56:01.027','2022-01-22T16:56:01.027');
INSERT INTO Gift_Certificate (name, description, price, create_day, last_update_date) values ( 'name_2', 'description_2', 5, '2022-01-22T16:56:01.027','2022-01-22T16:56:01.027');
INSERT INTO Gift_Certificate (name, description, price, create_day, last_update_date) values ( 'name_3', 'description_3', 3, '2022-01-22T16:56:01.027','2022-01-22T16:56:01.027');
INSERT INTO Gift_Certificate (name, description, price, create_day, last_update_date) values ( 'name_4', 'description_4', 6, '2022-01-22T16:56:01.027','2022-01-22T16:56:01.027');
INSERT INTO Gift_Certificate (name, description, price, create_day, last_update_date) values ( 'name_5', 'description_5', 9, '2022-01-22T16:56:01.027','2022-01-22T16:56:01.027');
INSERT INTO Gift_Certificate (name, description, price, create_day, last_update_date) values ( 'name_6', 'description_6', 7, '2022-01-22T16:56:01.027','2022-01-22T16:56:01.027');



INSERT INTO Tag (name) values ('nameTag_1');
INSERT INTO Tag (name) values ('nameTag_2');
INSERT INTO Tag (name) values ('nameTag_3');
INSERT INTO Tag (name) values ('nameTag_4');
INSERT INTO Tag (name) values ('nameTag_5');


INSERT INTO Gift_Tag (gift_certificate_id, tag_id) values (1,3);
INSERT INTO Gift_Tag (gift_certificate_id, tag_id) values (2,4);
INSERT INTO Gift_Tag (gift_certificate_id, tag_id) values (3,5);
INSERT INTO Gift_Tag (gift_certificate_id, tag_id) values (5,1);
INSERT INTO Gift_Tag (gift_certificate_id, tag_id) values (4,5);

