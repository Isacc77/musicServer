drop database if exists `musicserver`;
create database if not exists `musicserver` character set utf8;

use `musicserver`;

CREATE TABLE `user` (
`id` INT PRIMARY KEY AUTO_INCREMENT,
`username` varchar(20) NOT NULL,
`password` varchar(255) NOT NULL
);

CREATE TABLE `music` (
`id` int PRIMARY KEY AUTO_INCREMENT,
`title` varchar(50) NOT NULL,
`singer` varchar(30) NOT NULL,
`time` varchar(13) NOT NULL,
`url` varchar(1000) NOT NULL,
`userid` int(11) NOT NULL
);

CREATE TABLE `favouritemusic` (
`id` int PRIMARY KEY AUTO_INCREMENT,
`user_id` int(11) NOT NULL,
`music_id` int(11) NOT NULL
);

INSERT INTO user(username,password)
VALUES("bob","$2a$10$Bs4wNEkledVlGZa6wSfX7eCSD7wRMO0eUwkJH0WyhXzKQJrnk85li");

INSERT INTO user(username,password)
VALUES("test","123");

select m.*
from favouritemusic f, music m
where m.id = f.music_id
and f.user_id = '4'
and title like concat('%','a','%');