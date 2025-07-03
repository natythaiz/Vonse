CREATE TABLE public.produto (
    id SERIAL PRIMARY KEY,
    nome TEXT,
    categoria TEXT, -- Enum armazenado como texto
    tipo TEXT,      -- Enum armazenado como texto
    preco_custo DOUBLE PRECISION,
    preco_venda DOUBLE PRECISION,
    estoque BIGINT,
    descricao TEXT,
    fornecedor TEXT,
    status TEXT DEFAULT 'ATIVO'
);
