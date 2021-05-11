insert into pat_mobiliario.tb_reserva (rv_id, rv_codigo, rv_situacao, rv_preenchimento, rv_dthr_criacao, rv_qtd_reservada, rv_qtd_restante, rv_nro_inicio, rv_nro_fim)
values (1, '00001', 'EM_ELABORACAO', 'AUTOMATICO', '2021-04-23 14:00:00', 126, 126, 1, 126);

insert into pat_mobiliario.tb_reserva (rv_id, rv_codigo, rv_situacao, rv_preenchimento, rv_dthr_criacao, rv_qtd_reservada, rv_qtd_restante, rv_nro_inicio, rv_nro_fim)
values (2, '00002', 'EM_ELABORACAO', 'AUTOMATICO', '2021-04-23 14:00:00', 126, 126, 127, 252);

insert into pat_mobiliario.tb_reserva_intervalo (ri_id, rv_id, ri_situacao, ri_preenchimento, uo_id_orgao, ri_qtd_reservada, ri_nro_inicio, ri_nro_fim) values
(1, 1, 'FINALIZADO', 'AUTOMATICO', 1, 30, 1, 30);

insert into pat_mobiliario.tb_reserva_intervalo (ri_id, rv_id, ri_situacao, ri_preenchimento, uo_id_orgao, ri_qtd_reservada, ri_nro_inicio, ri_nro_fim) values
(2, 2, 'FINALIZADO', 'AUTOMATICO', 1, 30, 127, 156);

insert into pat_mobiliario.tb_reserva_intervalo_numero (rin_id, ri_id, rin_numero, rin_utilizado) values
(1, 1, 1, false),
(127, 2, 127, true);
