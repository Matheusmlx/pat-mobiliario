INSERT INTO PAT_MOBILIARIO.TB_INCORPORACAO (IN_ID,IN_CODIGO,IN_DTHR_CADASTRO,PE_ID,IN_SITUACAO,IN_DTHR_RECEBIMENTO,IN_DTHR_ALTERACAO,IN_USUARIO_CADASTRO,IN_USUARIO_ALTERACAO)
VALUES ( 1,'785491','2020-07-09 10:39:10.242000',1,'FINALIZADO','2020-02-24 03:00:00.000000','2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_total_unidades, ini_codigo, ini_descricao, ini_situacao, in_id)
values (2, 10,'0001' ,'descricao' , 'EM_ELABORACAO',1);

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
