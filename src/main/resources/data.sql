-- ===========================================================
-- DADOS INICIAIS DO MÓDULO DE ENDEREÇOS
-- ===========================================================

-- ===== ESTADOS =====
INSERT INTO estados (nome, uf) VALUES
('São Paulo', 'SP'),
('Rio de Janeiro', 'RJ');

-- ===== CIDADES =====
INSERT INTO cidades (nome, estado_id) VALUES
('São Paulo', 1),
('Rio de Janeiro', 2);

-- ===== CEPS =====
INSERT INTO ceps (codigo, cidade_id) VALUES
('01000-000', 1),
('20000-000', 2),
('04930-055', 1);

-- ===== ENDEREÇOS =====
INSERT INTO enderecos (nome, logradouro, numero, bairro, complemento, cep_id, latitude, longitude) VALUES
('Rua A', 'RUA', '123', 'Centro', 'Sem complemento', 1, -23.561, -46.656),
('Rua B', 'AVENIDA', '456', 'Copacabana', 'Apto 202', 2, -22.903, -43.176),
('Rua C', 'AVENIDA', '222', 'Copacabana', 'Apto 651', 3, -10.903, -60.176);

-- ===========================================================
-- CLIENTES
-- ===========================================================
INSERT INTO clientes (nome, email, data_cadastro, ativo, endereco_id) VALUES
('João Silva',  'joao@email.com',  CURRENT_TIMESTAMP, TRUE, 1),
('Maria Santos','maria@email.com', CURRENT_TIMESTAMP, TRUE, 2),
('Pedro Oliveira','pedro@email.com',CURRENT_TIMESTAMP, TRUE, 1);

-- ===== TELEFONES =====
INSERT INTO telefones (cliente_id, ddd, numero) VALUES
(1, '11', '999991111'),
(2, '11', '999992222'),
(3, '11', '999993333');

-- ===========================================================
-- RESTAURANTES
-- ===========================================================
INSERT INTO restaurantes (
  nome, cnpj, classe, telefone, email,
  horario_abertura, horario_fechamento,
  estado, data_cadastro, ativo, endereco_id
) VALUES
('Pizzaria Bella', '12.345.678/0001-11', 'ITALIANO', '(11) 3333-1111', 'pizzariabella@email.com',
  TIME '18:00:00', TIME '23:59:00',
  'ABERTO', CURRENT_TIMESTAMP, TRUE, 1),

('Burger House', '98.765.432/0001-22', 'BRASILEIRO', '(11) 3333-2222', NULL,
  TIME '11:00:00', TIME '22:00:00',
  'ABERTO', CURRENT_TIMESTAMP, TRUE, 2),

('Sushi Master', '11.222.333/0001-44', 'FRANCES', '(21) 3333-3333', NULL,
  TIME '17:30:00', TIME '23:00:00',
  'FECHADO', CURRENT_TIMESTAMP, TRUE, 3);

-- ===========================================================
-- PRODUTOS
-- ===========================================================

-- Pizzaria Bella (id=1)
INSERT INTO produtos (nome, descricao, categoria, estoque, preco, ativo, restaurante_id) VALUES
('Pizza Margherita', 'Molho de tomate, mussarela e manjericão', 'COMIDAS', 50, 35.90, TRUE, 1),
('Pizza Calabresa',  'Molho de tomate, mussarela e calabresa',   'COMIDAS', 40, 38.90, TRUE, 1),
('Lasanha Bolonhesa','Lasanha tradicional com molho bolonhesa',  'COMIDAS', 30, 28.90, TRUE, 1);

-- Burger House (id=2)
INSERT INTO produtos (nome, descricao, categoria, estoque, preco, ativo, restaurante_id) VALUES
('X-Burger',     'Hambúrguer, queijo, alface e tomate',         'COMIDAS', 100, 18.90, TRUE, 2),
('X-Bacon',      'Hambúrguer, queijo, bacon, alface e tomate',  'COMIDAS',  80, 22.90, TRUE, 2),
('Batata Frita', 'Porção de batata frita crocante',             'COMIDAS', 150, 12.90, TRUE, 2);

-- Sushi Master (id=3)
INSERT INTO produtos (nome, descricao, categoria, estoque, preco, ativo, restaurante_id) VALUES
('Combo Sashimi',  '15 peças de sashimi variado',      'COMIDAS', 25, 45.90, TRUE, 3),
('Hot Roll Salmão','8 peças de hot roll de salmão',    'COMIDAS', 40, 32.90, TRUE, 3),
('Temaki Atum',    'Temaki de atum com cream cheese',  'COMIDAS', 60, 15.90, TRUE, 3);

-- ===========================================================
-- AVALIAÇÕES
-- ===========================================================
INSERT INTO avaliacoes (cliente_id, restaurante_id, nota, comentario, data_avaliacao) VALUES
(1, 1, 'EXCELENTE', 'Pizza muito boa!',                 CURRENT_TIMESTAMP),
(2, 1, 'BOM',       'Entrega rápida e sabor ok',        CURRENT_TIMESTAMP),
(2, 2, 'OTIMO',     'Hambúrguer no ponto certo',        CURRENT_TIMESTAMP),
(3, 3, 'REGULAR',   'Poderia ter mais opções no combo', CURRENT_TIMESTAMP);

-- ===========================================================
-- PEDIDOS
-- ===========================================================
INSERT INTO pedidos (numero_pedido, data_pedido, status, valor_total, observacoes, cliente_id, restaurante_id)
VALUES
  ('PED20251105-0001', CURRENT_TIMESTAMP, 'PENDENTE', 112.70, 'Sem cebola na calabresa', 1, 1),
  ('PED20251105-0002', CURRENT_TIMESTAMP, 'CONFIRMADO', 31.80, 'Retirar no balcão', 2, 2);

-- ===========================================================
-- ITENS DOS PEDIDOS
-- ===========================================================

-- Pedido 1 - Pizzaria Bella
INSERT INTO pedido_itens (pedido_id, produto_id, quantidade, preco_unitario, subtotal)
VALUES
  ( (SELECT id FROM pedidos WHERE numero_pedido = 'PED20251105-0001'), 1, 1, 35.90, 35.90 ),
  ( (SELECT id FROM pedidos WHERE numero_pedido = 'PED20251105-0001'), 2, 2, 38.90, 77.80 );

-- Pedido 2 - Burger House
INSERT INTO pedido_itens (pedido_id, produto_id, quantidade, preco_unitario, subtotal)
VALUES
  ( (SELECT id FROM pedidos WHERE numero_pedido = 'PED20251105-0002'), 4, 1, 18.90, 18.90 ),
  ( (SELECT id FROM pedidos WHERE numero_pedido = 'PED20251105-0002'), 6, 1, 12.90, 12.90 );

-- ===========================================================
-- FIM DO DATA.SQL
-- ===========================================================
