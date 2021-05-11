INSERT INTO  comum_siga.tb_conta_contabil  (cc_id,cc_codigo, cc_descricao, cc_situacao) VALUES (1, '123110100', 'MÁQUINAS, APARELHOS, EQUIPAMENTOS E FERRAMENTAS' , 'ATIVO');
INSERT INTO  comum_siga.tb_conta_contabil  (cc_id,cc_codigo, cc_descricao, cc_situacao) VALUES (2, '123110101', 'APARELHOS DE MEDIÇÃO E ORIENTAÇÃO' , 'INATIVO');
INSERT INTO  comum_siga.tb_conta_contabil  (cc_id,cc_codigo, cc_descricao, cc_situacao) VALUES (3, '123110102', 'APARELHOS E EQUIPAMENTOS DE COMUNICAÇÃO' , 'ATIVO');
INSERT INTO  comum_siga.tb_conta_contabil  (cc_id,cc_codigo, cc_descricao, cc_situacao) VALUES (4, '123110103', 'APARELHOS E EQUIPAMENTOS DE COMUNICAÇÃO' , 'INATIVO');
INSERT INTO  comum_siga.tb_conta_contabil  (cc_id,cc_codigo, cc_descricao, cc_situacao) VALUES (5, '123110103', 'APARELHOS E EQUIPAMENTOS DE COMUNICAÇÃO' , 'INATIVO');
INSERT INTO  comum_siga.tb_conta_contabil  (cc_id,cc_codigo, cc_descricao, cc_situacao) VALUES (6, '123110103', 'APARELHOS E EQUIPAMENTOS DE COMUNICAÇÃO' , 'INATIVO');
INSERT INTO  comum_siga.tb_conta_contabil  (cc_id,cc_codigo, cc_descricao, cc_situacao) VALUES (7, '123110103', 'APARELHOS E EQUIPAMENTOS DE COMUNICAÇÃO' , 'INATIVO');
INSERT INTO  comum_siga.tb_conta_contabil  (cc_id,cc_codigo, cc_descricao, cc_situacao) VALUES (8, '123110104', 'APARELHOS E EQUIPAMENTOS DE COMUNICAÇÃO' , 'ATIVO');

INSERT INTO comum.tb_produto (pr_id, pr_nome) VALUES (410, 'patrimonio-mobiliario');
INSERT INTO comum.tb_produto (pr_id, pr_nome) VALUES (1, 'efornecedor');

INSERT INTO comum_siga.tb_conta_contabil_produto (cc_id, pr_id) VALUES (1, 410);
INSERT INTO comum_siga.tb_conta_contabil_produto (cc_id, pr_id) VALUES (2, 410);
INSERT INTO comum_siga.tb_conta_contabil_produto (cc_id, pr_id) VALUES (3, 410);
INSERT INTO comum_siga.tb_conta_contabil_produto (cc_id, pr_id) VALUES (4, 410);
INSERT INTO comum_siga.tb_conta_contabil_produto (cc_id, pr_id) VALUES (5, 410);
INSERT INTO comum_siga.tb_conta_contabil_produto (cc_id, pr_id) VALUES (6, 410);
INSERT INTO comum_siga.tb_conta_contabil_produto (cc_id, pr_id) VALUES (7, 410);
INSERT INTO comum_siga.tb_conta_contabil_produto (cc_id, pr_id) VALUES (8, 1);
