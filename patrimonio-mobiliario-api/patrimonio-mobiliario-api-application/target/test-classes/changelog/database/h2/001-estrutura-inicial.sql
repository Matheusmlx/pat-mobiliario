
-- ######################## Comum ################################

-- Sequences

create sequence comum.seq_usuario;
create sequence comum.seq_produto;
create sequence comum.seq_produto_atributo;

-- Tables

create table comum.tb_usuario(
    us_id bigint not null,
    us_email varchar(255) not null,
    us_email_contato varchar(255) null,
    us_doc_chave varchar(255),
    us_nome varchar(255) not null,
    us_exibir_representacoes boolean,
    us_telefone varchar(255),
    us_imagem varchar(500),
    us_tema varchar(20),
    us_situacao varchar(7),
    us_tipo_usuario varchar(20),
    id_id bigint,
    pa_id bigint not null
);

create table comum.tb_produto(
 pr_id Integer not null,
 pr_nome varchar(100) not null,
 pr_descricao varchar(4000),
 pr_logo varchar(4000),
 pr_logo_email varchar(4000),
 pr_titulo_img varchar(4000),
 pr_img_principal varchar(4000),
 pr_css_default varchar(4000),
 pl_id Integer,
 pr_dthr_cadastro timestamp(6),
 pr_dthr_alteracao timestamp(6),
 pr_usuario_cadastro varchar(255),
 pr_usuario_alteracao varchar(255)
);

create table comum.tb_produto_atributo(
 pa_id Integer not null,
 pa_atributo varchar(255) not null,
 pa_valor varchar(4000) not null,
 pa_tipo varchar(100),
 pr_id Integer not null,
 pa_dthr_cadastro timestamp(6),
 pa_dthr_alteracao timestamp(6),
 pa_usuario_cadastro varchar(255),
 pa_usuario_alteracao varchar(255)
);

create table comum.tb_perfil
(
    pf_id integer not null,
    pf_nome varchar(100) not null,
    pf_descricao varchar(200) not null,
    pf_tipo varchar(10) not null,
    pr_id integer not null,
    pf_dthr_cadastro timestamp(6),
    pf_dthr_alteracao timestamp(6),
    pf_usuario_cadastro varchar(255),
    pf_usuario_alteracao varchar(255)
);

create table comum.tb_plano
(
    pl_id integer not null,
    pl_nome varchar(100) not null,
    pl_descricao varchar(255),
    pl_dthr_cadastro timestamp(6),
    pl_dthr_alteracao timestamp(6),
    pl_usuario_cadastro varchar(255),
    pl_usuario_alteracao varchar(255)
);

-- ######################## Comum Siga ##########################

-- Sequences

create sequence comum_siga.seq_conta_contabil;
create sequence comum_siga.seq_pessoa;
create sequence comum_siga.seq_tipo_documento;
create sequence comum_siga.seq_unidade_organizacional;
create sequence comum_siga.seq_grupo;
create sequence comum_siga.seq_classe;
create sequence comum_siga.seq_material_servico;
create sequence comum_siga.tb_condicao_armazenamento;
create sequence comum_siga.tb_criterio_inspecao;
create sequence comum_siga.naturesa_despesa;



-- Tables

create table comum_siga.tb_conta_contabil
(
    cc_id                bigint      not null,
    cc_codigo            varchar(16)  not null,
    cc_descricao         varchar(152) not null,
    cc_situacao          varchar(10)  not null,
    cc_dthr_cadastro     timestamp(6),
    cc_dthr_alteracao    timestamp(6),
    cc_usuario_cadastro  varchar(255),
    cc_usuario_alteracao varchar(255)
);

create table comum_siga.tb_conta_contabil_produto
(
    cc_id bigint not null,
    pr_id bigint not null
);

create table comum_siga.tb_pessoa(
    pe_id Integer not null,
    pe_nome_razaosocial varchar(100) not null,
    pe_nome_fantasia varchar(100),
    pe_cpf_cnpj varchar(100) not null,
    pe_data_nascimento date,
    pe_data_fundacao date,
    pe_logradouro varchar(255),
    pe_bairro varchar(255),
    pe_numero varchar(20),
    pe_complemento varchar(100),
    pe_cep varchar(20),
    pe_site varchar(255),
    pe_logo varchar(255),
    pe_observacao varchar(4000),
    pe_cidade_estrangeira varchar(100),
    pe_estado_estrangeiro varchar(100),
    pe_cerca varchar(15),
    pe_situacao varchar(10),
    pe_impedido character(1),
    mu_id Integer,
    pl_id Integer,
    pa_id Integer not null,
    pe_numero_cerca Integer,
    pe_ano_cerca smallint,
    pe_data_emissao_cerca timestamp(6),
    pe_data_atualizacao_cerca timestamp(6),
    pe_data_validade_cerca timestamp(6)
);

create table comum_siga.tb_tipo_documento
(
    td_id                      Integer     not null,
    td_desc                    varchar(50) not null,
    td_permite_anexo           boolean     not null,
    td_dthr_cadastro           timestamp(6),
    td_dthr_alteracao          timestamp(6),
    td_usuario_cadastro        varchar(255),
    td_usuario_alteracao       varchar(255),
    td_identificacao_documento varchar(20) not null
);


create table comum_siga.tb_unidade_organizacional (
    uo_id Integer not null,
    uo_nome varchar(250) not null,
    uo_sigla varchar(50) not null,
    uo_situacao varchar(20) not null,
    uo_tipo varchar(10) not null,
    uo_tipo_adm varchar(50) ,
    uo_cod_hierarquia varchar(500) not null,
    uo_responsavel varchar(100),
    uo_substituto varchar(100),
    uo_logradouro varchar(255),
    uo_numero varchar(10),
    uo_complemento varchar(255),
    uo_bairro varchar(255),
    uo_cep varchar(9),
    uo_almoxarifado boolean,
    uo_cod_orgao varchar(20),
    uo_email varchar(200),
    uo_data_decreto timestamp(6),
    uo_numero_decreto varchar(20),
    uo_motivo_alteracao varchar(50),
    uo_justificativa varchar(255),
    uo_id_origem bigint,
    uo_id_externo bigint,
    uo_estrutura_administrativa bigint,
    uo_id_superior bigint,
    mu_id bigint
);

create table comum_siga.tb_grupo
(
    gr_id                Integer not null,
    gr_codigo            varchar(10)  not null,
    gr_tipo              varchar(8)   not null,
    gr_descricao         varchar(120) not null,
    gr_situacao          varchar(10)  not null,
    gr_dthr_cadastro     timestamp(6),
    gr_dthr_alteracao    timestamp(6),
    gr_usuario_cadastro  varchar(255),
    gr_usuario_alteracao varchar(255)
);

create table comum_siga.tb_classe
(
    cl_id                Integer not null,
    cl_codigo            varchar(10)  not null,
    cl_descricao         varchar(120) not null,
    cl_situacao          varchar(10)  not null,
    cl_dthr_cadastro     timestamp(6),
    cl_dthr_alteracao    timestamp(6),
    cl_usuario_cadastro  varchar(255),
    cl_usuario_alteracao varchar(255),
    gr_id                Integer
);

create table comum_siga.tb_condicao_armazenamento
(
    ar_id                Integer not null,
    ar_descricao         varchar(50) not null,
    ar_situacao          varchar(10) not null,
    ar_dthr_cadastro     timestamp(6),
    ar_dthr_alteracao    timestamp(6),
    ar_usuario_cadastro  varchar(255),
    ar_usuario_alteracao varchar(255)
);

create table comum_siga.tb_criterio_inspecao
(
    ci_id                Integer not null,
    ci_descricao         varchar(50) not null,
    ci_situacao          varchar(10) not null,
    ci_dthr_cadastro     timestamp(6),
    ci_dthr_alteracao    timestamp(6),
    ci_usuario_cadastro  varchar(255),
    ci_usuario_alteracao varchar(255)
);

create table comum_siga.tb_material_servico
(
    ms_id                       Integer not null,
    ms_codigo                   varchar(20),
    ms_tipo                     varchar(8)  not null,
    ms_descricao                varchar(120),
    ms_observacao               varchar(500),
    ms_classificacao            varchar(25),
    ms_situacao                 varchar(10) not null,
    ms_status                   varchar(20) not null,
    ms_codigo_ncm               varchar(15),
    ms_perecivel                boolean,
    ms_justificativa_inativacao varchar(500),
    ms_justificativa_reativacao varchar(500),
    cl_id                       Integer not null,
    ar_id                       Integer,
    ci_id                       Integer,
    ms_id_similar               Integer,
    ms_dthr_cadastro            timestamp(6),
    ms_dthr_alteracao           timestamp(6),
    ms_usuario_cadastro         varchar(255),
    ms_usuario_alteracao        varchar(255),
    ms_importacao               boolean,
    ms_descricao_resumida       varchar(500),
    ms_codigo_sequencial        varchar(20),
    ms_data_aprovacao           timestamp(6),
    us_id                       Integer
);

create table comum_siga.tb_item_material_servico
(
    ims_id                       Integer not null,
    ims_situacao                 varchar(10) not null,
    ims_codigo                   varchar(20),
    ims_tipo                     varchar(12) not null,
    ims_descricao_truncada       varchar(150) not null,
    ims_descricao_compl          text,
    ims_descricao                text not null,
    ims_ata_carona               boolean,
    ims_num_ata_carona           varchar(15),
    ims_acao_judicial            boolean,
    ims_marca_acao_judic         varchar(30),
    ims_sustentavel              boolean,
    ims_anvisa                   varchar(20),
    ims_legislacao               varchar(100),
    ims_referencia               varchar(1000),
    ims_dt_ini_utilizacao        date,
    ims_dt_fim_utilizacao        date,
    ims_status                   varchar(20) not null,
    ims_justificativa_inativacao varchar(500),
    ims_justificativa_reativacao varchar(500),
    ims_sinonimo                 varchar(500),
    ims_codigo_anterior          varchar(100),
    ims_id_similar               Integer,
    ims_id_clone                 Integer,
    ms_id                        Integer not null,
    ims_dthr_cadastro            timestamp(6),
    ims_dthr_alteracao           timestamp(6),
    ims_usuario_cadastro         varchar(255),
    ims_usuario_alteracao        varchar(255),
    ims_importacao               boolean,
    ims_data_aprovacao           timestamp(6),
    us_id                        Integer
);

create table comum_siga.tb_item_simplificado
(
	is_id integer not null,
	is_codigo varchar not null,
	is_descricao text not null,
	is_situacao varchar not null,
	is_status varchar not null,
	is_tipo varchar not null,
	ms_id integer,
	is_dthr_cadastro timestamp(6),
	is_dthr_alteracao timestamp(6),
	is_usuario_cadastro varchar(255),
	is_usuario_alteracao varchar(255)
);


create table comum_siga.tb_elemento_subelemen
(
    es_id integer not null constraint es_id unique,
    es_codigo varchar(10) not null,
    es_nome varchar(150) not null,
    es_situacao varchar(10) not null,
    es_tipo varchar(12) not null,
    es_pai integer,
    es_dthr_cadastro timestamp(6),
    es_dthr_alteracao timestamp(6),
    es_usuario_cadastro varchar(255),
    es_usuario_alteracao varchar(255),
    es_justificativa varchar(200)
);

create table comum_siga.tb_natureza_contratacao
(
    nc_id integer not null,
    nc_descricao varchar(50) not null,
    nc_situacao varchar(10) not null,
    nc_dthr_cadastro timestamp(6),
    nc_dthr_alteracao timestamp(6),
    nc_usuario_cadastro varchar(255),
    nc_usuario_alteracao varchar(255),
    nc_tipo_natureza_contratacao varchar(30) default 'CONSUMO'::character varying not null
);

create table comum_siga.tb_contratacao_subelem
(
    ns_id integer not null,
    ns_situacao varchar(10) not null,
    nc_id integer not null,
    es_id integer not null,
    ns_dthr_cadastro timestamp(6),
    ns_dthr_alteracao timestamp(6),
    ns_usuario_cadastro varchar(255),
    ns_usuario_alteracao varchar(255)
);

create table comum_siga.tb_matser_subelem
(
    ms_id integer not null,
    ns_id integer not null
);


create table comum_siga.tb_natureza_despesa
(
    nd_id integer not null,
    nd_despesa varchar(255),
    es_id integer not null,
    nd_descricao varchar(255),
    nd_justificativa varchar(200),
    nd_situacao varchar(20),
    nd_dthr_cadastro timestamp(6),
    nd_dthr_alteracao timestamp(6),
    nd_usuario_cadastro varchar(255),
    nd_usuario_alteracao varchar(255),
    cc_id integer
);

-- ######################## Hal ##########################

-- sequences

create sequence hal.tb_dominio;

-- tables

create table hal.tb_dominio
(
    dm_id integer not null,
    dm_tipo_cliente varchar(255),
    dm_id_cliente integer not null,
    us_id integer not null,
    dm_dthr_cadastro timestamp(6),
    dm_dthr_alteracao timestamp(6),
    dm_usuario_cadastro varchar(255),
    dm_usuario_alteracao varchar(255),
);

create table hal.tb_dominio_perfil
(
    dm_id integer not null,
    pf_id integer not null
);

create table hal.tb_permissao_perfil
(
    fu_id integer not null,
    pf_id integer not null,
    pl_id integer not null,
    pp_permissao char not null
);

create table hal.tb_funcao
(
    fu_id integer not null,
    fu_nome varchar(255) not null,
    fu_descricao varchar(255),
    fu_dthr_cadastro timestamp(6),
    fu_dthr_alteracao timestamp(6),
    fu_usuario_cadastro varchar(255),
    fu_usuario_alteracao varchar(255),
    mo_id integer,
    pl_id integer
);


-- CREATE SEQUENCES SECTION -------------------------------------------------

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_NOTA_LANCTO_CONTABIL"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_INCORPORACAO_ITEM"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_CONCEDENTE"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_ESTADO_CONSERVACAO"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_INCORPORACAO"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_DOCUMENTO"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_CONVENIO"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_PATRIMONIO"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_EMPENHO"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_RECONHECIMENTO"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_CONFIG_CONTA_CONTABIL"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_MOVIMENTACAO"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_LANCAMENTO_CONTABIL"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_RESPONSAVEL"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_COMODANTE"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_NOTIFICACAO"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_CONFIG_DEPRECIACAO"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_DEPRECIACAO"
    INCREMENT BY 1
    START WITH 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_RESERVA"
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_RESERVA_INTERVALO"
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE;

CREATE SEQUENCE "PAT_MOBILIARIO"."SEQ_RESERVA_INTERVALO_NUMERO"
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE;

-- CREATE TABLES SECTION -------------------------------------------------

CREATE TABLE "PAT_MOBILIARIO"."TB_NOTA_LANCAMENTO_CONTABIL"
(
    "NLC_ID" INTEGER NOT NULL,
    "NLC_NUMERO" CHARACTER VARYING(12) NULL,
    "NLC_DTHR_LANCAMENTO" TIMESTAMP(9) NULL,
    "NLC_DTHR_CADASTRO" TIMESTAMP(9) NULL,
    "NLC_DTHR_ALTERACAO" TIMESTAMP(9) NULL,
    "NLC_USUARIO_CADASTRO" CHARACTER VARYING(255) NULL,
    "NLC_USUARIO_ALTERACAO" CHARACTER VARYING(255) NULL
);

ALTER TABLE "PAT_MOBILIARIO"."TB_NOTA_LANCAMENTO_CONTABIL" ADD CONSTRAINT "PK_TB_NOTA_LANCAMENTO_CONTABIL" PRIMARY KEY ("NLC_ID");

-- TABLE PAT_MOBILIARIO.TB_INCORPORACAO

CREATE TABLE "PAT_MOBILIARIO"."TB_INCORPORACAO"
(
    "IN_ID" INTEGER NOT NULL,
    "IN_CODIGO" CHARACTER VARYING(20),
    "IN_DTHR_RECEBIMENTO" TIMESTAMP(9),
    "IN_SITUACAO" CHARACTER VARYING(50),
    "IN_NUM_PROCESSO" CHARACTER VARYING(255),
    "IN_NOTA" CHARACTER VARYING(20),
    "IN_VALOR_NOTA" NUMERIC(30,6),
    "UO_ID_ORGAO" INTEGER,
    "IN_DTHR_NOTA" NUMERIC(30,6),
    "IN_ORIGEM_CONVENIO" BOOLEAN DEFAULT FALSE,
    "IN_ORIGEM_FUNDO" BOOLEAN DEFAULT FALSE,
    "IN_ORIGEM_PROJETO" BOOLEAN DEFAULT FALSE,
    "IN_ORIGEM_COMODATO" BOOLEAN DEFAULT FALSE,
    "IN_PROJETO" CHARACTER VARYING(100),
    "UO_ID_SETOR" INTEGER,
    "UO_ID_FUNDO" INTEGER,
    "IN_DTHR_FINALIZACAO" TIMESTAMP(9),
    "IN_USUARIO_FINALIZACAO" CHARACTER VARYING(255),
    "CO_ID" INTEGER,
    "RE_ID" INTEGER,
    "PE_ID" INTEGER,
    "NLC_ID" INTEGER,
    "COM_ID" INTEGER,
    "IN_DTHR_INI_PROC" TIMESTAMP(9),
    "IN_DTHR_FIM_PROC" TIMESTAMP(9),
    "IN_ERRO_PROC" CHARACTER VARYING(255),
    "IN_DTHR_CADASTRO" TIMESTAMP,
    "IN_DTHR_ALTERACAO" TIMESTAMP,
    "IN_USUARIO_CADASTRO" CHARACTER VARYING(255),
    "IN_USUARIO_ALTERACAO" CHARACTER VARYING(255)
);

CREATE INDEX "IN_IN_UO_ID_ORGAO" ON "PAT_MOBILIARIO"."TB_INCORPORACAO" ("UO_ID_ORGAO");
CREATE INDEX "IN_IN_CO_ID" ON "PAT_MOBILIARIO"."TB_INCORPORACAO" ("CO_ID");
CREATE INDEX "IN_IN_RE_ID" ON "PAT_MOBILIARIO"."TB_INCORPORACAO" ("RE_ID");
CREATE INDEX "IN_IN_PE_ID" ON "PAT_MOBILIARIO"."TB_INCORPORACAO" ("PE_ID");
CREATE INDEX "IN_IN_NLC_ID" ON "PAT_MOBILIARIO"."TB_INCORPORACAO" ("NLC_ID");
CREATE INDEX "IN_IN_COM_ID" ON "PAT_MOBILIARIO"."TB_INCORPORACAO" ("COM_ID");
CREATE INDEX "IN_IN_UO_ID_SETOR" ON "PAT_MOBILIARIO"."TB_INCORPORACAO" ("UO_ID_SETOR");
ALTER TABLE "PAT_MOBILIARIO"."TB_INCORPORACAO" ADD CONSTRAINT "PK_TB_INCORPORACAO" PRIMARY KEY ("IN_ID");

-- TABLE PAT_MOBILIARIO.TB_INCORPORACAO_ITEM

CREATE TABLE "PAT_MOBILIARIO"."TB_INCORPORACAO_ITEM"
(
    "INI_ID" INTEGER NOT NULL,
    "INI_CODIGO" CHARACTER VARYING(100),
    "INI_DESCRICAO" TEXT,
    "INI_MARCA" CHARACTER VARYING(500),
    "INI_MODELO" CHARACTER VARYING(500),
    "INI_FABRICANTE" CHARACTER VARYING(500),
    "INI_VALOR_TOTAL" NUMERIC(14,2),
    "INI_TOTAL_UNIDADES" INTEGER,
    "INI_TIPO" CHARACTER VARYING(100),
    "INI_NUMERACAO_PATRIMONIAL" CHARACTER VARYING(20),
    "INI_URI_IMAGEM" CHARACTER VARYING(100),
    "INI_ANO_FABRICACAO_MODELO" CHARACTER VARYING(50),
    "INI_COMBUSTIVEL" CHARACTER VARYING(50),
    "INI_CATEGORIA" CHARACTER VARYING(50),
    "INI_SITUACAO" CHARACTER VARYING(100) NOT NULL,
    "INI_DEPRECIAVEL" BOOLEAN DEFAULT TRUE,
    "INI_DTHR_CADASTRO" TIMESTAMP(9),
    "INI_DTHR_ALTERACAO" TIMESTAMP(9),
    "INI_USUARIO_CADASTRO" CHARACTER VARYING(255),
    "INI_USUARIO_ALTERACAO" CHARACTER VARYING(255),
    "IN_ID" INTEGER NOT NULL,
    "CD_ID" INTEGER,
    "CC_ID" INTEGER,
    "ND_ID" INTEGER,
    "EC_ID" INTEGER
);

CREATE INDEX "IN_INI_IN_ID" ON "PAT_MOBILIARIO"."TB_INCORPORACAO_ITEM" ("IN_ID");
CREATE INDEX "IN_INI_CC_ID" ON "PAT_MOBILIARIO"."TB_INCORPORACAO_ITEM" ("CC_ID");
CREATE INDEX "IN_INI_ND_ID" ON "PAT_MOBILIARIO"."TB_INCORPORACAO_ITEM" ("ND_ID");
CREATE INDEX "IN_INI_CD_ID" ON "PAT_MOBILIARIO"."TB_INCORPORACAO_ITEM" ("CD_ID");
CREATE INDEX "IN_INI_EC_ID" ON "PAT_MOBILIARIO"."TB_INCORPORACAO_ITEM" ("EC_ID");
ALTER TABLE "PAT_MOBILIARIO"."TB_INCORPORACAO_ITEM" ADD CONSTRAINT "PK_TB_INCORPORACAO_ITEM" PRIMARY KEY ("INI_ID");

-- TABLE PAT_MOBILIARIO.TB_DOCUMENTO

CREATE TABLE "PAT_MOBILIARIO"."TB_DOCUMENTO"
(
    "DO_ID" Integer NOT NULL,
    "DO_NUMERO" Character varying(50) NULL,
    "DO_DT" Timestamp(9) NOT NULL,
    "DO_VALOR" Numeric(20,6) NOT NULL,
    "DO_URI_ANEXO" Character varying(45) NULL,
    "IN_ID" Integer,
    "MO_ID" Integer,
    "TD_ID" Integer NOT NULL,
    "DO_DTHR_CADASTRO" Timestamp(9),
    "DO_DTHR_ALTERACAO" Timestamp(9),
    "DO_USUARIO_CADASTRO" Character varying(255),
    "DO_USUARIO_ALTERACAO" Character varying(255)
);

-- TABLE PAT_MOBILIARIO.TB_CONVENIO

CREATE TABLE "PAT_MOBILIARIO"."TB_CONVENIO"
(
    "CO_ID" INTEGER NOT NULL,
    "CO_DTHR_INICIO" TIMESTAMP,
    "CO_DTHR_FIM" TIMESTAMP,
    "CO_NUMERO" CHARACTER VARYING(100),
    "CO_NOME" CHARACTER VARYING(100),
    "CO_SITUACAO" CHARACTER VARYING(50),
    "CO_FONTE_RECURSO" CHARACTER VARYING(500),
    "CO_TIPO" CHARACTER VARYING(50),
    "CO_DTHR_CADASTRO" TIMESTAMP,
    "CO_DTHR_ALTERACAO" TIMESTAMP,
    "CO_USUARIO_CADASTRO" CHARACTER VARYING(255),
    "CO_USUARIO_ALTERACAO" CHARACTER VARYING(255),
    "COC_ID" INTEGER
);

CREATE INDEX "IN_TB_CO_COC_ID" ON "PAT_MOBILIARIO"."TB_CONVENIO" ("COC_ID");
ALTER TABLE "PAT_MOBILIARIO"."TB_CONVENIO" ADD CONSTRAINT "PK_TB_CONVENIO" PRIMARY KEY ("CO_ID");

-- TABLE PAT_MOBILIARIO.TB_PATRIMONIO

CREATE TABLE "PAT_MOBILIARIO"."TB_PATRIMONIO"
(
    "PA_ID" INTEGER NOT NULL,
    "PA_PLACA" CHARACTER VARYING(20),
    "PA_RENAVAM" CHARACTER VARYING(20),
    "PA_URI_IMAGEM" CHARACTER VARYING(100),
    "PA_LICENCIAMENTO" CHARACTER VARYING(20),
    "PA_MOTOR" CHARACTER VARYING(20),
    "PA_CHASSI" CHARACTER VARYING(20),
    "PA_NUMERO" CHARACTER VARYING(30) NOT NULL,
    "PA_NUMERO_SERIE" CHARACTER VARYING(255),
    "PA_SITUACAO" CHARACTER VARYING(20),
    "PA_VALOR_LIQUIDO" NUMERIC(14,3),
    "PA_VALOR_ENTRADA" NUMERIC(14,3),
    "PA_VALOR_RESIDUAL" NUMERIC(14,3),
    "PA_VALOR_AQUISICAO" NUMERIC(14,3) NOT NULL,
    "PA_DTHR_INICIO_VIDA_UTIL" TIMESTAMP,
    "PA_DTHR_FIM_VIDA_UTIL" TIMESTAMP,
    "PA_DEPRECIAVEL" BOOLEAN,
    "PA_DIFERENCA_DIZIMA" BOOLEAN,
    "PA_MOTIVO_ESTORNO" CHARACTER VARYING(500),
    "PA_PROJETO" CHARACTER VARYING(100),
    "PA_DTHR_ESTORNO" TIMESTAMP,
    "PA_USUARIO_ESTORNO" CHARACTER VARYING(255),
    "PA_VALOR_DEPRECIACAO_MENSAL" NUMERIC(14,3),
    "PA_DTHR_CADASTRO" TIMESTAMP(9),
    "PA_DTHR_ALTERACAO" TIMESTAMP(9),
    "PA_USUARIO_CADASTRO" CHARACTER VARYING(255),
    "PA_USUARIO_ALTERACAO" CHARACTER VARYING(255),
    "UO_ID_ORGAO" integer,
    "UO_ID_SETOR" integer,
    "UO_ID_FUNDO" integer,
    "CO_ID" integer,
    "ND_ID" integer,
    "EC_ID" integer,
    "CC_ID_CLASSIFICACAO" Integer,
    "CC_ID_ATUAL" Integer,
    "INI_ID" INTEGER NOT NULL,
    "COM_ID" INTEGER,
);

CREATE INDEX "IN_PA_NUM_PATRIMONIO" ON "PAT_MOBILIARIO"."TB_PATRIMONIO" ("PA_NUMERO");
CREATE INDEX "IN_PA_CC_ID_CLASSIFICACAO" ON "PAT_MOBILIARIO"."TB_PATRIMONIO" ("CC_ID_CLASSIFICACAO");
CREATE INDEX "IN_PA_CC_ID_ATUAL" ON "PAT_MOBILIARIO"."TB_PATRIMONIO" ("CC_ID_ATUAL");
CREATE INDEX "IN_PA_INI_ID" ON "PAT_MOBILIARIO"."TB_PATRIMONIO" ("INI_ID");
CREATE INDEX "IN_PA_COM_ID" ON "PAT_MOBILIARIO"."TB_PATRIMONIO" ("COM_ID");
ALTER TABLE "PAT_MOBILIARIO"."TB_PATRIMONIO" ADD CONSTRAINT "PK_PATRIMONIO" PRIMARY KEY ("PA_ID");

-- TABLE PAT_MOBILIARIO.TB_RECONHECIMENTO

CREATE TABLE "PAT_MOBILIARIO"."TB_RECONHECIMENTO"
(
    "RE_ID" INTEGER NOT NULL,
    "RE_NOME" CHARACTER VARYING(100),
    "RE_EXECUCAO_ORCAMENTARIA" BOOLEAN,
    "RE_NOTA_FICAL" BOOLEAN,
    "RE_EMPENHO" BOOLEAN,
    "RE_SITUACAO" CHARACTER VARYING(100),
    "RE_DTHR_ALTERACAO" TIMESTAMP(9),
    "RE_DTHR_CADASTRO" TIMESTAMP(9),
    "RE_USUARIO_CADASTRO" CHARACTER VARYING(255),
    "RE_USUARIO_ALTERACAO" CHARACTER VARYING(255)
);

ALTER TABLE "PAT_MOBILIARIO"."TB_RECONHECIMENTO" ADD CONSTRAINT "PK_TB_RECONHECIMENTO" PRIMARY KEY ("RE_ID");

-- TABLE PAT_MOBILIARIO.TB_EMPENHO

CREATE TABLE "PAT_MOBILIARIO"."TB_EMPENHO"
(
    "EM_ID" INTEGER NOT NULL,
    "EM_DTHR" TIMESTAMP,
    "EM_VALOR" NUMERIC(30,6),
    "EM_NUMERO" CHARACTER VARYING(50),
    "EM_DTHR_CADASTRO" TIMESTAMP(9),
    "EM_DTHR_ALTERACAO" TIMESTAMP(9),
    "EM_USUARIO_CADASTRO" CHARACTER VARYING(20),
    "EM_USUARIO_ALTERACAO" CHARACTER VARYING(20),
    "IN_ID" INTEGER NOT NULL
);

CREATE INDEX "IN_EM_IN_ID" ON "PAT_MOBILIARIO"."TB_EMPENHO" ("IN_ID");
ALTER TABLE "PAT_MOBILIARIO"."TB_EMPENHO" ADD CONSTRAINT "PK_TB_EMPENHO" PRIMARY KEY ("EM_ID");

-- TABLE PAT_MOBILIARIO.TB_CONFIG_CONTA_CONTABIL

CREATE TABLE "PAT_MOBILIARIO"."TB_CONFIG_CONTA_CONTABIL"
(
    "CCC_ID" INTEGER NOT NULL,
    "CCC_METODO" CHARACTER VARYING(100),
    "CCC_TIPO" CHARACTER VARYING(100),
    "CCC_TIPO_BEM" CHARACTER VARYING(100) NOT NULL,
    "CCC_VIDA_UTIL_MESES" INTEGER,
    "CCC_PERCENTUAL_RESIDUAL" NUMERIC(5,2),
    "CC_ID" INTEGER NOT NULL,
    "CCC_DTHR_CADASTRO" TIMESTAMP(9),
    "CCC_DTHR_ALTERACAO" TIMESTAMP(9),
    "CCC_USUARIO_CADASTRO" CHARACTER VARYING(20),
    "CCC_USUARIO_ALTERACAO" CHARACTER VARYING(20)
);

CREATE INDEX "IN_CONFIG_CC_CC_ID" ON "PAT_MOBILIARIO"."TB_CONFIG_CONTA_CONTABIL" ("CC_ID");
ALTER TABLE "PAT_MOBILIARIO"."TB_CONFIG_CONTA_CONTABIL" ADD CONSTRAINT "PK_TB_CONFIG_CONTA_CONTABIL" PRIMARY KEY ("CCC_ID");

-- TABLE PAT_MOBILIARIO.TB_CONCEDENTE

CREATE TABLE "PAT_MOBILIARIO"."TB_CONCEDENTE"
(
    "COC_ID" INTEGER NOT NULL,
    "COC_NOME" CHARACTER VARYING(100),
    "COC_SITUACAO" CHARACTER VARYING(20),
    "COC_TIPO" CHARACTER VARYING(20),
    "COC_CPF_CNPJ" CHARACTER VARYING(20),
    "COC_DTHR_CADASTRO" TIMESTAMP(9),
    "COC_DTHR_ALTERACAO" TIMESTAMP(9),
    "COC_USUARIO_CADASTRO" CHARACTER VARYING(20),
    "COC_USUARIO_ALTERACAO" CHARACTER VARYING(20)
);

ALTER TABLE "PAT_MOBILIARIO"."TB_CONCEDENTE" ADD CONSTRAINT "PK_TB_CONCEDENTE" PRIMARY KEY ("COC_ID");

-- TABLE PAT_MOBILIARIO.TB_ESTADO_CONSERVACAO

CREATE TABLE "PAT_MOBILIARIO"."TB_ESTADO_CONSERVACAO"
(
    "EC_ID" INTEGER NOT NULL,
    "EC_NOME" CHARACTER VARYING(100) NOT NULL,
    "EC_PRIORIDADE" INTEGER NOT NULL,
    "EC_DTHR_CADASTRO" TIMESTAMP(9),
    "EC_DTHR_ALTERACAO" TIMESTAMP(9),
    "EC_USUARIO_CADASTRO" CHARACTER VARYING(20),
    "EC_USUARIO_ALTERACAO" CHARACTER VARYING(20)
);

ALTER TABLE "PAT_MOBILIARIO"."TB_ESTADO_CONSERVACAO" ADD CONSTRAINT "PK_TB_ESTADO_CONSERVACAO" PRIMARY KEY ("EC_ID");

-- TABLE PAT_MOBILIARIO.TB_MOVIMENTACAO

CREATE TABLE "PAT_MOBILIARIO"."TB_MOVIMENTACAO"
(
    "MO_ID" integer not null,
    "MO_CODIGO" character varying(20),
    "MO_TIPO" character varying(50),
    "MO_SITUACAO" character varying(20) not null,
    "MO_MOTIVO_OBS" Varchar(500),
    "MO_NUMERO_TERMO" Varchar(20) UNIQUE,
    "MO_USUARIO_FINALIZACAO" CHARACTER VARYING(20),
    "MO_DTHR_FINALIZACAO" timestamp(9),
    "MO_DTHR_DEVOLUCAO" timestamp(9),
    "MO_DTHR_ENVIO" timestamp(9),
    "MO_USUARIO_CRIACAO" character varying (20),
    "MO_NUMERO_PROCESSO" character varying (100),
    "UO_ID_ORGAO_ORIGEM" integer,
    "UO_ID_ORGAO_DESTINO" integer,
    "UO_ID_SETOR_ORIGEM" integer,
    "UO_ID_SETOR_DESTINO" integer,
    "NLC_ID" integer,
    "RES_ID" integer,
    "MO_DTHR_INI_PROC" TIMESTAMP(9),
    "MO_DTHR_FIM_PROC" TIMESTAMP(9),
    "MO_ERRO_PROC" CHARACTER VARYING(255),
    "MO_DTHR_CADASTRO" TIMESTAMP(9),
    "MO_DTHR_ALTERACAO" TIMESTAMP(9),
    "MO_USUARIO_CADASTRO" CHARACTER VARYING(20),
    "MO_USUARIO_ALTERACAO" CHARACTER VARYING(20)
);

ALTER TABLE "PAT_MOBILIARIO"."TB_MOVIMENTACAO" ADD CONSTRAINT "PK_TB_MOVIMENTACAO" PRIMARY KEY ("MO_ID");

-- TABLE PAT_MOBILIARIO.TB_MOVIMENTACAO_ITEM

CREATE TABLE "PAT_MOBILIARIO"."TB_MOVIMENTACAO_ITEM"
(
    "MO_ID" INTEGER NOT NULL,
    "PA_ID" INTEGER NOT NULL,
    "MI_DTHR_DEVOLUCAO" TIMESTAMP(9)
);

ALTER TABLE "PAT_MOBILIARIO"."TB_MOVIMENTACAO_ITEM" ADD CONSTRAINT "PK_TB_MOVIMENTACAO_ITEM" PRIMARY KEY ("MO_ID", "PA_ID");


-- TABLE PAT_MOBILIARIO.TB_LANCAMENTO_CONTABIL

CREATE TABLE "PAT_MOBILIARIO"."TB_LANCAMENTO_CONTABIL"
(
    "LC_ID" BIGINT NOT NULL,
    "LC_TIPO_LANCAMENTO" CHARACTER VARYING(20) NOT NULL,
    "LC_DATA_LANCAMENTO" TIMESTAMP NOT NULL,
    "LC_VALOR" NUMERIC(30,6) NOT NULL,
    "LC_TIPO_MOVIMENTACAO" CHARACTER VARYING(50) NOT NULL,
    "UO_ID_ORGAO" INTEGER NOT NULL,
    "UO_ID_SETOR" INTEGER NOT NULL,
    "CC_ID" INTEGER NOT NULL,
    "PA_ID" INTEGER NOT NULL,
    "IN_ID" INTEGER,
    "MO_ID" INTEGER,
    "LC_DTHR_CADASTRO" TIMESTAMP(9),
    "LC_DTHR_ALTERACAO" TIMESTAMP(9),
    "LC_USUARIO_CADASTRO" CHARACTER VARYING(20),
    "LC_USUARIO_ALTERACAO" CHARACTER VARYING(20)
);

ALTER TABLE "PAT_MOBILIARIO"."TB_LANCAMENTO_CONTABIL" ADD CONSTRAINT "PK_TB_LANCAMENTO_CONTABIL" PRIMARY KEY ("LC_ID");

ALTER TABLE "PAT_MOBILIARIO"."TB_LANCAMENTO_CONTABIL" ADD CONSTRAINT "FK_LC_ORGAO" FOREIGN KEY ("UO_ID_ORGAO") REFERENCES "COMUM_SIGA"."TB_UNIDADE_ORGANIZACIONAL" ("UO_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_LANCAMENTO_CONTABIL" ADD CONSTRAINT "FK_LC_SETOR" FOREIGN KEY ("UO_ID_SETOR") REFERENCES "COMUM_SIGA"."TB_UNIDADE_ORGANIZACIONAL" ("UO_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_LANCAMENTO_CONTABIL" ADD CONSTRAINT "FK_LC_CONTA_CONTABIL" FOREIGN KEY ("CC_ID") REFERENCES "COMUM_SIGA"."TB_CONTA_CONTABIL" ("CC_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_LANCAMENTO_CONTABIL" ADD CONSTRAINT "FK_LC_PATRIMONIO" FOREIGN KEY ("PA_ID") REFERENCES "PAT_MOBILIARIO"."TB_PATRIMONIO" ("PA_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_LANCAMENTO_CONTABIL" ADD CONSTRAINT "FK_LC_INCORPORACAO" FOREIGN KEY ("IN_ID") REFERENCES "PAT_MOBILIARIO"."TB_INCORPORACAO" ("IN_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_LANCAMENTO_CONTABIL" ADD CONSTRAINT "FK_LC_MOVIMENTACAO" FOREIGN KEY ("MO_ID") REFERENCES "PAT_MOBILIARIO"."TB_MOVIMENTACAO" ("MO_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- TABLE PAT_MOBILIARIO.TB_RESPONSAVEL

CREATE TABLE "PAT_MOBILIARIO"."TB_RESPONSAVEL"
(
    "RES_ID" INTEGER NOT NULL,
    "RES_NOME" VARCHAR(255) NOT NULL,
    "RES_DTHR_CADASTRO" TIMESTAMP(9),
    "RES_DTHR_ALTERACAO" TIMESTAMP(9),
    "RES_USUARIO_CADASTRO" CHARACTER VARYING(255),
    "RES_USUARIO_ALTERACAO" CHARACTER VARYING(255)
);

ALTER TABLE "PAT_MOBILIARIO"."TB_RESPONSAVEL" ADD CONSTRAINT PK_TB_RESPONSAVEL_ID PRIMARY KEY ("RES_ID");

-- TABLE PAT_MOBILIARIO.TB_COMODANTE

CREATE TABLE "PAT_MOBILIARIO"."TB_COMODANTE"
(
    "COM_ID" INTEGER NOT NULL,
    "COM_NOME" VARCHAR(255) NOT NULL,
    "COM_DTHR_CADASTRO" TIMESTAMP(9),
    "COM_DTHR_ALTERACAO" TIMESTAMP(9),
    "COM_USUARIO_CADASTRO" CHARACTER VARYING(255),
    "COM_USUARIO_ALTERACAO" CHARACTER VARYING(255)
);

ALTER TABLE "PAT_MOBILIARIO"."TB_COMODANTE" ADD CONSTRAINT PK_TB_COMODANTE_ID PRIMARY KEY ("COM_ID");

-- TABLE PAT_MOBILIARIO.TB_NOTIFICACAO

CREATE TABLE "PAT_MOBILIARIO"."TB_NOTIFICACAO"
(
    "NO_ID" INTEGER NOT NULL,
    "NO_ORIGEM" VARCHAR(50) NOT NULL,
    "NO_ORIGEM_ID" INTEGER,
    "NO_ASSUNTO" VARCHAR(100) NOT NULL,
    "NO_MENSAGEM" VARCHAR(255) NOT NULL,
    "NO_DTHR_CRIACAO" TIMESTAMP(9),
    "NO_VISUALIZADA" BOOLEAN DEFAULT FALSE,
    "US_ID" INTEGER NOT NULL,
    "NO_DTHR_CADASTRO" TIMESTAMP(9),
    "NO_DTHR_ALTERACAO" TIMESTAMP(9),
    "NO_USUARIO_CADASTRO" CHARACTER VARYING(255),
    "NO_USUARIO_ALTERACAO" CHARACTER VARYING(255)
);

ALTER TABLE "PAT_MOBILIARIO"."TB_NOTIFICACAO" ADD CONSTRAINT PK_TB_NOTIFICACAO PRIMARY KEY ("NO_ID");

CREATE INDEX "IN_NOTIFICACAO_USUARIO" ON "PAT_MOBILIARIO"."TB_NOTIFICACAO" ("US_ID");

-- TABLE PAT_MOBILIARIO_TB_CONFIG_DEPRECIACAO

CREATE TABLE "PAT_MOBILIARIO"."TB_CONFIG_DEPRECIACAO"
(
  "CD_ID" INTEGER NOT NULL,
  "CD_VIDA_UTIL_MESES" INTEGER,
  "CD_PERCENTUAL_RESIDUAL" INTEGER,
  "CC_ID" INTEGER NOT NULL,
  "CD_DEPRECIAVEL" BOOLEAN NOT NULL,
  "CD_DTHR_CADASTRO" TIMESTAMP(9),
  "CD_DTHR_ALTERACAO" TIMESTAMP(9),
  "CD_USUARIO_CADASTRO" CHARACTER VARYING (20),
  "CD_USUARIO_ALTERACAO" CHARACTER VARYING (20)
);

CREATE INDEX "IN_CD_ID" ON "PAT_MOBILIARIO"."TB_CONFIG_DEPRECIACAO" ("CC_ID");
ALTER TABLE "PAT_MOBILIARIO"."TB_CONFIG_DEPRECIACAO" ADD CONSTRAINT PK_TB_CONFIG_DEPRECIACAO PRIMARY KEY ("CD_ID");


-- TABLE PAT_MOBILIARIO.TB_DEPRECIACAO

CREATE TABLE "PAT_MOBILIARIO"."TB_DEPRECIACAO"
(
    "DE_ID" INTEGER NOT NULL,
    "DE_VALOR_ANTERIOR" NUMERIC(14,3) NOT NULL,
    "DE_VALOR_SUBTRAIDO" NUMERIC(14,3) NOT NULL,
    "DE_VALOR_POSTERIOR" NUMERIC(14,3) NOT NULL,
    "DE_TAXA_APLICADA" NUMERIC(5,3) NOT NULL,
    "DE_DTHR_INICIAL" TIMESTAMP(9) NOT NULL,
    "DE_DTHR_FINAL" TIMESTAMP(9) NOT NULL,
    "DE_MES_REFERENCIA" CHARACTER VARYING(7) NOT NULL,
    "PA_ID" INTEGER,
    "UO_ID_ORGAO" INTEGER,
    "UO_ID_SETOR" INTEGER,
    "CC_ID" INTEGER,
    "DE_DTHR_CADASTRO" TIMESTAMP(6),
    "DE_DTHR_ALTERACAO" TIMESTAMP(6),
    "DE_USUARIO_CADASTRO" CHARACTER VARYING(255),
    "DE_USUARIO_ALTERACAO" CHARACTER VARYING(255)
);

CREATE INDEX "IN_DEPRECIACAO_PA_ID" ON "PAT_MOBILIARIO"."TB_DEPRECIACAO" ("PA_ID");
CREATE INDEX "IN_DEPRECIACAO_UO_ID_ORGAO" ON "PAT_MOBILIARIO"."TB_DEPRECIACAO" ("UO_ID_ORGAO");
CREATE INDEX "IN_DEPRECIACAO_UO_ID_SETOR" ON "PAT_MOBILIARIO"."TB_DEPRECIACAO" ("UO_ID_SETOR");
CREATE INDEX "IN_DEPRECIACAO_CC_ID" ON "PAT_MOBILIARIO"."TB_DEPRECIACAO" ("CC_ID");
ALTER TABLE "PAT_MOBILIARIO"."TB_DEPRECIACAO" ADD CONSTRAINT "PK_TB_DEPRECIACAO" PRIMARY KEY ("DE_ID");

-- TABLE PAT_MOBILIARIO.TB_RESERVA

CREATE TABLE "PAT_MOBILIARIO"."TB_RESERVA"
(
    "RV_ID" INTEGER NOT NULL,
    "RV_CODIGO" VARCHAR(10) NOT NULL,
    "RV_SITUACAO" VARCHAR(20) NOT NULL,
    "RV_PREENCHIMENTO" VARCHAR(10) NOT NULL,
    "RV_DTHR_CRIACAO" TIMESTAMP(9) NOT NULL,
    "RV_QTD_RESERVADA" INTEGER,
    "RV_QTD_RESTANTE" INTEGER,
    "RV_NRO_INICIO" INTEGER,
    "RV_NRO_FIM" INTEGER,
    "RV_DTHR_CADASTRO" TIMESTAMP(9),
    "RV_DTHR_ALTERACAO" TIMESTAMP(9),
    "RV_USUARIO_CADASTRO" VARCHAR(255),
    "RV_USUARIO_ALTERACAO" VARCHAR(255)
);

ALTER TABLE "PAT_MOBILIARIO"."TB_RESERVA" ADD CONSTRAINT "PK_TB_RESERVA" PRIMARY KEY ("RV_ID");

-- TABLE PAT_MOBILIARIO.TB_RESERVA_INTERVALO

CREATE TABLE "PAT_MOBILIARIO"."TB_RESERVA_INTERVALO"
(
    "RI_ID" INTEGER NOT NULL,
    "RV_ID" INTEGER NOT NULL,
    "RI_CODIGO" CHARACTER VARYING(10),
    "RI_SITUACAO" CHARACTER VARYING(20),
    "RI_PREENCHIMENTO" CHARACTER VARYING(10),
    "UO_ID_ORGAO" INTEGER,
    "RI_QTD_RESERVADA" INTEGER,
    "RI_NRO_INICIO" INTEGER,
    "RI_NRO_FIM" INTEGER,
    "RI_NRO_TERMO" VARCHAR(20),
    "RI_DTHR_FINALIZACAO" TIMESTAMP(9),
    "RI_DTHR_CADASTRO" TIMESTAMP(9),
    "RI_DTHR_ALTERACAO" TIMESTAMP(9),
    "RI_USUARIO_CADASTRO" CHARACTER VARYING(255),
    "RI_USUARIO_ALTERACAO" CHARACTER VARYING(255)
);

CREATE INDEX "IN_RI_RV_ID" ON "PAT_MOBILIARIO"."TB_RESERVA_INTERVALO" ("RV_ID");
CREATE INDEX "IN_RI_UO_ID_ORGAO" ON "PAT_MOBILIARIO"."TB_RESERVA_INTERVALO" ("UO_ID_ORGAO");
ALTER TABLE "PAT_MOBILIARIO"."TB_RESERVA_INTERVALO" ADD CONSTRAINT "PK_TB_RESERVA_INTERVALO" PRIMARY KEY ("RI_ID");

-- TABLE PAT_MOBILIARIO.TB_RESERVA_INTERVALO_NUMERO

CREATE TABLE "PAT_MOBILIARIO"."TB_RESERVA_INTERVALO_NUMERO"
(
    "RIN_ID" INTEGER NOT NULL,
    "RI_ID" INTEGER NOT NULL,
    "RIN_NUMERO" INTEGER,
    "RIN_UTILIZADO" BOOLEAN DEFAULT FALSE,
    "RIN_DTHR_CADASTRO" TIMESTAMP(9),
    "RIN_DTHR_ALTERACAO" TIMESTAMP(9),
    "RIN_USUARIO_CADASTRO" CHARACTER VARYING(255),
    "RIN_USUARIO_ALTERACAO" CHARACTER VARYING(255)
);

CREATE INDEX "IN_RESERVA_INTERVALO_RESERVA_INTERVALO_NUMERO" ON "PAT_MOBILIARIO"."TB_RESERVA_INTERVALO_NUMERO" ("RI_ID");
ALTER TABLE "PAT_MOBILIARIO"."TB_RESERVA_INTERVALO_NUMERO" ADD CONSTRAINT "PK_TB_RESERVA_INTERVALO_NUMERO" PRIMARY KEY ("RIN_ID");

-- CREATE FOREIGN KEYS (RELATIONSHIPS) SECTION -------------------------------------------------

ALTER TABLE "PAT_MOBILIARIO"."TB_INCORPORACAO_ITEM" ADD CONSTRAINT "FK_INCORPORACAO_ITEM_INCORP" FOREIGN KEY ("IN_ID") REFERENCES "PAT_MOBILIARIO"."TB_INCORPORACAO" ("IN_ID") ON DELETE CASCADE ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_INCORPORACAO" ADD CONSTRAINT "FK_INCORPORACAO_ORGAO" FOREIGN KEY ("UO_ID_ORGAO") REFERENCES "COMUM_SIGA"."TB_UNIDADE_ORGANIZACIONAL" ("UO_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_INCORPORACAO" ADD CONSTRAINT "FK_INCORPORACAO_CONVENIO" FOREIGN KEY ("CO_ID") REFERENCES "PAT_MOBILIARIO"."TB_CONVENIO" ("CO_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_INCORPORACAO" ADD CONSTRAINT "FK_INCORPORACAO_NOTA_LANCTO_CONTABIL" FOREIGN KEY ("NLC_ID") REFERENCES "PAT_MOBILIARIO"."TB_NOTA_LANCAMENTO_CONTABIL" ("NLC_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_INCORPORACAO" ADD CONSTRAINT "FK_INCORPORACAO_COMODANTE" FOREIGN KEY ("COM_ID") REFERENCES "PAT_MOBILIARIO"."TB_COMODANTE" ("COM_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_PATRIMONIO" ADD CONSTRAINT "FK_PATRIMONIO_CONTA_CONTABIL_CLASSIFICACAO" FOREIGN KEY ("CC_ID_CLASSIFICACAO") REFERENCES "COMUM_SIGA"."TB_CONTA_CONTABIL" ("CC_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_PATRIMONIO" ADD CONSTRAINT "FK_PATRIMONIO_CONTA_CONTABIL_ATUAL" FOREIGN KEY ("CC_ID_ATUAL") REFERENCES "COMUM_SIGA"."TB_CONTA_CONTABIL" ("CC_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_INCORPORACAO" ADD CONSTRAINT "FK_INCORPORACAO_RECONHECIMENTO" FOREIGN KEY ("RE_ID") REFERENCES "PAT_MOBILIARIO"."TB_RECONHECIMENTO" ("RE_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_PATRIMONIO" ADD CONSTRAINT "FK_PATRIMONIO_INC_ITEM" FOREIGN KEY ("INI_ID") REFERENCES "PAT_MOBILIARIO"."TB_INCORPORACAO_ITEM" ("INI_ID") ON DELETE CASCADE ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_PATRIMONIO" ADD CONSTRAINT "FK_PATRIMONIO_COMODANTE" FOREIGN KEY ("COM_ID") REFERENCES "PAT_MOBILIARIO"."TB_COMODANTE" ("COM_ID") ON DELETE CASCADE ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_CONFIG_CONTA_CONTABIL" ADD CONSTRAINT "FK_CONFIG_CONTA_CONTABIL" FOREIGN KEY ("CC_ID") REFERENCES "COMUM_SIGA"."TB_CONTA_CONTABIL" ("CC_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_INCORPORACAO" ADD CONSTRAINT "FK_INCOPORACAO_PESSOA" FOREIGN KEY ("PE_ID") REFERENCES "COMUM_SIGA"."TB_PESSOA" ("PE_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_INCORPORACAO" ADD CONSTRAINT "FK_INCOPORACAO_SETOR" FOREIGN KEY ("UO_ID_SETOR") REFERENCES "COMUM_SIGA"."TB_UNIDADE_ORGANIZACIONAL" ("UO_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_INCORPORACAO_ITEM" ADD CONSTRAINT "FK_ITEM_INC_CONTA_CONTABIL" FOREIGN KEY ("CC_ID") REFERENCES "COMUM_SIGA"."TB_CONTA_CONTABIL" ("CC_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_INCORPORACAO_ITEM" ADD CONSTRAINT "FK_ITEM_INC_NATUREZA_DESPESA" FOREIGN KEY ("ND_ID") REFERENCES "COMUM_SIGA"."TB_NATUREZA_DESPESA" ("ND_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_INCORPORACAO_ITEM" ADD CONSTRAINT "FK_ITEM_INC_ESTADO_CONSERV" FOREIGN KEY ("EC_ID") REFERENCES "PAT_MOBILIARIO"."TB_ESTADO_CONSERVACAO" ("EC_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_INCORPORACAO_ITEM" ADD CONSTRAINT "FK_ITEM_INC_CONFIG_DEPRECIACAO" FOREIGN KEY ("CD_ID") REFERENCES "PAT_MOBILIARIO"."TB_CONFIG_DEPRECIACAO" ("CD_ID") ON DELETE SET NULL;

ALTER TABLE "PAT_MOBILIARIO"."TB_CONVENIO" ADD CONSTRAINT "FK_CONCEDENTE_CONVENIO" FOREIGN KEY ("COC_ID") REFERENCES "PAT_MOBILIARIO"."TB_CONCEDENTE" ("COC_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_EMPENHO" ADD CONSTRAINT "FK_EMPENHO_INCORPORACAO" FOREIGN KEY ("IN_ID") REFERENCES "PAT_MOBILIARIO"."TB_INCORPORACAO" ("IN_ID") ON DELETE CASCADE ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_DOCUMENTO" ADD CONSTRAINT "FK_INCORPORACAO_DOCUMENTO" FOREIGN KEY ("IN_ID") REFERENCES "PAT_MOBILIARIO"."TB_INCORPORACAO"  ("IN_ID") ON DELETE CASCADE ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_DOCUMENTO" ADD CONSTRAINT "FK_MOVIMENTACAO_DOCUMENTO" FOREIGN KEY ("MO_ID") REFERENCES "PAT_MOBILIARIO"."TB_MOVIMENTACAO"  ("MO_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_DOCUMENTO" ADD CONSTRAINT "FK_TIPODOC_DOCUMENTO" FOREIGN KEY ("TD_ID") REFERENCES "COMUM_SIGA"."TB_TIPO_DOCUMENTO" ("TD_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_MOVIMENTACAO_ITEM" ADD CONSTRAINT "FK_MOVIMENTACAO_ITEM_MOVIMENTACAO" FOREIGN KEY ("MO_ID") REFERENCES "PAT_MOBILIARIO"."TB_MOVIMENTACAO" ("MO_ID");

ALTER TABLE "PAT_MOBILIARIO"."TB_MOVIMENTACAO_ITEM" ADD CONSTRAINT "FK_MOVIMENTACAO_ITEM_PATRIMONIO" FOREIGN KEY ("PA_ID") REFERENCES "PAT_MOBILIARIO"."TB_PATRIMONIO" ("PA_ID");

ALTER TABLE "PAT_MOBILIARIO"."TB_MOVIMENTACAO" ADD CONSTRAINT "FK_MOVIMENTACAO_ORGAO_ORIGEM" FOREIGN KEY ("UO_ID_ORGAO_ORIGEM") REFERENCES "COMUM_SIGA"."TB_UNIDADE_ORGANIZACIONAL" ("UO_ID");

ALTER TABLE "PAT_MOBILIARIO"."TB_MOVIMENTACAO" ADD CONSTRAINT "FK_MOVIMENTACAO_ORGAO_DESTINO" FOREIGN KEY ("UO_ID_ORGAO_DESTINO") REFERENCES "COMUM_SIGA"."TB_UNIDADE_ORGANIZACIONAL" ("UO_ID");

ALTER TABLE "PAT_MOBILIARIO"."TB_MOVIMENTACAO" ADD CONSTRAINT "FK_MOVIMENTACAO_SETOR_ORIGEM" FOREIGN KEY ("UO_ID_SETOR_ORIGEM") REFERENCES "COMUM_SIGA"."TB_UNIDADE_ORGANIZACIONAL" ("UO_ID");

ALTER TABLE "PAT_MOBILIARIO"."TB_MOVIMENTACAO" ADD CONSTRAINT "FK_MOVIMENTACAO_SETOR_DESTINO" FOREIGN KEY ("UO_ID_SETOR_DESTINO") REFERENCES "COMUM_SIGA"."TB_UNIDADE_ORGANIZACIONAL" ("UO_ID");

ALTER TABLE "PAT_MOBILIARIO"."TB_MOVIMENTACAO" ADD CONSTRAINT "FK_MOVIMENTACAO_NL" FOREIGN KEY ("NLC_ID") REFERENCES "PAT_MOBILIARIO"."TB_NOTA_LANCAMENTO_CONTABIL" ("NLC_ID");

ALTER TABLE "PAT_MOBILIARIO"."TB_MOVIMENTACAO" ADD CONSTRAINT "FK_MOVIMENTACAO_RESPONSAVEL" FOREIGN KEY ("RES_ID") REFERENCES "PAT_MOBILIARIO"."TB_RESPONSAVEL" ("RES_ID");

ALTER TABLE "PAT_MOBILIARIO"."TB_NOTIFICACAO" ADD CONSTRAINT "FK_NOTIFICACAO_USUARIO" FOREIGN KEY ("US_ID") REFERENCES "COMUM"."TB_USUARIO" ("US_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_CONFIG_DEPRECIACAO" ADD CONSTRAINT "FK_CD_CC_ID" FOREIGN KEY ("CC_ID") REFERENCES "COMUM_SIGA"."TB_CONTA_CONTABIL" ("CC_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_DEPRECIACAO" ADD CONSTRAINT "FK_DEPRECIACAO_PATRIMONIO" FOREIGN KEY ("PA_ID") REFERENCES "PAT_MOBILIARIO"."TB_PATRIMONIO" ("PA_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_DEPRECIACAO" ADD CONSTRAINT "FK_DEPRECIACAO_ORGAO" FOREIGN KEY ("UO_ID_ORGAO") REFERENCES "COMUM_SIGA"."TB_UNIDADE_ORGANIZACIONAL" ("UO_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_DEPRECIACAO" ADD CONSTRAINT "FK_DEPRECIACAO_SETOR" FOREIGN KEY ("UO_ID_SETOR") REFERENCES "COMUM_SIGA"."TB_UNIDADE_ORGANIZACIONAL" ("UO_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_DEPRECIACAO" ADD CONSTRAINT "FK_DEPRECIACAO_CONTA_CONTABIL" FOREIGN KEY ("CC_ID") REFERENCES "COMUM_SIGA"."TB_CONTA_CONTABIL" ("CC_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_RESERVA_INTERVALO" ADD CONSTRAINT "FK_RESERVA_RESERVA_INTERVALO" FOREIGN KEY ("RV_ID") REFERENCES "PAT_MOBILIARIO"."TB_RESERVA" ("RV_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_RESERVA_INTERVALO_NUMERO" ADD CONSTRAINT "FK_RESERVA_INTERVALO_RESERVA_INTERVALO_NUMERO" FOREIGN KEY ("RI_ID") REFERENCES "PAT_MOBILIARIO"."TB_RESERVA_INTERVALO" ("RI_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "PAT_MOBILIARIO"."TB_RESERVA_INTERVALO" ADD CONSTRAINT "FK_UNIDADEORG_RESERVA_INTERVALO" FOREIGN KEY ("UO_ID_ORGAO") REFERENCES "COMUM_SIGA"."TB_UNIDADE_ORGANIZACIONAL" ("UO_ID") ON DELETE NO ACTION ON UPDATE NO ACTION;
