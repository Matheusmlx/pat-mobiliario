insert into pat_mobiliario.tb_incorporacao (in_id, in_codigo, in_dthr_recebimento, in_situacao, in_dthr_finalizacao) values
(7, 8, now(), 'FINALIZADO', now());

insert into comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao) values (1, '123110100', 'MÁQUINAS, APARELHOS, EQUIPAMENTOS E FERRAMENTAS', 'ATIVO');

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_codigo, ini_descricao, ini_valor_total, ini_total_unidades, ini_tipo, ini_situacao, in_id, cc_id, ec_id) values
(nextval('pat_mobiliario.seq_incorporacao_item'), 'asdf23a', 'Item incorporação descrição 1', 1500.30, 2, 'VEICULO', 'FINALIZADO', 7, 1, 1);

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_situacao, pa_motivo_estorno, ini_id) values
(nextval('pat_mobiliario.seq_patrimonio'), '00000001', 750.15, 'ESTORNADO', 'motivo 1', currval('pat_mobiliario.seq_incorporacao_item'));

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_situacao, pa_motivo_estorno, ini_id) values
(nextval('pat_mobiliario.seq_patrimonio'), '00000002', 750.15, 'ESTORNADO', 'motivo 2', currval('pat_mobiliario.seq_incorporacao_item'));

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_codigo, ini_descricao, ini_valor_total, ini_total_unidades, ini_tipo, ini_situacao, in_id, cc_id, ec_id) values
(nextval('pat_mobiliario.seq_incorporacao_item'), 'erq234', 'Item incorporação descrição 2', 800, 2, 'VEICULO', 'FINALIZADO', 7, 1, 1);

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_situacao, pa_motivo_estorno, ini_id) values
(nextval('pat_mobiliario.seq_patrimonio'), '00000003', 400, 'ESTORNADO', 'motivo 3', currval('pat_mobiliario.seq_incorporacao_item'));

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_situacao, ini_id) values
(nextval('pat_mobiliario.seq_patrimonio'), '00000004', 400, 'ATIVO', currval('pat_mobiliario.seq_incorporacao_item'));
