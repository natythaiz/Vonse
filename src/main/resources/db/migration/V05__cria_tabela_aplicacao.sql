CREATE TABLE public.aplicacao
(
    codigo serial NOT NULL,
    data date,
    codigo_pessoa integer,
    codigo_lote integer,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo)
);