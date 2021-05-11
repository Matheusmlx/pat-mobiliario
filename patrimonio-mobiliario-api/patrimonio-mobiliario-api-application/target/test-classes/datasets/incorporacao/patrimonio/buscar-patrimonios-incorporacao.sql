insert into pat_mobiliario.tb_incorporacao (in_id, in_codigo, pe_id, in_situacao, in_dthr_recebimento, in_dthr_cadastro, in_dthr_alteracao, in_usuario_cadastro, in_usuario_alteracao)
values (25, '785491', 1, 'FINALIZADO', '2020-02-24 03:00:00.000000', '2020-07-10 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_situacao, in_id, ini_codigo, ini_descricao)
values (nextval('pat_mobiliario.seq_incorporacao_item'), 'FINALIZADO', 25, 'asdf351', 'item incorporacao 1');

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, ini_id, pa_situacao)
values (nextval('pat_mobiliario.seq_patrimonio'), '00000111', 350.50, currval('pat_mobiliario.seq_incorporacao_item'),'ATIVO');

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_situacao, in_id, ini_codigo, ini_descricao)
values (nextval('pat_mobiliario.seq_incorporacao_item'), 'FINALIZADO', 25, 'asdf352', 'item incorporacao 2');

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, ini_id, pa_situacao)
values (nextval('pat_mobiliario.seq_patrimonio'), '00000112', 27000, currval('pat_mobiliario.seq_incorporacao_item'),'ATIVO');

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_situacao, in_id, ini_codigo, ini_descricao)
values (nextval('pat_mobiliario.seq_incorporacao_item'), 'FINALIZADO', 25, 'asdf353', 'item incorporacao 3');

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, ini_id, pa_situacao)
values (nextval('pat_mobiliario.seq_patrimonio'), '00000113', 45344.35, currval('pat_mobiliario.seq_incorporacao_item'),'ATIVO');

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_situacao, in_id, ini_codigo, ini_descricao)
values (nextval('pat_mobiliario.seq_incorporacao_item'), 'FINALIZADO', 25, 'asdf354', 'item incorporacao 4');

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, ini_id, pa_situacao)
values (nextval('pat_mobiliario.seq_patrimonio'), '00000114', 98485.50, currval('pat_mobiliario.seq_incorporacao_item'),'ATIVO');
