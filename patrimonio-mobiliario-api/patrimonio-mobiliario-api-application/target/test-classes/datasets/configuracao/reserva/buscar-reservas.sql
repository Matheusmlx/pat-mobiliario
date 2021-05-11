insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id) values
(1, 'ESTRUTURA_ORGANIZACIONAL', (select uo_id from comum_siga.tb_unidade_organizacional where uo_sigla = 'ADMIN'), 2),
(2, 'ESTRUTURA_ORGANIZACIONAL', (select uo_id from comum_siga.tb_unidade_organizacional where uo_sigla = 'EDUCA'), 2);

insert into comum.tb_perfil (pf_id, pf_nome, pf_descricao, pf_tipo, pr_id) values
(1, 'Administrador', 'Administrador do sistema', 'INTERNO', 410);

insert into hal.tb_dominio_perfil (dm_id, pf_id) values
(1, 1),
(2, 1);

insert into hal.tb_funcao (fu_id, fu_nome, fu_descricao) values
(2, 'Mobiliario.Normal', 'Função de acesso normal');

insert into comum.tb_plano (pl_id, pl_nome, pl_descricao) values
(1, 'plano-basico', 'Plano básico');

insert into hal.tb_permissao_perfil (fu_id, pf_id, pl_id, pp_permissao) values
(2, 1, 1, 'S');

insert into pat_mobiliario.tb_reserva (rv_id, rv_codigo, rv_situacao, rv_preenchimento, rv_dthr_criacao, rv_qtd_reservada, rv_qtd_restante, rv_nro_inicio, rv_nro_fim, rv_dthr_cadastro, rv_dthr_alteracao)
values (1, '00001', 'EM_ELABORACAO', 'AUTOMATICO', '2021-03-15 00:00:00', 50, 50, 1, 50, '2020-03-15', '2020-03-15');

insert into pat_mobiliario.tb_reserva (rv_id, rv_codigo, rv_situacao, rv_preenchimento, rv_dthr_criacao, rv_qtd_reservada, rv_qtd_restante, rv_nro_inicio, rv_nro_fim, rv_dthr_cadastro, rv_dthr_alteracao)
values (2, '00002', 'FINALIZADO', 'AUTOMATICO', '2021-04-15 00:00:00', 50, 0, 51, 100, '2020-04-15', '2020-04-15');

insert into pat_mobiliario.tb_reserva_intervalo (ri_id, rv_id, ri_situacao, ri_preenchimento, uo_id_orgao, ri_qtd_reservada, ri_nro_inicio, ri_nro_fim, ri_dthr_finalizacao) values
(1, 2, 'FINALIZADO', 'MANUAL', (select uo_id from comum_siga.tb_unidade_organizacional where uo_sigla = 'ADMIN'), 10, 51, 60, '2020-04-15 10:39:10.242000'),
(2, 2, 'FINALIZADO', 'AUTOMATICO', (select uo_id from comum_siga.tb_unidade_organizacional where uo_sigla = 'EDUCA'), 20, 61, 80, '2020-04-15 10:39:10.242000'),
(3, 2, 'FINALIZADO', 'MANUAL', (select uo_id from comum_siga.tb_unidade_organizacional where uo_sigla = 'ADMIN'), 20, 81, 100, '2020-04-15 10:39:10.242000');

insert into pat_mobiliario.tb_reserva (rv_id, rv_codigo, rv_situacao, rv_preenchimento, rv_dthr_criacao, rv_qtd_reservada, rv_qtd_restante, rv_nro_inicio, rv_nro_fim, rv_dthr_cadastro, rv_dthr_alteracao)
values (3, '00003', 'PARCIAL', 'AUTOMATICO', '2021-04-15 00:00:00', 10, 7, 101, 110, '2020-04-15', '2020-04-15');

insert into pat_mobiliario.tb_reserva_intervalo (ri_id, rv_id, ri_situacao, ri_preenchimento, uo_id_orgao, ri_qtd_reservada, ri_nro_inicio, ri_nro_fim, ri_dthr_finalizacao) values
(4, 3, 'FINALIZADO', 'AUTOMATICO', (select uo_id from comum_siga.tb_unidade_organizacional where uo_sigla = 'ADMIN'), 3, 101, 103, '2020-04-15 10:39:10.242000');

insert into pat_mobiliario.tb_reserva (rv_id, rv_codigo, rv_situacao, rv_preenchimento, rv_dthr_criacao, rv_qtd_reservada, rv_qtd_restante, rv_nro_inicio, rv_nro_fim, rv_dthr_cadastro, rv_dthr_alteracao)
values (4, '00004', 'PARCIAL', 'AUTOMATICO', '2021-04-15 00:00:00', 10, 5, 111, 120, '2020-04-15', '2020-04-15');

insert into pat_mobiliario.tb_reserva_intervalo (ri_id, rv_id, ri_situacao, ri_preenchimento, uo_id_orgao, ri_qtd_reservada, ri_nro_inicio, ri_nro_fim, ri_dthr_finalizacao) values
(5, 4, 'FINALIZADO', 'MANUAL', (select uo_id from comum_siga.tb_unidade_organizacional where uo_sigla = 'DESEN'), 5, 111, 115, '2020-04-15 10:39:10.242000');


