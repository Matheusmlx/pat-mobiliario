insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_tipo_adm, uo_cod_hierarquia, uo_almoxarifado) values
(89, 'Governo do Estado do Mato Grosso do Sul', 'GEMGS', 'ATIVO', 'INFORMAL', 'DIRETA', '0002', false),
(8772, 'Defensoria Pública Geral do Estado', 'DPGE', 'ATIVO', 'INFORMAL', null, '0002.0001', false),
(1164, 'Assessoria de Relações Institucionais', 'ARI', 'ATIVO', 'FORMAL', null,'0002.0002.0002.0006.0002.0001', true);

insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id) values
(1, 'ESTRUTURA_ORGANIZACIONAL', 89, 2),
(2, 'ESTRUTURA_ORGANIZACIONAL', 8772, 2),
(3, 'ESTRUTURA_ORGANIZACIONAL', 1164, 2);

insert into comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao, cc_dthr_cadastro, cc_dthr_alteracao, cc_usuario_cadastro, cc_usuario_alteracao)
values (100,'0001','TESTE','TESTE','2020-07-10 10:39:10.242000','2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO PAT_MOBILIARIO.TB_CONFIG_CONTA_CONTABIL (CCC_ID,CCC_METODO,CCC_TIPO,CCC_TIPO_BEM,CCC_VIDA_UTIL_MESES,CCC_PERCENTUAL_RESIDUAL,CC_ID,CCC_DTHR_CADASTRO,CCC_DTHR_ALTERACAO, CCC_USUARIO_CADASTRO, CCC_USUARIO_ALTERACAO)
VALUES (100,'QUOTAS_CONSTANTES','CONTA','CONTA',10, 2.5,100,'2020-07-10 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO PAT_MOBILIARIO.TB_INCORPORACAO (IN_ID,IN_CODIGO,IN_DTHR_CADASTRO,PE_ID,IN_SITUACAO,IN_DTHR_RECEBIMENTO,IN_DTHR_ALTERACAO,IN_USUARIO_CADASTRO,IN_USUARIO_ALTERACAO)
VALUES (1,'785491','2020-07-09 10:39:10.242000',1,'FINALIZADO','2020-02-24 03:00:00.000000','2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_total_unidades, ini_codigo, ini_descricao, ini_situacao, in_id, cc_id)
values (1, 10, '0001', 'descricao', 'EM_ELABORACAO', 1, 100);

-- TB_PATRIMONIO
insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_situacao, ini_id, uo_id_orgao, uo_id_setor) values
(1, '0000001', 100, 'ATIVO', 1, 8772, 1164),
(2, '0000002', 100, 'ATIVO', 1, 8772, 1164),
(3, '0000003', 100, 'ATIVO', 1, 8772, 1164),
(4, '0000004', 100, 'ATIVO', 1, 8772, 1164),
(5, '0000005', 100, 'ATIVO', 1, 8772, 1164);

-- TB_MOVIMENTACAO
insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao, uo_id_orgao_origem, uo_id_setor_origem) values
(1, '00001', 'DISTRIBUICAO', 'EM_ELABORACAO', 8772, 1164);

insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao, uo_id_orgao_origem, uo_id_setor_origem) values
(2, '00002', 'DISTRIBUICAO', 'EM_ELABORACAO', 8772, 1164);

insert into pat_mobiliario.tb_movimentacao_item (mo_id, pa_id) values
(2, 3),
(2, 5);

insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao, uo_id_orgao_origem, uo_id_setor_origem) values
(3, '00003', 'DISTRIBUICAO', 'EM_ELABORACAO', 8772, 1164);


