insert into comum_siga.tb_pessoa (pe_id, pe_nome_razaosocial, pe_cpf_cnpj, pe_logradouro, pe_bairro, pe_numero, pe_cep, pe_situacao, mu_id, pa_id)
values
    (nextval('comum_siga.seq_pessoa'), 'Google Brasil Internet Ltda', '06990590000123', 'Av. Afonso Pena', 'Centro', '12345', '79032500', 'ATIVO',  4156, 30),
    (nextval('comum_siga.seq_pessoa'), 'Nu Pagamentos S.A.', '18236120000158', 'Av. Mato Grosso', 'Centro', '538', '79010220', 'ATIVO', 4156, 30),
    (nextval('comum_siga.seq_pessoa'), 'Jumbitos Comercio de Alimentos Ltda', '01382349000106', 'Rua Bahia', 'São Francisco', '979', '79010180', 'ATIVO', 4156, 30);


insert into comum.tb_usuario (us_id, us_email, us_email_contato, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, us_situacao, id_id, pa_id, us_tipo_usuario)
values
    (nextval('comum.seq_usuario'), 'admin', 'admin@azi.com.br', '61486745210', 'Administrador', FALSE, '33233232', 'ATIVO', 1, 30, 'INTERNO');

insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia, uo_almoxarifado, mu_id)
values (nextval('comum_siga.seq_unidade_organizacional'), 'Governo', 'GOV', 'ATIVO','INFORMAL', '1', FALSE, 4156);

insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior, mu_id)
values (nextval('comum_siga.seq_unidade_organizacional'), 'Administração', 'ADMIN', 'ATIVO','INFORMAL', '1.1', FALSE,
    (select uo_id from comum_siga.tb_unidade_organizacional where uo_sigla = 'GOV'), 4156);

Insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior, mu_id)
values (nextval('comum_siga.seq_unidade_organizacional'), 'Educação', 'EDUCA', 'ATIVO','INFORMAL', '1.2', FALSE,
    (select uo_id from comum_siga.tb_unidade_organizacional where uo_sigla = 'GOV'), 4156);

insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia, uo_almoxarifado, uo_id_superior, mu_id)
values (nextval('comum_siga.seq_unidade_organizacional'), 'Desenvolvimento', 'DESEN', 'ATIVO','INFORMAL', '1.4', FALSE,
    (select uo_id from comum_siga.tb_unidade_organizacional where uo_sigla = 'GOV'), 4156);

insert into pat_mobiliario.tb_estado_conservacao(ec_id, ec_nome, ec_prioridade) values (1, 'Ótimo', 1);
insert into pat_mobiliario.tb_estado_conservacao(ec_id, ec_nome, ec_prioridade) values (2, 'Bom', 2);
insert into pat_mobiliario.tb_estado_conservacao(ec_id, ec_nome, ec_prioridade) values (3, 'Regular', 3);
insert into pat_mobiliario.tb_estado_conservacao(ec_id, ec_nome, ec_prioridade) values (4, 'Desuso', 4);
insert into pat_mobiliario.tb_estado_conservacao(ec_id, ec_nome, ec_prioridade) values (5, 'Precário', 5);
insert into pat_mobiliario.tb_estado_conservacao(ec_id, ec_nome, ec_prioridade) values (6, 'Obsoleto', 6);
insert into pat_mobiliario.tb_estado_conservacao(ec_id, ec_nome, ec_prioridade) values (7, 'Inservível', 7);
