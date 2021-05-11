insert into comum.tb_usuario (us_id, us_email, us_email_contato, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, us_situacao, id_id, pa_id, us_tipo_usuario)
values
    (nextval('comum.seq_usuario'), 'usuario', 'usuario1@azi.com.br', '57708514738', 'Fornecedor Google', FALSE, '33266655', 'ATIVO', 1, 30, 'EXTERNO');

insert into pat_mobiliario.tb_reconhecimento (re_id, re_execucao_orcamentaria, re_nome, re_situacao)
values (1, true, 'Compra', 'Ativo');

INSERT INTO pat_mobiliario.TB_INCORPORACAO (IN_ID,IN_CODIGO,IN_DTHR_CADASTRO,PE_ID,IN_SITUACAO,IN_DTHR_RECEBIMENTO, RE_ID, IN_DTHR_ALTERACAO,IN_USUARIO_CADASTRO,IN_USUARIO_ALTERACAO)
 VALUES ( 1,'785491','2020-07-09 10:39:10.242000',1,'ERRO_PROCESSAMENTO','2020-02-24 03:00:00.000000', 1, '2020-07-10 10:39:10.242000', 'admin', null);

 INSERT INTO comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao)
VALUES (41, '123110103', 'APARELHOS E EQUIPAMENTOS DE COMUNICAÇÃO', 'INATIVO');

insert into pat_mobiliario.tb_config_conta_contabil(ccc_id, ccc_metodo, ccc_tipo, cc_id, ccc_vida_util_meses, ccc_percentual_residual, ccc_tipo_bem)
values (1, 'QUOTAS_CONSTANTES', 'DEPRECIAVEL', 41, 36, 10, 'VEICULO');

 insert into comum_siga.tb_elemento_subelemen (es_id, es_codigo, es_nome, es_situacao, es_tipo)
values (93, '5248', 'VEICULOS DIVERSOS', 'ATIVO', 'SUBELEMENTO');

 insert into comum_siga.tb_natureza_despesa (nd_id, nd_despesa, es_id, nd_descricao, nd_situacao, cc_id)
values (1, '44905248', 93, 'VEICULOS DIVERSOS', 'ATIVO', 41);

 insert into comum_siga.tb_natureza_despesa (nd_id, nd_despesa, es_id, nd_descricao, nd_situacao, cc_id)
values (2, '55555555', 93, 'VEICULOS COMBATE', 'ATIVO', 41);

 INSERT INTO pat_mobiliario.tb_incorporacao_item (ini_id,ini_codigo,ini_descricao,ini_situacao,in_id,nd_id,ec_id, ini_dthr_cadastro, ini_dthr_alteracao, ini_usuario_cadastro, ini_usuario_alteracao)
VALUES (1,'255555','descricao 4','EM_ELABORACAO',1,2,1,'2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

insert into comum_siga.tb_pessoa (pe_id, pe_nome_razaosocial, pe_cpf_cnpj, pe_logradouro, pe_bairro, pe_numero, pe_cep, pe_situacao, mu_id, pa_id)
values
    (nextval('comum_siga.seq_pessoa'), 'Google Brasil Internet Ltda', '06990590000123', 'Av. Afonso Pena', 'Centro', '12345', '79032500', 'ATIVO',  4156, 30),
    (nextval('comum_siga.seq_pessoa'), 'Nu Pagamentos S.A.', '18236120000158', 'Av. Mato Grosso', 'Centro', '538', '79010220', 'ATIVO', 4156, 30),
    (nextval('comum_siga.seq_pessoa'), 'Jumbitos Comercio de Alimentos Ltda', '01382349000106', 'Rua Bahia', 'São Francisco', '979', '79010180', 'ATIVO', 4156, 30);
