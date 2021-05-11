insert into comum.tb_usuario (us_id, us_email, us_email_contato, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, us_situacao, id_id, pa_id, us_tipo_usuario)
values
(nextval('comum.seq_usuario'), 'usuario', 'usuario1@azi.com.br', '57708514738', 'Fornecedor Google', FALSE, '33266655', 'ATIVO', 1, 30, 'EXTERNO');

INSERT INTO pat_mobiliario.tb_nota_lancamento_contabil
(nlc_id, nlc_numero, nlc_dthr_lancamento)
VALUES (1, '2015NL98576', '2020-05-28T18:58:07.358Z');

INSERT INTO pat_mobiliario.tb_incorporacao (IN_ID,IN_CODIGO,IN_DTHR_CADASTRO,PE_ID,IN_SITUACAO,IN_DTHR_RECEBIMENTO,IN_DTHR_ALTERACAO,IN_USUARIO_CADASTRO,IN_USUARIO_ALTERACAO)
VALUES ( 1,'785491','2020-07-09 10:39:10.242000',1,'FINALIZADO','2020-02-24 03:00:00.000000','2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_movimentacao
(mo_id, mo_tipo, mo_situacao, mo_motivo_obs, uo_id_orgao_origem, uo_id_orgao_destino, uo_id_setor_origem,
 uo_id_setor_destino, nlc_id, mo_numero_processo)
VALUES (20, 'DISTRIBUICAO', 'EM_ELABORACAO', 'motivo', 1, 2, 1, 2, 1, '12345');

insert into comum_siga.tb_tipo_documento (td_id,td_desc,td_permite_anexo,td_dthr_cadastro,td_dthr_alteracao,td_usuario_cadastro,td_usuario_alteracao,td_identificacao_documento) values (1, 'Contrato',true,null,null,null,null,'CONTRATO');

insert into pat_mobiliario.tb_documento (do_id,do_numero,do_dt,do_valor,do_uri_anexo,in_id,mo_id,td_id,do_dthr_cadastro,do_dthr_alteracao) values (1,'454453','2020-06-02',1200550000,'repo1:patrimoniomobiliario/teste.pdf',1,20,1,'2020-06-25 11:36:56.350000',null);

