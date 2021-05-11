insert into comum_siga.tb_classe (cl_id, cl_codigo, cl_descricao, cl_situacao)
values (1, '1000A','Classe A', 'ATIVO');

insert into comum_siga.tb_material_servico (ms_id, ms_codigo, ms_tipo, ms_descricao, ms_observacao, ms_classificacao,
                                            ms_situacao, ms_status, cl_id)
values (1, '81223', 'MATERIAL', 'Descricao', 'Observacao', 'Agricultura Familiar', 'ATIVO', 'APROVADO', 1);

insert into comum_siga.tb_item_material_servico (ims_id, ims_codigo, ims_descricao, ims_situacao, ims_status, ims_tipo, ms_id, ims_descricao_truncada)
values (1, '0000019',
        'descricao',
        'ATIVO', 'APROVADO', 'MATERIAL', 1, 'descricao truncada');

insert into comum_siga.tb_item_material_servico (ims_id, ims_codigo, ims_descricao, ims_situacao, ims_status, ims_tipo, ms_id, ims_descricao_truncada)
values (2, '0053235',
        'descricao 2',
        'ATIVO', 'APROVADO', 'MATERIAL', 1, 'descricao truncada');


insert into comum_siga.tb_item_material_servico (ims_id, ims_codigo, ims_descricao, ims_situacao, ims_status, ims_tipo, ms_id, ims_descricao_truncada)
values (3, '0000018',
        '1 descricao',
        'INATIVO', 'APROVADO', 'MATERIAL', 1, 'descricao truncada');
