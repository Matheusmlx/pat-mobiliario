insert into pat_mobiliario.tb_reconhecimento(re_id, re_nome, re_situacao, re_execucao_orcamentaria, re_nota_fical, re_empenho, re_dthr_cadastro, re_dthr_alteracao) values (1, 'compras', 'ATIVO', true, true, false, '2020-07-10 10:39:10.242000','2020-07-11 10:39:10.242000');

INSERT INTO pat_mobiliario.tb_incorporacao(in_id, in_codigo, in_dthr_recebimento, in_situacao, in_num_processo, pe_id, in_nota, in_valor_nota)
VALUES
(1, '1531315', '2020-05-28T18:58:07.358Z', 'EM_ELABORACAO', '957267', 1, '5', '7.1E2'),
(2, '9871238', '2020-05-28T18:58:07.358Z', 'EM_ELABORACAO', '983213', 1, '5', '7.1E2'),
(3, '9234556', '2020-05-28T18:58:07.358Z', 'FINALIZADO', '987123', 1, '5', '7.1E2');

INSERT INTO pat_mobiliario.tb_convenio(co_id)
VALUES (1);

INSERT INTO pat_mobiliario.tb_empenho(em_id,in_id)
VALUES (7,1);

INSERT INTO pat_mobiliario.tb_comodante(com_id, com_nome)
VALUES (1, 'João');

insert into comum_siga.tb_pessoa (pe_id, pe_nome_razaosocial, pe_cpf_cnpj, pe_logradouro, pe_bairro, pe_numero, pe_cep, pe_situacao, mu_id, pa_id)
values
    (nextval('comum_siga.seq_pessoa'), 'Google Brasil Internet Ltda', '06990590000123', 'Av. Afonso Pena', 'Centro', '12345', '79032500', 'ATIVO',  4156, 30),
    (nextval('comum_siga.seq_pessoa'), 'Nu Pagamentos S.A.', '18236120000158', 'Av. Mato Grosso', 'Centro', '538', '79010220', 'ATIVO', 4156, 30),
    (nextval('comum_siga.seq_pessoa'), 'Jumbitos Comercio de Alimentos Ltda', '01382349000106', 'Rua Bahia', 'São Francisco', '979', '79010180', 'ATIVO', 4156, 30);
