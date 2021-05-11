insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_tipo_adm, uo_cod_hierarquia, uo_almoxarifado) values
(89, 'Governo do Estado do Mato Grosso do Sul', 'GEMGS', 'ATIVO', 'INFORMAL', 'DIRETA', '0002', true),
(8772, 'Defensoria Pública Geral do Estado', 'DPGE', 'ATIVO', 'INFORMAL', null, '0002.0001', false),
(1164, 'Assessoria de Relações Institucionais', 'ARI', 'ATIVO', 'FORMAL', null,'0002.0002.0002.0006.0002.0001', false),
(6056, 'Unidade de Almoxarifado', 'UA', 'ATIVO', 'INFORMAL', null, '0002.0002.0008.0039.0004.0003', true);

insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id) values
(1, 'ESTRUTURA_ORGANIZACIONAL', 89, 2),
(2, 'ESTRUTURA_ORGANIZACIONAL', 8772, 2),
(3, 'ESTRUTURA_ORGANIZACIONAL', 1164, 2),
(4, 'ESTRUTURA_ORGANIZACIONAL', 6056, 2);

insert into pat_mobiliario.tb_movimentacao(mo_id, mo_tipo, mo_situacao) values
(1, 'TEMPORARIA', 'EM_ELABORACAO');

alter sequence pat_mobiliario.seq_movimentacao restart with 2;
