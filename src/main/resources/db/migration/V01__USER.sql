
CREATE TABLE usuario (
	codigo INTEGER PRIMARY KEY,
	nome VARCHAR(50) NOT NULL,
	senha VARCHAR(150) NOT NULL,
    tipo BOOLEAN,
    username VARCHAR(50)
);

CREATE TABLE permissao (
	codigo INTEGER PRIMARY KEY,
	descricao VARCHAR(50) NOT NULL
);

CREATE TABLE usuario_permissao (
	codigo_usuario INTEGER NOT NULL,
	codigo_permissao INTEGER NOT NULL,
	PRIMARY KEY (codigo_usuario, codigo_permissao),
	FOREIGN KEY (codigo_usuario) REFERENCES usuario(codigo),
	FOREIGN KEY (codigo_permissao) REFERENCES permissao(codigo)
);


INSERT INTO USUARIO VALUES (1, 'admin', ' $2a$10$rxHDQl9js8MfezOr9Ajvoec8774kdtljqqEwxxqYlqmHvNHz5LBL6', true, 'admin');
INSERT INTO PERMISSAO VALUES (1,'ADMIN');
INSERT INTO USUARIO_PERMISSAO VALUES (1,1);