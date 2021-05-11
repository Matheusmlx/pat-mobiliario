insert into pat_mobiliario.tb_reserva (rv_id, rv_codigo, rv_situacao, rv_preenchimento, rv_dthr_criacao, rv_qtd_reservada, rv_qtd_restante, rv_nro_inicio, rv_nro_fim)
values (1, '00001', 'EM_ELABORACAO', 'AUTOMATICO', '2020-04-23 14:00:00', 200, 150, 1, 200);

insert into pat_mobiliario.tb_reserva_intervalo (ri_id, rv_id, ri_situacao, ri_preenchimento, uo_id_orgao, ri_qtd_reservada, ri_nro_inicio, ri_nro_fim)
values (1, 1, 'EM_ELABORACAO', 'MANUAL', 1, 50, 100, 149);


insert into pat_mobiliario.tb_reserva (rv_id, rv_codigo, rv_situacao, rv_preenchimento, rv_dthr_criacao, rv_qtd_reservada, rv_qtd_restante, rv_nro_inicio, rv_nro_fim)
values (2, '00001', 'EM_ELABORACAO', 'AUTOMATICO', '2020-04-23 14:00:00', 200, 9, 1, 200);

insert into pat_mobiliario.tb_reserva_intervalo (ri_id, rv_id, ri_situacao, ri_preenchimento, uo_id_orgao, ri_qtd_reservada, ri_nro_inicio, ri_nro_fim)
values (2, 2, 'EM_ELABORACAO', 'MANUAL', 1, 140, 1, 140);

insert into pat_mobiliario.tb_reserva_intervalo (ri_id, rv_id, ri_situacao, ri_preenchimento, uo_id_orgao, ri_qtd_reservada, ri_nro_inicio, ri_nro_fim)
values (3, 2, 'EM_ELABORACAO', 'MANUAL', 1, 51, 150, 200);
