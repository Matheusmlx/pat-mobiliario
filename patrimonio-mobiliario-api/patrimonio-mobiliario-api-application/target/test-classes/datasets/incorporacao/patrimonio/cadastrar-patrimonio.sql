
INSERT INTO  comum_siga.tb_conta_contabil  (cc_id,cc_codigo, cc_descricao, cc_situacao, cc_dthr_cadastro, cc_dthr_alteracao, cc_usuario_cadastro, cc_usuario_alteracao) VALUES (1, '123110100', 'MÁQUINAS, APARELHOS, EQUIPAMENTOS E FERRAMENTAS' , 'ATIVO', '2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_config_conta_contabil(ccc_id, ccc_metodo, ccc_tipo,ccc_tipo_bem, cc_id, ccc_vida_util_meses, ccc_percentual_residual)
values (1, 'QUOTAS_CONSTANTES', 'DEPRECIAVEL','ARMAMENTO', 1, 36, 10);

INSERT INTO pat_mobiliario.tb_incorporacao (IN_ID,IN_CODIGO,IN_DTHR_CADASTRO,PE_ID,IN_SITUACAO,IN_DTHR_RECEBIMENTO,IN_DTHR_ALTERACAO,IN_USUARIO_CADASTRO,IN_USUARIO_ALTERACAO)
 VALUES ( 1,'785491','2020-07-09 10:39:10.242000',1,'FINALIZADO','2020-02-24 03:00:00.000000','2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO comum_siga.tb_natureza_despesa (nd_id,es_id,nd_despesa,nd_descricao, nd_dthr_cadastro, nd_dthr_alteracao, nd_usuario_cadastro, nd_usuario_alteracao,cc_id) VALUES (1,1,'234567','descricao 1', '2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null,1);

INSERT INTO pat_mobiliario.tb_incorporacao_item (ini_id,ini_codigo,ini_descricao,ini_situacao,in_id, ini_dthr_cadastro, ini_dthr_alteracao, ini_usuario_cadastro, ini_usuario_alteracao)
VALUES (1,'456789','descricao 4','EM_ELABORACAO',1,'2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO pat_mobiliario.tb_incorporacao_item (ini_id,ini_codigo,ini_descricao,ini_situacao, ini_valor_total, in_id, ini_dthr_cadastro, ini_dthr_alteracao, ini_usuario_cadastro, ini_usuario_alteracao)
VALUES (2,'153153','descricao 5','EM_ELABORACAO', 5000, 1,'2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, ini_id) VALUES
(nextval('pat_mobiliario.seq_patrimonio'), '0000000009', 2500, 2),
(nextval('pat_mobiliario.seq_patrimonio'), '0000000010', 2500, 2);
