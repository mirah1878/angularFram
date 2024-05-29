create table users (
    id_user serial primary key,
    email varchar(70) not null,
    passwords varchar(20) not null,
    role int default 0
);

INSERT INTO users (email, passwords, role) VALUES ('user', 'user', 0);
INSERT INTO users (email, passwords, role) VALUES ('admin', 'admin', 1);

