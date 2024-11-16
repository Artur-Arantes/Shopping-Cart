insert into permission VALUES(1,'ADMIN');
insert into permission VALUES(2,'USER');
insert into person VALUES('liven', 'liven@teste.com');
insert into users VALUES(1,'$2a$10$aOKNjGjqTnGDv1Nxxb.RGOGxFdGYk7b80Tr0Uu9uiSEvMjjYmGl4y','liven@teste.com',1,'ADMIN', now(), now(), 1);
insert into user_permission VALUES(1,1);
