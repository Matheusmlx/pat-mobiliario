-- FORNECEDORES
insert into comum_siga.tb_pessoa (pe_id, pe_nome_razaosocial, pe_cpf_cnpj, pe_logradouro, pe_bairro, pe_numero, pe_cep, pe_situacao, mu_id, pa_id)
values
    (1, 'Google Brasil Internet Ltda', '06990590000123', 'Av. Afonso Pena', 'Centro', '12345', '79032500', 'ATIVO',  4156, 30),
    (2, 'Nu Pagamentos S.A.', '18236120000158', 'Av. Mato Grosso', 'Centro', '538', '79010220', 'ATIVO', 4156, 30),
    (3, 'Jumbitos Comercio de Alimentos Ltda', '01382349000106', 'Rua Bahia', 'São Francisco', '979', '79010180', 'ATIVO', 4156, 30);
alter sequence comum_siga.seq_pessoa restart with 3;

-- ATRIBUTOS DOS FORNECEDORES
insert into comum_siga.tb_pessoa_atributo (pa_id, pa_atributo, pa_tipo_atributo, pa_valor, pe_id)
values
    (nextval('comum_siga.seq_pessoa_atributo'), 'EMAIL', 'EMAIL', 'fornecedor1@azi.com.br', 1),
    (nextval('comum_siga.seq_pessoa_atributo'), 'EMAIL', 'EMAIL', 'fornecedor2@azi.com.br', 2),
    (nextval('comum_siga.seq_pessoa_atributo'), 'EMAIL', 'EMAIL', 'fornecedor3@azi.com.br', 3),

    (nextval('comum_siga.seq_pessoa_atributo'), 'TELEFONE', 'TELEFONE_FIXO_COMERCIAL', '30270001', 1),
    (nextval('comum_siga.seq_pessoa_atributo'), 'TELEFONE', 'TELEFONE_FIXO_COMERCIAL', '30215050', 2),
    (nextval('comum_siga.seq_pessoa_atributo'), 'TELEFONE', 'TELEFONE_FIXO_COMERCIAL', '99186117', 3)
;
-- USUARIOS
insert into comum.tb_usuario (us_id, us_email, us_email_contato, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, us_situacao, id_id, pa_id, us_tipo_usuario)
values
    (nextval('comum.seq_usuario'), 'fornecedor1', 'fornecedor1@azi.com.br', '57708514738', 'Fornecedor Google', false, '33266655', 'ATIVO', 1, 30, 'EXTERNO'),
    (nextval('comum.seq_usuario'), 'fornecedor2', 'fornecedor2@azi.com.br', '90787358215', 'Fornecedor Nu Bank', false, '99814242', 'ATIVO', 1, 30, 'EXTERNO'),
    (nextval('comum.seq_usuario'), 'fornecedor3', 'fornecedor3@azi.com.br', '93548959059', 'Fornecedor Jumbitos', false, '33514332', 'ATIVO', 1, 30, 'EXTERNO')
;

insert into hal.tb_usuario_hal (us_id, us_senha)
values
  ((select us_id from comum.tb_usuario where us_email = 'fornecedor1'), 'UTr6YlcHzuQ=' ),
  ((select us_id from comum.tb_usuario where us_email = 'fornecedor2'), 'UTr6YlcHzuQ=' ),
  ((select us_id from comum.tb_usuario where us_email = 'fornecedor3'), 'UTr6YlcHzuQ=' )
;

-- DOMINIO (LICITANTES X FORNECEDORES DO COMPRAS)
insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id)
values
    -- Vincula licitantes aos fornecedores
    (nextval('hal.seq_dominio'), 'DEFAULT', (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '06990590000123'), (select us_id from comum.tb_usuario where us_email = 'fornecedor1')),
    (nextval('hal.seq_dominio'), 'DEFAULT', (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '18236120000158'), (select us_id from comum.tb_usuario where us_email = 'fornecedor1')),
    (nextval('hal.seq_dominio'), 'DEFAULT', (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '01382349000106'), (select us_id from comum.tb_usuario where us_email = 'fornecedor1')),

    (nextval('hal.seq_dominio'), 'DEFAULT', (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '18236120000158'), (select us_id from comum.tb_usuario where us_email = 'fornecedor2')),
    (nextval('hal.seq_dominio'), 'DEFAULT', (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '01382349000106'), (select us_id from comum.tb_usuario where us_email = 'fornecedor3'))
;

-- DETALHES EFCAZ
insert into efcaz.tb_pessoa_efcaz(pe_id, pe_emissao_cerca, nj_id, po_id)
values
    (1, true, 1, 1),
    (2, true, 1, 1),
    (3, true, 1, 2)
;

-- DOMÍNIO PERFIL FORNECEDORES
insert into hal.tb_dominio_perfil (dm_id, pf_id)
values
    --Vincula dominio DEFAULT ao perfil 'Edital'
    ((select dm_id from hal.tb_dominio where dm_tipo_cliente = 'DEFAULT' and us_id = (select us_id from comum.tb_usuario where us_email = 'fornecedor1') and dm_id_cliente = (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '06990590000123')), (select pf_id from comum.tb_perfil where pf_nome = 'Edital' and pr_id = 1)),
    ((select dm_id from hal.tb_dominio where dm_tipo_cliente = 'DEFAULT' and us_id = (select us_id from comum.tb_usuario where us_email = 'fornecedor1') and dm_id_cliente = (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '18236120000158')), (select pf_id from comum.tb_perfil where pf_nome = 'Edital' and pr_id = 1)),
    ((select dm_id from hal.tb_dominio where dm_tipo_cliente = 'DEFAULT' and us_id = (select us_id from comum.tb_usuario where us_email = 'fornecedor1') and dm_id_cliente = (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '01382349000106')), (select pf_id from comum.tb_perfil where pf_nome = 'Edital' and pr_id = 1)),
    ((select dm_id from hal.tb_dominio where dm_tipo_cliente = 'DEFAULT' and us_id = (select us_id from comum.tb_usuario where us_email = 'fornecedor2')), (select pf_id from comum.tb_perfil where pf_nome = 'Edital' and pr_id = 1)),
    ((select dm_id from hal.tb_dominio where dm_tipo_cliente = 'DEFAULT' and us_id = (select us_id from comum.tb_usuario where us_email = 'fornecedor3')), (select pf_id from comum.tb_perfil where pf_nome = 'Edital' and pr_id = 1))
;


-- Vincula usuário a uma empresa (tb_usuario_fornecedor no Compras).
insert into efcaz.tb_representante (re_id, us_id, pe_id, re_situacao)
values
    (
      nextval('efcaz.seq_representante'),
      (select us_id from comum.tb_usuario where us_email = 'fornecedor1'),
      (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '06990590000123'),
      'ATIVO'
    ),
    (
      nextval('efcaz.seq_representante'),
      (select us_id from comum.tb_usuario where us_email = 'fornecedor1'),
      (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '18236120000158'),
      'ATIVO'
    ),
    (
      nextval('efcaz.seq_representante'),
      (select us_id from comum.tb_usuario where us_email = 'fornecedor1'),
      (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '01382349000106'),
      'ATIVO'
    ),
    (
      nextval('efcaz.seq_representante'),
      (select us_id from comum.tb_usuario where us_email = 'fornecedor2'),
      (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '18236120000158'),
      'ATIVO'
    ),
    (
      nextval('efcaz.seq_representante'),
      (select us_id from comum.tb_usuario where us_email = 'fornecedor3'),
      (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '01382349000106'),
      'ATIVO'
    )
;
