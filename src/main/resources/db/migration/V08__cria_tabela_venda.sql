CREATE TABLE public.venda (
    id SERIAL PRIMARY KEY,
    data DATE,
    cliente_id INTEGER REFERENCES cliente(id),
    valor_total DOUBLE PRECISION,
    status TEXT DEFAULT 'ATIVO'
);
