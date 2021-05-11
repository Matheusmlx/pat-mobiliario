insert into comum_siga.tb_material_servico (ms_id, ms_codigo, ms_tipo, ms_descricao, ms_observacao, ms_classificacao,
                                            ms_situacao, ms_status, cl_id)
values (1, '81223', 'MATERIAL', 'Descricao', 'Observacao', 'Agricultura Familiar', 'ATIVO', 'APROVADO', 1);

insert into comum_siga.tb_material_servico (ms_id, ms_codigo, ms_tipo, ms_descricao, ms_observacao, ms_classificacao,
                                            ms_situacao, ms_status, cl_id)
values (2, '97856', 'MATERIAL', 'Descricao', 'Observacao', 'Agricultura Familiar', 'ATIVO', 'APROVADO', 1);

-- conta contabil ativa e com configuração
INSERT INTO comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao)
VALUES (41, '123110103', 'APARELHOS E EQUIPAMENTOS DE COMUNICAÇÃO', 'ATIVO');

-- conta contabil ativa e sem configuração
INSERT INTO comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao)
VALUES (42, '123110103', 'APARELHOS E EQUIPAMENTOS DE COMUNICAÇÃO', 'ATIVO');

-- conta contabil inativa e com configuração
INSERT INTO comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao)
VALUES (43, '123110103', 'APARELHOS E EQUIPAMENTOS DE COMUNICAÇÃO', 'INATIVO');

insert into pat_mobiliario.tb_config_conta_contabil(ccc_id, ccc_metodo, ccc_tipo, ccc_tipo_bem, cc_id, ccc_vida_util_meses, ccc_percentual_residual)
values (1, 'QUOTAS_CONSTANTES', 'DEPRECIAVEL', 'VEICULO', 41, 36, 10);

insert into pat_mobiliario.tb_config_conta_contabil(ccc_id, ccc_metodo, ccc_tipo, ccc_tipo_bem, cc_id, ccc_vida_util_meses, ccc_percentual_residual)
values (2, 'QUOTAS_CONSTANTES', 'DEPRECIAVEL', 'VEICULO', 43, 36, 10);

--- Cadastro elemento subelemento
insert into comum_siga.tb_elemento_subelemen (es_id, es_codigo, es_nome, es_situacao, es_tipo)
values (93, '5248', 'VEICULOS DIVERSOS', 'ATIVO', 'SUBELEMENTO');

insert into comum_siga.tb_elemento_subelemen (es_id, es_codigo, es_nome, es_situacao, es_tipo)
values (94, '5248', 'VEICULOS DIVERSOS', 'ATIVO', 'SUBELEMENTO');

--- Cadastro Natureza de contratação
insert into comum_siga.tb_natureza_contratacao(nc_id, nc_descricao, nc_situacao, nc_tipo_natureza_contratacao)
values (2, 'Permanente', 'ATIVO', 'PERMANENTE');

--- Cadastro contratação subelemento
insert into comum_siga.tb_contratacao_subelem (ns_id, ns_situacao, nc_id, es_id)
values (57, 'ATIVO', 2, 93);

insert into comum_siga.tb_contratacao_subelem (ns_id, ns_situacao, nc_id, es_id)
values (58, 'ATIVO', 2, 94);

--- Cadastro Natureza de despesa

--- natureza ativa com conta ativa e com configuração (deve aparecer na listagem)
insert into comum_siga.tb_natureza_despesa (nd_id, nd_despesa, es_id, nd_descricao, nd_situacao, cc_id)
values (1, '44905248', 93, 'VEICULOS DIVERSOS', 'ATIVO', 41);

--- natureza ativa com conta ativa e sem configuracao(não deve aparecer na listagem)
insert into comum_siga.tb_natureza_despesa (nd_id, nd_despesa, es_id, nd_descricao, nd_situacao, cc_id)
values (2, '88549488', 93, 'VEICULOS DIVERSOS', 'ATIVO', 42);

--- natureza ativa com conta inativa e com configuracao(não deve aparecer na listagem)
insert into comum_siga.tb_natureza_despesa (nd_id, nd_despesa, es_id, nd_descricao, nd_situacao, cc_id)
values (3, '95222845', 93, 'VEICULOS DIVERSOS', 'ATIVO', 43);

--- natureza inativa com conta ativa e com configuracao(não deve aparecer na listagem)
insert into comum_siga.tb_natureza_despesa (nd_id, nd_despesa, es_id, nd_descricao, nd_situacao, cc_id)
values (4, '78623298', 93, 'VEICULOS DIVERSOS', 'INATIVO', 41);

-- Relação entre material serviço e contratação subelemento

insert into comum_siga.tb_matser_subelem (ms_id, ns_id)
values (1, 57);

insert into comum_siga.tb_matser_subelem (ms_id, ns_id)
values (2, 58);

insert into comum_siga.tb_item_simplificado (is_id, is_codigo, is_descricao, is_situacao, is_status, is_tipo, ms_id)
values (1, '0000019',
        'Fogão Industrial de quatro (04) bocas, com um (01) forno,',
        'ATIVO', 'APROVADO', 'MATERIAL', 1);

insert into comum_siga.tb_item_simplificado (is_id, is_codigo, is_descricao, is_situacao, is_status, is_tipo, ms_id)
values (2, '0000018',
        'Fogão Industrial de quatro (04) bocas, com um (01) forno,',
        'ATIVO', 'APROVADO', 'MATERIAL', 2);
