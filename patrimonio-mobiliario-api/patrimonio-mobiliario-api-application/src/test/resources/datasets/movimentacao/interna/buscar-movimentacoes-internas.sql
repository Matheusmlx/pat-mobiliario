insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior) values
(5255, 'Bens Patrimoniais a Localizar e Outros Bens', 'BPLOB', 'ATIVO', 'FORMAL', '0002.0005.0007.0016.0001.0002', false, 5252);

insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior) values
(5141, 'Assessoria de Comunicação Social', 'ASCOM', 'ATIVO', 'FORMAL', '0002.0005.0007', false, 101);

insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior) values
(4767, 'Assessoria de Tecnologia em Informação', 'ASTI', 'ATIVO', 'FORMAL', '0002.0005.0007.0016.0001.0002', false, 5252);

insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao, mo_usuario_criacao, uo_id_orgao_origem, uo_id_orgao_destino, uo_id_setor_origem, uo_id_setor_destino, mo_dthr_cadastro, mo_dthr_alteracao, mo_usuario_cadastro, mo_usuario_alteracao) values
(17, '00017', 'DISTRIBUICAO', 'EM_ELABORACAO', 'admin', 5255, 5255, 5141, 4767, '2021-01-06 12:39:56.435744', '2021-01-06 12:40:14.531167', 'admin', 'admin');


insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior) values
(11222, 'Governo do Estado do Mato Grosso do Sul', 'GEMGS', 'ATIVO', 'FORMAL', '0002.0005.0007.0016.0001.0002', false, 5252);

insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior) values
(11220, 'Governança e Gestão do Estado', 'SEGOV', 'ATIVO', 'FORMAL', '0002.0005.0007', false, 101);

insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior) values
(11221, 'Defensoria Pública Geral do Estado', 'DPGE', 'ATIVO', 'FORMAL', '0002.0005.0007.0016.0001.0002', false, 5252);

insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao, mo_usuario_criacao, uo_id_orgao_origem, uo_id_orgao_destino, uo_id_setor_origem, uo_id_setor_destino, mo_dthr_cadastro, mo_dthr_alteracao, mo_usuario_cadastro, mo_usuario_alteracao) values
(18, '00018', 'DISTRIBUICAO', 'EM_ELABORACAO', 'admin', 11222, 11222, 11220, 11221, '2021-01-06 12:39:56.435744', '2021-01-06 12:40:14.531166', 'admin', 'admin');


insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior) values
(5808, 'Divisão de Cobranças', 'COB', 'ATIVO', 'FORMAL', '0002.0005.0007.0016.0001.0002', false, 5252);

insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior) values
(5253, 'Conselho Diretor', 'CONDIR', 'ATIVO', 'FORMAL', '0002.0005.0007', false, 101);

insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior) values
(5251, 'Conselho Administrativo', 'CONSAD', 'ATIVO', 'FORMAL', '0002.0005.0007.0016.0001.0002', false, 5252);

insert into pat_mobiliario.tb_nota_lancamento_contabil (nlc_id, nlc_numero, nlc_dthr_lancamento) values
(30, '2342NL342342', '2021-01-04 00:00:00.000000');

insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao, mo_motivo_obs, mo_usuario_criacao, mo_numero_processo, uo_id_orgao_origem, uo_id_orgao_destino, uo_id_setor_origem, uo_id_setor_destino, nlc_id,mo_dthr_cadastro, mo_dthr_alteracao, mo_usuario_cadastro, mo_usuario_alteracao) values
(19, '00019', 'DISTRIBUICAO', 'EM_ELABORACAO', 'Motivo Estorno', 'admin', '351351', 5808, 5808, 5253, 5251, 30, '2021-01-06 12:39:56.435744', '2021-01-06 12:40:14.531166', 'admin', 'admin');

insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id) values
(1, 'ESTRUTURA_ORGANIZACIONAL', 5255, 2),
(2, 'ESTRUTURA_ORGANIZACIONAL', 5808, 2);

insert into comum.tb_perfil (pf_id, pf_nome, pf_descricao, pf_tipo, pr_id) values
(1, 'Administrador', 'Administrador do sistema', 'INTERNO', 410);

insert into hal.tb_dominio_perfil (dm_id, pf_id) values
(1, 1),
(2, 1);

insert into hal.tb_funcao (fu_id, fu_nome, fu_descricao) values
(2, 'Mobiliario.Normal', 'Função de acesso normal');

insert into comum.tb_plano (pl_id, pl_nome, pl_descricao) values
(1, 'plano-basico', 'Plano básico');

insert into hal.tb_permissao_perfil (fu_id, pf_id, pl_id, pp_permissao) values
(2, 1, 1, 'S');
