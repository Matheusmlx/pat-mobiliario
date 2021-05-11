insert into pat_mobiliario.tb_movimentacao(mo_id, mo_codigo, mo_tipo, mo_situacao) values
(14, '00014', 'INCORPORACAO', 'EM_ELABORACAO');

insert into pat_mobiliario.tb_nota_lancamento_contabil (nlc_id, nlc_numero, nlc_dthr_lancamento, nlc_dthr_cadastro, nlc_dthr_alteracao, nlc_usuario_cadastro, nlc_usuario_alteracao) values
(3, '1234NL123412', '2020-12-08 00:00:00.000000', '2020-12-18 13:04:43.084736', '2020-12-18 13:04:45.087677', 'admin', 'admin');

insert into pat_mobiliario.tb_movimentacao(mo_id, mo_codigo, mo_tipo, mo_situacao, nlc_id) values
(10, '00010', 'DISTRIBUICAO', 'EM_ELABORACAO', 3);

insert into  comum_siga.tb_conta_contabil  (cc_id,cc_codigo, cc_descricao, cc_situacao) values (8, '123110104', 'APARELHOS E EQUIPAMENTOS DE COMUNICAÇÃO' , 'ATIVO');

insert into pat_mobiliario.tb_incorporacao (in_id)
values (15);

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_codigo, ini_situacao, in_id) values
(17, '0153asd5', 'FINALIDO', 15);

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_diferenca_dizima, pa_situacao, uo_id_orgao, uo_id_setor, ini_id) values
(3, '0000000001', 8.110, true, 'ATIVO', 8773, 8774, 17);

insert into pat_mobiliario.tb_lancamento_contabil (lc_id, lc_tipo_lancamento, lc_data_lancamento, lc_valor, lc_tipo_movimentacao, uo_id_orgao, uo_id_setor, pa_id, cc_id, mo_id, lc_dthr_cadastro, lc_dthr_alteracao, lc_usuario_cadastro) values
(12, 'CREDITO', '2020-09-01 23:00:00.000000', 510.510000, 'DISTRIBUICAO', 1, 2, 3, 8, 10, '2020-12-18 13:04:47.192060', '2020-12-18 13:04:47.192074', 'admin');
