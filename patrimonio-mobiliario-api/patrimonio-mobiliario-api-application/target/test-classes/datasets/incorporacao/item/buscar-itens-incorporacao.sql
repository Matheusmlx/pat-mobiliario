
INSERT INTO  comum_siga.tb_conta_contabil  (cc_id,cc_codigo, cc_descricao, cc_situacao, cc_dthr_cadastro, cc_dthr_alteracao, cc_usuario_cadastro, cc_usuario_alteracao) VALUES (1, '123110100', 'MÁQUINAS, APARELHOS, EQUIPAMENTOS E FERRAMENTAS' , 'ATIVO', '2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO  comum_siga.tb_conta_contabil  (cc_id,cc_codigo, cc_descricao, cc_situacao, cc_dthr_cadastro, cc_dthr_alteracao, cc_usuario_cadastro, cc_usuario_alteracao) VALUES (2, '123110200', 'MÁQUINAS, APARELHOS, EQUIPAMENTOS E FERRAMENTAS' , 'ATIVO', '2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_config_conta_contabil(ccc_id, ccc_metodo, ccc_tipo,ccc_tipo_bem, cc_id, ccc_vida_util_meses, ccc_percentual_residual)
values (1, 'QUOTAS_CONSTANTES', 'DEPRECIAVEL','ARMAMENTO', 2, 36, 10);

INSERT INTO pat_mobiliario.tb_incorporacao (IN_ID,IN_CODIGO,IN_DTHR_CADASTRO,IN_SITUACAO,IN_DTHR_RECEBIMENTO,IN_DTHR_ALTERACAO,IN_USUARIO_CADASTRO,IN_USUARIO_ALTERACAO)
 VALUES ( 1,'785491','2020-07-09 10:39:10.242000','FINALIZADO','2020-02-24 03:00:00.000000','2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO pat_mobiliario.tb_incorporacao (IN_ID,IN_CODIGO,IN_DTHR_CADASTRO,IN_SITUACAO,IN_DTHR_RECEBIMENTO,IN_DTHR_ALTERACAO,IN_USUARIO_CADASTRO,IN_USUARIO_ALTERACAO)
 VALUES ( 2,'785491','2020-07-09 10:39:10.242000','FINALIZADO','2020-02-24 03:00:00.000000','2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO comum_siga.tb_natureza_despesa (nd_id,es_id,nd_despesa,nd_descricao, nd_dthr_cadastro, nd_dthr_alteracao, nd_usuario_cadastro, nd_usuario_alteracao,cc_id) VALUES (1,1,'234567','descricao 1', '2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null,1);

INSERT INTO comum_siga.tb_natureza_despesa (nd_id,es_id,nd_despesa,nd_descricao, nd_dthr_cadastro, nd_dthr_alteracao, nd_usuario_cadastro, nd_usuario_alteracao,cc_id) VALUES (2,1,'123456','descricao 2', '2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null,1);

INSERT INTO pat_mobiliario.tb_incorporacao_item (ini_id,ini_valor_total,ini_total_unidades,ini_situacao,ini_numeracao_patrimonial,ec_id,ini_depreciavel,ini_codigo,ini_descricao,cc_id,nd_id,in_id, ini_dthr_cadastro, ini_dthr_alteracao, ini_usuario_cadastro, ini_usuario_alteracao)
VALUES (1,10,1,'FINALIZADO','8888',3,true,'123456','descricao 1',1,1,1, '2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO pat_mobiliario.tb_incorporacao_item (ini_id,ini_valor_total,ini_total_unidades,ini_situacao,ini_numeracao_patrimonial,ec_id,ini_depreciavel,ini_codigo,ini_descricao,cc_id,nd_id,in_id, ini_dthr_cadastro, ini_dthr_alteracao, ini_usuario_cadastro, ini_usuario_alteracao)
VALUES (2,20,3,'EM_ELABORACAO','9999',1,false,'234567','descricao 3',1,2,1, '2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO pat_mobiliario.tb_incorporacao_item (ini_id,ini_valor_total,ini_total_unidades,ini_situacao,ini_numeracao_patrimonial,ec_id,ini_depreciavel,ini_codigo,ini_descricao,cc_id,nd_id,in_id, ini_dthr_cadastro, ini_dthr_alteracao, ini_usuario_cadastro, ini_usuario_alteracao)
VALUES (3,15,2,'EM_ELABORACAO','9999',1,false,'345678','descricao 2',2,1,2,'2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO pat_mobiliario.tb_incorporacao_item (ini_id,ini_codigo,ini_descricao,ini_situacao,in_id, ini_dthr_cadastro, ini_dthr_alteracao, ini_usuario_cadastro, ini_usuario_alteracao)
VALUES (4,'456789','descricao 4','EM_ELABORACAO',1,'2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

insert into comum_siga.tb_pessoa (pe_id, pe_nome_razaosocial, pe_cpf_cnpj, pe_logradouro, pe_bairro, pe_numero, pe_cep, pe_situacao, mu_id, pa_id)
values
    (nextval('comum_siga.seq_pessoa'), 'Google Brasil Internet Ltda', '06990590000123', 'Av. Afonso Pena', 'Centro', '12345', '79032500', 'ATIVO',  4156, 30),
    (nextval('comum_siga.seq_pessoa'), 'Nu Pagamentos S.A.', '18236120000158', 'Av. Mato Grosso', 'Centro', '538', '79010220', 'ATIVO', 4156, 30),
    (nextval('comum_siga.seq_pessoa'), 'Jumbitos Comercio de Alimentos Ltda', '01382349000106', 'Rua Bahia', 'São Francisco', '979', '79010180', 'ATIVO', 4156, 30);
