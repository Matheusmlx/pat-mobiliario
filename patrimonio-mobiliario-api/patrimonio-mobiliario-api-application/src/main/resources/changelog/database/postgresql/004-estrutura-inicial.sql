-- create sequences section -------------------------------------------------

create sequence "pat_mobiliario"."seq_comodante"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_concedente"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_config_conta_contabil"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_convenio"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_documento"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_empenho"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_estado_conservacao"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_incorporacao"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_incorporacao_item"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_lancamento_contabil"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_movimentacao"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_nota_lancto_contabil"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_notificacao"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_patrimonio"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_reconhecimento"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_responsavel"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_config_depreciacao"
    as bigint
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_depreciacao"
    as bigint
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_reserva"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_reserva_intervalo"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

create sequence "pat_mobiliario"."seq_reserva_intervalo_numero"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

-- create tables section -------------------------------------------------

-- table pat_mobiliario.tb_comodante

create table "pat_mobiliario"."tb_comodante"
(
    "com_id" integer not null,
    "com_nome" character varying(255) not null,
    "com_dthr_cadastro" timestamp(6),
    "com_dthr_alteracao" timestamp(6),
    "com_usuario_cadastro" character varying(255),
    "com_usuario_alteracao" character varying(255)
);

comment on table "pat_mobiliario"."tb_comodante" is 'tabela referente aos comodantes';

comment on column "pat_mobiliario"."tb_comodante"."com_id" is 'número de identificação do comodante.';

comment on column "pat_mobiliario"."tb_comodante"."com_nome" is 'nome do comodante.';

comment on column "pat_mobiliario"."tb_comodante"."com_dthr_cadastro" is 'data/hora de criação do registro.';

comment on column "pat_mobiliario"."tb_comodante"."com_dthr_alteracao" is 'data/hora da última alteração do registro.';

comment on column "pat_mobiliario"."tb_comodante"."com_usuario_cadastro" is 'usuário que criou o registro.';

comment on column "pat_mobiliario"."tb_comodante"."com_usuario_alteracao" is 'Último usuário a alterar o registro.';

alter table "pat_mobiliario"."tb_comodante" add constraint "pk_tb_comodante_id" primary key ("com_id");
-- table pat_mobiliario.tb_concedente

create table "pat_mobiliario"."tb_concedente"
(
    "coc_id" integer not null,
    "coc_nome" character varying(100),
    "coc_situacao" character varying(20),
    "coc_tipo" character varying(20),
    "coc_cpf_cnpj" character varying(20),
    "coc_dthr_cadastro" timestamp(6),
    "coc_dthr_alteracao" timestamp(6),
    "coc_usuario_cadastro" character varying(20),
    "coc_usuario_alteracao" character varying(20)
);

comment on table "pat_mobiliario"."tb_concedente" is 'tabela que armazena registros de concedentes.';

comment on column "pat_mobiliario"."tb_concedente"."coc_id" is 'chave primária do concedente.';

comment on column "pat_mobiliario"."tb_concedente"."coc_nome" is 'nome do concedente.';

comment on column "pat_mobiliario"."tb_concedente"."coc_situacao" is 'situação atual do concedente poder ser ativo ou inativo.';

comment on column "pat_mobiliario"."tb_concedente"."coc_tipo" is 'tipo da pessoa concedente pode ser fisica ou juridica.';

comment on column "pat_mobiliario"."tb_concedente"."coc_cpf_cnpj" is 'cpf/cnpj do concedente.';

comment on column "pat_mobiliario"."tb_concedente"."coc_dthr_cadastro" is 'coluna de auditoria data do cadastro.';

comment on column "pat_mobiliario"."tb_concedente"."coc_dthr_alteracao" is 'coluna de auditora data última alteração cadastro.';

comment on column "pat_mobiliario"."tb_concedente"."coc_usuario_cadastro" is 'coluna de auditora usuário que relaizou o cadastro.';

comment on column "pat_mobiliario"."tb_concedente"."coc_usuario_alteracao" is 'coluna de auditoria usuário que realizou última alteração no registro.';

alter table "pat_mobiliario"."tb_concedente" add constraint "pk_tb_concedente" primary key ("coc_id");
-- table pat_mobiliario.tb_config_conta_contabil

create table "pat_mobiliario"."tb_config_conta_contabil"
(
    "ccc_id" integer not null,
    "ccc_metodo" character varying(100),
    "ccc_tipo" character varying(100),
    "ccc_tipo_bem" character varying(100) not null,
    "ccc_vida_util_meses" integer,
    "ccc_percentual_residual" numeric(5,2),
    "cc_id" integer not null,
    "ccc_dthr_cadastro" timestamp(6),
    "ccc_dthr_alteracao" timestamp(6),
    "ccc_usuario_cadastro" character varying(20),
    "ccc_usuario_alteracao" character varying(20)
);

comment on column "pat_mobiliario"."tb_config_conta_contabil"."ccc_id" is 'chave primária de configuração de conta contábil.';

comment on column "pat_mobiliario"."tb_config_conta_contabil"."ccc_metodo" is 'metodo de depreciação da conta. pode ser quotas_constantes.';

comment on column "pat_mobiliario"."tb_config_conta_contabil"."ccc_tipo" is 'tipo da conta. pode ser depreciavel ou nao_depreciavel.';

comment on column "pat_mobiliario"."tb_config_conta_contabil"."ccc_tipo_bem" is 'tipo do patrimônio que deve ser associado a conta contábil.';

comment on column "pat_mobiliario"."tb_config_conta_contabil"."ccc_vida_util_meses" is 'tempo da vida útil da conta contábil em meses.';

comment on column "pat_mobiliario"."tb_config_conta_contabil"."ccc_percentual_residual" is 'percentual residual da conta contábil.';

comment on column "pat_mobiliario"."tb_config_conta_contabil"."cc_id" is 'chave estrangeira para conta contábil.';

comment on column "pat_mobiliario"."tb_config_conta_contabil"."ccc_dthr_cadastro" is 'coluna de auditoria data do cadastro.';

comment on column "pat_mobiliario"."tb_config_conta_contabil"."ccc_dthr_alteracao" is 'coluna de auditoria data  da última alteração no registro.';

comment on column "pat_mobiliario"."tb_config_conta_contabil"."ccc_usuario_cadastro" is 'coluna de auditora usuário autor do cadastro.';

comment on column "pat_mobiliario"."tb_config_conta_contabil"."ccc_usuario_alteracao" is 'coluna de auditoria usuário que realizou a última alteração no registro.';

create index "in_config_cc_cc_id" on "pat_mobiliario"."tb_config_conta_contabil" ("cc_id");
alter table "pat_mobiliario"."tb_config_conta_contabil" add constraint "pk_tb_config_conta_contabil" primary key ("ccc_id");
-- table pat_mobiliario.tb_convenio

create table "pat_mobiliario"."tb_convenio"
(
    "co_id" integer not null,
    "co_dthr_inicio" timestamp,
    "co_dthr_fim" timestamp,
    "co_numero" character varying(100),
    "co_nome" character varying(100),
    "co_situacao" character varying(50),
    "co_fonte_recurso" character varying(500),
    "co_tipo" character varying(50),
    "co_dthr_cadastro" timestamp,
    "co_dthr_alteracao" timestamp,
    "co_usuario_cadastro" character varying(255),
    "co_usuario_alteracao" character varying(255),
    "coc_id" integer
);

comment on table "pat_mobiliario"."tb_convenio" is 'tabela que armazena as informações do convênio.';

comment on column "pat_mobiliario"."tb_convenio"."co_id" is 'código do patrimônio convênio';

comment on column "pat_mobiliario"."tb_convenio"."co_dthr_inicio" is 'data inicio do convênio';

comment on column "pat_mobiliario"."tb_convenio"."co_dthr_fim" is 'data final do convênio';

comment on column "pat_mobiliario"."tb_convenio"."co_numero" is 'número do convênio';

comment on column "pat_mobiliario"."tb_convenio"."co_nome" is 'nome do convênio';

comment on column "pat_mobiliario"."tb_convenio"."co_situacao" is 'situação do convênio';

comment on column "pat_mobiliario"."tb_convenio"."co_fonte_recurso" is 'fonte de recurso do convênio';

comment on column "pat_mobiliario"."tb_convenio"."co_tipo" is 'tipo do convênio';

comment on column "pat_mobiliario"."tb_convenio"."co_dthr_cadastro" is 'campo de auditoria que guarda a data de cadastro ';

comment on column "pat_mobiliario"."tb_convenio"."co_dthr_alteracao" is 'campo de auditoria que guarda a data de edição';

comment on column "pat_mobiliario"."tb_convenio"."co_usuario_cadastro" is 'campo de auditoria que guarda o usuário que realizou o cadastro';

comment on column "pat_mobiliario"."tb_convenio"."co_usuario_alteracao" is 'campo de auditoria que guarda o usuário que realizou a edição';

comment on column "pat_mobiliario"."tb_convenio"."coc_id" is 'chave estrangeira que indica o concedente do convênio.';

create index "in_tb_co_coc_id" on "pat_mobiliario"."tb_convenio" ("coc_id");
alter table "pat_mobiliario"."tb_convenio" add constraint "pk_tb_convenio" primary key ("co_id");
-- table pat_mobiliario.tb_documento

create table "pat_mobiliario"."tb_documento"
(
    "do_id" integer not null,
    "do_numero" character varying(50),
    "do_dt" timestamp(6),
    "do_valor" numeric(20,6),
    "do_uri_anexo" character varying(500),
    "in_id" integer,
    "mo_id" integer,
    "td_id" integer not null,
    "do_dthr_cadastro" timestamp(6),
    "do_dthr_alteracao" timestamp(6),
    "do_usuario_cadastro" character varying(255),
    "do_usuario_alteracao" character varying(255)
);

comment on table "pat_mobiliario"."tb_documento" is 'documentos referentes ao bem.';

comment on column "pat_mobiliario"."tb_documento"."do_numero" is 'número de identificação do documento.';

comment on column "pat_mobiliario"."tb_documento"."do_dt" is 'data do documento.';

comment on column "pat_mobiliario"."tb_documento"."do_valor" is 'valor do dcocumento.';

comment on column "pat_mobiliario"."tb_documento"."do_uri_anexo" is 'caminho para o documento anexado.';

comment on column "pat_mobiliario"."tb_documento"."in_id" is 'chave estrangeira para a incorporação.';

comment on column "pat_mobiliario"."tb_documento"."td_id" is 'chave estrangeira para o tipo de documento.';

comment on column "pat_mobiliario"."tb_documento"."do_dthr_cadastro" is 'data/hora de criação do registro.';

comment on column "pat_mobiliario"."tb_documento"."do_dthr_alteracao" is 'data/hora da última alteração do registro.';

comment on column "pat_mobiliario"."tb_documento"."do_usuario_cadastro" is 'usuário que criou o registro.';

comment on column "pat_mobiliario"."tb_documento"."do_usuario_alteracao" is 'Último usuário a alterar o registro.';

create index "in_do_in_id" on "pat_mobiliario"."tb_documento" ("in_id");
create index "in_do_td_id" on "pat_mobiliario"."tb_documento" ("td_id");
alter table "pat_mobiliario"."tb_documento" add constraint "pk_tb_documento" primary key ("do_id");
-- table pat_mobiliario.tb_empenho

create table "pat_mobiliario"."tb_empenho"
(
    "em_id" integer not null,
    "em_dthr" timestamp,
    "em_valor" numeric(30,6),
    "em_numero" character varying(50),
    "in_id" integer not null,
    "em_dthr_cadastro" timestamp(6),
    "em_dthr_alteracao" timestamp(6),
    "em_usuario_cadastro" character varying(20),
    "em_usuario_alteracao" character varying(20)
);

comment on table "pat_mobiliario"."tb_empenho" is 'tabela que armazena registros de empenhos.';

comment on column "pat_mobiliario"."tb_empenho"."em_id" is 'chave primária.';

comment on column "pat_mobiliario"."tb_empenho"."em_dthr" is 'data/hora do empenho.';

comment on column "pat_mobiliario"."tb_empenho"."em_valor" is 'valor do empenho.';

comment on column "pat_mobiliario"."tb_empenho"."em_numero" is 'número do empenho.';

comment on column "pat_mobiliario"."tb_empenho"."in_id" is 'chave estrangeira para incorporação a qual o empenho pertence.';

comment on column "pat_mobiliario"."tb_empenho"."em_dthr_cadastro" is 'campo de auditoria com data/hora cadastro.';

comment on column "pat_mobiliario"."tb_empenho"."em_dthr_alteracao" is 'campo de auditora com data/hora da última alteração.';

comment on column "pat_mobiliario"."tb_empenho"."em_usuario_cadastro" is 'campo de auditoria que armazena usuário que realizou o cadastro.';

comment on column "pat_mobiliario"."tb_empenho"."em_usuario_alteracao" is 'campo de auditoria que armazena usuário que realizou última alteração.';

create index "in_em_in_id" on "pat_mobiliario"."tb_empenho" ("in_id");
alter table "pat_mobiliario"."tb_empenho" add constraint "pk_tb_empenho" primary key ("em_id");
-- table pat_mobiliario.tb_estado_conservacao

create table "pat_mobiliario"."tb_estado_conservacao"
(
    "ec_id" integer not null,
    "ec_nome" character varying(100) not null,
    "ec_prioridade" integer not null,
    "ec_dthr_cadastro" timestamp(6),
    "ec_dthr_alteracao" timestamp(6),
    "ec_usuario_cadastro" character varying(20),
    "ec_usuario_alteracao" character varying(20)
);

comment on table "pat_mobiliario"."tb_estado_conservacao" is 'tabela com estados de conservação usados pelo item incoporação. deve ter carga inicial.';

comment on column "pat_mobiliario"."tb_estado_conservacao"."ec_id" is 'chave primária.';

comment on column "pat_mobiliario"."tb_estado_conservacao"."ec_nome" is 'nome do estado de conservação. ex.: bom.';

comment on column "pat_mobiliario"."tb_estado_conservacao"."ec_prioridade" is 'escala que identifica o estado de conservação do melhor para o pior.';

comment on column "pat_mobiliario"."tb_estado_conservacao"."ec_dthr_cadastro" is 'campo de auditoria que armazena data/hora do cadastro.';

comment on column "pat_mobiliario"."tb_estado_conservacao"."ec_dthr_alteracao" is 'campo de auditoria que armazena data/hora da última alteração.';

comment on column "pat_mobiliario"."tb_estado_conservacao"."ec_usuario_cadastro" is 'campo de auditoria que armazena usuário que realizou o cadastro.';

comment on column "pat_mobiliario"."tb_estado_conservacao"."ec_usuario_alteracao" is 'campo de auditoria que armazena usuário que realizou última alteração.';

alter table "pat_mobiliario"."tb_estado_conservacao" add constraint "pk_tb_estado_conservacao" primary key ("ec_id");
-- table pat_mobiliario.tb_incorporacao

create table "pat_mobiliario"."tb_incorporacao"
(
    "in_id" integer not null,
    "in_codigo" character varying(20),
    "in_dthr_recebimento" timestamp(6),
    "in_situacao" character varying(50),
    "in_num_processo" character varying(255),
    "in_nota" character varying(30),
    "in_valor_nota" numeric(30,6),
    "in_dthr_nota" timestamp,
    "in_origem_convenio" boolean default false,
    "in_origem_fundo" boolean default false,
    "in_origem_projeto" boolean default false,
    "in_origem_comodato" boolean default false,
    "in_projeto" character varying(100),
    "in_usuario_finalizacao" character varying(255),
    "in_dthr_finalizacao" timestamp(6),
    "uo_id_orgao" integer,
    "uo_id_setor" integer,
    "uo_id_fundo" integer,
    "co_id" integer,
    "re_id" integer,
    "pe_id" integer,
    "nlc_id" integer,
    "com_id" integer,
    "in_dthr_ini_proc" timestamp(6),
    "in_dthr_fim_proc" timestamp(6),
    "in_erro_proc" character varying(255),
    "in_dthr_cadastro" timestamp(6),
    "in_dthr_alteracao" timestamp(6),
    "in_usuario_cadastro" character varying(255),
    "in_usuario_alteracao" character varying(255)
);

comment on table "pat_mobiliario"."tb_incorporacao" is 'armazena as informações de um registro de entrada';

comment on column "pat_mobiliario"."tb_incorporacao"."in_id" is 'chave primária da tabela registro entrada';

comment on column "pat_mobiliario"."tb_incorporacao"."in_codigo" is 'código único que identifica a incorporação.';

comment on column "pat_mobiliario"."tb_incorporacao"."in_dthr_recebimento" is 'data/hora recebimento da incorporação.';

comment on column "pat_mobiliario"."tb_incorporacao"."in_situacao" is 'armazena a situação atual do registro de entrada';

comment on column "pat_mobiliario"."tb_incorporacao"."in_num_processo" is 'armazena o número do proceso';

comment on column "pat_mobiliario"."tb_incorporacao"."in_nota" is 'identificador da nota';

comment on column "pat_mobiliario"."tb_incorporacao"."in_valor_nota" is 'valor da nota';

comment on column "pat_mobiliario"."tb_incorporacao"."in_dthr_nota" is 'data/hora emissão da nota.';

comment on column "pat_mobiliario"."tb_incorporacao"."in_origem_convenio" is 'indica se a incorporação é proveniente de convênio';

comment on column "pat_mobiliario"."tb_incorporacao"."in_origem_fundo" is 'indica se a incorporação é proveniente de fundo';

comment on column "pat_mobiliario"."tb_incorporacao"."in_origem_projeto" is 'indica se a incorporação é proveniente de projeto';

comment on column "pat_mobiliario"."tb_incorporacao"."in_origem_comodato" is 'indica se a incorporação é proveniente de um comodato';

comment on column "pat_mobiliario"."tb_incorporacao"."in_projeto" is 'armazena o projeto de origem';

comment on column "pat_mobiliario"."tb_incorporacao"."in_usuario_finalizacao" is 'armazena o usuário que realizou a finalização da incorporação';

comment on column "pat_mobiliario"."tb_incorporacao"."in_dthr_finalizacao" is 'armazena a data e a hora que a movimentação foi finalizada';

comment on column "pat_mobiliario"."tb_incorporacao"."uo_id_orgao" is 'chave estrangeira que indica o órgão da incoporação.';

comment on column "pat_mobiliario"."tb_incorporacao"."uo_id_setor" is 'chave estrangeira que indica a incorporação ao setor.';

comment on column "pat_mobiliario"."tb_incorporacao"."uo_id_fundo" is 'chave estrangeira que indica fundo da incoporação.';

comment on column "pat_mobiliario"."tb_incorporacao"."co_id" is 'chave estrangeira que indica o convênio da incorporação.';

comment on column "pat_mobiliario"."tb_incorporacao"."re_id" is 'chave estrangeira que indica o tipo de reconhecimento usado na incorporação.';

comment on column "pat_mobiliario"."tb_incorporacao"."pe_id" is 'chave estrangeira que indica o fornecedor da incorporação.';

comment on column "pat_mobiliario"."tb_incorporacao"."nlc_id" is 'chave estrangeira que indica a nota de lançamento contábil da incorporação.';

comment on column "pat_mobiliario"."tb_incorporacao"."com_id" is 'chave estrangeira que indica o comodante responsável pelo comodato.';

comment on column "pat_mobiliario"."tb_incorporacao"."in_dthr_ini_proc" is 'data/hora de início do processamento de finalização da incorporação.';

comment on column "pat_mobiliario"."tb_incorporacao"."in_dthr_fim_proc" is 'data/hora fim do processamento de finalização da incorporação.';

comment on column "pat_mobiliario"."tb_incorporacao"."in_erro_proc" is 'mensagem de erro do processamento de finalização da incorporação.';

comment on column "pat_mobiliario"."tb_incorporacao"."in_dthr_cadastro" is 'campo de auditoria que guarda a data de cadastro ';

comment on column "pat_mobiliario"."tb_incorporacao"."in_dthr_alteracao" is 'campo de auditoria que guarda a data de edição';

comment on column "pat_mobiliario"."tb_incorporacao"."in_usuario_cadastro" is 'campo de auditoria que guarda o usuário que realizou o cadastro';

comment on column "pat_mobiliario"."tb_incorporacao"."in_usuario_alteracao" is 'campo de auditoria que guarda o usuário que realizou a edição';

create index "in_in_uo_id_orgao" on "pat_mobiliario"."tb_incorporacao" ("uo_id_orgao");
create index "in_in_co_id" on "pat_mobiliario"."tb_incorporacao" ("co_id");
create index "in_in_re_id" on "pat_mobiliario"."tb_incorporacao" ("re_id");
create index "in_in_pe_id" on "pat_mobiliario"."tb_incorporacao" ("pe_id");
create index "in_in_uo_id_setor" on "pat_mobiliario"."tb_incorporacao" ("uo_id_setor");
create index "in_in_uo_id_fundo" on "pat_mobiliario"."tb_incorporacao" ("uo_id_fundo");
create index "in_in_nlc_id" on "pat_mobiliario"."tb_incorporacao" ("nlc_id");
create index "in_in_com_id" on "pat_mobiliario"."tb_incorporacao" ("com_id");
alter table "pat_mobiliario"."tb_incorporacao" add constraint "pk_tb_incorporacao" primary key ("in_id");
-- table pat_mobiliario.tb_incorporacao_item

create table "pat_mobiliario"."tb_incorporacao_item"
(
    "ini_id" integer not null,
    "ini_codigo" character varying(100),
    "ini_descricao" text,
    "ini_marca" character varying(500),
    "ini_modelo" character varying(500),
    "ini_fabricante" character varying(500),
    "ini_valor_total" numeric(10,2),
    "ini_total_unidades" integer,
    "ini_tipo" character varying(100),
    "ini_numeracao_patrimonial" character varying(20),
    "ini_uri_imagem" character varying(100),
    "ini_ano_fabricacao_modelo" character varying(50),
    "ini_combustivel" character varying(50),
    "ini_categoria" character varying(50),
    "ini_situacao" character varying(100) not null,
    "ini_depreciavel" boolean default true,
    "in_id" integer not null,
    "cc_id" integer,
    "nd_id" integer,
    "cd_id" integer,
    "ec_id" integer,
    "ini_dthr_cadastro" timestamp(6),
    "ini_dthr_alteracao" timestamp(6),
    "ini_usuario_cadastro" character varying(255),
    "ini_usuario_alteracao" character varying(255)
);

comment on table "pat_mobiliario"."tb_incorporacao_item" is 'tabela que armazena os itens de uma incorporação.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_id" is 'código do registro de entrada do item';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_codigo" is 'código do item proveniente do item selecionado do catálogo.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_descricao" is 'descrição do item proveniente do item selecionado do catálogo.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_marca" is 'marca do item.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_modelo" is 'modelo do item.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_fabricante" is 'fabricante do item.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_valor_total" is 'valor total dos itens';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_total_unidades" is 'quantidade total de itens';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_tipo" is 'tipo do item. pode ser armamento, equipamento, movel ou veiculo.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_numeracao_patrimonial" is 'tipo da numeração patrimonial usada. pode ser automática ou reserva.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_uri_imagem" is 'uri da imagem salva no mongo.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_ano_fabricacao_modelo" is 'ano  e modelo de fabricação do item.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_combustivel" is 'combustível utilizado nos itens do tipo automóveis.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_categoria" is 'categoria do item.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_situacao" is 'situação do item. pode ser em_elaboracao ou finalizado.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_depreciavel" is 'indica se item é depreciável.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."in_id" is 'campo referente a chave estrangeira para a tabela registro entrada';

comment on column "pat_mobiliario"."tb_incorporacao_item"."cc_id" is 'chave estrangeira para conta contábil associada ao item.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."nd_id" is 'chave estrangeira para natureza de despesa associada ao item.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ec_id" is 'chave estrangeira para estado de conservação do item.';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_dthr_cadastro" is 'campo de auditoria que guarda a data de cadastro ';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_dthr_alteracao" is 'campo de auditoria que guarda a data de edição';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_usuario_cadastro" is 'campo de auditoria que guarda o usuário que realizou o cadastro';

comment on column "pat_mobiliario"."tb_incorporacao_item"."ini_usuario_alteracao" is 'campo de auditoria que guarda o usuário que realizou a edição';
comment on column "pat_mobiliario"."tb_incorporacao_item"."in_id" is 'campo referente a chave estrangeira para a tabela registro entrada';
comment on column "pat_mobiliario"."tb_incorporacao_item"."cc_id" is 'chave estrangeira para conta contábil associada ao item.';
comment on column "pat_mobiliario"."tb_incorporacao_item"."cd_id" is 'chave estrangeira para configuracao de depreciação associada ao item.';
comment on column "pat_mobiliario"."tb_incorporacao_item"."nd_id" is 'chave estrangeira para natureza de despesa associada ao item.';
comment on column "pat_mobiliario"."tb_incorporacao_item"."ec_id" is 'chave estrangeira para estado de conservação do item.';

create index "in_ini_in_id" on "pat_mobiliario"."tb_incorporacao_item" ("in_id");
create index "in_ini_cc_id" on "pat_mobiliario"."tb_incorporacao_item" ("cc_id");
create index "in_ini_cd_id" on "pat_mobiliario"."tb_incorporacao_item" ("cd_id");
create index "in_ini_nd_id" on "pat_mobiliario"."tb_incorporacao_item" ("nd_id");
create index "in_ini_ec_id" on "pat_mobiliario"."tb_incorporacao_item" ("ec_id");
alter table "pat_mobiliario"."tb_incorporacao_item" add constraint "pk_tb_incorporacao_item" primary key ("ini_id");
-- table pat_mobiliario.tb_lancamento_contabil

create table "pat_mobiliario"."tb_lancamento_contabil"
(
    "lc_id" bigint not null,
    "lc_tipo_lancamento" character varying(20) not null,
    "lc_data_lancamento" timestamp not null,
    "lc_valor" numeric(30,6) not null,
    "lc_tipo_movimentacao" character varying(50) not null,
    "uo_id_orgao" integer not null,
    "uo_id_setor" integer not null,
    "cc_id" integer not null,
    "pa_id" integer not null,
    "in_id" integer,
    "mo_id" integer,
    "lc_dthr_cadastro" timestamp(6),
    "lc_dthr_alteracao" timestamp(6),
    "lc_usuario_cadastro" character varying(20),
    "lc_usuario_alteracao" character varying(20)
);

comment on table "pat_mobiliario"."tb_lancamento_contabil" is 'tabela responsável por armazenar lançamentos contábeis.';

comment on column "pat_mobiliario"."tb_lancamento_contabil"."lc_id" is 'chave primária de lançamento contábil';

comment on column "pat_mobiliario"."tb_lancamento_contabil"."lc_tipo_lancamento" is 'tipo de lançamento contábil. pode assumir os valores credito ou debito.';

comment on column "pat_mobiliario"."tb_lancamento_contabil"."lc_data_lancamento" is 'campo que irá guardar a data do lançamento contábil, em casos de lançamentos retroativos irá receber a data retroativa';

comment on column "pat_mobiliario"."tb_lancamento_contabil"."lc_valor" is 'valor monetário referente ao lançamento contábil.';

comment on column "pat_mobiliario"."tb_lancamento_contabil"."lc_tipo_movimentacao" is 'tipo da movimentação que deu origem ao lançamento contábil.';

comment on column "pat_mobiliario"."tb_lancamento_contabil"."uo_id_orgao" is 'chave estrangeira para o órgão no qual o lançamento contábil foi efetuado.';

comment on column "pat_mobiliario"."tb_lancamento_contabil"."uo_id_setor" is 'chave estrangeira para o setor no qual o lançamento contábil foi efetuado.';

comment on column "pat_mobiliario"."tb_lancamento_contabil"."cc_id" is 'chave estrangeira para a conta contábil.';

comment on column "pat_mobiliario"."tb_lancamento_contabil"."pa_id" is 'chave estrangeira para o patrimônio.';

comment on column "pat_mobiliario"."tb_lancamento_contabil"."in_id" is 'chave estrangeira para a incorporação.';

comment on column "pat_mobiliario"."tb_lancamento_contabil"."mo_id" is 'chave estrangeira para a movimentação.';

comment on column "pat_mobiliario"."tb_lancamento_contabil"."lc_dthr_cadastro" is 'campo de auditoria que armazena data/hora do cadastro.';

comment on column "pat_mobiliario"."tb_lancamento_contabil"."lc_dthr_alteracao" is 'campo de auditoria que armazena data/hora da última alteração.';

comment on column "pat_mobiliario"."tb_lancamento_contabil"."lc_usuario_cadastro" is 'campo de auditoria que armazena usuário que realizou o cadastro.';

comment on column "pat_mobiliario"."tb_lancamento_contabil"."lc_usuario_alteracao" is 'campo de auditoria que armazena usuário que realizou última alteração.';

create index "in_lc_uo_id_orgao" on "pat_mobiliario"."tb_lancamento_contabil" ("uo_id_orgao");
create index "in_lc_uo_setor" on "pat_mobiliario"."tb_lancamento_contabil" ("uo_id_setor");
create index "in_lc_cc_id" on "pat_mobiliario"."tb_lancamento_contabil" ("cc_id");
create index "in_lc_pa_id" on "pat_mobiliario"."tb_lancamento_contabil" ("pa_id");
create index "in_lc_in_id" on "pat_mobiliario"."tb_lancamento_contabil" ("in_id");
create index "in_lc_mo_id" on "pat_mobiliario"."tb_lancamento_contabil" ("mo_id");
alter table "pat_mobiliario"."tb_lancamento_contabil" add constraint "pk_tb_lancamento_contabil" primary key ("lc_id");
-- table pat_mobiliario.tb_movimentacao

create table "pat_mobiliario"."tb_movimentacao"
(
    "mo_id" integer not null,
    "mo_codigo" character varying(20),
    "mo_tipo" character varying(50),
    "mo_situacao" character varying(50) not null,
    "mo_numero_processo" character varying(100),
    "mo_motivo_obs" character varying(500),
    "mo_usuario_criacao" character varying(20),
    "mo_usuario_finalizacao" character varying(20),
    "mo_dthr_finalizacao" timestamp(6),
    "mo_dthr_devolucao" timestamp(6),
    "mo_dthr_envio" timestamp(6),
    "mo_numero_termo" character varying(20),
    "uo_id_orgao_origem" integer,
    "uo_id_orgao_destino" integer,
    "uo_id_setor_origem" integer,
    "uo_id_setor_destino" integer,
    "nlc_id" integer,
    "res_id" integer,
    "mo_dthr_ini_proc" timestamp(6),
    "mo_dthr_fim_proc" timestamp(6),
    "mo_erro_proc" character varying(255),
    "mo_dthr_cadastro" timestamp(6),
    "mo_dthr_alteracao" timestamp(6),
    "mo_usuario_cadastro" character varying(20),
    "mo_usuario_alteracao" character varying(20)
);

comment on table "pat_mobiliario"."tb_movimentacao" is 'tabela responsável por registrar as movimentações de um patrimônio.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_id" is 'chave primário da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_codigo" is 'código da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_tipo" is 'tipo da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_situacao" is 'possíveis situações que a movimentação pode se encontrar. pode assumir os valores:em_elaboracao,aguardando_recebimento,finalizado ou rejeitado';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_numero_processo" is 'número de processo da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_motivo_obs" is 'motivo/obs da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_usuario_criacao" is 'usuário responsável pela movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_usuario_finalizacao" is 'usuário responsável pela finalização da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_dthr_finalizacao" is 'data e hora de finalização da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_dthr_devolucao" is 'data e hora da devolução da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_dthr_envio" is 'data e hora do envio da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_numero_termo" is 'número do termo de responsabilidade gerado para movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."uo_id_orgao_origem" is 'chave estrangeira para órgão origem da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."uo_id_orgao_destino" is 'chave estrangeira para órgão destino da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."uo_id_setor_origem" is 'chave estrangeira para setor origem da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."uo_id_setor_destino" is 'chave estrangeira para setor destino da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."nlc_id" is 'chave estrangeira para nota de lançamento contábil.';

comment on column "pat_mobiliario"."tb_movimentacao"."res_id" is 'chave estrangeira para o responsável.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_dthr_ini_proc" is 'data/hora de início do processamento de finalização da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_dthr_fim_proc" is 'data/hora fim do processamento de finalização da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_erro_proc" is 'mensagem de erro do processamento de finalização da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_dthr_cadastro" is 'data e hora do cadastro da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_dthr_alteracao" is 'data e hora da alteração da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_usuario_cadastro" is 'usuário responsável pelo cadastro da movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao"."mo_usuario_alteracao" is 'usuário responsável pela alteração da movimentação.';

create index "in_mo_orgao_origem" on "pat_mobiliario"."tb_movimentacao" ("uo_id_orgao_origem");
create index "in_mo_orgao_destino" on "pat_mobiliario"."tb_movimentacao" ("uo_id_orgao_destino");
create index "in_mo_setor_origem" on "pat_mobiliario"."tb_movimentacao" ("uo_id_setor_origem");
create index "in_mo_setor_destino" on "pat_mobiliario"."tb_movimentacao" ("uo_id_setor_destino");
create index "in_mo_nlc_id" on "pat_mobiliario"."tb_movimentacao" ("nlc_id");
create index "in_do_mo_id" on "pat_mobiliario"."tb_movimentacao" ("mo_id");
alter table "pat_mobiliario"."tb_movimentacao" add constraint "pk_tb_movimentacao" primary key ("mo_id");
alter table "pat_mobiliario"."tb_movimentacao" add constraint "tb_movimentacao_mo_numero_termo_key" unique ("mo_numero_termo");
-- table pat_mobiliario.tb_movimentacao_item

create table "pat_mobiliario"."tb_movimentacao_item"
(
    "mo_id" integer not null,
    "pa_id" integer not null,
    "mi_dthr_devolucao" timestamp(6)
);

comment on column "pat_mobiliario"."tb_movimentacao_item"."mo_id" is 'chave estrangeira para a movimentação.';

comment on column "pat_mobiliario"."tb_movimentacao_item"."pa_id" is 'chave estrangeira para o patrimônio.';

comment on column "pat_mobiliario"."tb_movimentacao_item"."mi_dthr_devolucao" is 'data e hora da devolução do patrimônio da movimentação.';

create index "in_moi_mo" on "pat_mobiliario"."tb_movimentacao_item" ("mo_id");
create index "in_moi_pat" on "pat_mobiliario"."tb_movimentacao_item" ("pa_id");
alter table "pat_mobiliario"."tb_movimentacao_item" add constraint "pk_tb_movimentacao_item" primary key ("mo_id","pa_id");
-- table pat_mobiliario.tb_nota_lancamento_contabil

create table "pat_mobiliario"."tb_nota_lancamento_contabil"
(
    "nlc_id" integer not null,
    "nlc_numero" character varying(12),
    "nlc_dthr_lancamento" timestamp(6),
    "nlc_dthr_cadastro" timestamp(6),
    "nlc_dthr_alteracao" timestamp(6),
    "nlc_usuario_cadastro" character varying(255),
    "nlc_usuario_alteracao" character varying(255)
);

comment on table "pat_mobiliario"."tb_nota_lancamento_contabil" is 'armazena as informações de um registro de nota de lançamento contábil';

comment on column "pat_mobiliario"."tb_nota_lancamento_contabil"."nlc_id" is 'chave primária da tabela nota de lançamento';

comment on column "pat_mobiliario"."tb_nota_lancamento_contabil"."nlc_numero" is 'armazena o número da nota de lançamento contábil';

comment on column "pat_mobiliario"."tb_nota_lancamento_contabil"."nlc_dthr_lancamento" is 'data/hora em que foi realizado o lançamento contábil';

comment on column "pat_mobiliario"."tb_nota_lancamento_contabil"."nlc_dthr_cadastro" is 'campo de auditoria que guarda a data de cadastro ';

comment on column "pat_mobiliario"."tb_nota_lancamento_contabil"."nlc_dthr_alteracao" is 'campo de auditoria que guarda a data de edição';

comment on column "pat_mobiliario"."tb_nota_lancamento_contabil"."nlc_usuario_cadastro" is 'campo de auditoria que guarda o usuário que realizou o cadastro';

comment on column "pat_mobiliario"."tb_nota_lancamento_contabil"."nlc_usuario_alteracao" is 'campo de auditoria que guarda o usuário que realizou a edição';

alter table "pat_mobiliario"."tb_nota_lancamento_contabil" add constraint "pk_tb_nota_lancamento_contabil" primary key ("nlc_id");
-- table pat_mobiliario.tb_notificacao

create table "pat_mobiliario"."tb_notificacao"
(
    "no_id" integer not null,
    "no_origem" character varying(50) not null,
    "no_origem_id" integer,
    "no_assunto" character varying(100) not null,
    "no_mensagem" character varying(255) not null,
    "no_dthr_criacao" timestamp(6),
    "no_visualizada" boolean default false,
    "us_id" integer not null,
    "no_dthr_cadastro" timestamp(6),
    "no_dthr_alteracao" timestamp(6),
    "no_usuario_cadastro" character varying(255),
    "no_usuario_alteracao" character varying(255)
);

comment on table "pat_mobiliario"."tb_notificacao" is 'tabela que armazena as notificações para os usuários do sistema.';

comment on column "pat_mobiliario"."tb_notificacao"."no_id" is 'número de identificação da notificação.';

comment on column "pat_mobiliario"."tb_notificacao"."no_origem" is 'origem da notificação, ex.: incorporacao, distribuicao';

comment on column "pat_mobiliario"."tb_notificacao"."no_origem_id" is 'id da origem da notificação';

comment on column "pat_mobiliario"."tb_notificacao"."no_assunto" is 'assunto da notificação';

comment on column "pat_mobiliario"."tb_notificacao"."no_mensagem" is 'mensagem da notificação';

comment on column "pat_mobiliario"."tb_notificacao"."no_dthr_criacao" is 'data/hora da criação da notificação';

comment on column "pat_mobiliario"."tb_notificacao"."no_visualizada" is 'armazena se a notificação foi visualizada';

comment on column "pat_mobiliario"."tb_notificacao"."us_id" is 'armazena o usuário que receberá a notificação';

comment on column "pat_mobiliario"."tb_notificacao"."no_dthr_cadastro" is 'data/hora de criação do registro.';

comment on column "pat_mobiliario"."tb_notificacao"."no_dthr_alteracao" is 'data/hora da última alteração do registro.';

comment on column "pat_mobiliario"."tb_notificacao"."no_usuario_cadastro" is 'usuário que criou o registro.';

comment on column "pat_mobiliario"."tb_notificacao"."no_usuario_alteracao" is 'usuário que alterou o registro.';

create index "in_notificacao_usuario" on "pat_mobiliario"."tb_notificacao" ("us_id");
alter table "pat_mobiliario"."tb_notificacao" add constraint "pk_tb_notificacao" primary key ("no_id");
-- table pat_mobiliario.tb_patrimonio

create table "pat_mobiliario"."tb_patrimonio"
(
    "pa_id" bigint not null,
    "pa_placa" character varying(20),
    "pa_renavam" character varying(20),
    "pa_uri_imagem" character varying(100),
    "pa_licenciamento" character varying(20),
    "pa_motor" character varying(20),
    "pa_chassi" character varying(20),
    "pa_numero" character varying(30) not null,
    "pa_numero_serie" character varying(255),
    "pa_valor_liquido" numeric(14,3),
    "pa_valor_residual" numeric(14,3),
    "pa_valor_aquisicao" numeric(14,3) not null,
    "pa_valor_depreciacao_mensal" numeric(14,3),
    "pa_valor_entrada" numeric(14,3),
    "pa_diferenca_dizima" boolean,
    "pa_dthr_inicio_vida_util" timestamp,
    "pa_dthr_fim_vida_util" timestamp,
    "pa_situacao" character varying(20),
    "pa_depreciavel" boolean,
    "pa_motivo_estorno" character varying(500),
    "pa_projeto" character varying(100),
    "pa_dthr_estorno" timestamp,
    "pa_usuario_estorno" character varying(255),
    "uo_id_orgao" integer,
    "uo_id_setor" integer,
    "uo_id_fundo" integer,
    "co_id" integer,
    "nd_id" integer,
    "ec_id" integer,
    "cc_id_classificacao" integer,
    "cc_id_atual" integer,
    "ini_id" integer not null,
    "com_id" integer,
    "pa_dthr_cadastro" timestamp(6),
    "pa_dthr_alteracao" timestamp(6),
    "pa_usuario_cadastro" character varying(255),
    "pa_usuario_alteracao" character varying(255)
);

comment on table "pat_mobiliario"."tb_patrimonio" is 'tabela que armazena as informações de um patrimônio';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_id" is 'identificador do patrimônio';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_placa" is 'número da placa para patrimônios do tipo veículo.';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_renavam" is 'identificação do registro nacional de veículos automotores para patrimônios do tipo veículo.';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_uri_imagem" is 'imagem do patrimônio.';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_licenciamento" is 'identificação de licenciamento para patrimônios do tipo veículo.';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_motor" is 'motor para patrimônios do tipo veículo.';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_chassi" is 'número de chassi para patrimônios do tipo veículo.';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_numero" is 'número do patrimônio';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_numero_serie" is 'número de sério do patrimônio';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_valor_liquido" is 'valor líquido do patrimônio.';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_valor_residual" is 'valor residual do patrimônio.';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_valor_aquisicao" is 'valor de aquisição do patrimônio.';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_valor_entrada" is 'campo que armazena o valor de entrada de um patrimonio';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_diferenca_dizima" is 'campo que indica se valor de aquisição possui arredondamento de dizimas.';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_dthr_inicio_vida_util" is 'data inicial da vida útil';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_dthr_fim_vida_util" is 'data final da vida útil';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_situacao" is 'indica a situação do patrimônio.';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_depreciavel" is 'indica se o patrimônio não é depreciável';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_motivo_estorno" is 'armazena o motivo do estorno';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_projeto" is 'armazena o nome do projeto de origem da incorporação';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_dthr_estorno" is 'data/hora em que o patrimônio foi estornado.';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_usuario_estorno" is 'armazena o nome do usuario que estornou';

comment on column "pat_mobiliario"."tb_patrimonio"."uo_id_orgao" is 'chave estrangeira que indica o órgao ao qual o patrimônio pertence.';

comment on column "pat_mobiliario"."tb_patrimonio"."uo_id_setor" is 'chave que indica o setor ao qual o patrimônio pertence.';

comment on column "pat_mobiliario"."tb_patrimonio"."uo_id_fundo" is 'chave estrangeira que indica fundo do patrimônio.';

comment on column "pat_mobiliario"."tb_patrimonio"."co_id" is 'chave estrangeira que indica o convênio do patrimonio.';

comment on column "pat_mobiliario"."tb_patrimonio"."nd_id" is 'chave estrangeira para a natureza de despesa';

comment on column "pat_mobiliario"."tb_patrimonio"."ec_id" is 'chave estrangeira para o estado de conservação do item.';

comment on column "pat_mobiliario"."tb_patrimonio"."cc_id_classificacao" is 'código da conta contábil de classificação';

comment on column "pat_mobiliario"."tb_patrimonio"."cc_id_atual" is 'código da conta contábil atual';

comment on column "pat_mobiliario"."tb_patrimonio"."ini_id" is 'chave estrangeira para item incorporação do patrimônio.';

comment on column "pat_mobiliario"."tb_patrimonio"."com_id" is 'chave estrangeira que indica o comodante responsável pelo comodato.';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_dthr_cadastro" is 'campo de auditoria que armazena data/hora em que o cadastro foi realizado.';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_dthr_alteracao" is 'campo de auditoria que guarda a data de edição';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_usuario_cadastro" is 'campo de auditoria que guarda o usuário que realizou o cadastro';

comment on column "pat_mobiliario"."tb_patrimonio"."pa_usuario_alteracao" is 'campo de auditoria que guarda o usuário que realizou a edição';

create index "in_pa_num_patrimonio" on "pat_mobiliario"."tb_patrimonio" ("pa_numero");
create index "in_pa_cc_id_classificacao" on "pat_mobiliario"."tb_patrimonio" ("cc_id_classificacao");
create index "in_pa_cc_id_atual" on "pat_mobiliario"."tb_patrimonio" ("cc_id_atual");
create index "in_pa_ini_id" on "pat_mobiliario"."tb_patrimonio" ("ini_id");
create index "in_pa_uo_id_orgao" on "pat_mobiliario"."tb_patrimonio" ("uo_id_orgao");
create index "in_pa_uo_id_setor" on "pat_mobiliario"."tb_patrimonio" ("uo_id_setor");
create index "in_pa_uo_id_fundo" on "pat_mobiliario"."tb_patrimonio" ("uo_id_fundo");
create index "in_pa_co_id" on "pat_mobiliario"."tb_patrimonio" ("co_id");
create index "in_pa_nd_id" on "pat_mobiliario"."tb_patrimonio" ("nd_id");
create index "in_pa_ec_id" on "pat_mobiliario"."tb_patrimonio" ("ec_id");
create index "in_pa_com_id" on "pat_mobiliario"."tb_patrimonio" ("com_id");
alter table "pat_mobiliario"."tb_patrimonio" add constraint "pk_patrimonio" primary key ("pa_id");
-- table pat_mobiliario.tb_reconhecimento

create table "pat_mobiliario"."tb_reconhecimento"
(
    "re_id" integer not null,
    "re_nome" character varying(100),
    "re_execucao_orcamentaria" boolean,
    "re_nota_fical" boolean,
    "re_empenho" boolean,
    "re_situacao" character varying(100),
    "re_dthr_alteracao" timestamp(6),
    "re_dthr_cadastro" timestamp(6),
    "re_usuario_cadastro" character varying(255),
    "re_usuario_alteracao" character varying(255)
);

comment on table "pat_mobiliario"."tb_reconhecimento" is 'tabela que armazena os registros de reconhecimento.';

comment on column "pat_mobiliario"."tb_reconhecimento"."re_id" is 'chave primária de reconhecimento';

comment on column "pat_mobiliario"."tb_reconhecimento"."re_nome" is 'nome do reconhecimento';

comment on column "pat_mobiliario"."tb_reconhecimento"."re_execucao_orcamentaria" is 'campo booleano que indica se reconhecimento é do tipo de execução orçamentária';

comment on column "pat_mobiliario"."tb_reconhecimento"."re_nota_fical" is 'campo que indica obrigatoriedade da nota fiscal na incorporação.';

comment on column "pat_mobiliario"."tb_reconhecimento"."re_empenho" is 'campo que indica a obrigatoriedade de empenho na incorporação.';

comment on column "pat_mobiliario"."tb_reconhecimento"."re_situacao" is 'situação de reconhecimento, pode assumir o valor ativo ou inativo';

comment on column "pat_mobiliario"."tb_reconhecimento"."re_dthr_alteracao" is 'campo de auditoria que guarda data/hora da última alteração';

comment on column "pat_mobiliario"."tb_reconhecimento"."re_dthr_cadastro" is 'campo de auditoria que guarda data/hora do cadastro';

comment on column "pat_mobiliario"."tb_reconhecimento"."re_usuario_cadastro" is 'campo de auditoria que guarda usuário que realizou o cadastro';

comment on column "pat_mobiliario"."tb_reconhecimento"."re_usuario_alteracao" is 'campo de auditoria que guarda usuário que realizou última alteração';

alter table "pat_mobiliario"."tb_reconhecimento" add constraint "pk_tb_reconhecimento" primary key ("re_id");
-- table pat_mobiliario.tb_responsavel

create table "pat_mobiliario"."tb_responsavel"
(
    "res_id" integer not null,
    "res_nome" character varying(255) not null,
    "res_dthr_cadastro" timestamp(6),
    "res_dthr_alteracao" timestamp(6),
    "res_usuario_cadastro" character varying(255),
    "res_usuario_alteracao" character varying(255)
);

comment on table "pat_mobiliario"."tb_responsavel" is 'tabela referente ao responsável da movimentação';

comment on column "pat_mobiliario"."tb_responsavel"."res_id" is 'número de identificação do responsável.';

comment on column "pat_mobiliario"."tb_responsavel"."res_nome" is 'nome do responsável.';

comment on column "pat_mobiliario"."tb_responsavel"."res_dthr_cadastro" is 'data/hora de criação do registro.';

comment on column "pat_mobiliario"."tb_responsavel"."res_dthr_alteracao" is 'data/hora da última alteração do registro.';

comment on column "pat_mobiliario"."tb_responsavel"."res_usuario_cadastro" is 'usuário que criou o registro.';

comment on column "pat_mobiliario"."tb_responsavel"."res_usuario_alteracao" is 'Último usuário a alterar o registro.';

alter table "pat_mobiliario"."tb_responsavel" add constraint "pk_tb_responsavel_id" primary key ("res_id");
---- table pat_mobiliario_tb_config_depreciacao ---------------------

create table pat_mobiliario.tb_config_depreciacao (
  "cd_id" integer not null,
  "cd_vida_util_meses" integer,
  "cd_percentual_residual" numeric(5,2),
  "cc_id" integer not null,
  "cd_depreciavel" boolean not null,
  "cd_dthr_cadastro" timestamp(9),
  "cd_dthr_alteracao" timestamp(9),
  "cd_usuario_cadastro" character varying(20),
  "cd_usuario_alteracao" character varying(20)
);

comment on table "pat_mobiliario"."tb_config_depreciacao" is 'Tabela que serve como historico para armazenar as config das contas contabeis.';
comment on column "pat_mobiliario"."tb_config_depreciacao"."cd_id" is 'chave primária da configuração da depreciação.';
comment on column "pat_mobiliario"."tb_config_depreciacao"."cd_vida_util_meses" is 'tempo da vida útil da conta contábil em meses.';
comment on column "pat_mobiliario"."tb_config_depreciacao"."cd_percentual_residual" is 'percentual residual da conta contábil.';
comment on column "pat_mobiliario"."tb_config_depreciacao"."cc_id" is 'chave estrangeira para conta contábil.';
comment on column "pat_mobiliario"."tb_config_depreciacao"."cd_depreciavel" is 'Armazena se o campo é depreciavel ou não.';
comment on column "pat_mobiliario"."tb_config_depreciacao"."cd_dthr_cadastro" is 'coluna de auditoria data do cadastro.';
comment on column "pat_mobiliario"."tb_config_depreciacao"."cd_dthr_alteracao" is 'coluna de auditoria data  da última alteração no registro.';
comment on column "pat_mobiliario"."tb_config_depreciacao"."cd_usuario_cadastro" is 'coluna de auditora usuário autor do cadastro.';
comment on column "pat_mobiliario"."tb_config_depreciacao"."cd_usuario_alteracao" is 'coluna de auditoria usuário que realizou a última alteração no registro.';

create index "in_cd_id" on "pat_mobiliario"."tb_config_depreciacao" ("cc_id");
alter table "pat_mobiliario"."tb_config_depreciacao" add constraint "pk_tb_config_depreciacao" primary key ("cd_id");

-- table pat_mobiliario.tb_depreciacao

create table "pat_mobiliario"."tb_depreciacao"
(
    "de_id" integer not null,
    "de_valor_anterior" numeric(14,3) not null,
    "de_valor_subtraido" numeric(14,3) not null,
    "de_valor_posterior" numeric(14,3) not null,
    "de_taxa_aplicada" numeric(5,3) not null,
    "de_dthr_inicial" timestamp(9) not null,
    "de_dthr_final" timestamp(9) not null,
    "de_mes_referencia" character varying(7) not null,
    "pa_id" integer,
    "uo_id_orgao" integer,
    "uo_id_setor" integer,
    "cc_id" integer,
    "de_dthr_cadastro" timestamp(6),
    "de_dthr_alteracao" timestamp(6),
    "de_usuario_cadastro" character varying(255),
    "de_usuario_alteracao" character varying(255)
);

comment on column "pat_mobiliario"."tb_depreciacao"."de_valor_anterior" is 'valor liquido anterior a depreciação.';

comment on column "pat_mobiliario"."tb_depreciacao"."de_valor_subtraido" is 'valor subtraido referente a depreciação mensal.';

comment on column "pat_mobiliario"."tb_depreciacao"."de_valor_posterior" is 'valor liquido após a depreciação mensal.';

comment on column "pat_mobiliario"."tb_depreciacao"."de_taxa_aplicada" is 'taxa de depreciação aplicada.';

comment on column "pat_mobiliario"."tb_depreciacao"."de_dthr_inicial" is 'data referente ao início do período depreciado no mês.';

comment on column "pat_mobiliario"."tb_depreciacao"."de_dthr_final" is 'data referente ao fim do período depreciado no mês.';

comment on column "pat_mobiliario"."tb_depreciacao"."de_mes_referencia" is 'mês/ano de referência para a depreciação. este mês deve ser idêntico ao mês contido nos campos de_dthr_inicio  e  de_dthr_fim';

comment on column "pat_mobiliario"."tb_depreciacao"."pa_id" is 'chave estrangeira para patrimônio depreciado.';

comment on column "pat_mobiliario"."tb_depreciacao"."uo_id_orgao" is 'chave estrangeira para órgão referente a depreciação.';

comment on column "pat_mobiliario"."tb_depreciacao"."uo_id_setor" is 'chave estrangeira para o setor em que o patrimônio está localizado no momento da depreciação.';

comment on column "pat_mobiliario"."tb_depreciacao"."cc_id" is 'chave estrangeira para conta contábil referente a depreciação.';

comment on column "pat_mobiliario"."tb_depreciacao"."de_dthr_cadastro" is 'campo de auditoria que guarda data/hora do cadastro.';

comment on column "pat_mobiliario"."tb_depreciacao"."de_dthr_alteracao" is 'campo de auditoria que guarda data/hora da última alteração.';

comment on column "pat_mobiliario"."tb_depreciacao"."de_usuario_cadastro" is 'campo de auditoria que guarda usuário que realizou o cadastro.';

comment on column "pat_mobiliario"."tb_depreciacao"."de_usuario_alteracao" is 'campo de auditoria que guarda usuário que realizou última alteração.';

create index "in_depreciacao_pa_id" on "pat_mobiliario"."tb_depreciacao" ("pa_id");
create index "in_depreciacao_uo_id_orgao" on "pat_mobiliario"."tb_depreciacao" ("uo_id_orgao");
create index "in_depreciacao_uo_id_setor" on "pat_mobiliario"."tb_depreciacao" ("uo_id_setor");
create index "in_depreciacao_cc_id" on "pat_mobiliario"."tb_depreciacao" ("cc_id");
alter table "pat_mobiliario"."tb_depreciacao" add constraint "pk_tb_depreciacao" primary key ("de_id");

-- table pat_mobiliario.tb_reserva

create table "pat_mobiliario"."tb_reserva"
(
    "rv_id" bigint not null,
    "rv_codigo" character varying(10) not null,
    "rv_situacao" character varying(20) not null,
    "rv_preenchimento" character varying(10) not null,
    "rv_dthr_criacao" Timestamp(9) not null,
    "rv_qtd_reservada" bigint,
    "rv_qtd_restante" bigint,
    "rv_nro_inicio" bigint,
    "rv_nro_fim" bigint,
    "rv_dthr_cadastro" Timestamp(9),
    "rv_dthr_alteracao" Timestamp(9),
    "rv_usuario_cadastro" Character varying(255),
    "rv_usuario_alteracao" Character varying(255)
);

comment on column "pat_mobiliario"."tb_reserva"."rv_id" is 'id da tabela reserva.';

comment on column "pat_mobiliario"."tb_reserva"."rv_codigo" is 'código da reserva.';

comment on column "pat_mobiliario"."tb_reserva"."rv_situacao" is 'situação da reserva.';

comment on column "pat_mobiliario"."tb_reserva"."rv_preenchimento" is 'tipo de preenchimento automático ou manual.';

comment on column "pat_mobiliario"."tb_reserva"."rv_dthr_criacao" is 'Data/Hora de criação do registro.';

comment on column "pat_mobiliario"."tb_reserva"."rv_qtd_reservada" is 'quantidade de números reservado.';

comment on column "pat_mobiliario"."tb_reserva"."rv_qtd_restante" is 'quantidade de números restantes.';

comment on column "pat_mobiliario"."tb_reserva"."rv_nro_inicio" is 'número de início da reserva.';

comment on column "pat_mobiliario"."tb_reserva"."rv_nro_fim" is 'número final da reserva.';

comment on column "pat_mobiliario"."tb_reserva"."rv_dthr_cadastro" is 'Data/Hora de criação do registro.';

comment on column "pat_mobiliario"."tb_reserva"."rv_dthr_alteracao" is 'Data/Hora da última alteração do registro.';

comment on column "pat_mobiliario"."tb_reserva"."rv_usuario_cadastro" is 'Usuário que criou o registro.';

comment on column "pat_mobiliario"."tb_reserva"."rv_usuario_alteracao" is 'Último usuário a alterar o registro.';

alter table "pat_mobiliario"."tb_reserva" add constraint "pk_tb_reserva" primary key ("rv_id");

-- table pat_mobiliario.tb_reserva_intervalo

create table "pat_mobiliario"."tb_reserva_intervalo"
(
    "ri_id" bigint not null,
    "rv_id" bigint not null,
    "ri_codigo" character varying(10),
    "ri_situacao" character varying(20),
    "ri_preenchimento" character varying(10),
    "uo_id_orgao" bigint,
    "ri_qtd_reservada" bigint,
    "ri_nro_inicio" bigint,
    "ri_nro_fim" bigint,
    "ri_nro_termo" character varying(20),
    "ri_dthr_finalizacao" Timestamp(9),
    "ri_dthr_cadastro" Timestamp(9),
    "ri_dthr_alteracao" Timestamp(9),
    "ri_usuario_cadastro" Character varying(255),
    "ri_usuario_alteracao" Character varying(255)
);

comment on column "pat_mobiliario"."tb_reserva_intervalo"."ri_id" is 'id da tabela reserva intervalo.';

comment on column "pat_mobiliario"."tb_reserva_intervalo"."rv_id" is 'chave estrangeira para a reserva.';

comment on column "pat_mobiliario"."tb_reserva_intervalo"."ri_codigo" is 'código do intervalo da reserva.';

comment on column "pat_mobiliario"."tb_reserva_intervalo"."ri_situacao" is 'situação do intervalo da reserva.';

comment on column "pat_mobiliario"."tb_reserva_intervalo"."ri_preenchimento" is 'tipo de preenchimento automático ou manual.';

comment on column "pat_mobiliario"."tb_reserva_intervalo"."uo_id_orgao" is 'chave estrangeira para o orgão a qual a reserva pertence.';

comment on column "pat_mobiliario"."tb_reserva_intervalo"."ri_qtd_reservada" is 'quantidade de números reservado,';

comment on column "pat_mobiliario"."tb_reserva_intervalo"."ri_nro_inicio" is 'número de início da reserva.';

comment on column "pat_mobiliario"."tb_reserva_intervalo"."ri_nro_fim" is 'número final da reserva.';

comment on column "pat_mobiliario"."tb_reserva_intervalo"."ri_nro_termo" is 'número do termo do intervalo da reserva.';

comment on column "pat_mobiliario"."tb_reserva_intervalo"."ri_dthr_cadastro" is 'Data/Hora de criação do registro.';

comment on column "pat_mobiliario"."tb_reserva_intervalo"."ri_dthr_alteracao" is 'Data/Hora da última alteração do registro.';

comment on column "pat_mobiliario"."tb_reserva_intervalo"."ri_usuario_cadastro" is 'Usuário que criou o registro.';

comment on column "pat_mobiliario"."tb_reserva_intervalo"."ri_usuario_alteracao" is 'Último usuário a alterar o registro.';

create index "in_ri_rv_id" on "pat_mobiliario"."tb_reserva_intervalo" ("rv_id");

create index "in_ri_uo_id_orgao" on "pat_mobiliario"."tb_reserva_intervalo" ("uo_id_orgao");

alter table "pat_mobiliario"."tb_reserva_intervalo" add constraint "pk_tb_reserva_intervalo" primary key ("ri_id");

-- table pat_mobiliario.tb_reserva_intervalo_numero

create table "pat_mobiliario"."tb_reserva_intervalo_numero"
(
    "rin_id" bigint not null,
    "ri_id" bigint not null,
    "rin_numero" bigint,
    "rin_utilizado" boolean default false,
    "rin_dthr_cadastro" Timestamp(9),
    "rin_dthr_alteracao" Timestamp(9),
    "rin_usuario_cadastro" Character varying(255),
    "rin_usuario_alteracao" Character varying(255)
);

comment on column "pat_mobiliario"."tb_reserva_intervalo_numero"."rin_id" is 'id da tabela reserva intervalo número.';

comment on column "pat_mobiliario"."tb_reserva_intervalo_numero"."ri_id" is 'chave estrangeira para a reserva intervalo a qual o número pertence.';

comment on column "pat_mobiliario"."tb_reserva_intervalo_numero"."rin_numero" is 'número da tabela.';

comment on column "pat_mobiliario"."tb_reserva_intervalo_numero"."rin_utilizado" is 'valor booleano para indicar se o número foi utilizado ou não.';

comment on column "pat_mobiliario"."tb_reserva_intervalo_numero"."rin_dthr_cadastro" is 'Data/Hora de criação do registro.';

comment on column "pat_mobiliario"."tb_reserva_intervalo_numero"."rin_dthr_alteracao" is 'Data/Hora da última alteração do registro.';

comment on column "pat_mobiliario"."tb_reserva_intervalo_numero"."rin_usuario_cadastro" is 'Usuário que criou o registro.';

comment on column "pat_mobiliario"."tb_reserva_intervalo_numero"."rin_usuario_alteracao" is 'Último usuário a alterar o registro.';

create index "in_reserva_intervalo_reserva_intervalo_numero" on "pat_mobiliario"."tb_reserva_intervalo_numero" ("ri_id");

alter table "pat_mobiliario"."tb_reserva_intervalo_numero" add constraint "pk_tb_reserva_intervalo_numero" primary key ("rin_id");

-- create foreign keys (relationships) section -------------------------------------------------

alter table "pat_mobiliario"."tb_convenio" add constraint "fk_concedente_convenio" foreign key ("coc_id") references "pat_mobiliario"."tb_concedente" ("coc_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_empenho" add constraint "fk_empenho_incorporacao" foreign key ("in_id") references "pat_mobiliario"."tb_incorporacao" ("in_id") on delete cascade on update no action;

alter table "pat_mobiliario"."tb_config_conta_contabil" add constraint "fk_config_conta_contabil" foreign key ("cc_id") references "comum_siga"."tb_conta_contabil" ("cc_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_incorporacao" add constraint "fk_incoporacao_fundo" foreign key ("uo_id_fundo") references "comum_siga"."tb_unidade_organizacional" ("uo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_incorporacao" add constraint "fk_incoporacao_setor" foreign key ("uo_id_setor") references "comum_siga"."tb_unidade_organizacional" ("uo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_incorporacao" add constraint "fk_incorporacao_orgao" foreign key ("uo_id_orgao") references "comum_siga"."tb_unidade_organizacional" ("uo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_incorporacao" add constraint "fk_incoporacao_pessoa" foreign key ("pe_id") references "comum_siga"."tb_pessoa" ("pe_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_incorporacao" add constraint "fk_incorporacao_convenio" foreign key ("co_id") references "pat_mobiliario"."tb_convenio" ("co_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_incorporacao" add constraint "fk_incorporacao_reconhecimento" foreign key ("re_id") references "pat_mobiliario"."tb_reconhecimento" ("re_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_incorporacao" add constraint "fk_incorporacao_nota_lancto_contabil" foreign key ("nlc_id") references "pat_mobiliario"."tb_nota_lancamento_contabil" ("nlc_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_incorporacao" add constraint "fk_incorporacao_comodante" foreign key ("com_id") references "pat_mobiliario"."tb_comodante" ("com_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_incorporacao_item" add constraint "fk_item_inc_natureza_despesa" foreign key ("nd_id") references "comum_siga"."tb_natureza_despesa" ("nd_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_incorporacao_item" add constraint "fk_item_inc_conta_contabil" foreign key ("cc_id") references "comum_siga"."tb_conta_contabil" ("cc_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_incorporacao_item" add constraint "fk_item_inc_estado_conserv" foreign key ("ec_id") references "pat_mobiliario"."tb_estado_conservacao" ("ec_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_incorporacao_item" add constraint "fk_incorporacao_item_incorp" foreign key ("in_id") references "pat_mobiliario"."tb_incorporacao" ("in_id") on delete cascade on update no action;

alter table "pat_mobiliario"."tb_patrimonio" add constraint "fk_pat_fundo" foreign key ("uo_id_fundo") references "comum_siga"."tb_unidade_organizacional" ("uo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_patrimonio" add constraint "fk_pat_orgao" foreign key ("uo_id_orgao") references "comum_siga"."tb_unidade_organizacional" ("uo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_patrimonio" add constraint "fk_pat_setor" foreign key ("uo_id_setor") references "comum_siga"."tb_unidade_organizacional" ("uo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_patrimonio" add constraint "fk_pat_natureza_despesa" foreign key ("nd_id") references "comum_siga"."tb_natureza_despesa" ("nd_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_patrimonio" add constraint "fk_patrimonio_conta_contabil_atual" foreign key ("cc_id_atual") references "comum_siga"."tb_conta_contabil" ("cc_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_patrimonio" add constraint "fk_patrimonio_conta_contabil_classificacao" foreign key ("cc_id_classificacao") references "comum_siga"."tb_conta_contabil" ("cc_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_patrimonio" add constraint "fk_patrimonio_convenio" foreign key ("co_id") references "pat_mobiliario"."tb_convenio" ("co_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_patrimonio" add constraint "fk_patrimonio_estado_conserv" foreign key ("ec_id") references "pat_mobiliario"."tb_estado_conservacao" ("ec_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_patrimonio" add constraint "fk_patrimonio_inc_item" foreign key ("ini_id") references "pat_mobiliario"."tb_incorporacao_item" ("ini_id") on delete cascade on update no action;

alter table "pat_mobiliario"."tb_patrimonio" add constraint "fk_patrimonio_comodante" foreign key ("com_id") references "pat_mobiliario"."tb_comodante" ("com_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_movimentacao" add constraint "fk_movimentacao_orgao_destino" foreign key ("uo_id_orgao_destino") references "comum_siga"."tb_unidade_organizacional" ("uo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_movimentacao" add constraint "fk_movimentacao_orgao_origem" foreign key ("uo_id_orgao_origem") references "comum_siga"."tb_unidade_organizacional" ("uo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_movimentacao" add constraint "fk_movimentacao_setor_destino" foreign key ("uo_id_setor_destino") references "comum_siga"."tb_unidade_organizacional" ("uo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_movimentacao" add constraint "fk_movimentacao_setor_origem" foreign key ("uo_id_setor_origem") references "comum_siga"."tb_unidade_organizacional" ("uo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_incorporacao_item" add constraint "fk_item_inc_config_depreciacao" foreign key ("cd_id") references "pat_mobiliario"."tb_config_depreciacao" ("cd_id") on delete set null;

alter table "pat_mobiliario"."tb_movimentacao" add constraint "fk_movimentacao_nl" foreign key ("nlc_id") references "pat_mobiliario"."tb_nota_lancamento_contabil" ("nlc_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_movimentacao" add constraint "fk_movimentacao_responsavel" foreign key ("res_id") references "pat_mobiliario"."tb_responsavel" ("res_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_movimentacao_item" add constraint "fk_movimentacao_item_patrimonio" foreign key ("pa_id") references "pat_mobiliario"."tb_patrimonio" ("pa_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_movimentacao_item" add constraint "fk_movimentacao_item_movimentacao" foreign key ("mo_id") references "pat_mobiliario"."tb_movimentacao" ("mo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_lancamento_contabil" add constraint "fk_lc_orgao" foreign key ("uo_id_orgao") references "comum_siga"."tb_unidade_organizacional" ("uo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_lancamento_contabil" add constraint "fk_lc_setor" foreign key ("uo_id_setor") references "comum_siga"."tb_unidade_organizacional" ("uo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_lancamento_contabil" add constraint "fk_lc_conta_contabil" foreign key ("cc_id") references "comum_siga"."tb_conta_contabil" ("cc_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_lancamento_contabil" add constraint "fk_lc_incorporacao" foreign key ("in_id") references "pat_mobiliario"."tb_incorporacao" ("in_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_lancamento_contabil" add constraint "fk_lc_patrimonio" foreign key ("pa_id") references "pat_mobiliario"."tb_patrimonio" ("pa_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_lancamento_contabil" add constraint "fk_lc_movimentacao" foreign key ("mo_id") references "pat_mobiliario"."tb_movimentacao" ("mo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_documento" add constraint "fk_incorporacao_documento" foreign key ("in_id") references "pat_mobiliario"."tb_incorporacao" ("in_id") on delete cascade on update no action;

alter table "pat_mobiliario"."tb_documento" add constraint "fk_movimentacao_documento" foreign key ("mo_id") references "pat_mobiliario"."tb_movimentacao" ("mo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_config_depreciacao" add constraint "fk_config_depre_conta_contabil" foreign key ("cc_id") references "comum_siga"."tb_conta_contabil" ("cc_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_incorporacao_item" add constraint "fk_incorp_item_config_depre" foreign key ("cd_id") references "pat_mobiliario"."tb_config_depreciacao" ("cd_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_depreciacao" add constraint "fk_depreciacao_patrimonio" foreign key ("pa_id") references "pat_mobiliario"."tb_patrimonio" ("pa_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_depreciacao" add constraint "fk_depreciacao_orgao" foreign key ("uo_id_orgao") references "comum_siga"."tb_unidade_organizacional" ("uo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_depreciacao" add constraint "fk_depreciacao_setor" foreign key ("uo_id_setor") references "comum_siga"."tb_unidade_organizacional" ("uo_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_depreciacao" add constraint "fk_depreciacao_conta_contabil" foreign key ("cc_id") references "comum_siga"."tb_conta_contabil" ("cc_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_config_depreciacao" add constraint "fk_cd_cc_id" foreign key ("cc_id") references "comum_siga"."tb_conta_contabil" ("cc_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_reserva_intervalo" add constraint "fk_reserva_reserva_intervalo" foreign key ("rv_id") references "pat_mobiliario"."tb_reserva" ("rv_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_reserva_intervalo_numero" add constraint "fk_reserva_intervalo_reserva_intervalo_numero" foreign key ("ri_id") references "pat_mobiliario"."tb_reserva_intervalo" ("ri_id") on delete no action on update no action;

alter table "pat_mobiliario"."tb_reserva_intervalo" add constraint "fk_unidadeorg_reserva_intervalo" foreign key ("uo_id_orgao") references "comum_siga"."tb_unidade_organizacional" ("uo_id") on delete no action on update no action;
