INSERT INTO pat_mobiliario.TB_INCORPORACAO (IN_ID, IN_CODIGO, IN_NOTA, IN_DTHR_CADASTRO, PE_ID, IN_SITUACAO,
                                            IN_DTHR_RECEBIMENTO, IN_DTHR_ALTERACAO, IN_USUARIO_CADASTRO,
                                            IN_USUARIO_ALTERACAO)
VALUES (5, '785491', '3548', '2020-07-09 10:39:10.242000', 1, 'FINALIZADO', '2020-02-24 03:00:00.000000',
        '2020-07-10 10:39:10.242000', 'admin', null);

insert into pat_mobiliario.tb_incorporacao_item (ini_id, ini_codigo, ini_situacao, ini_descricao, in_id)
values (10, '0053235', 'FINALIZADO',
        'Caminhão semi-pesado, zero quilômetro, cabina semi-avançada,motor diesel,turbinado, potência mínima de 200 cv',
        5);

insert into pat_mobiliario.tb_patrimonio (pa_id, pa_numero, pa_valor_aquisicao, ini_id)
values (10, '000000001', 150.50, 10);

INSERT INTO pat_mobiliario.tb_nota_lancamento_contabil (nlc_id, nlc_numero, nlc_dthr_lancamento) values
(12, '4444NL444444', '2021-01-05 00:00:00.000000');

INSERT INTO pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao, mo_dthr_finalizacao,mo_dthr_cadastro, uo_id_setor_origem, uo_id_setor_destino, nlc_id)
VALUES (15, '00001', 'TEMPORARIA', 'FINALIZADO', '2020-12-15 21:14:55.000000','2020-11-15 21:14:55.000000', 1, 2, 12);

INSERT INTO pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_motivo_obs, mo_situacao, mo_dthr_finalizacao,mo_dthr_cadastro, uo_id_orgao_origem, uo_id_setor_origem, uo_id_setor_destino, nlc_id)
VALUES (16, '00002', 'DISTRIBUICAO', 'Observacao movimentação', 'EM_ELABORACAO', '2020-12-15 21:15:55.000000','2020-11-15 22:14:55.000000', 3, 1, 2, 12);

insert into pat_mobiliario.tb_movimentacao_item (mo_id, pa_id)
values (15, 10), (16, 10);
