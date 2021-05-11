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

insert into pat_mobiliario.tb_reserva (rv_id, rv_codigo, rv_situacao, rv_preenchimento, rv_dthr_criacao, rv_qtd_reservada, rv_qtd_restante, rv_nro_inicio, rv_nro_fim)
values (1, '00001', 'EM_ELABORACAO', 'AUTOMATICO', '2020-04-23 14:00:00', 200, 200, 200, 501);

insert into pat_mobiliario.tb_reserva_intervalo (ri_id, rv_id, ri_situacao, ri_preenchimento, uo_id_orgao, ri_qtd_reservada, ri_nro_inicio, ri_nro_fim)
values (1, 1, 'EM_ELABORACAO', 'AUTOMATICO', (select uo_id from comum_siga.tb_unidade_organizacional where uo_sigla = 'ADMIN'), 100, 200, 299);
