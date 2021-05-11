insert into comum.tb_usuario (us_id, us_email, us_email_contato, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, us_situacao, id_id, pa_id, us_tipo_usuario)
values
(nextval('comum.seq_usuario'), 'usuario', 'usuario1@azi.com.br', '57708514738', 'Fornecedor Google', FALSE, '33266655', 'ATIVO', 1, 30, 'EXTERNO');

insert into comum_siga.tb_tipo_documento (td_id, td_desc, td_permite_anexo, td_dthr_cadastro, td_dthr_alteracao, td_usuario_cadastro, td_usuario_alteracao, td_identificacao_documento) values
(1, 'Contrato', true, null, null, null, null, 'CONTRATO');

insert into comum_siga.tb_tipo_documento (td_id, td_desc, td_permite_anexo, td_dthr_cadastro, td_dthr_alteracao, td_usuario_cadastro, td_usuario_alteracao, td_identificacao_documento) values
(2, 'Comprovante de Compra', true, null, null, null, null, 'COMPROVANTE_COMPRA');

insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao) values
(2, '00001', 'DISTRIBUICAO', 'EM_ELABORACAO');

insert into pat_mobiliario.tb_documento(do_id, do_numero, do_dt, do_valor, do_uri_anexo, mo_id, td_id) values
(1, '1234', '2021-01-01', 1000.0, 'repo1:patrimoniomobiliario/teste1.pdf', 2, 1);
