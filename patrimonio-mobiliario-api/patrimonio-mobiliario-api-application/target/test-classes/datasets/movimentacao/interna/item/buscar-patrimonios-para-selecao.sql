insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_tipo_adm, uo_cod_hierarquia, uo_almoxarifado) values
(8773, 'Defensoria Pública Geral do Estado', 'DPGE', 'ATIVO', 'INFORMAL', null, '0002.0001', false),
(8814, 'Fundo Especial para o Aperfeiçoamento e o Desenvolvimento das Atividades da Defensoria Pública', 'CONADEP', 'ATIVO', 'FORMAL', null, '0002.0001.0001.0009', true);

insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao, uo_id_orgao_origem, uo_id_setor_origem) values
(6, '00006', 'DISTRIBUICAO', 'FINALIZADO', 8773, 8814);

insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao, uo_id_orgao_origem, uo_id_setor_origem) values
(7, '00007', 'DISTRIBUICAO', 'EM_ELABORACAO', 8773, 8814);

insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao, uo_id_orgao_origem, uo_id_setor_origem) values
(8, '00008', 'DISTRIBUICAO', 'EM_ELABORACAO', 8773, 8814);

insert into pat_mobiliario.tb_incorporacao (in_id, in_codigo, in_situacao) values
(7, '2', 'FINALIZADO');

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_codigo, ini_situacao, ini_descricao, in_id) values
(1, '000dvf331', 'FINALIZADO', 'Veículo caminhão leve com as seguintes características técnicas mínimas', 7);

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_diferenca_dizima, pa_situacao, uo_id_orgao, uo_id_setor, ini_id, pa_dthr_cadastro, pa_dthr_alteracao, pa_usuario_cadastro, pa_usuario_alteracao) values
(1, '0000000015', 10077.010, false, 'ATIVO', 8773, 8814, 1, '2020-12-29 12:20:21.751292', '2020-12-29 12:23:49.842917', 'admin', 'admin');

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_diferenca_dizima, pa_situacao, uo_id_orgao, uo_id_setor, ini_id, pa_dthr_cadastro, pa_dthr_alteracao, pa_usuario_cadastro, pa_usuario_alteracao) values
(2, '0000000016', 10077.010, false, 'ATIVO', 8773, 8814, 1, '2020-12-29 12:20:21.751292', '2020-12-29 12:23:49.842917', 'admin', 'admin');

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_diferenca_dizima, pa_situacao, uo_id_orgao, uo_id_setor, ini_id, pa_dthr_cadastro, pa_dthr_alteracao, pa_usuario_cadastro, pa_usuario_alteracao) values
(3, '0000000017', 10077.010, false, 'ATIVO', 8773, 8814, 1, '2020-12-29 12:20:21.751292', '2020-12-29 12:23:49.842917', 'admin', 'admin');

insert into pat_mobiliario.tb_movimentacao_item (mo_id, pa_id) values
(7, 1),
(6, 2);

insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_tipo_adm, uo_cod_hierarquia, uo_almoxarifado) values
(8813, 'AAINT - Assessoria de Assuntos Institucionais', 'AAINT', 'ATIVO', 'INFORMAL', null, '0002.0001', false),
(8818, 'AMC - 1ª Regional de Campo Grande - Unidade Antônio Maria Coelho', 'AMC', 'ATIVO', 'FORMAL', null, '0002.0001.0001.0009', false),
(8815, 'BELMAR - 1ª Regional de Campo Grande - Unidade Belmar', 'CONADEP', 'BELMAR', 'FORMAL', null, '0002.0001.0001.0009', false),
(8816, 'ASSEJUR - Assessoria Jurídica', 'ASSEJUR', 'BELMAR', 'FORMAL', null, '0002.0001.0001.0009', false),
(8817, 'ASSEJUR - Assessoria Jurídica', 'ASSEJUR', 'BELMAR', 'FORMAL', null, '0002.0001.0001.0009', false);

--- Cenário para movimentação temporária devolvida parcial
insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao, uo_id_orgao_origem, uo_id_setor_origem) values
(11, '00007', 'TEMPORARIA', 'DEVOLVIDO_PARCIAL', 8773, 8813);

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_diferenca_dizima, pa_situacao, uo_id_orgao, uo_id_setor, ini_id) values
(5, '000000001', 10077.010, false, 'ATIVO', 8773, 8813, 1);

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_diferenca_dizima, pa_situacao, uo_id_orgao, uo_id_setor, ini_id) values
(6, '000000002', 10077.010, false, 'ATIVO', 8773, 8818, 1);

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_diferenca_dizima, pa_situacao, uo_id_orgao, uo_id_setor, ini_id) values
(7, '000000003', 10077.010, false, 'ATIVO', 8773, 8818, 1);


insert into pat_mobiliario.tb_movimentacao_item (mo_id, pa_id, mi_dthr_devolucao) values
(11, 5, '2020-12-29 12:20:21.751292'),
(11, 6, null),
(11, 7, null);

--- Cenário para movimentação temporária aguardando devolução
insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao, uo_id_orgao_origem, uo_id_setor_origem) values
(10, '00006', 'TEMPORARIA', 'AGUARDANDO_DEVOLUCAO', 8773, 8813);

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_diferenca_dizima, pa_situacao, uo_id_orgao, uo_id_setor, ini_id) values
(8, '000000004', 10077.010, false, 'ATIVO', 8773, 8818, 1);

insert into pat_mobiliario.tb_movimentacao_item (mo_id, pa_id, mi_dthr_devolucao) values
(10, 8, null);

--- Cenário para movimentação temporária devolvida
insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao, uo_id_orgao_origem, uo_id_setor_origem) values
(9, '00006', 'TEMPORARIA', 'DEVOLVIDO', 8773, 8813);

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, pa_diferenca_dizima, pa_situacao, uo_id_orgao, uo_id_setor, ini_id) values
(9, '000000005', 10077.010, false, 'ATIVO', 8773, 8813, 1);

insert into pat_mobiliario.tb_movimentacao_item (mo_id, pa_id, mi_dthr_devolucao) values
(9, 9, '2020-12-29 12:20:21.751292');
