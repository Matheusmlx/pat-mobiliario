INSERT INTO comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_tipo_adm, uo_cod_hierarquia, uo_responsavel, uo_substituto, uo_logradouro, uo_numero, uo_complemento, uo_bairro, uo_cep, uo_almoxarifado, uo_cod_orgao, uo_email, uo_data_decreto, uo_numero_decreto, uo_motivo_alteracao, uo_justificativa, uo_id_origem, uo_estrutura_administrativa, uo_id_superior, mu_id, uo_id_externo) VALUES (8773, 'Defensoria Pública Geral do Estado', 'DPGE', 'ATIVO', 'INFORMAL', 'REGIME_ESPECIAL', '0002.0001.0001', null, null, null, null, null, null, null, false, null, null, null, null, null, null, null, null, 8772, null, null);
INSERT INTO comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_tipo_adm, uo_cod_hierarquia, uo_responsavel, uo_substituto, uo_logradouro, uo_numero, uo_complemento, uo_bairro, uo_cep, uo_almoxarifado, uo_cod_orgao, uo_email, uo_data_decreto, uo_numero_decreto, uo_motivo_alteracao, uo_justificativa, uo_id_origem, uo_estrutura_administrativa, uo_id_superior, mu_id, uo_id_externo) VALUES (8774, 'Primeira Subdefensoria Pública-Geral', '1SUBDEF', 'ATIVO', 'FORMAL', null, '0002.0001.0001.0001', null, null, null, null, null, null, null, false, null, null, null, null, null, null, null, null, 8773, null, null);
INSERT INTO comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_tipo_adm, uo_cod_hierarquia, uo_responsavel, uo_substituto, uo_logradouro, uo_numero, uo_complemento, uo_bairro, uo_cep, uo_almoxarifado, uo_cod_orgao, uo_email, uo_data_decreto, uo_numero_decreto, uo_motivo_alteracao, uo_justificativa, uo_id_origem, uo_estrutura_administrativa, uo_id_superior, mu_id, uo_id_externo) VALUES (8775, 'Segunda Subdefensoria Pública-Geral', '2SUBDEF', 'ATIVO', 'INFORMAL', null, '0002.0001.0001.0002', 'eu', '', null, null, null, null, null, true, null, null, null, null, null, 'a', null, null, 8773, null, null);

INSERT INTO pat_mobiliario.tb_responsavel (res_id, res_nome) VALUES
(1, 'Fábio');

INSERT INTO pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao, mo_numero_processo, mo_motivo_obs,
                                            mo_usuario_criacao, mo_dthr_finalizacao, uo_id_orgao_origem,
                                            uo_id_orgao_destino, uo_id_setor_origem, uo_id_setor_destino, nlc_id,
                                            res_id, mo_dthr_cadastro, mo_dthr_alteracao, mo_usuario_cadastro,
                                            mo_usuario_alteracao)
VALUES (1, '00152', 'DISTRIBUICAO', 'FINALIZADO', null, null, 'admin', '2021-01-18 10:03:59.872000', 8773, 8773, 8775,
        8774, null, 1, '2021-01-18 10:02:42.688285', '2021-01-18 10:03:59.915573', 'admin', 'admin');
