INSERT INTO USUARIOS (id, username, password, role) VALUES (100, 'ana@email.com', '$2a$12$OGI/8jZil7Xk8otZqbwyDuqUbkYYXTi43ZoEYjIWShUcq8JLXJ0T.', 'ROLE_ADMIN');
INSERT INTO USUARIOS (id, username, password, role) VALUES (101, 'bia@email.com', '$2a$12$OGI/8jZil7Xk8otZqbwyDuqUbkYYXTi43ZoEYjIWShUcq8JLXJ0T.', 'ROLE_CLIENTE');
INSERT INTO USUARIOS (id, username, password, role) VALUES (102, 'bob@email.com', '$2a$12$OGI/8jZil7Xk8otZqbwyDuqUbkYYXTi43ZoEYjIWShUcq8JLXJ0T.', 'ROLE_CLIENTE');
INSERT INTO USUARIOS (id, username, password, role) VALUES (103, 'toby@email.com', '$2a$12$OGI/8jZil7Xk8otZqbwyDuqUbkYYXTi43ZoEYjIWShUcq8JLXJ0T.', 'ROLE_CLIENTE');

INSERT INTO CLIENTES (id, nome, cpf, id_usuario) VALUES (10, 'Bianca Silva', '79074426050', 101);
INSERT INTO CLIENTES (id, nome, cpf, id_usuario) VALUES (20, 'Roberto Gomes', '55352517047', 102);