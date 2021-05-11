insert into pat_mobiliario.tb_movimentacao (mo_id, mo_codigo, mo_tipo, mo_situacao, mo_usuario_criacao) values
(1, '00001', 'DISTRIBUICAO', 'EM_ELABORACAO', 'admin'),
(2, '00002', 'DISTRIBUICAO', 'EM_ELABORACAO', 'admin');

alter sequence pat_mobiliario.seq_movimentacao restart with 3;

