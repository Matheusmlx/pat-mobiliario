INSERT INTO pat_mobiliario.tb_nota_lancamento_contabil
    (nlc_id, nlc_numero, nlc_dthr_lancamento)
VALUES (1, '2015NL98576', '2020-05-28T18:58:07.358Z');

INSERT INTO pat_mobiliario.tb_incorporacao
(in_id, in_codigo, in_dthr_recebimento, in_situacao, in_num_processo, pe_id, in_nota, in_valor_nota)
VALUES (1, '1531315', '2020-05-28T18:58:07.358Z', 'EM_ELABORACAO', '987465', 1, '5', '7.988');

insert into pat_mobiliario.tb_movimentacao
(mo_id, mo_tipo, mo_situacao, mo_motivo_obs, uo_id_orgao_origem, uo_id_orgao_destino, uo_id_setor_origem,
 uo_id_setor_destino, nlc_id, mo_numero_processo)
VALUES (20, 'DISTRIBUICAO', 'EM_ELABORACAO', 'motivo', 1, 2, 1, 2, 1, '12345');
