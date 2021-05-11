alter sequence pat_mobiliario.seq_documento restart with 1;

insert into comum.tb_usuario (us_id, us_email, us_email_contato, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, us_situacao, id_id, pa_id, us_tipo_usuario)
values
(nextval('comum.seq_usuario'), 'usuario', 'usuario1@azi.com.br', '57708514738', 'Fornecedor Google', FALSE, '33266655', 'ATIVO', 1, 30, 'EXTERNO');

insert into comum_siga.tb_tipo_documento (td_id, td_desc, td_permite_anexo, td_dthr_cadastro, td_dthr_alteracao, td_usuario_cadastro, td_usuario_alteracao, td_identificacao_documento) values
(1, 'Contrato', true, null, null, null, null, 'CONTRATO');

insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao) values
(1, '00001', 'DISTRIBUICAO', 'EM_ELABORACAO');
