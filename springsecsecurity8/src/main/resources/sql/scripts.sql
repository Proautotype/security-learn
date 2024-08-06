create table users(
                      username varchar(50) not null primary key,
                      password varchar(500) not null,
                      enabled boolean not null
);

create table authorities (
                             username varchar(50) not null,
                             authority varchar(50) not null,
                             constraint fk_authorities_users
                                 foreign key(username)
                                     references users(username)
);

create unique index ix_auth_username on authorities (username,authority);

INSERT IGNORE INTO `users` VALUES('user','{noop}EazyBytes@12345','1');
INSERT IGNORE INTO `authorities` VALUES('user','read');

INSERT IGNORE INTO `users` VALUES('admin','{bcrypt}$2a$12$CjL.tS.m/dmqosqHbtQOV.GHDq6OC87zdukUHTAkXuPpDSKXcSYLG','1');
INSERT IGNORE INTO `authorities` VALUES('admin','admin');

create table customers(
  `id` int NOT NULL AUTO_INCREMENT primary key,
  `email` varchar(45) not null,
  `pwd` varchar(200) not null,
  `role` varchar(45) not null
);

INSERT INTO `customers` (email, pwd, role) VALUES('happy@mail.com','{noop}EazyBytes@12345','read');
INSERT INTO `customers` (email, pwd, role) VALUES('admin@mail.com','{bcrypt}$2a$12$CjL.tS.m/dmqosqHbtQOV.GHDq6OC87zdukUHTAkXuPpDSKXcSYLG','admin');