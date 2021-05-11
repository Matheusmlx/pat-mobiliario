INSERT INTO pat_mobiliario.tb_comodante(com_id, com_nome) VALUES (1, 'João');

INSERT INTO pat_mobiliario.tb_incorporacao(in_id, in_codigo, in_dthr_recebimento, in_situacao, in_num_processo, pe_id, in_nota, in_valor_nota, in_origem_comodato, com_id)
VALUES (1,'1531315', '2020-05-28T18:58:07.358Z', 'EM_ELABORACAO', '987465', 1, '5', '7.988', true, 1);

insert into comum_siga.tb_pessoa (pe_id, pe_nome_razaosocial, pe_cpf_cnpj, pe_logradouro, pe_bairro, pe_numero, pe_cep, pe_situacao, mu_id, pa_id)
values
    (nextval('comum_siga.seq_pessoa'), 'Google Brasil Internet Ltda', '06990590000123', 'Av. Afonso Pena', 'Centro', '12345', '79032500', 'ATIVO',  4156, 30),
    (nextval('comum_siga.seq_pessoa'), 'Nu Pagamentos S.A.', '18236120000158', 'Av. Mato Grosso', 'Centro', '538', '79010220', 'ATIVO', 4156, 30),
    (nextval('comum_siga.seq_pessoa'), 'Jumbitos Comercio de Alimentos Ltda', '01382349000106', 'Rua Bahia', 'São Francisco', '979', '79010180', 'ATIVO', 4156, 30);
