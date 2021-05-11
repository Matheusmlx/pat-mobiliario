insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_tipo_adm, uo_cod_hierarquia, uo_almoxarifado) values
(89, 'Governo do Estado do Mato Grosso do Sul', 'GEMGS', 'ATIVO', 'INFORMAL', 'DIRETA', '0002', false),
(8772, 'Defensoria Pública Geral do Estado', 'DPGE', 'ATIVO', 'INFORMAL', null, '0002.0001', false),
(1164, 'Assessoria de Relações Institucionais', 'ARI', 'ATIVO', 'FORMAL', null,'0002.0002.0002.0006.0002.0001', false),
(1165, 'Assessoria de Relações Extrangeiras e Internacionais', 'AREI', 'ATIVO', 'FORMAL', null,'0002.0002.0002.0006.0002.0002', false);

insert into comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao, cc_dthr_cadastro, cc_dthr_alteracao, cc_usuario_cadastro, cc_usuario_alteracao)
values (100,'0001','TESTE','TESTE','2020-07-10 10:39:10.242000','2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_config_conta_contabil (ccc_id,ccc_metodo,ccc_tipo,ccc_tipo_bem,ccc_vida_util_meses,ccc_percentual_residual,cc_id,ccc_dthr_cadastro,ccc_dthr_alteracao, ccc_usuario_cadastro, ccc_usuario_alteracao)
values (100,'QUOTAS_CONSTANTES','CONTA','CONTA',10, 2.5,100,'2020-07-10 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_incorporacao (in_id,in_codigo,in_dthr_cadastro,pe_id,in_situacao,in_dthr_recebimento,in_dthr_alteracao,in_usuario_cadastro,in_usuario_alteracao)
values (1,'785491','2020-07-09 10:39:10.242000',1,'FINALIZADO','2020-02-24 03:00:00.000000','2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_total_unidades, ini_codigo, ini_descricao, ini_situacao, in_id, cc_id)
values (1, 10, '0001', 'descricao', 'EM_ELABORACAO', 1, 100);

-- TB_PATRIMONIO
insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_situacao, ini_id, uo_id_orgao, uo_id_setor,  cc_id_atual, cc_id_classificacao) values
(1, '0000001', 1000, 'ATIVO', 1, 8772, 1164, 100, 100),
(2, '0000002', 1000, 'ATIVO', 1, 8772, 1165, 100, 100),
(3, '0000003', 1000, 'ATIVO', 1, 8772, 1165, 100, 100),
(4, '0000004', 1000, 'ATIVO', 1, 8772, 1164, 100, 100),
(5, '0000005', 1000, 'ATIVO', 1, 8772, 1164, 100, 100);

-- TB_MOVIMENTACAO
insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao, uo_id_orgao_origem, uo_id_setor_origem, uo_id_orgao_destino, uo_id_setor_destino) values
(1, '00001', 'TEMPORARIA', 'DEVOLVIDO_PARCIAL', 8772, 1164, 8772, 1165);

insert into pat_mobiliario.tb_movimentacao_item (mo_id, pa_id, mi_dthr_devolucao) values
(1, 3, null),
(1, 5, '2021-01-01 10:39:10.242000'),
(1, 4, '2021-01-01 10:39:10.242000'),
(1, 2, null);

