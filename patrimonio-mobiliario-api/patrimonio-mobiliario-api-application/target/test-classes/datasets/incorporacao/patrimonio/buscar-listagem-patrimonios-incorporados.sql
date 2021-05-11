insert into comum.tb_usuario (us_id, us_email, us_email_contato, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, us_situacao, id_id, pa_id, us_tipo_usuario)
values (2, 'usuario', 'usuario1@azi.com.br', '57708514738', 'Fornecedor Google', FALSE, '33266655', 'ATIVO', 1, 30, 'EXTERNO');

insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_tipo_adm, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior) values
(89, 'Governo do Estado do Mato Grosso do Sul', 'GEMGS', 'ATIVO', 'INFORMAL', 'DIRETA', '0002', true, null),
(5255, 'Bens Patrimoniais a Localizar e Outros Bens', 'BPLOB', 'ATIVO', 'INFORMAL', 'DIRETA', '0002.0005.0007.0016.0001.0003.0004.0001', false, 89),
(5256, 'Bens Patrimoniais a Localizar', 'BPL', 'ATIVO', 'INFORMAL', 'DIRETA', '0002.0005.0007.0016.0001.0003.0004.0001.0001', false, 5255),
(5257, 'Outros Bens', 'OB', 'ATIVO', 'INFORMAL', 'DIRETA', '0002.0005.0007.0016.0001.0003.0004.0001.0002', false, 5255);

insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id) values
(1, 'ESTRUTURA_ORGANIZACIONAL', 89, 2),
(2, 'ESTRUTURA_ORGANIZACIONAL', 5255, 2),
(3, 'ESTRUTURA_ORGANIZACIONAL', 5256, 2),
(4, 'ESTRUTURA_ORGANIZACIONAL', 5257, 2);

-- incorporacao finalizada
insert into PAT_MOBILIARIO.TB_INCORPORACAO (IN_ID,IN_CODIGO,IN_DTHR_CADASTRO,PE_ID,IN_SITUACAO,IN_DTHR_RECEBIMENTO,IN_DTHR_ALTERACAO,IN_USUARIO_CADASTRO,IN_USUARIO_ALTERACAO)
values (1, '785491','2020-07-09 10:39:10.242000', 1, 'FINALIZADO', '2020-02-24 03:00:00.000000','2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_total_unidades, ini_codigo, ini_descricao, ini_situacao, in_id)
values (1, 10, '0001', 'descricao', 'FINALIZADO', 1);

insert into pat_mobiliario.tb_patrimonio (PA_ID, PA_NUMERO, PA_VALOR_AQUISICAO, PA_VALOR_LIQUIDO, PA_VALOR_RESIDUAL, PA_SITUACAO, INI_ID, uo_id_orgao, uo_id_setor)
values
(1, '2398472894', '14900', '15600', '12500', 'ATIVO', 1, 5255, 5256),
(2, '3758472887', '14900', '15600', '12500', 'ATIVO', 1, 5255, 5256),
(3, '1234567881', '14900', '15600', '12500', 'ATIVO', 1, 5255, 5256),
(4, '1050568418', '14900', '15600', '12500', 'ATIVO', 1, 5255, 5256),
(5, '1544835188', '14900', '15600', '12500', 'ATIVO', 1, 5255, 5256);


-- incorporacao em elaboracao
insert into PAT_MOBILIARIO.TB_INCORPORACAO (IN_ID,IN_CODIGO,IN_DTHR_CADASTRO,PE_ID,IN_SITUACAO,IN_DTHR_RECEBIMENTO,IN_DTHR_ALTERACAO,IN_USUARIO_CADASTRO,IN_USUARIO_ALTERACAO)
values (2, '785492','2020-07-10 10:39:10.242000', 1, 'EM_ELABORACAO', '2020-02-24 03:00:00.000000','2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_total_unidades, ini_codigo, ini_descricao, ini_situacao, in_id)
values (2, 2, '0001', 'descricao', 'EM_ELABORACAO', 2);

insert into pat_mobiliario.tb_patrimonio (PA_ID, PA_NUMERO, PA_VALOR_AQUISICAO, PA_VALOR_LIQUIDO, PA_VALOR_RESIDUAL, PA_SITUACAO, INI_ID, uo_id_orgao, uo_id_setor)
values
(6, '2398472888', '14900', '15600', '12500', NULL, 2, 5255, 5257),
(7, '3758472889', '14900', '15600', '12500', NULL, 2, 5255, 5257);


-- Patrimonios que o usuario nao tem acesso

insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_tipo_adm, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior) values
(7777, 'Secretaria Teste', 'ST', 'ATIVO', 'INFORMAL', 'DIRETA', '0002.0002', false, 89),
(7778, 'Setor da Secretaria Teste', 'SST', 'ATIVO', 'INFORMAL', 'DIRETA', '0002.0002.0001', false, 7777);

-- incorporacao finalizada
insert into PAT_MOBILIARIO.TB_INCORPORACAO (IN_ID,IN_CODIGO,IN_DTHR_CADASTRO,PE_ID,IN_SITUACAO,IN_DTHR_RECEBIMENTO,IN_DTHR_ALTERACAO,IN_USUARIO_CADASTRO,IN_USUARIO_ALTERACAO)
values (3, '785493','2020-07-09 10:39:10.242000', 1, 'FINALIZADO', '2020-02-24 03:00:00.000000','2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_total_unidades, ini_codigo, ini_descricao, ini_situacao, in_id)
values (3, 10, '0001', 'descricao', 'FINALIZADO', 3);

insert into pat_mobiliario.tb_patrimonio (PA_ID, PA_NUMERO, PA_VALOR_AQUISICAO, PA_VALOR_LIQUIDO, PA_VALOR_RESIDUAL, PA_SITUACAO, INI_ID, uo_id_orgao, uo_id_setor)
values
(8, '2398472899', '14900', '15600', '12500', 'ATIVO', 3, 7777, 7778);
