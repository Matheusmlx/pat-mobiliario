insert into comum_siga.tb_classe (cl_id, cl_codigo, cl_descricao, cl_situacao)
values (1, '1000A','Classe A', 'ATIVO');

insert into comum_siga.tb_natureza_contratacao (nc_id, nc_descricao, nc_situacao, nc_tipo_natureza_contratacao) values
(1, 'Permanente', 'ATIVO', 'PERMANENTE'),
(2, 'Natureza Contratacao', 'ATIVO', 'CONSUMO');

insert into comum_siga.tb_contratacao_subelem (ns_id, ns_situacao, nc_id, es_id)
values (1, 'ATIVO', 1, 1);

insert into comum_siga.tb_matser_subelem (ms_id, ns_id) values
(1, 1),
(2, 2);

insert into comum_siga.tb_material_servico (ms_id, ms_codigo, ms_tipo, ms_descricao, ms_observacao, ms_classificacao,ms_situacao, ms_status, cl_id)
values (1, '81223', 'MATERIAL', 'Descricao', 'Observacao', 'Agricultura Familiar', 'ATIVO', 'APROVADO', 1);

insert into comum_siga.tb_item_material_servico (ims_id, ims_codigo, ims_descricao, ims_situacao, ims_status, ims_tipo, ms_id, ims_descricao_truncada)
values (1, '1','descricao 2 regular','ATIVO', 'APROVADO', 'MATERIAL', 1, 'descricao truncada');

insert into comum_siga.tb_item_material_servico (ims_id, ims_codigo, ims_descricao, ims_situacao, ims_status, ims_tipo, ms_id, ims_descricao_truncada)
values (2, '3','descricao 1 regular','ATIVO', 'APROVADO', 'MATERIAL', 1, 'descricao truncada');

insert into comum_siga.tb_item_material_servico (ims_id, ims_codigo, ims_descricao, ims_situacao, ims_status, ims_tipo, ms_id, ims_descricao_truncada)
values (3, '6','descricao 3 regular','ATIVO', 'APROVADO', 'MATERIAL', 1, 'descricao truncada');

insert into comum_siga.tb_item_simplificado (is_id, is_codigo, is_descricao, is_situacao, is_status, is_tipo, ms_id)
values (1, '2','descricao 2 simplificado','ATIVO', 'APROVADO', 'MATERIAL', 1);

insert into comum_siga.tb_item_simplificado (is_id, is_codigo, is_descricao, is_situacao, is_status, is_tipo, ms_id)
values (2, '4','descricao 1 simplificado','ATIVO', 'APROVADO', 'MATERIAL', 1);

insert into comum_siga.tb_item_simplificado (is_id, is_codigo, is_descricao, is_situacao, is_status, is_tipo, ms_id)
values (3, '5','descricao 3 simplificado','ATIVO', 'APROVADO', 'MATERIAL', 1);

insert into comum_siga.tb_item_simplificado (is_id, is_codigo, is_descricao, is_situacao, is_status, is_tipo, ms_id)
values (4, '0','0 descricao 3 simplificado','INATIVO', 'APROVADO', 'MATERIAL', 1);

insert into comum_siga.tb_item_simplificado (is_id, is_codigo, is_descricao, is_situacao, is_status, is_tipo, ms_id)
values (5, '0','0 descricao 4 simplificado','ATIVO', 'APROVADO', 'MATERIAL', 2);

insert into comum_siga.tb_item_material_servico (ims_id, ims_codigo, ims_descricao, ims_situacao, ims_status, ims_tipo, ms_id, ims_descricao_truncada)
values (4, '0','0 descricao 3 regular','INATIVO', 'APROVADO', 'MATERIAL', 1, 'descricao truncada');
