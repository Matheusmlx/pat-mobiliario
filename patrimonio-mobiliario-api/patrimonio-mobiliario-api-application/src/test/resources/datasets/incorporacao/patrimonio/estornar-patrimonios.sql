INSERT INTO comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao, cc_dthr_cadastro, cc_dthr_alteracao, cc_usuario_cadastro, cc_usuario_alteracao)
VALUES (1,'0001','TESTE','TESTE','2020-07-10 10:39:10.242000','2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO pat_mobiliario.tb_incorporacao (in_id,in_codigo,in_dthr_cadastro,pe_id,in_situacao,in_dthr_recebimento,in_dthr_alteracao,in_usuario_cadastro,in_usuario_alteracao)
VALUES ( 1,'785491','2020-07-09 10:39:10.242000',1,'FINALIZADO','2020-02-24 03:00:00.000000','2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO pat_mobiliario.tb_incorporacao_item (ini_id, ini_total_unidades, ini_codigo, ini_descricao, ini_situacao, in_id, cc_id, ini_valor_total)
VALUES (2, 10,'0001' ,'descricao' , 'FINALIZADO',1, 1, '74500');

INSERT INTO pat_mobiliario.tb_patrimonio (pa_id,uo_id_orgao,uo_id_setor, pa_situacao, pa_numero, pa_valor_aquisicao, pa_valor_liquido, pa_valor_residual,cc_id_atual, ini_id, pa_dthr_cadastro, pa_dthr_alteracao, pa_usuario_cadastro, pa_usuario_alteracao)
VALUES
(1, 1, 2, 'ATIVO', '2398472894', '14900', '15600', '12500', 1, 2, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null),
(2, 1, 2, 'ATIVO', '3758472887', '14900', '15600', '12500', 1, 2, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null),
(3, 1, 2, 'ATIVO', '1234567881', '14900', '15600', '12500', 1, 2, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null),
(4, 1, 2, 'ATIVO', '1050568418', '14900', '15600', '12500', 1, 2, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null),
(5, 1, 2, 'ATIVO', '1544835188', '14900', '15600', '12500', 1, 2, '2020-07-10 10:39:10.242000', '2020-08-10 10:39:10.242000', 'admin', null);

INSERT INTO pat_mobiliario.tb_nota_lancamento_contabil (nlc_id, nlc_numero, nlc_dthr_lancamento) values
(12, '4444NL444444', '2021-01-05 00:00:00.000000');

INSERT INTO pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_motivo_obs, mo_situacao, mo_dthr_finalizacao,mo_dthr_cadastro, uo_id_orgao_origem, uo_id_setor_origem, uo_id_setor_destino, nlc_id)
VALUES (16, '00002', 'DISTRIBUICAO', 'Observacao movimentação', 'EM_ELABORACAO', '2020-12-15 21:15:55.000000','2020-11-15 22:14:55.000000', 3, 1, 2, 12);
