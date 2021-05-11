insert into comum.tb_usuario (us_id, us_email, us_email_contato, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, us_situacao, id_id, pa_id, us_tipo_usuario) values
(nextval('comum.seq_usuario'), 'usuario', 'usuario1@azi.com.br', '57708514738', 'Fornecedor Google', FALSE, '33266655', 'ATIVO', 1, 30, 'EXTERNO');

insert into pat_mobiliario.tb_reconhecimento (re_id, re_nome, re_situacao, re_execucao_orcamentaria, re_nota_fical, re_empenho, re_dthr_cadastro, re_dthr_alteracao) values
(1, 'compras', 'ATIVO', true, true, false, '2020-07-10 10:39:10.242000','2020-07-11 10:39:10.242000');

INSERT INTO  comum_siga.tb_conta_contabil (cc_id,cc_codigo, cc_descricao, cc_situacao, cc_dthr_cadastro, cc_dthr_alteracao, cc_usuario_cadastro, cc_usuario_alteracao) VALUES
(2, '123110200', 'MÁQUINAS, APARELHOS, EQUIPAMENTOS E FERRAMENTAS' , 'ATIVO', '2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO pat_mobiliario.tb_config_conta_contabil(ccc_id, ccc_metodo, ccc_tipo, cc_id, ccc_vida_util_meses, ccc_percentual_residual, ccc_tipo_bem)
values (1, 'QUOTAS_CONSTANTES', 'DEPRECIAVEL', 2, 36, 10,'ARMAMENTO');

INSERT INTO comum_siga.tb_natureza_despesa (nd_id,es_id,nd_despesa,nd_descricao, nd_dthr_cadastro, nd_dthr_alteracao, nd_usuario_cadastro, nd_usuario_alteracao,cc_id, nd_situacao) VALUES
(1,1,'234567','descricao 1', '2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null,2, 'ATIVO');

-- INCORPORACAO COM 1 PATRIMONIO
INSERT INTO pat_mobiliario.tb_incorporacao (in_id, in_codigo, in_dthr_recebimento, in_situacao, in_num_processo, pe_id, in_nota, in_valor_nota, re_id,uo_id_orgao, uo_id_setor) VALUES
(1, '00001', '2020-05-28T18:58:07.358Z', 'EM_ELABORACAO', '957267', 1, '5', '10',1,1,2);

INSERT INTO pat_mobiliario.tb_incorporacao_item (ini_id,ini_valor_total,ini_total_unidades,ini_situacao,ini_numeracao_patrimonial,ec_id,ini_depreciavel,ini_codigo,ini_descricao,cc_id,nd_id,in_id, ini_dthr_cadastro, ini_dthr_alteracao, ini_usuario_cadastro, ini_usuario_alteracao) VALUES
(2,5,1,'FINALIZADO','9999',1,false,'234567','descricao 3',2,1,1, '2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO pat_mobiliario.TB_PATRIMONIO (PA_ID, PA_NUMERO, PA_VALOR_AQUISICAO, PA_VALOR_LIQUIDO, PA_VALOR_RESIDUAL, INI_ID, PA_DTHR_CADASTRO, PA_DTHR_ALTERACAO, PA_USUARIO_CADASTRO, PA_USUARIO_ALTERACAO) VALUES
(1, '2398472894', '14900', '15600', '12500', 2, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null);

-- INCORPORACAO COM 2 PATRIMONIOS
INSERT INTO pat_mobiliario.tb_incorporacao (in_id, in_codigo, in_dthr_recebimento, in_situacao, in_num_processo, pe_id, in_nota, in_valor_nota, re_id,uo_id_orgao, uo_id_setor) VALUES
(2, '00002', '2020-05-28T18:58:07.358Z', 'EM_ELABORACAO', '957267', 1, '5', '10',1,1,2);

INSERT INTO pat_mobiliario.tb_incorporacao_item (ini_id,ini_valor_total,ini_total_unidades,ini_situacao,ini_numeracao_patrimonial,ec_id,ini_depreciavel,ini_codigo,ini_descricao,cc_id,nd_id,in_id, ini_dthr_cadastro, ini_dthr_alteracao, ini_usuario_cadastro, ini_usuario_alteracao) VALUES
(3,5,2,'FINALIZADO','9999',1,false,'234567','descricao 3',2,1,2, '2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO pat_mobiliario.TB_PATRIMONIO (PA_ID, PA_NUMERO, PA_VALOR_AQUISICAO, PA_VALOR_LIQUIDO, PA_VALOR_RESIDUAL, INI_ID, PA_DTHR_CADASTRO, PA_DTHR_ALTERACAO, PA_USUARIO_CADASTRO, PA_USUARIO_ALTERACAO) VALUES
(2, '2398472894', '14900', '15600', '12500', 3, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null),
(3, '2398472895', '14900', '15600', '12500', 3, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null);

