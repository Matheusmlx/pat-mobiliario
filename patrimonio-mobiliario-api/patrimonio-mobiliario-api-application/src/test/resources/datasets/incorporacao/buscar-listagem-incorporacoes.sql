insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior) values
(5255, 'Bens Patrimoniais a Localizar e Outros Bens', 'BPLOB', 'ATIVO', 'FORMAL', '0002.0005.0007.0016.0001.0002', false, 5252);

insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior) values
(5141, 'Assessoria de Comunicação Social', 'ASCOM', 'ATIVO', 'FORMAL', '0002.0005.0007', false, 101);

insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior) values
(11222, 'Governo do Estado do Mato Grosso do Sul', 'GEMGS', 'ATIVO', 'FORMAL', '0002.0005.0007.0016.0001.0002', false, 5252);

insert into pat_mobiliario.tb_incorporacao (in_id,in_codigo,in_dthr_cadastro,pe_id,in_situacao,in_dthr_recebimento,in_dthr_alteracao,in_usuario_cadastro,in_usuario_alteracao)
values (1,'785491','2020-07-09 10:39:10.242000',1,'FINALIZADO','2020-02-24 03:00:00.000000','2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_incorporacao (in_id,in_codigo,in_dthr_cadastro,pe_id,in_situacao,in_dthr_recebimento,in_dthr_alteracao,in_usuario_cadastro,in_usuario_alteracao)
values (2,'123456','2020-07-09 10:39:10.242000',2,'EM_ELABORACAO','2020-02-24 03:00:00.000000','2020-07-10 10:39:10.242000', 'admin', null);

insert into PAT_MOBILIARIO.TB_INCORPORACAO (in_id,in_codigo,in_dthr_cadastro,in_situacao,in_dthr_recebimento,in_dthr_alteracao,in_usuario_cadastro,in_usuario_alteracao)
values (3,'123654','2020-07-09 10:39:10.242000','EM_ELABORACAO','2020-02-24 03:00:00.000000','2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_incorporacao (in_id,in_codigo,in_dthr_cadastro,pe_id,in_situacao,in_dthr_recebimento,uo_id_orgao,in_dthr_alteracao,in_usuario_cadastro,in_usuario_alteracao)
values (4,'654123','2020-07-09 10:39:10.242000',1,'FINALIZADO','2020-02-24 03:00:00.000000',5255,'2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_incorporacao (in_id,in_codigo,in_dthr_cadastro,pe_id,in_situacao,in_dthr_recebimento,uo_id_orgao,in_dthr_alteracao,in_usuario_cadastro,in_usuario_alteracao)
values (5,'789654','2020-07-09 10:39:10.242000',2,'FINALIZADO','2020-02-24 03:00:00.000000',5141,'2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_incorporacao (in_id,in_codigo,in_dthr_cadastro,pe_id,in_situacao,in_dthr_recebimento,uo_id_orgao,in_dthr_alteracao,in_usuario_cadastro,in_usuario_alteracao)
values (6,'956463','2020-07-09 10:39:10.242000',2,'FINALIZADO','2020-02-24 03:00:00.000000',11222,'2020-07-10 10:39:10.242000', 'admin', null);

insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id) values
(1, 'ESTRUTURA_ORGANIZACIONAL', 5255, 2),
(2, 'ESTRUTURA_ORGANIZACIONAL', 5141, 2);

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
