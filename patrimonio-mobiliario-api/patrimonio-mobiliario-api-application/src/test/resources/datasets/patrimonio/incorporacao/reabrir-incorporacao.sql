insert into pat_mobiliario.tb_reconhecimento (re_id, re_nome, re_execucao_orcamentaria) values
(nextval('pat_mobiliario.seq_reconhecimento'), 'Doação', false);

insert into pat_mobiliario.tb_incorporacao (in_id, in_codigo, in_situacao, in_origem_convenio, in_origem_fundo, re_id, in_dthr_finalizacao, in_dthr_cadastro, in_dthr_alteracao, in_usuario_cadastro, in_usuario_alteracao) values
(20, 2, 'FINALIZADO', false, false, currval('pat_mobiliario.seq_reconhecimento'), now(),'2020-09-15 20:51:08.974954', '2020-09-15 20:51:12.182831', 'admin', 'admin');

insert into pat_mobiliario.tb_movimentacao (mo_id, mo_tipo, mo_situacao, mo_numero_processo) VALUES
(20, 'INCORPORACAO', 'FINALIZADO', '12345');
