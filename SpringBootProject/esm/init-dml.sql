
INSERT INTO Gift_Certificate (name, description, price, create_day, last_update_date, duration) values ( 'name_1', 'description_1', 4, '2022-01-22T16:56:01.027','2022-01-22T16:56:01.027','2022-03-22T16:56:01.027');
INSERT INTO Gift_Certificate (name, description, price, create_day, last_update_date, duration) values ( 'name_2', 'description_2', 5, '2022-01-22T16:56:01.027','2022-01-22T16:56:01.027','2022-03-22T16:56:01.027');
INSERT INTO Gift_Certificate (name, description, price, create_day, last_update_date, duration) values ( 'name_3', 'description_3', 3, '2022-01-22T16:56:01.027','2022-01-22T16:56:01.027','2022-03-22T16:56:01.027');
INSERT INTO Gift_Certificate (name, description, price, create_day, last_update_date, duration) values ( 'name_4', 'description_4', 6, '2022-01-22T16:56:01.027','2022-01-22T16:56:01.027','2022-03-22T16:56:01.027');
INSERT INTO Gift_Certificate (name, description, price, create_day, last_update_date, duration) values ( 'name_5', 'description_5', 9, '2022-01-22T16:56:01.027','2022-01-22T16:56:01.027','2022-03-22T16:56:01.027');
INSERT INTO Gift_Certificate (name, description, price, create_day, last_update_date, duration) values ( 'name_6', 'description_6', 7, '2022-01-22T16:56:01.027','2022-01-22T16:56:01.027','2022-03-22T16:56:01.027');



INSERT INTO Tag (name) values ('nameTag_1');
INSERT INTO Tag (name) values ('nameTag_2');
INSERT INTO Tag (name) values ('nameTag_3');
INSERT INTO Tag (name) values ('nameTag_4');
INSERT INTO Tag (name) values ('nameTag_5');


INSERT INTO Gift_Tag (gift_certificate_id, tag_id) values (19,17);
INSERT INTO Gift_Tag (gift_certificate_id, tag_id) values (20,18);
INSERT INTO Gift_Tag (gift_certificate_id, tag_id) values (21,16);
INSERT INTO Gift_Tag (gift_certificate_id, tag_id) values (22,16);
INSERT INTO Gift_Tag (gift_certificate_id, tag_id) values (23,16);

INSERT INTO user (name) values ('userName_1');
INSERT INTO user (name) values ('userName_2');
INSERT INTO user (name) values ('userName_3');
INSERT INTO user (name) values ('userName_4');
INSERT INTO user (name) values ('userName_5');

INSERT INTO spring_boot_certificates.order (date, cost,user_id,gift_certificate_id) values ('2022-02-10T16:56:01.027',4,1,19);
INSERT INTO spring_boot_certificates.order (date, cost,user_id,gift_certificate_id) values ('2022-02-10T16:56:01.027',6,2,20);
INSERT INTO spring_boot_certificates.order (date, cost,user_id,gift_certificate_id) values ('2022-02-10T16:56:01.027',7,3,21);
INSERT INTO spring_boot_certificates.order (date, cost,user_id,gift_certificate_id) values ('2022-02-10T16:56:01.027',8,4,22);
INSERT INTO spring_boot_certificates.order (date, cost,user_id,gift_certificate_id) values ('2022-02-10T16:56:01.027',9,3,23);