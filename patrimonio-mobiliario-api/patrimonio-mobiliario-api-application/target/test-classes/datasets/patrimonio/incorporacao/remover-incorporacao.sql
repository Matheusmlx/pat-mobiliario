INSERT INTO pat_mobiliario.tb_incorporacao(in_id, in_codigo, in_dthr_recebimento, in_situacao, in_num_processo, pe_id, in_nota, in_valor_nota)
VALUES (1,'1531315', '2020-05-28T18:58:07.358Z', 'EM_ELABORACAO', '987465', 1, '5', '7.988');

INSERT INTO pat_mobiliario.TB_EMPENHO (EM_ID, EM_NUMERO, IN_ID, EM_DTHR_CADASTRO, EM_DTHR_ALTERACAO, EM_USUARIO_CADASTRO, EM_USUARIO_ALTERACAO)
VALUES ( 1, '9419818', 1, '2020-02-24 03:00:00.000000', '2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_total_unidades, ini_codigo, ini_descricao, ini_situacao, in_id)
values (1, 10,'0001' ,'descricao' , 'EM_ELABORACAO',1);

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_total_unidades, ini_codigo, ini_descricao, ini_situacao, in_id)
values (2, 10,'0002' ,'descricao' , 'EM_ELABORACAO',1);

INSERT INTO pat_mobiliario.TB_PATRIMONIO (PA_ID, PA_NUMERO, PA_VALOR_AQUISICAO, PA_VALOR_LIQUIDO, PA_VALOR_RESIDUAL, INI_ID, PA_DTHR_CADASTRO, PA_DTHR_ALTERACAO, PA_USUARIO_CADASTRO, PA_USUARIO_ALTERACAO)
VALUES
(1, '2398472894', '14900', '15600', '12500', 2, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null),
(2, '3758472887', '14900', '15600', '12500', 2, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null),
(3, '1234567881', '14900', '15600', '12500', 2, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null),
(4, '1050568418', '14900', '15600', '12500', 2, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null),
(5, '1544835188', '14900', '15600', '12500', 2, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null);

insert into comum_siga.tb_pessoa (pe_id, pe_nome_razaosocial, pe_cpf_cnpj, pe_logradouro, pe_bairro, pe_numero, pe_cep, pe_situacao, mu_id, pa_id)
values
(nextval('comum_siga.seq_pessoa'), 'Google Brasil Internet Ltda', '06990590000123', 'Av. Afonso Pena', 'Centro', '12345', '79032500', 'ATIVO',  4156, 30),
(nextval('comum_siga.seq_pessoa'), 'Nu Pagamentos S.A.', '18236120000158', 'Av. Mato Grosso', 'Centro', '538', '79010220', 'ATIVO', 4156, 30),
(nextval('comum_siga.seq_pessoa'), 'Jumbitos Comercio de Alimentos Ltda', '01382349000106', 'Rua Bahia', 'São Francisco', '979', '79010180', 'ATIVO', 4156, 30);
