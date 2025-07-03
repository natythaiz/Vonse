CREATE TABLE public.item_venda (
    id SERIAL PRIMARY KEY,
    venda_id INTEGER REFERENCES venda(id) ON DELETE CASCADE,
    produto_id INTEGER REFERENCES produto(id),
    quantidade BIGINT,
    preco_unitario DOUBLE PRECISION,
    subtotal DOUBLE PRECISION
);
