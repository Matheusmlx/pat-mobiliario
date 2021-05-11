INSERT INTO pat_mobiliario.tb_concedente(coc_id,coc_nome,coc_situacao,coc_tipo,coc_cpf_cnpj, coc_dthr_cadastro, coc_dthr_alteracao, coc_usuario_cadastro, coc_usuario_alteracao)
VALUES (1,'B nome concedente', 'ATIVO', 'FISICA', '201.222.510-16', '2020-07-09 10:39:10.242000', null, 'admin', null);

INSERT INTO pat_mobiliario.tb_concedente(coc_id,coc_nome,coc_situacao,coc_tipo,coc_cpf_cnpj, coc_dthr_cadastro, coc_dthr_alteracao, coc_usuario_cadastro, coc_usuario_alteracao)
VALUES (2,'A nome concedente', 'INATIVO', 'FISICA', '471.849.790-60', '2020-07-09 10:39:10.242000', null, 'admin', null);

INSERT INTO pat_mobiliario.tb_concedente(coc_id,coc_nome,coc_situacao,coc_tipo,coc_cpf_cnpj, coc_dthr_cadastro, coc_dthr_alteracao, coc_usuario_cadastro, coc_usuario_alteracao)
VALUES (3,'C concedente', 'ATIVO', 'FISICA', '201.222.510-16', '2020-07-09 10:39:10.242000', null, 'admin', null);

INSERT INTO pat_mobiliario.tb_convenio(co_id,co_dthr_inicio,co_dthr_fim,co_numero,co_nome, co_fonte_recurso, co_tipo, co_situacao, co_dthr_cadastro, co_dthr_alteracao, co_usuario_cadastro, co_usuario_alteracao, coc_id)
VALUES (1,'2020-07-09 10:39:10.242000', '2020-07-20 10:39:10.242000', '1', 'convenio alagoas', 'Aquisição de material Permanente', 'Tipo','ATIVO', '2020-07-10 10:39:10.242000', '2020-07-11 10:39:10.242000', 'admin', null, 1 );

INSERT INTO pat_mobiliario.tb_convenio(co_id,co_dthr_inicio,co_dthr_fim,co_numero,co_nome, co_fonte_recurso, co_tipo, co_situacao, co_dthr_cadastro, co_dthr_alteracao, co_usuario_cadastro, co_usuario_alteracao, coc_id)
VALUES (2,'2020-07-09 10:39:10.242000', '2020-07-20 10:39:10.242000', '2', 'bragantino','Aquisição de bens', 'Tipo','INATIVO', '2020-07-11 10:39:10.242000', '2020-07-12 10:39:10.242000', 'admin', null, 2 );

INSERT INTO pat_mobiliario.tb_convenio(co_id,co_dthr_inicio,co_dthr_fim,co_numero,co_nome, co_fonte_recurso, co_tipo, co_situacao, co_dthr_cadastro, co_dthr_alteracao, co_usuario_cadastro, co_usuario_alteracao, coc_id)
VALUES (3,'2020-07-09 10:39:10.242000', '2020-07-20 10:39:10.242000', '3', 'convenio correlacao', 'Material Permanente', 'Tipo','ATIVO', '2020-07-09 10:39:10.242000', '2020-07-13 10:39:10.242000', 'admin', null, 3 );
