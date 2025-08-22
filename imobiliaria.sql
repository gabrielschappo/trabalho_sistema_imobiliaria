CREATE DATABASE imobiliaria;
CREATE USER imobiliaria WITH PASSWORD '123456';
GRANT ALL PRIVILEGES ON DATABASE imobiliaria TO imobiliaria;

CREATE TABLE public.cliente (
	id int8 NOT NULL,
	nome varchar(255) NOT NULL,
	cpf varchar(14) NOT NULL,
	telefone varchar(20) NULL,
	email varchar(255) NULL,
	CONSTRAINT cliente_cpf_key UNIQUE (cpf),
	CONSTRAINT cliente_pkey PRIMARY KEY (id)
);

CREATE TABLE public.contrato_aluguel (
	id int8 NOT NULL,
	id_cliente int8 NOT NULL,
	id_imovel int8 NOT NULL,
	data_inicio date NOT NULL,
	data_fim date NOT NULL,
	valor_aluguel_pactuado numeric(10, 2) NOT NULL,
	ativo bool DEFAULT true NULL,
	CONSTRAINT contrato_aluguel_pkey PRIMARY KEY (id)
);

ALTER TABLE public.contrato_aluguel ADD CONSTRAINT contrato_aluguel_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.cliente(id);
ALTER TABLE public.contrato_aluguel ADD CONSTRAINT contrato_aluguel_id_imovel_fkey FOREIGN KEY (id_imovel) REFERENCES public.imovel(id);

CREATE TABLE public.imovel (
	id int8 NOT NULL,
	endereco varchar(255) NOT NULL,
	tipo varchar(100) NULL,
	quartos int4 NULL,
	banheiros int4 NULL,
	valor_aluguel_base numeric(10, 2) NOT NULL,
	disponivel bool DEFAULT true NULL,
	CONSTRAINT imovel_pkey PRIMARY KEY (id)
);


INSERT INTO cliente (id, nome, cpf, telefone, email) VALUES
(1, 'Ana Clara Souza', '111.222.333-44', '47999887766', 'ana.souza@email.com'),
(2, 'Bruno Mattos', '555.666.777-88', '47988776655', 'bruno.mattos@email.com'),
(3, 'Carla Dias', '999.888.777-66', '47977665544', 'carla.dias@email.com');

INSERT INTO imovel (id, endereco, tipo, quartos, banheiros, valor_aluguel_base, disponivel) VALUES
(101, 'Rua das Palmeiras, 10, Centro', 'Apartamento', 2, 1, 1800.00, TRUE),
(102, 'Avenida Brasil, 1500, América', 'Casa', 3, 2, 2900.00, FALSE),
(103, 'Rua XV de Novembro, 550, Glória', 'Apartamento', 1, 1, 1550.00, FALSE),
(104, 'Rua Tuiuti, 234, Aventureiro', 'Casa', 2, 1, 1950.00, TRUE);


INSERT INTO contrato_aluguel (id, id_cliente, id_imovel, data_inicio, data_fim, valor_aluguel_pactuado, ativo) VALUES
(1001, 1, 102, '2024-10-01', '2025-09-30', 2900.00, TRUE);

INSERT INTO contrato_aluguel (id, id_cliente, id_imovel, data_inicio, data_fim, valor_aluguel_pactuado, ativo) VALUES
(1002, 2, 103, '2024-09-11', '2025-09-10', 1500.00, TRUE);

INSERT INTO contrato_aluguel (id, id_cliente, id_imovel, data_inicio, data_fim, valor_aluguel_pactuado, ativo) VALUES
(1003, 3, 101, '2023-05-01', '2024-04-30', 1750.00, FALSE);


ALTER TABLE public.cliente OWNER TO imobiliaria;
ALTER TABLE public.imovel OWNER TO imobiliaria;
ALTER TABLE public.contrato_aluguel OWNER TO imobiliaria;