CREATE TABLE permissao(
	codigo SERIAL primary key,
    descricao VARCHAR(100) NOT NULL
);

insert into permissao (codigo, descricao) values (1, 'ADMIN');
insert into permissao (codigo, descricao) values (2, 'USER');