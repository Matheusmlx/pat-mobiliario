insert into pat_mobiliario.tb_reserva (rv_id, rv_codigo, rv_situacao, rv_preenchimento, rv_dthr_criacao, rv_qtd_reservada, rv_qtd_restante, rv_nro_inicio, rv_nro_fim)
values (1, '00001', 'FINALIZADO', 'AUTOMATICO', '2021-04-23 14:00:00', 150, 150, 1, 150);

insert into pat_mobiliario.tb_reserva_intervalo (ri_id, rv_id, ri_situacao, ri_preenchimento, uo_id_orgao, ri_qtd_reservada, ri_nro_inicio, ri_nro_fim)
values (1, 1, 'FINALIZADO', 'AUTOMATICO', 1, 150, 1, 150);


insert into pat_mobiliario.tb_reserva (rv_id, rv_codigo, rv_situacao, rv_preenchimento, rv_dthr_criacao, rv_qtd_reservada, rv_qtd_restante, rv_nro_inicio, rv_nro_fim)
values (2, '00002', 'FINALIZADO', 'AUTOMATICO', '2021-04-23 14:00:00', 50, 50, 151, 200);

insert into pat_mobiliario.tb_reserva_intervalo (ri_id, rv_id, ri_situacao, ri_preenchimento, uo_id_orgao, ri_qtd_reservada, ri_nro_inicio, ri_nro_fim)
values (2, 2, 'FINALIZADO', 'AUTOMATICO', 2, 50, 151, 200);
