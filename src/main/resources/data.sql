-- ===========
-- CLIENTES
-- ===========

INSERT INTO clientes (nome, email, telefone, endereco, data_cadastro, ativo) VALUES
('João Silva',  'joao@email.com',  '(11) 99999-1111', 'Rua A, 123 - São Paulo/SP', CURRENT_TIMESTAMP, TRUE),
('Maria Santos','maria@email.com', '(11) 99999-2222', 'Rua B, 456 - São Paulo/SP', CURRENT_TIMESTAMP, TRUE),
('Pedro Oliveira','pedro@email.com','(11) 99999-3333','Rua C, 789 - São Paulo/SP', CURRENT_TIMESTAMP, TRUE);

-- ===========
-- RESTAURANTES
--  - cnpj obrigatório e único
--  - estado (STRING): ABERTO | FECHADO | MANUTENCAO
--  - email pode ser nulo se não quiser usar agora
-- ===========

INSERT INTO restaurantes (
  nome, cnpj, categoria, endereco, telefone, email,
  latitude, longitude, horario_abertura, horario_fechamento,
  estado, data_cadastro, ativo
) VALUES
('Pizzaria Bella', '12.345.678/0001-11', 'Italiana', 'Av. Paulista, 1000 - São Paulo/SP', '(11) 3333-1111', 'pizzariabela@email.com',
  0, 0, TIME '18:00:00', TIME '23:59:00',
  'ABERTO', CURRENT_TIMESTAMP, TRUE),

('Burger House', '98.765.432/0001-22', 'Hamburgueria', 'Rua Augusta, 500 - São Paulo/SP', '(11) 3333-2222', NULL,
  0, 0, TIME '11:00:00', TIME '22:00:00',
  'ABERTO', CURRENT_TIMESTAMP, TRUE),

('Sushi Master', '11.222.333/0001-44', 'Japonesa', 'Rua Liberdade, 200 - São Paulo/SP', '(11) 3333-3333', NULL,
  0, 0, TIME '17:30:00', TIME '23:00:00',
  'FECHADO', CURRENT_TIMESTAMP, TRUE);

-- ===========
-- PRODUTOS
--  - categoria (STRING): BEBIDAS | COMIDAS | SOBREMESAS
--  - estoque (int), preco (decimal), ativo (bool), restaurante_id (FK)
-- ===========

-- Pizzaria Bella (id=1)
INSERT INTO produtos (nome, descricao, categoria, estoque, preco, ativo, data_cadastro, restaurante_id) VALUES
('Pizza Margherita', 'Molho de tomate, mussarela e manjericão', 'COMIDAS', 50, 35.90, TRUE, CURRENT_TIMESTAMP, 1),
('Pizza Calabresa',  'Molho de tomate, mussarela e calabresa',   'COMIDAS', 40, 38.90, TRUE, CURRENT_TIMESTAMP, 1),
('Lasanha Bolonhesa','Lasanha tradicional com molho bolonhesa',  'COMIDAS', 30, 28.90, TRUE, CURRENT_TIMESTAMP, 1);

-- Burger House (id=2)
INSERT INTO produtos (nome, descricao, categoria, estoque, preco, ativo, data_cadastro, restaurante_id) VALUES
('X-Burger',     'Hambúrguer, queijo, alface e tomate',         'COMIDAS', 100, 18.90, TRUE, CURRENT_TIMESTAMP, 2),
('X-Bacon',      'Hambúrguer, queijo, bacon, alface e tomate',  'COMIDAS',  80, 22.90, TRUE, CURRENT_TIMESTAMP, 2),
('Batata Frita', 'Porção de batata frita crocante',             'COMIDAS', 150, 12.90, TRUE, CURRENT_TIMESTAMP, 2);

-- Sushi Master (id=3)
INSERT INTO produtos (nome, descricao, categoria, estoque, preco, ativo, data_cadastro, restaurante_id) VALUES
('Combo Sashimi',  '15 peças de sashimi variado',      'COMIDAS', 25, 45.90, TRUE, CURRENT_TIMESTAMP, 3),
('Hot Roll Salmão','8 peças de hot roll de salmão',    'COMIDAS', 40, 32.90, TRUE, CURRENT_TIMESTAMP, 3),
('Temaki Atum',    'Temaki de atum com cream cheese',  'COMIDAS', 60, 15.90, TRUE, CURRENT_TIMESTAMP, 3);

-- ===========
-- AVALIAÇÕES
--  - nota (STRING): PESSIMO | RUIM | REGULAR | BOM | OTIMO | EXCELENTE
--  - cliente_id e restaurante_id devem existir
-- ===========

INSERT INTO avaliacoes (cliente_id, restaurante_id, nota, comentario, data_avaliacao) VALUES
(1, 1, 'EXCELENTE', 'Pizza muito boa!',                 CURRENT_TIMESTAMP),
(2, 1, 'BOM',       'Entrega rápida e sabor ok',        CURRENT_TIMESTAMP),
(2, 2, 'OTIMO',     'Hambúrguer no ponto certo',        CURRENT_TIMESTAMP),
(3, 3, 'REGULAR',   'Poderia ter mais opções no combo', CURRENT_TIMESTAMP);

-- ===== PEDIDOS =====
INSERT INTO pedidos (numero_pedido, data_pedido, status, valor_total, observacoes, cliente_id, restaurante_id)
VALUES
  ('PED20251030-0001', CURRENT_TIMESTAMP, 'PENDENTE', 112.70, 'Sem cebola na calabresa', 1, 1),
  ('PED20251030-0002', CURRENT_TIMESTAMP, 'CONFIRMADO', 31.80,  'Retirar no balcão',     2, 2);

-- ===== ITENS DO PEDIDO 0001 (Restaurante 1) =====
-- Pizza Margherita (35,90) x1 = 35,90
-- Pizza Calabresa (38,90)  x2 = 77,80  -> total = 112,70
INSERT INTO pedido_itens (pedido_id, produto_id, quantidade, preco_unitario, subtotal)
VALUES
  ( (SELECT id FROM pedidos WHERE numero_pedido = 'PED20251030-0001'), 1, 1, 35.90, 35.90 ),
  ( (SELECT id FROM pedidos WHERE numero_pedido = 'PED20251030-0001'), 2, 2, 38.90, 77.80 );

-- ===== ITENS DO PEDIDO 0002 (Restaurante 2) =====
-- X-Burger (18,90) x1 = 18,90
-- Batata Frita (12,90) x1 = 12,90 -> total = 31,80
INSERT INTO pedido_itens (pedido_id, produto_id, quantidade, preco_unitario, subtotal)
VALUES
  ( (SELECT id FROM pedidos WHERE numero_pedido = 'PED20251030-0002'), 4, 1, 18.90, 18.90 ),
  ( (SELECT id FROM pedidos WHERE numero_pedido = 'PED20251030-0002'), 6, 1, 12.90, 12.90 );

