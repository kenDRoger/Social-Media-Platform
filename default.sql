drop database if exists mp2;

create database mp2;

use mp2;

create table account(
    username VARCHAR(30) primary key,
    password VARCHAR(30),
    user_role text check (user_role in ('admin','super_admin', 'user'))
);

create table post(
    title VARCHAR(30) not null,
    content text(200) not null,
    id int auto_increment primary key,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    username varchar(30) not null,
    foreign key (username) references account(username)
 );

create table posts(
    username VARCHAR(30) not null,
    post1 int,
    post2 int,
    post3 int,
    post4 int,
    post5 int,
    foreign key (username) references account(username),
    foreign key (post1) references post(id),
    foreign key (post2) references post(id),
    foreign key (post3) references post(id),
    foreign key (post4) references post(id),
    foreign key (post5) references post(id)
);

create table follows(
    username VARCHAR(30) not null unique,
    follow1 VARCHAR(30),
    follow2 VARCHAR(30),
    follow3 VARCHAR(30),
    foreign key (username) references account(username),
    foreign key (follow1) references account(username),
    foreign key (follow2) references account(username),
    foreign key (follow3) references account(username)
);

-- Create new table for messages, wait sa email ni sir
create table messages(
    username varchar(30) not null,
    subject text not null,
    content text not null,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (username) references account(username)
);


INSERT INTO account (username, password, user_role) VALUES 
('superadmin', 'superpassword', 'super_admin'),
('admin1', 'password1', 'admin'),
('admin2', 'password2', 'admin'),
('guest1', 'password3', 'user'),
('guest2', 'password4', 'user'),
('guest3', 'password5', 'user');

-- Insert sample follows, e.g.:
-- Guest1 is followed by Guest2 and Guest3
-- Guest2 is followed by Guest1 and Guest3
-- Guest3 is followed by Guest1 and Guest2
INSERT INTO follows (username, follow1, follow2, follow3) VALUES 
('guest1', 'guest2', 'guest3', null),
('guest2', 'guest1', 'guest3', null),
('guest3', 'guest1', 'guest2', null);

-- Insert sample posts for each guest
INSERT INTO post (title, content, username) VALUES 
('First Post - Guest1', 'This is the first post of guest1', 'guest1'),
('Second Post - Guest1', 'This is the second post of guest1', 'guest1'),
('Third Post - Guest1', 'This is the third post of guest1', 'guest1'),
('Fourth Post - Guest1', 'This is the fourth post of guest1', 'guest1'),
('Fifth Post - Guest1', 'This is the fifth post of guest1', 'guest1'),
('First Post - Guest2', 'This is the first post of guest2', 'guest2'),
('Second Post - Guest2', 'This is the second post of guest2', 'guest2'),
('Third Post - Guest2', 'This is the third post of guest2', 'guest2'),
('Fourth Post - Guest2', 'This is the fourth post of guest2', 'guest2'),
('Fifth Post - Guest2', 'This is the fifth post of guest2', 'guest2'),
('First Post - Guest3', 'This is the first post of guest3', 'guest3'),
('Second Post - Guest3', 'This is the second post of guest3', 'guest3'),
('Third Post - Guest3', 'This is the third post of guest3', 'guest3'),
('Fourth Post - Guest3', 'This is the fourth post of guest3', 'guest3'),
('Fifth Post - Guest3', 'This is the fifth post of guest3', 'guest3');

-- Map the posts to the guests
INSERT INTO posts (username, post1, post2, post3, post4, post5) VALUES 
('guest1', 1, 2, 3, 4, 5),
('guest2', 6, 7, 8, 9, 10),
('guest3', 11, 12, 13, 14, 15);

-- messages to admins
INSERT INTO messages (username, subject, content) VALUES 
('guest1', 'Hello Guest1', 'Welcome to your wall1'),
('guest2', 'Hello Guest2', 'Welcome to your wall2'),
('guest3', 'Hello Guest3', 'Welcome to your wall3');
