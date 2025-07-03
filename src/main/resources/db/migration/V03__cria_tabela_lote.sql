CREATE TABLE public.lote
(
    codigo serial NOT NULL,
    validade date,
    nro_doses_do_lote integer,
    nro_doses_atual integer,
    status text DEFAULT 'ATIVO',
    codigo_vacina integer,
    PRIMARY KEY (codigo)
);