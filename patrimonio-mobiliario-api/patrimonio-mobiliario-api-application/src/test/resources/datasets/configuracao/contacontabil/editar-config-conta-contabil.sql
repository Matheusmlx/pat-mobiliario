insert into comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao) values (1, '124110100', 'BENS INTANGIVEIS>SOFTWARE', 'ATIVO' );

insert into pat_mobiliario.tb_config_conta_contabil(ccc_id, ccc_metodo, ccc_tipo, cc_id, ccc_vida_util_meses, ccc_percentual_residual, ccc_tipo_bem)
values (1, 'QUOTAS_CONSTANTES', 'DEPRECIAVEL', 1, 36, 10,'ARMAMENTO')
