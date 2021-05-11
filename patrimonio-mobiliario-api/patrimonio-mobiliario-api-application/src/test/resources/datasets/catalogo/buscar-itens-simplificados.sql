insert into comum_siga.tb_classe (cl_id, cl_codigo, cl_descricao, cl_situacao)
values (1, '1000A','Classe A', 'ATIVO');

insert into comum_siga.tb_material_servico (ms_id, ms_codigo, ms_tipo, ms_descricao, ms_observacao, ms_classificacao,
                                            ms_situacao, ms_status, cl_id)
values (1, '81223', 'MATERIAL', 'Descricao', 'Observacao', 'Agricultura Familiar', 'ATIVO', 'APROVADO', 1);

insert into comum_siga.tb_item_simplificado (is_id, is_codigo, is_descricao, is_situacao, is_status, is_tipo, ms_id)
values (1, '0000019',
        'Legume fresco - Tipo: milho; Espécie: verde; Tamanho:  médio, no grau máximo de evolução .; Requisito: grão em estado leitoso (ponto de pamonha); Dados Complementares: palha de coloração uniforme e sem manchas.',
        'ATIVO', 'APROVADO', 'MATERIAL', 1);

insert into comum_siga.tb_item_simplificado (is_id, is_codigo, is_descricao, is_situacao, is_status, is_tipo, ms_id)
values (2, '0000020',
        'Abacate fresco - Tipo: milho; Espécie: verde; Tamanho:  médio, no grau máximo de evolução .; Requisito: grão em estado leitoso (ponto de pamonha); Dados Complementares: palha de coloração uniforme e sem manchas.',
        'ATIVO', 'APROVADO', 'MATERIAL', 1);
