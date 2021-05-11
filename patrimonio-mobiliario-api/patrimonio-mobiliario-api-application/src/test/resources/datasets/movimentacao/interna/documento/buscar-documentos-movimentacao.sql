insert into comum_siga.tb_tipo_documento (td_id, td_desc, td_permite_anexo, td_dthr_cadastro, td_dthr_alteracao, td_usuario_cadastro, td_usuario_alteracao, td_identificacao_documento) values
(1, 'Contrato', true, null, null, null, null, 'CONTRATO');

insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao) values
(1, '00001', 'DISTRIBUICAO', 'EM_ELABORACAO'),
(2, '00001', 'DISTRIBUICAO', 'EM_ELABORACAO');

insert into pat_mobiliario.tb_documento (do_id, do_numero, do_dt, do_valor, do_uri_anexo, mo_id, td_id, do_dthr_cadastro, do_dthr_alteracao) values
(1, '454453', '2020-06-02', 1200, 'repo1:patrimoniomobiliario/teste.pdf', 1, 1, '2020-06-25 11:36:56.350000', null),
(2, '454454', '2020-06-04', 1300, null, 1, 1, '2020-06-25 11:36:56.350000', null);
