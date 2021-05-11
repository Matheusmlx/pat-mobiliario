insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_tipo_adm, uo_cod_hierarquia, uo_almoxarifado, uo_estrutura_administrador) values
(89, 'Governo do Estado do Mato Grosso do Sul', 'GEMGS', 'ATIVO', 'INFORMAL', 'DIRETA', '0002', true, null),
(8772, 'Defensoria Pública Geral do Estado', 'DPGE', 'ATIVO', 'INFORMAL', 'FUNDOS', '0002.0001', false, 89);

insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id) values
(1, 'ESTRUTURA_ORGANIZACIONAL', 89, 2),
(2, 'ESTRUTURA_ORGANIZACIONAL', 8772, 2);
