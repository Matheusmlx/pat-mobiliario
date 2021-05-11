insert into pat_mobiliario.tb_incorporacao (in_id, in_codigo, pe_id, in_situacao, in_dthr_recebimento, in_dthr_cadastro, in_dthr_alteracao, in_usuario_cadastro, in_usuario_alteracao)
values (25, '785491', 1, 'FINALIZADO', '2020-01-01 03:00:00.000000', '2020-01-01 10:39:10.242000', '2020-01-01 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_situacao, in_id, ini_codigo, ini_descricao)
values (nextval('pat_mobiliario.seq_incorporacao_item'), 'FINALIZADO', 25, 'asdf352', 'item incorporacao 2');

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, ini_id, pa_situacao)
values (1, '00000112', 27000, currval('pat_mobiliario.seq_incorporacao_item'),'ATIVO');

insert into comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao) values (1, '124110100', 'BENS INTANGIVEIS>SOFTWARE', 'ATIVO' );

INSERT INTO pat_mobiliario.tb_depreciacao (de_id, de_dthr_inicial, de_dthr_final, de_valor_anterior, de_valor_posterior, de_valor_subtraido, de_taxa_aplicada, de_mes_referencia, pa_id, cc_id, uo_id_orgao, uo_id_setor, de_dthr_cadastro, de_dthr_alteracao, de_usuario_cadastro, de_usuario_alteracao) VALUES
(1, '2020-01-07 00:00:00.000000', '2020-01-31 23:59:59.000000', 1000.000000, 881.000000, 119.000000, 11.900000, '01/2020', 1, 1, (select uo_id from comum_siga.tb_unidade_organizacional where uo_sigla = 'GOV'), (select uo_id from comum_siga.tb_unidade_organizacional where uo_sigla = 'ADMIN'), '2020-07-09 10:39:21.221000', null, 'admin', null),
(2, '2020-02-01 00:00:00.000000', '2020-02-29 23:59:59.000000', 881.000000, 738.100000, 142.900000, 14.290000, '02/2020',1, 1, (select uo_id from comum_siga.tb_unidade_organizacional where uo_sigla = 'GOV'), (select uo_id from comum_siga.tb_unidade_organizacional where uo_sigla = 'ADMIN'), '2020-07-09 10:39:21.350000', null, 'admin', null);
