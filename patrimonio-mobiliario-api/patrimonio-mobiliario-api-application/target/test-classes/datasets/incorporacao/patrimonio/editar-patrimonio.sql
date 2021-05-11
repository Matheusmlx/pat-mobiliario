insert into comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao, cc_dthr_cadastro, cc_dthr_alteracao, cc_usuario_cadastro, cc_usuario_alteracao)
values (1,'0001','TESTE','TESTE','2020-07-10 10:39:10.242000','2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO PAT_MOBILIARIO.TB_CONFIG_CONTA_CONTABIL (CCC_ID,CCC_METODO,CCC_TIPO,CCC_TIPO_BEM,CCC_VIDA_UTIL_MESES,CCC_PERCENTUAL_RESIDUAL,CC_ID,CCC_DTHR_CADASTRO,CCC_DTHR_ALTERACAO, CCC_USUARIO_CADASTRO, CCC_USUARIO_ALTERACAO)
VALUES ( 1,'QUOTAS_CONSTANTES','CONTA','CONTA',10, 2.5,1,'2020-07-10 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO PAT_MOBILIARIO.TB_INCORPORACAO (IN_ID,IN_CODIGO,IN_DTHR_CADASTRO,PE_ID,IN_SITUACAO,IN_DTHR_RECEBIMENTO,IN_DTHR_ALTERACAO,IN_USUARIO_CADASTRO,IN_USUARIO_ALTERACAO, IN_DTHR_FINALIZACAO)
VALUES ( 1,'785491','2020-07-09 10:39:10.242000',1,'FINALIZADO','2020-02-24 03:00:00.000000','2020-07-10 10:39:10.242000', 'admin', null, '2020-07-09 10:39:10.242000');

INSERT INTO PAT_MOBILIARIO.TB_CONFIG_DEPRECIACAO (CD_ID,CD_VIDA_UTIL_MESES,CD_DEPRECIAVEL,CC_ID)
VALUES (2,125,true,1);

INSERT INTO pat_mobiliario.tb_incorporacao_item (ini_id, ini_total_unidades, ini_codigo, ini_descricao, ini_situacao, in_id, cc_id,cd_id)
VALUES (2, 10,'0001' ,'descricao','EM_ELABORACAO',1, 1,2);

insert into pat_mobiliario.tb_comodante (com_id, com_nome)
values (1, 'Luiz');

INSERT INTO pat_mobiliario.TB_PATRIMONIO (PA_ID, PA_SITUACAO, PA_NUMERO, PA_VALOR_AQUISICAO, PA_VALOR_LIQUIDO, PA_VALOR_RESIDUAL,PA_VALOR_DEPRECIACAO_MENSAL,CC_ID_CLASSIFICACAO, INI_ID, PA_DTHR_CADASTRO, PA_DTHR_ALTERACAO, PA_USUARIO_CADASTRO, PA_USUARIO_ALTERACAO, COM_ID)
VALUES
(1, 'ATIVO', '2398472894', '14900', '15600', '12500','35200', 1, 2, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null, 1),
(2, 'ATIVO', '3758472887', '14900', '15600', '12500','35200', 1, 2, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null, null),
(3, 'ATIVO', '1234567881', '14900', '15600', '12500','35200', 1, 2, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null, null),
(4, 'ATIVO', '1050568418', '14900', '15600', '12500','35200', 1, 2, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null, null),
(5, 'ATIVO', '1544835188', '14900', '15600', '12500','35200', 1, 2, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null, null);


INSERT INTO pat_mobiliario.TB_CONFIG_DEPRECIACAO(CD_ID,CD_VIDA_UTIL_MESES,CD_PERCENTUAL_RESIDUAL,CC_ID,CD_DEPRECIAVEL)
VALUES
(1,125,15,1,'true');

insert into comum_siga.tb_pessoa (pe_id, pe_nome_razaosocial, pe_cpf_cnpj, pe_logradouro, pe_bairro, pe_numero, pe_cep, pe_situacao, mu_id, pa_id)
values
    (nextval('comum_siga.seq_pessoa'), 'Google Brasil Internet Ltda', '06990590000123', 'Av. Afonso Pena', 'Centro', '12345', '79032500', 'ATIVO',  4156, 30),
    (nextval('comum_siga.seq_pessoa'), 'Nu Pagamentos S.A.', '18236120000158', 'Av. Mato Grosso', 'Centro', '538', '79010220', 'ATIVO', 4156, 30),
    (nextval('comum_siga.seq_pessoa'), 'Jumbitos Comercio de Alimentos Ltda', '01382349000106', 'Rua Bahia', 'São Francisco', '979', '79010180', 'ATIVO', 4156, 30);
