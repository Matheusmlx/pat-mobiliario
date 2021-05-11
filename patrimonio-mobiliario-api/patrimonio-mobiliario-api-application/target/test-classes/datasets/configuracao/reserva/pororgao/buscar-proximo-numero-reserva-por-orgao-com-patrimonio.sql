insert into pat_mobiliario.tb_reserva (rv_id, rv_codigo, rv_situacao, rv_preenchimento, rv_dthr_criacao, rv_qtd_reservada, rv_qtd_restante, rv_nro_inicio, rv_nro_fim)
values (1, '00001', 'FINALIZADO', 'AUTOMATICO', '2021-04-23 14:00:00', 150, 150, 1, 150);

insert into pat_mobiliario.tb_reserva_intervalo (ri_id, rv_id, ri_situacao, ri_preenchimento, uo_id_orgao, ri_qtd_reservada, ri_nro_inicio, ri_nro_fim)
values (1, 1, 'FINALIZADO', 'AUTOMATICO', 1, 150, 1, 150);


insert into pat_mobiliario.tb_reserva (rv_id, rv_codigo, rv_situacao, rv_preenchimento, rv_dthr_criacao, rv_qtd_reservada, rv_qtd_restante, rv_nro_inicio, rv_nro_fim)
values (2, '00002', 'FINALIZADO', 'AUTOMATICO', '2021-04-23 14:00:00', 50, 50, 201, 250);

insert into pat_mobiliario.tb_reserva_intervalo (ri_id, rv_id, ri_situacao, ri_preenchimento, uo_id_orgao, ri_qtd_reservada, ri_nro_inicio, ri_nro_fim)
values (2, 2, 'FINALIZADO', 'AUTOMATICO', 2, 50, 201, 2250);


insert into pat_mobiliario.tb_incorporacao (in_id,in_codigo,in_dthr_cadastro,pe_id,in_situacao,in_dthr_recebimento,in_dthr_alteracao,in_usuario_cadastro,in_usuario_alteracao)
values (1, '785491','2020-07-09 10:39:10.242000', 1, 'FINALIZADO', '2020-02-24 03:00:00.000000','2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_total_unidades, ini_codigo, ini_descricao, ini_situacao, in_id)
values (1, 10, '0001', 'descricao', 'FINALIZADO', 1);

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_valor_liquido, pa_valor_residual, pa_situacao, ini_id, uo_id_orgao, uo_id_setor)
values (1, '0000000151', '14900', '15600', '12500', 'ATIVO', 1, 1, 1);

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_valor_liquido, pa_valor_residual, pa_situacao, ini_id, uo_id_orgao, uo_id_setor)
values (2, '0000000152', '14900', '15600', '12500', 'ATIVO', 1, 2, 1);