-- ESTADO
INSERT INTO estados (nome, uf) VALUES ('São Paulo', 'SP');

-- CIDADE
INSERT INTO cidades (nome, estado_id) VALUES ('São Paulo', 1);

-- CEP
INSERT INTO ceps (codigo, cidade_id)
VALUES ('01001-000', 1),
       ('01430-000', 1);

-- ENDERECOS
INSERT INTO enderecos (logradouro, tipo_logradouro, numero, complemento, bairro, cep_id)
VALUES ('Rua das Flores', 'RUA', '123', 'Apto 5', 'Centro', 1),
       ('Av. Brasil', 'AVENIDA', '456', 'Loja 2', 'Jardim América', 2);

-- USUARIOS
INSERT INTO usuarios (nome, email, tipo_telefone, endereco_id, status, data_cadastro)
VALUES ('Victor Martins', 'victor@cliente.com', 'CLIENTE', 1, TRUE, CURRENT_TIMESTAMP),
       ('Restaurante Ecully', 'contato@ecully.com', 'RESTAURANTE', 2, TRUE, CURRENT_TIMESTAMP);

-- CLIENTES / RESTAURANTES
INSERT INTO clientes (id, tipo_usuario) VALUES (1, 'CLIENTE');

INSERT INTO restaurantes (id, cnpj, categoria, horario_abertura, horario_fechamento, taxa_entrega, estado, tipo_usuario)
VALUES (2, '12.345.678/0001-99', 'Contemporânea', '11:00:00', '23:00:00', 8.50, 'ABERTO', 'RESTAURANTE');

-- TELEFONES
INSERT INTO telefones (ddd, numero, tipo, ativo, usuario_id)
VALUES ('11', '99999-0001', 'CELULAR', TRUE, 1),
       ('11', '2222-3333', 'FIXO', TRUE, 2);
