insert into comum_siga.tb_pessoa (pe_id, pe_nome_razaosocial, pe_cpf_cnpj, pe_logradouro, pe_bairro, pe_numero, pe_cep, pe_situacao, mu_id, pa_id)
values
(nextval('comum_siga.seq_pessoa'), 'Google Brasil Internet Ltda', '06990590000123', 'Av. Afonso Pena', 'Centro', '12345', '79032500', 'ATIVO',  4156, 30);

INSERT INTO  comum_siga.tb_conta_contabil  (cc_id,cc_codigo, cc_descricao, cc_situacao, cc_dthr_cadastro, cc_dthr_alteracao, cc_usuario_cadastro, cc_usuario_alteracao)
VALUES (2, '123110200', 'MÁQUINAS, APARELHOS, EQUIPAMENTOS E FERRAMENTAS' , 'ATIVO', '2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null);

INSERT INTO comum_siga.tb_natureza_despesa (nd_id,es_id,nd_despesa,nd_descricao, nd_dthr_cadastro, nd_dthr_alteracao, nd_usuario_cadastro, nd_usuario_alteracao,cc_id, nd_situacao)
VALUES (1,1,'234567','descricao 1', '2020-07-09 10:39:10.242000', '2020-07-10 10:39:10.242000', 'admin', null,2, 'ATIVO');

INSERT INTO pat_mobiliario.tb_incorporacao(in_id, in_codigo, in_dthr_recebimento, in_situacao, in_num_processo, pe_id, in_nota, in_valor_nota)
VALUES (1,'1531315', '2020-05-28T18:58:07.358Z', 'FINALIZADO', '987465', 1, '5', '7.988');

INSERT INTO pat_mobiliario.tb_incorporacao_item (ini_id,ini_valor_total,ini_total_unidades,ini_situacao,ini_numeracao_patrimonial,ec_id,ini_depreciavel,ini_codigo,ini_descricao,cc_id,nd_id,in_id)
VALUES (3,5,2,'EM_ELABORACAO','9999',1,false,'345678','descricao 2',2,1,1);
