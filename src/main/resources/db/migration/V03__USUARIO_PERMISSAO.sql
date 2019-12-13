CREATE TABLE usuario_permissao(
	usuario_codigo SERIAL NOT NULL,
	permissao_codigo SERIAL NOT NULL,
	PRIMARY KEY (usuario_codigo, permissao_codigo),
	FOREIGN KEY (usuario_codigo) REFERENCES usuario(codigo),
	FOREIGN KEY (permissao_codigo) REFERENCES permissao(codigo)
);

insert into usuario_permissao(usuario_codigo, permissao_codigo) values (1, 1);
insert into usuario_permissao(usuario_codigo, permissao_codigo) values (1, 2);