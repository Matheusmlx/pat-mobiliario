insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_tipo_adm, uo_cod_hierarquia, uo_almoxarifado) values
(9, 'Governo do Estado do Mato Grosso do Sul', 'GEMGS', 'ATIVO', 'INFORMAL', 'DIRETA', '0002', true),
(10, 'Defensoria Pública Geral do Estado', 'DPGE', 'ATIVO', 'INFORMAL', null, '0002.0001', false),
(11, 'Assessoria de Relações Institucionais', 'ARI', 'ATIVO', 'FORMAL', null,'0002.0002.0002.0006.0002.0001', false),
(12, 'Unidade de Almoxarifado', 'UA', 'ATIVO', 'INFORMAL', null, '0002.0002.0008.0039.0004.0003', true);

insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id) values
(1, 'ESTRUTURA_ORGANIZACIONAL', 9, 2),
(2, 'ESTRUTURA_ORGANIZACIONAL', 10, 2),
(3, 'ESTRUTURA_ORGANIZACIONAL', 11, 2),
(4, 'ESTRUTURA_ORGANIZACIONAL', 12, 2);

insert into pat_mobiliario.tb_movimentacao(mo_id, mo_codigo, mo_dthr_finalizacao, mo_tipo, mo_motivo_obs,  mo_situacao, mo_numero_processo, mo_usuario_criacao, uo_id_orgao_origem, uo_id_orgao_destino, uo_id_setor_origem, uo_id_setor_destino) values
(1, '00001', '020-07-10 10:39:10.242000', 'DEFINITIVA', 'motivo teste', 'FINALIZADO', '12345', 'admin', 10, 11, 11, 12);

alter sequence pat_mobiliario.seq_movimentacao restart with 2;
