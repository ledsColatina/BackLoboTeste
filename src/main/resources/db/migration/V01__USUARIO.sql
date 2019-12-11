CREATE TABLE usuario( 
     codigo SERIAL primary key, 
     nome   VARCHAR(50) NOT NULL, 
     senha   VARCHAR(250) NOT NULL, 
     tipo  BOOLEAN NOT NULL, 
     username VARCHAR(50) NOT NULL
  );

insert into usuario (codigo, nome, senha, tipo, username) values (1,'admin', '$2a$10$BENPJuJZZGBJPwcqegyWcebaqdLLsPRhKXLBCxVybZBuRXEUPD2Z2', false, 'admin');