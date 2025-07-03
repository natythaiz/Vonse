CREATE TABLE public.cliente (
    id SERIAL PRIMARY KEY,
    nome TEXT,
    contato TEXT,
    data_cadastro DATE,
    status TEXT DEFAULT 'ATIVO'
);
