
-- Create sequences section -------------------------------------------------

CREATE SEQUENCE pat_mobiliario.SEQ_COMODANTE AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_CONCEDENTE AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_CONFIG_CONTA_CONTABIL AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_CONVENIO AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_DOCUMENTO AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_EMPENHO AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_ESTADO_CONSERVACAO AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_INCORPORACAO AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_INCORPORACAO_ITEM AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_LANCAMENTO_CONTABIL AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_MOVIMENTACAO AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_NOTA_LANCTO_CONTABIL AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_NOTIFICACAO AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_PATRIMONIO AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_RECONHECIMENTO AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_RESPONSAVEL AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_CONFIG_DEPRECIACAO AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.SEQ_DEPRECIACAO AS bigint
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.seq_reserva AS int
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.seq_reserva_intervalo AS int
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

CREATE SEQUENCE pat_mobiliario.seq_reserva_intervalo_numero AS int
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
go

-- Create tables section -------------------------------------------------

-- Table pat_mobiliario.TB_COMODANTE

CREATE TABLE pat_mobiliario.TB_COMODANTE
(
    com_id Int NOT NULL,
    com_nome Varchar(255) NOT NULL,
    com_dthr_cadastro Datetime NULL,
    com_dthr_alteracao Datetime NULL,
    com_usuario_cadastro Varchar(255) NULL,
    com_usuario_alteracao Varchar(255) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'Tabela referente aos comodantes', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_COMODANTE', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'Número de identificação do comodante.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_COMODANTE', 'COLUMN', 'com_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'Nome do comodante.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_COMODANTE', 'COLUMN', 'com_nome'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_COMODANTE', 'COLUMN', 'com_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da última alteração do registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_COMODANTE', 'COLUMN', 'com_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que criou o registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_COMODANTE', 'COLUMN', 'com_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'Último usuário a alterar o registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_COMODANTE', 'COLUMN', 'com_usuario_alteracao'
go

-- Add keys for table pat_mobiliario.TB_COMODANTE

ALTER TABLE pat_mobiliario.TB_COMODANTE ADD CONSTRAINT pk_TB_COMODANTE_id PRIMARY KEY (com_id)
go

-- Table pat_mobiliario.TB_CONCEDENTE

CREATE TABLE pat_mobiliario.TB_CONCEDENTE
(
    coc_id Int NOT NULL,
    coc_nome Varchar(100) NULL,
    coc_situacao Varchar(20) NULL,
    coc_tipo Varchar(20) NULL,
    coc_cpf_cnpj Varchar(20) NULL,
    coc_dthr_cadastro Datetime NULL,
    coc_dthr_alteracao Datetime NULL,
    coc_usuario_cadastro Varchar(20) NULL,
    coc_usuario_alteracao Varchar(20) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'tabela que armazena registros de concedentes.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONCEDENTE', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'chave primária do concedente.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONCEDENTE', 'COLUMN', 'coc_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'nome do concedente.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONCEDENTE', 'COLUMN', 'coc_nome'
go
EXEC sp_addextendedproperty 'MS_Description', 'situação atual do concedente poder ser ativo ou inativo.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONCEDENTE', 'COLUMN', 'coc_situacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'tipo da pessoa concedente pode ser fisica ou juridica.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONCEDENTE', 'COLUMN', 'coc_tipo'
go
EXEC sp_addextendedproperty 'MS_Description', 'cpf/cnpj do concedente.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONCEDENTE', 'COLUMN', 'coc_cpf_cnpj'
go
EXEC sp_addextendedproperty 'MS_Description', 'coluna de auditoria data do cadastro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONCEDENTE', 'COLUMN', 'coc_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'coluna de auditora data última alteração cadastro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONCEDENTE', 'COLUMN', 'coc_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'coluna de auditora usuário que relaizou o cadastro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONCEDENTE', 'COLUMN', 'coc_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'coluna de auditoria usuário que realizou última alteração no registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONCEDENTE', 'COLUMN', 'coc_usuario_alteracao'
go

-- Add keys for table pat_mobiliario.TB_CONCEDENTE

ALTER TABLE pat_mobiliario.TB_CONCEDENTE ADD CONSTRAINT pk_TB_CONCEDENTE PRIMARY KEY (coc_id)
go

-- Table pat_mobiliario.TB_CONFIG_CONTA_CONTABIL

CREATE TABLE pat_mobiliario.TB_CONFIG_CONTA_CONTABIL
(
    ccc_id Int NOT NULL,
    ccc_metodo Varchar(100) NULL,
    ccc_tipo Varchar(100) NULL,
    ccc_tipo_bem Varchar(100) NOT NULL,
    ccc_vida_util_meses Int NULL,
    ccc_percentual_residual Decimal(5,2) NULL,
    cc_id Int NOT NULL,
    ccc_dthr_cadastro Datetime NULL,
    ccc_dthr_alteracao Datetime NULL,
    ccc_usuario_cadastro Varchar(20) NULL,
    ccc_usuario_alteracao Varchar(20) NULL
)
go
EXEC sp_addextendedproperty 'MS_Description', 'chave primária de configuração de conta contábil.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'ccc_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'metodo de depreciação da conta. pode ser quotas_constantes.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'ccc_metodo'
go
EXEC sp_addextendedproperty 'MS_Description', 'tipo da conta. pode ser depreciavel ou nao_depreciavel.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'ccc_tipo'
go
EXEC sp_addextendedproperty 'MS_Description', 'tipo do patrimônio que deve ser associado a conta contábil.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'ccc_tipo_bem'
go
EXEC sp_addextendedproperty 'MS_Description', 'tempo da vida útil da conta contábil em meses.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'ccc_vida_util_meses'
go
EXEC sp_addextendedproperty 'MS_Description', 'percentual residual da conta contábil.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'ccc_percentual_residual'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para conta contábil.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'cc_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'coluna de auditoria data do cadastro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'ccc_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'coluna de auditoria data  da última alteração no registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'ccc_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'coluna de auditora usuário autor do cadastro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'ccc_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'coluna de auditoria usuário que realizou a última alteração no registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'ccc_usuario_alteracao'
go

-- Create indexes for table pat_mobiliario.TB_CONFIG_CONTA_CONTABIL

CREATE INDEX in_config_cc_cc_id ON pat_mobiliario.TB_CONFIG_CONTA_CONTABIL (cc_id)
go

-- Add keys for table pat_mobiliario.TB_CONFIG_CONTA_CONTABIL

ALTER TABLE pat_mobiliario.TB_CONFIG_CONTA_CONTABIL ADD CONSTRAINT pk_TB_CONFIG_CONTA_CONTABIL PRIMARY KEY (ccc_id)
go

-- Table pat_mobiliario.TB_CONVENIO

CREATE TABLE pat_mobiliario.TB_CONVENIO
(
    co_id Int NOT NULL,
    co_dthr_inicio Datetime NULL,
    co_dthr_fim Datetime NULL,
    co_numero Varchar(100) NULL,
    co_nome Varchar(100) NULL,
    co_situacao Varchar(50) NULL,
    co_fonte_recurso Varchar(500) NULL,
    co_tipo Varchar(50) NULL,
    co_dthr_cadastro Datetime NULL,
    co_dthr_alteracao Datetime NULL,
    co_usuario_cadastro Varchar(255) NULL,
    co_usuario_alteracao Varchar(255) NULL,
    coc_id Int NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'tabela que armazena as informações do convênio.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONVENIO', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'código do patrimônio convênio', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONVENIO', 'COLUMN', 'co_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'data inicio do convênio', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONVENIO', 'COLUMN', 'co_dthr_inicio'
go
EXEC sp_addextendedproperty 'MS_Description', 'data final do convênio', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONVENIO', 'COLUMN', 'co_dthr_fim'
go
EXEC sp_addextendedproperty 'MS_Description', 'número do convênio', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONVENIO', 'COLUMN', 'co_numero'
go
EXEC sp_addextendedproperty 'MS_Description', 'Nome do convênio', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONVENIO', 'COLUMN', 'co_nome'
go
EXEC sp_addextendedproperty 'MS_Description', 'Situação do convênio', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONVENIO', 'COLUMN', 'co_situacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'fonte de recurso do convênio', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONVENIO', 'COLUMN', 'co_fonte_recurso'
go
EXEC sp_addextendedproperty 'MS_Description', 'tipo do convênio', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONVENIO', 'COLUMN', 'co_tipo'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda a data de cadastro ', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONVENIO', 'COLUMN', 'co_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda a data de edição', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONVENIO', 'COLUMN', 'co_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda o usuário que realizou o cadastro', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONVENIO', 'COLUMN', 'co_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda o usuário que realizou a edição', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONVENIO', 'COLUMN', 'co_usuario_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira que indica o concedente do convênio.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONVENIO', 'COLUMN', 'coc_id'
go

-- Create indexes for table pat_mobiliario.TB_CONVENIO

CREATE INDEX in_tb_co_coc_id ON pat_mobiliario.TB_CONVENIO (coc_id)
go

-- Add keys for table pat_mobiliario.TB_CONVENIO

ALTER TABLE pat_mobiliario.TB_CONVENIO ADD CONSTRAINT pk_TB_CONVENIO PRIMARY KEY (co_id)
go

-- Table pat_mobiliario.TB_DOCUMENTO

CREATE TABLE pat_mobiliario.TB_DOCUMENTO
(
    do_id Int NOT NULL,
    do_numero Varchar(50) NULL,
    do_dt Datetime NULL,
    do_valor Decimal(20,6) NULL,
    do_uri_anexo Varchar(500) NULL,
    in_id Int NULL,
    mo_id Int NULL,
    td_id Int NOT NULL,
    do_dthr_cadastro Datetime NULL,
    do_dthr_alteracao Datetime NULL,
    do_usuario_cadastro Varchar(255) NULL,
    do_usuario_alteracao Varchar(255) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'Documentos referentes ao bem.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DOCUMENTO', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'Número de identificação do documento.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'do_numero'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data do documento.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'do_dt'
go
EXEC sp_addextendedproperty 'MS_Description', 'Valor do dcocumento.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'do_valor'
go
EXEC sp_addextendedproperty 'MS_Description', 'Caminho para o documento anexado.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'do_uri_anexo'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para a incorporação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'in_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para o tipo de documento.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'td_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'do_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da última alteração do registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'do_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que criou o registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'do_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'Último usuário a alterar o registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'do_usuario_alteracao'
go

-- Create indexes for table pat_mobiliario.TB_DOCUMENTO

CREATE INDEX in_do_in_id ON pat_mobiliario.TB_DOCUMENTO (in_id)
go

CREATE INDEX in_do_td_id ON pat_mobiliario.TB_DOCUMENTO (td_id)
go

-- Add keys for table pat_mobiliario.TB_DOCUMENTO

ALTER TABLE pat_mobiliario.TB_DOCUMENTO ADD CONSTRAINT pk_TB_DOCUMENTO PRIMARY KEY (do_id)
go

-- Table pat_mobiliario.TB_EMPENHO

CREATE TABLE pat_mobiliario.TB_EMPENHO
(
    em_id Int NOT NULL,
    em_dthr Datetime NULL,
    em_valor Decimal(30,6) NULL,
    em_numero Varchar(50) NULL,
    in_id Int NOT NULL,
    em_dthr_cadastro Datetime NULL,
    em_dthr_alteracao Datetime NULL,
    em_usuario_cadastro Varchar(20) NULL,
    em_usuario_alteracao Varchar(20) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'tabela que armazena registros de empenhos.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_EMPENHO', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'chave primária.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_EMPENHO', 'COLUMN', 'em_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'data/hora do empenho.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_EMPENHO', 'COLUMN', 'em_dthr'
go
EXEC sp_addextendedproperty 'MS_Description', 'valor do empenho.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_EMPENHO', 'COLUMN', 'em_valor'
go
EXEC sp_addextendedproperty 'MS_Description', 'número do empenho.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_EMPENHO', 'COLUMN', 'em_numero'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para incorporação a qual o empenho pertence.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_EMPENHO', 'COLUMN', 'in_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria com data/hora cadastro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_EMPENHO', 'COLUMN', 'em_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditora com data/hora da última alteração.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_EMPENHO', 'COLUMN', 'em_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que armazena usuário que realizou o cadastro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_EMPENHO', 'COLUMN', 'em_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que armazena usuário que realizou última alteração.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_EMPENHO', 'COLUMN', 'em_usuario_alteracao'
go

-- Create indexes for table pat_mobiliario.TB_EMPENHO

CREATE INDEX in_em_in_id ON pat_mobiliario.TB_EMPENHO (in_id)
go

-- Add keys for table pat_mobiliario.TB_EMPENHO

ALTER TABLE pat_mobiliario.TB_EMPENHO ADD CONSTRAINT pk_TB_EMPENHO PRIMARY KEY (em_id)
go

-- Table pat_mobiliario.tb_estado_conservacao

CREATE TABLE pat_mobiliario.tb_estado_conservacao
(
    ec_id Int NOT NULL,
    ec_nome Varchar(100) NOT NULL,
    ec_prioridade Int NOT NULL,
    ec_dthr_cadastro Datetime NULL,
    ec_dthr_alteracao Datetime NULL,
    ec_usuario_cadastro Varchar(20) NULL,
    ec_usuario_alteracao Varchar(20) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'tabela com estados de conservação usados pelo item incoporação. deve ter carga inicial.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_estado_conservacao', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'chave primária.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_estado_conservacao', 'COLUMN', 'ec_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'nome do estado de conservação. ex.: bom.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_estado_conservacao', 'COLUMN', 'ec_nome'
go
EXEC sp_addextendedproperty 'MS_Description', 'escala que identifica o estado de conservação do melhor para o pior.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_estado_conservacao', 'COLUMN', 'ec_prioridade'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que armazena data/hora do cadastro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_estado_conservacao', 'COLUMN', 'ec_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que armazena data/hora da última alteração.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_estado_conservacao', 'COLUMN', 'ec_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que armazena usuário que realizou o cadastro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_estado_conservacao', 'COLUMN', 'ec_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que armazena usuário que realizou última alteração.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_estado_conservacao', 'COLUMN', 'ec_usuario_alteracao'
go

-- Add keys for table pat_mobiliario.tb_estado_conservacao

ALTER TABLE pat_mobiliario.tb_estado_conservacao ADD CONSTRAINT pk_tb_estado_conservacao PRIMARY KEY (ec_id)
go

-- Table pat_mobiliario.TB_INCORPORACAO

CREATE TABLE pat_mobiliario.TB_INCORPORACAO
(
    in_id Int NOT NULL,
    in_codigo Varchar(20) NULL,
    in_dthr_recebimento Datetime NULL,
    in_situacao Varchar(50) NULL,
    in_num_processo Varchar(255) NULL,
    in_nota Varchar(30) NULL,
    in_valor_nota Decimal(30,6) NULL,
    in_dthr_nota Datetime NULL,
    in_origem_convenio Bit DEFAULT 0 NULL,
    in_origem_fundo Bit DEFAULT 0 NULL,
    in_origem_projeto Bit DEFAULT 0 NULL,
    in_origem_comodato Bit DEFAULT 0 NULL,
    in_projeto Varchar(100) NULL,
    in_usuario_finalizacao Varchar(255) NULL,
    in_dthr_finalizacao Datetime NULL,
    uo_id_orgao Int NULL,
    uo_id_setor Int NULL,
    uo_id_fundo Int NULL,
    co_id Int NULL,
    re_id Int NULL,
    pe_id Int NULL,
    nlc_id Int NULL,
    com_id Int NULL,
    in_dthr_ini_proc Datetime NULL,
    in_dthr_fim_proc Datetime NULL,
    in_erro_proc Varchar(255) NULL,
    in_dthr_cadastro Datetime NULL,
    in_dthr_alteracao Datetime NULL,
    in_usuario_cadastro Varchar(255) NULL,
    in_usuario_alteracao Varchar(255) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'armazena as informações de um registro de entrada', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'chave primária da tabela registro entrada', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'código único que identifica a incorporação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_codigo'
go
EXEC sp_addextendedproperty 'MS_Description', 'data/hora recebimento da incorporação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_dthr_recebimento'
go
EXEC sp_addextendedproperty 'MS_Description', 'armazena a situação atual do registro de entrada', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_situacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'armazena o número do proceso', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_num_processo'
go
EXEC sp_addextendedproperty 'MS_Description', 'identificador da nota', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_nota'
go
EXEC sp_addextendedproperty 'MS_Description', 'valor da nota', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_valor_nota'
go
EXEC sp_addextendedproperty 'MS_Description', 'data/hora emissão da nota.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_dthr_nota'
go
EXEC sp_addextendedproperty 'MS_Description', 'indica se a incorporação é proveniente de convênio', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_origem_convenio'
go
EXEC sp_addextendedproperty 'MS_Description', 'indica se a incorporação é proveniente de fundo', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_origem_fundo'
go
EXEC sp_addextendedproperty 'MS_Description', 'indica se a incorporação é proveniente de projeto', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_origem_projeto'
go
EXEC sp_addextendedproperty 'MS_Description', 'indica se a incorporação é proveniente de um comodato', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_origem_comodato'
go
EXEC sp_addextendedproperty 'MS_Description', 'armazena o projeto de origem', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_projeto'
go
EXEC sp_addextendedproperty 'MS_Description', 'armazena o usuário que realizou a finalização da incorporação', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_usuario_finalizacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'armazena a data e a hora que a movimentação foi finalizada', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_dthr_finalizacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira que indica o órgão da incoporação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'uo_id_orgao'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira que indica a incorporação ao setor.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'uo_id_setor'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira que indica fundo da incoporação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'uo_id_fundo'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira que indica o convênio da incorporação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'co_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira que indica o tipo de reconhecimento usado na incorporação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 're_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira que indica o fornecedor da incorporação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'pe_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira que indica a nota de lançamento contábil da incorporação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'nlc_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira que indica o comodante responsável pelo comodato.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'com_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'data/hora de início do processamento de finalização da incorporação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_dthr_ini_proc'
go
EXEC sp_addextendedproperty 'MS_Description', 'data/hora fim do processamento de finalização da incorporação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_dthr_fim_proc'
go
EXEC sp_addextendedproperty 'MS_Description', 'mensagem de erro do processamento de finalização da incorporação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_erro_proc'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda a data de cadastro ', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda a data de edição', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda o usuário que realizou o cadastro', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda o usuário que realizou a edição', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO', 'COLUMN', 'in_usuario_alteracao'
go

-- Create indexes for table pat_mobiliario.TB_INCORPORACAO

CREATE INDEX in_in_uo_id_orgao ON pat_mobiliario.TB_INCORPORACAO (uo_id_orgao)
go

CREATE INDEX in_in_co_id ON pat_mobiliario.TB_INCORPORACAO (co_id)
go

CREATE INDEX in_in_re_id ON pat_mobiliario.TB_INCORPORACAO (re_id)
go

CREATE INDEX in_in_pe_id ON pat_mobiliario.TB_INCORPORACAO (pe_id)
go

CREATE INDEX in_in_uo_id_setor ON pat_mobiliario.TB_INCORPORACAO (uo_id_setor)
go

CREATE INDEX in_in_uo_id_fundo ON pat_mobiliario.TB_INCORPORACAO (uo_id_fundo)
go

CREATE INDEX in_in_nlc_id ON pat_mobiliario.TB_INCORPORACAO (nlc_id)
go

CREATE INDEX in_in_com_id ON pat_mobiliario.TB_INCORPORACAO (com_id)
go

-- Add keys for table pat_mobiliario.TB_INCORPORACAO

ALTER TABLE pat_mobiliario.TB_INCORPORACAO ADD CONSTRAINT pk_tb_incorporacao PRIMARY KEY (in_id)
go

-- Table pat_mobiliario.TB_INCORPORACAO_ITEM

CREATE TABLE pat_mobiliario.TB_INCORPORACAO_ITEM
(
    ini_id Int NOT NULL,
    ini_codigo Varchar(100) NULL,
    ini_descricao Text NULL,
    ini_marca Varchar(500) NULL,
    ini_modelo Varchar(500) NULL,
    ini_fabricante Varchar(500) NULL,
    ini_valor_total Decimal(10,2) NULL,
    ini_total_unidades Int NULL,
    ini_tipo Varchar(100) NULL,
    ini_numeracao_patrimonial Varchar(20) NULL,
    ini_uri_imagem Varchar(100) NULL,
    ini_ano_fabricacao_modelo Varchar(50) NULL,
    ini_combustivel Varchar(50) NULL,
    ini_categoria Varchar(50) NULL,
    ini_situacao Varchar(100) NOT NULL,
    ini_depreciavel Bit NULL,
    in_id Int NOT NULL,
    cc_id Int NULL,
    nd_id Int NULL,
    ec_id Int NULL,
    ini_dthr_cadastro Datetime NULL,
    ini_dthr_alteracao Datetime NULL,
    ini_usuario_cadastro Varchar(255) NULL,
    ini_usuario_alteracao Varchar(255) NULL,
    CD_ID Int NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'tabela que armazena os itens de uma incorporação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'código do registro de entrada do item', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'código do item proveniente do item selecionado do catálogo.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_codigo'
go
EXEC sp_addextendedproperty 'MS_Description', 'descrição do item proveniente do item selecionado do catálogo.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_descricao'
go
EXEC sp_addextendedproperty 'MS_Description', 'marca do item.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_marca'
go
EXEC sp_addextendedproperty 'MS_Description', 'modelo do item.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_modelo'
go
EXEC sp_addextendedproperty 'MS_Description', 'fabricante do item.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_fabricante'
go
EXEC sp_addextendedproperty 'MS_Description', 'valor total dos itens', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_valor_total'
go
EXEC sp_addextendedproperty 'MS_Description', 'quantidade total de itens', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_total_unidades'
go
EXEC sp_addextendedproperty 'MS_Description', 'tipo do item. pode ser armamento, equipamento, movel ou veiculo.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_tipo'
go
EXEC sp_addextendedproperty 'MS_Description', 'tipo da numeração patrimonial usada. pode ser automática ou reserva.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_numeracao_patrimonial'
go
EXEC sp_addextendedproperty 'MS_Description', 'uri da imagem salva no mongo.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_uri_imagem'
go
EXEC sp_addextendedproperty 'MS_Description', 'ano  e modelo de fabricação do item.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_ano_fabricacao_modelo'
go
EXEC sp_addextendedproperty 'MS_Description', 'combustível utilizado nos itens do tipo automóveis.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_combustivel'
go
EXEC sp_addextendedproperty 'MS_Description', 'categoria do item.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_categoria'
go
EXEC sp_addextendedproperty 'MS_Description', 'situação do item. pode ser em_elaboracao ou finalizado.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_situacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'indica se item é depreciável.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_depreciavel'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo referente a chave estrangeira para a tabela registro entrada', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'in_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para conta contábil associada ao item.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'cc_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para natureza de despesa associada ao item.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'nd_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para estado de conservação do item.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ec_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda a data de cadastro ', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda a data de edição', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda o usuário que realizou o cadastro', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda o usuário que realizou a edição', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_INCORPORACAO_ITEM', 'COLUMN', 'ini_usuario_alteracao'
go

-- Create indexes for table pat_mobiliario.TB_INCORPORACAO_ITEM

CREATE INDEX in_ini_in_id ON pat_mobiliario.TB_INCORPORACAO_ITEM (in_id)
go

CREATE INDEX in_ini_cc_id ON pat_mobiliario.TB_INCORPORACAO_ITEM (cc_id)
go

CREATE INDEX in_ini_nd_id ON pat_mobiliario.TB_INCORPORACAO_ITEM (nd_id)
go

CREATE INDEX in_ini_ec_id ON pat_mobiliario.TB_INCORPORACAO_ITEM (ec_id)
go

CREATE INDEX IN_INI_CD_ID ON pat_mobiliario.TB_INCORPORACAO_ITEM (CD_ID)
go

-- Add keys for table pat_mobiliario.TB_INCORPORACAO_ITEM

ALTER TABLE pat_mobiliario.TB_INCORPORACAO_ITEM ADD CONSTRAINT pk_tb_incorporacao_item PRIMARY KEY (ini_id)
go

-- Table pat_mobiliario.TB_LANCAMENTO_CONTABIL

CREATE TABLE pat_mobiliario.TB_LANCAMENTO_CONTABIL
(
    lc_id Bigint NOT NULL,
    lc_tipo_lancamento Varchar(20) NOT NULL,
    lc_data_lancamento Datetime NOT NULL,
    lc_valor Decimal(30,6) NOT NULL,
    lc_tipo_movimentacao Varchar(50) NOT NULL,
    uo_id_orgao Int NOT NULL,
    uo_id_setor Int NOT NULL,
    cc_id Int NOT NULL,
    pa_id Bigint NOT NULL,
    in_id Int NULL,
    mo_id Int NULL,
    lc_dthr_cadastro Datetime NULL,
    lc_dthr_alteracao Datetime NULL,
    lc_usuario_cadastro Varchar(20) NULL,
    lc_usuario_alteracao Varchar(20) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'tabela responsável por armazenar lançamentos contábeis.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_LANCAMENTO_CONTABIL', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'chave primária de lançamento contábil', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_LANCAMENTO_CONTABIL', 'COLUMN', 'lc_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'tipo de lançamento contábil. pode assumir os valores credito ou debito.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_LANCAMENTO_CONTABIL', 'COLUMN', 'lc_tipo_lancamento'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo que irá guardar a data do lançamento contábil, em casos de lançamentos retroativos irá receber a data retroativa', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_LANCAMENTO_CONTABIL', 'COLUMN', 'lc_data_lancamento'
go
EXEC sp_addextendedproperty 'MS_Description', 'valor monetário referente ao lançamento contábil.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_LANCAMENTO_CONTABIL', 'COLUMN', 'lc_valor'
go
EXEC sp_addextendedproperty 'MS_Description', 'tipo da movimentação que deu origem ao lançamento contábil.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_LANCAMENTO_CONTABIL', 'COLUMN', 'lc_tipo_movimentacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para o órgão no qual o lançamento contábil foi efetuado.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_LANCAMENTO_CONTABIL', 'COLUMN', 'uo_id_orgao'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para o setor no qual o lançamento contábil foi efetuado.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_LANCAMENTO_CONTABIL', 'COLUMN', 'uo_id_setor'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para a conta contábil.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_LANCAMENTO_CONTABIL', 'COLUMN', 'cc_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para o patrimônio.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_LANCAMENTO_CONTABIL', 'COLUMN', 'pa_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para a incorporação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_LANCAMENTO_CONTABIL', 'COLUMN', 'in_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para a movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_LANCAMENTO_CONTABIL', 'COLUMN', 'mo_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que armazena data/hora do cadastro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_LANCAMENTO_CONTABIL', 'COLUMN', 'lc_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que armazena data/hora da última alteração.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_LANCAMENTO_CONTABIL', 'COLUMN', 'lc_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que armazena usuário que realizou o cadastro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_LANCAMENTO_CONTABIL', 'COLUMN', 'lc_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que armazena usuário que realizou última alteração.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_LANCAMENTO_CONTABIL', 'COLUMN', 'lc_usuario_alteracao'
go

-- Create indexes for table pat_mobiliario.TB_LANCAMENTO_CONTABIL

CREATE INDEX in_lc_uo_id_orgao ON pat_mobiliario.TB_LANCAMENTO_CONTABIL (uo_id_orgao)
go

CREATE INDEX in_lc_uo_setor ON pat_mobiliario.TB_LANCAMENTO_CONTABIL (uo_id_setor)
go

CREATE INDEX in_lc_cc_id ON pat_mobiliario.TB_LANCAMENTO_CONTABIL (cc_id)
go

CREATE INDEX in_lc_pa_id ON pat_mobiliario.TB_LANCAMENTO_CONTABIL (pa_id)
go

CREATE INDEX in_lc_in_id ON pat_mobiliario.TB_LANCAMENTO_CONTABIL (in_id)
go

CREATE INDEX in_lc_mo_id ON pat_mobiliario.TB_LANCAMENTO_CONTABIL (mo_id)
go

-- Add keys for table pat_mobiliario.TB_LANCAMENTO_CONTABIL

ALTER TABLE pat_mobiliario.TB_LANCAMENTO_CONTABIL ADD CONSTRAINT pk_tb_lancamento_contabil PRIMARY KEY (lc_id)
go

-- Table pat_mobiliario.TB_MOVIMENTACAO

CREATE TABLE pat_mobiliario.TB_MOVIMENTACAO
(
    mo_id Int NOT NULL,
    mo_codigo Varchar(20) NULL,
    mo_tipo Varchar(50) NULL,
    mo_situacao Varchar(50) NOT NULL,
    mo_numero_processo Varchar(100) NULL,
    mo_motivo_obs Varchar(500) NULL,
    mo_usuario_criacao Varchar(20) NULL,
    mo_usuario_finalizacao Varchar(20) NULL,
    mo_dthr_finalizacao Datetime NULL,
    mo_dthr_devolucao Datetime NULL,
    mo_dthr_envio Datetime NULL,
    mo_numero_termo Varchar(20) NULL,
    uo_id_orgao_origem Int NULL,
    uo_id_orgao_destino Int NULL,
    uo_id_setor_origem Int NULL,
    uo_id_setor_destino Int NULL,
    nlc_id Int NULL,
    res_id Int NULL,
    mo_dthr_ini_proc Datetime NULL,
    mo_dthr_fim_proc Datetime NULL,
    mo_erro_proc Varchar(255) NULL,
    mo_dthr_cadastro Datetime NULL,
    mo_dthr_alteracao Datetime NULL,
    mo_usuario_cadastro Varchar(20) NULL,
    mo_usuario_alteracao Varchar(20) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'tabela responsável por registrar as movimentações de um patrimônio.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'chave primário da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'código da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_codigo'
go
EXEC sp_addextendedproperty 'MS_Description', 'tipo da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_tipo'
go
EXEC sp_addextendedproperty 'MS_Description', 'possíveis situações que a movimentação pode se encontrar. pode assumir os valores:em_elaboracao,aguardando_recebimento,finalizado ou rejeitado', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_situacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'número de processo da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_numero_processo'
go
EXEC sp_addextendedproperty 'MS_Description', 'Motivo/Obs da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_motivo_obs'
go
EXEC sp_addextendedproperty 'MS_Description', 'usuário responsável pela movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_usuario_criacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'usuário responsável pela finalização da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_usuario_finalizacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'data e hora de finalização da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_dthr_finalizacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'data e hora da devolução da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_dthr_devolucao'
go
EXEC sp_addextendedproperty 'MS_Description', 'data e hora do envio da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_dthr_envio'
go
EXEC sp_addextendedproperty 'MS_Description', 'Número do termo de responsabilidade gerado para movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_numero_termo'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para órgão origem da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'uo_id_orgao_origem'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para órgão destino da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'uo_id_orgao_destino'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para setor origem da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'uo_id_setor_origem'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para setor destino da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'uo_id_setor_destino'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para nota de lançamento contábil.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'nlc_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para o responsável.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'res_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'data/hora de início do processamento de finalização da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_dthr_ini_proc'
go
EXEC sp_addextendedproperty 'MS_Description', 'data/hora fim do processamento de finalização da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_dthr_fim_proc'
go
EXEC sp_addextendedproperty 'MS_Description', 'mensagem de erro do processamento de finalização da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_erro_proc'
go
EXEC sp_addextendedproperty 'MS_Description', 'data e hora do cadastro da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'data e hora da alteração da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'usuário responsável pelo cadastro da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'usuário responsável pela alteração da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'mo_usuario_alteracao'
go

-- Create indexes for table pat_mobiliario.TB_MOVIMENTACAO

CREATE INDEX in_mo_orgao_origem ON pat_mobiliario.TB_MOVIMENTACAO (uo_id_orgao_origem)
go

CREATE INDEX in_mo_orgao_destino ON pat_mobiliario.TB_MOVIMENTACAO (uo_id_orgao_destino)
go

CREATE INDEX in_mo_setor_origem ON pat_mobiliario.TB_MOVIMENTACAO (uo_id_setor_origem)
go

CREATE INDEX in_mo_setor_destino ON pat_mobiliario.TB_MOVIMENTACAO (uo_id_setor_destino)
go

CREATE INDEX in_mo_nlc_id ON pat_mobiliario.TB_MOVIMENTACAO (nlc_id)
go

CREATE INDEX in_do_mo_id ON pat_mobiliario.TB_MOVIMENTACAO (mo_id)
go

-- Add keys for table pat_mobiliario.TB_MOVIMENTACAO

ALTER TABLE pat_mobiliario.TB_MOVIMENTACAO ADD CONSTRAINT pk_tb_movimentacao PRIMARY KEY (mo_id)
go

ALTER TABLE pat_mobiliario.TB_MOVIMENTACAO ADD CONSTRAINT tb_movimentacao_mo_numero_termo_key UNIQUE  (mo_numero_termo)
go

-- Table pat_mobiliario.TB_MOVIMENTACAO_ITEM

CREATE TABLE pat_mobiliario.TB_MOVIMENTACAO_ITEM
(
    mo_id Int NOT NULL,
    pa_id Bigint NOT NULL,
    mi_dthr_devolucao Datetime NULL
)
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para a movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO_ITEM', 'COLUMN', 'mo_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para o patrimônio.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO_ITEM', 'COLUMN', 'pa_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'data e hora da devolução do patrimônio da movimentação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_MOVIMENTACAO_ITEM', 'COLUMN', 'mi_dthr_devolucao'
go

-- Create indexes for table pat_mobiliario.TB_MOVIMENTACAO_ITEM

CREATE INDEX in_moi_mo ON pat_mobiliario.TB_MOVIMENTACAO_ITEM (mo_id)
go

CREATE INDEX in_moi_pat ON pat_mobiliario.TB_MOVIMENTACAO_ITEM (pa_id)
go

-- Add keys for table pat_mobiliario.TB_MOVIMENTACAO_ITEM

ALTER TABLE pat_mobiliario.TB_MOVIMENTACAO_ITEM ADD CONSTRAINT pk_tb_movimentacao_item PRIMARY KEY (mo_id,pa_id)
go

-- Table pat_mobiliario.TB_NOTA_LANCAMENTO_CONTABIL

CREATE TABLE pat_mobiliario.TB_NOTA_LANCAMENTO_CONTABIL
(
    nlc_id Int NOT NULL,
    nlc_numero Varchar(12) NULL,
    nlc_dthr_lancamento Datetime NULL,
    nlc_dthr_cadastro Datetime NULL,
    nlc_dthr_alteracao Datetime NULL,
    nlc_usuario_cadastro Varchar(255) NULL,
    nlc_usuario_alteracao Varchar(255) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'armazena as informações de um registro de nota de lançamento contábil', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTA_LANCAMENTO_CONTABIL', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'chave primária da tabela nota de lançamento', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTA_LANCAMENTO_CONTABIL', 'COLUMN', 'nlc_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'armazena o número da nota de lançamento contábil', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTA_LANCAMENTO_CONTABIL', 'COLUMN', 'nlc_numero'
go
EXEC sp_addextendedproperty 'MS_Description', 'data/hora em que foi realizado o lançamento contábil', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTA_LANCAMENTO_CONTABIL', 'COLUMN', 'nlc_dthr_lancamento'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda a data de cadastro ', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTA_LANCAMENTO_CONTABIL', 'COLUMN', 'nlc_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda a data de edição', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTA_LANCAMENTO_CONTABIL', 'COLUMN', 'nlc_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda o usuário que realizou o cadastro', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTA_LANCAMENTO_CONTABIL', 'COLUMN', 'nlc_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda o usuário que realizou a edição', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTA_LANCAMENTO_CONTABIL', 'COLUMN', 'nlc_usuario_alteracao'
go

-- Add keys for table pat_mobiliario.TB_NOTA_LANCAMENTO_CONTABIL

ALTER TABLE pat_mobiliario.TB_NOTA_LANCAMENTO_CONTABIL ADD CONSTRAINT pk_tb_nota_lancamento_contabil PRIMARY KEY (nlc_id)
go

-- Table pat_mobiliario.TB_NOTIFICACAO

CREATE TABLE pat_mobiliario.TB_NOTIFICACAO
(
    no_id Int NOT NULL,
    no_origem Varchar(50) NOT NULL,
    no_origem_id Int NULL,
    no_assunto Varchar(100) NOT NULL,
    no_mensagem Varchar(255) NOT NULL,
    no_dthr_criacao Datetime NULL,
    no_visualizada Bit DEFAULT 0 NULL,
    us_id Int NOT NULL,
    no_dthr_cadastro Datetime NULL,
    no_dthr_alteracao Datetime NULL,
    no_usuario_cadastro Varchar(255) NULL,
    no_usuario_alteracao Varchar(255) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'Tabela que armazena as notificações para os usuários do sistema.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTIFICACAO', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'Número de identificação da notificação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTIFICACAO', 'COLUMN', 'no_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'Origem da notificação, ex.: INCORPORACAO, DISTRIBUICAO', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTIFICACAO', 'COLUMN', 'no_origem'
go
EXEC sp_addextendedproperty 'MS_Description', 'Id da origem da notificação', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTIFICACAO', 'COLUMN', 'no_origem_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'Assunto da notificação', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTIFICACAO', 'COLUMN', 'no_assunto'
go
EXEC sp_addextendedproperty 'MS_Description', 'Mensagem da notificação', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTIFICACAO', 'COLUMN', 'no_mensagem'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da criação da notificação', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTIFICACAO', 'COLUMN', 'no_dthr_criacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'Armazena se a notificação foi visualizada', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTIFICACAO', 'COLUMN', 'no_visualizada'
go
EXEC sp_addextendedproperty 'MS_Description', 'Armazena o usuário que receberá a notificação', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTIFICACAO', 'COLUMN', 'us_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTIFICACAO', 'COLUMN', 'no_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da última alteração do registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTIFICACAO', 'COLUMN', 'no_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que criou o registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTIFICACAO', 'COLUMN', 'no_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que alterou o registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_NOTIFICACAO', 'COLUMN', 'no_usuario_alteracao'
go

-- Create indexes for table pat_mobiliario.TB_NOTIFICACAO

CREATE INDEX in_notificacao_usuario ON pat_mobiliario.TB_NOTIFICACAO (us_id)
go

-- Add keys for table pat_mobiliario.TB_NOTIFICACAO

ALTER TABLE pat_mobiliario.TB_NOTIFICACAO ADD CONSTRAINT pk_tb_notificacao PRIMARY KEY (no_id)
go

-- Table pat_mobiliario.TB_PATRIMONIO

CREATE TABLE pat_mobiliario.TB_PATRIMONIO
(
    pa_id bigint NOT NULL,
    pa_placa Varchar(20) NULL,
    pa_renavam Varchar(20) NULL,
    pa_uri_imagem Varchar(100) NULL,
    pa_licenciamento Varchar(20) NULL,
    pa_motor Varchar(20) NULL,
    pa_chassi Varchar(20) NULL,
    pa_numero Varchar(30) NOT NULL,
    pa_numero_serie Varchar(255) NULL,
    pa_valor_liquido Decimal(14,3) NULL,
    pa_valor_residual Decimal(14,3) NULL,
    pa_valor_aquisicao Decimal(14,3) NOT NULL,
    pa_valor_depreciacao_mensal Decimal(14,3) NULL,
    pa_valor_entrada Decimal(14,3) NULL,
    pa_diferenca_dizima Bit NULL,
    pa_dthr_inicio_vida_util Datetime NULL,
    pa_dthr_fim_vida_util Datetime NULL,
    pa_situacao Varchar(20) NULL,
    pa_depreciavel Bit NULL,
    pa_motivo_estorno Varchar(500) NULL,
    pa_projeto Varchar(100) NULL,
    pa_dthr_estorno Datetime NULL,
    pa_usuario_estorno Varchar(255) NULL,
    uo_id_orgao Int NULL,
    uo_id_setor Int NULL,
    uo_id_fundo Int NULL,
    co_id Int NULL,
    nd_id Int NULL,
    ec_id Int NULL,
    cc_id_classificacao Int NULL,
    cc_id_atual Int NULL,
    ini_id Int NOT NULL,
    com_id Int NULL,
    pa_dthr_cadastro Datetime NULL,
    pa_dthr_alteracao Datetime NULL,
    pa_usuario_cadastro Varchar(255) NULL,
    pa_usuario_alteracao Varchar(255) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'tabela que armazena as informações de um patrimônio', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'identificador do patrimônio', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'número da placa para patrimônios do tipo veículo.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_placa'
go
EXEC sp_addextendedproperty 'MS_Description', 'identificação do registro nacional de veículos automotores para patrimônios do tipo veículo.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_renavam'
go
EXEC sp_addextendedproperty 'MS_Description', 'imagem do patrimônio.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_uri_imagem'
go
EXEC sp_addextendedproperty 'MS_Description', 'identificação de licenciamento para patrimônios do tipo veículo.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_licenciamento'
go
EXEC sp_addextendedproperty 'MS_Description', 'motor para patrimônios do tipo veículo.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_motor'
go
EXEC sp_addextendedproperty 'MS_Description', 'número de chassi para patrimônios do tipo veículo.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_chassi'
go
EXEC sp_addextendedproperty 'MS_Description', 'número do patrimônio', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_numero'
go
EXEC sp_addextendedproperty 'MS_Description', 'número de sério do patrimônio', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_numero_serie'
go
EXEC sp_addextendedproperty 'MS_Description', 'valor líquido do patrimônio.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_valor_liquido'
go
EXEC sp_addextendedproperty 'MS_Description', 'valor residual do patrimônio.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_valor_residual'
go
EXEC sp_addextendedproperty 'MS_Description', 'valor de aquisição do patrimônio.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_valor_aquisicao'
go
EXEC sp_addextendedproperty 'MS_Description', 'Campo que armazena o valor de entrada de um patrimonio', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_valor_entrada'
go
EXEC sp_addextendedproperty 'MS_Description', 'Campo que indica se valor de aquisição possui arredondamento de dizimas.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_diferenca_dizima'
go
EXEC sp_addextendedproperty 'MS_Description', 'data inicial da vida útil', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_dthr_inicio_vida_util'
go
EXEC sp_addextendedproperty 'MS_Description', 'data final da vida útil', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_dthr_fim_vida_util'
go
EXEC sp_addextendedproperty 'MS_Description', 'indica a situação do patrimônio.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_situacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'indica se o patrimônio não é depreciável', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_depreciavel'
go
EXEC sp_addextendedproperty 'MS_Description', 'armazena o motivo do estorno', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_motivo_estorno'
go
EXEC sp_addextendedproperty 'MS_Description', 'armazena o nome do projeto de origem da incorporação', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_projeto'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/hora em que o patrimônio foi estornado.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_dthr_estorno'
go
EXEC sp_addextendedproperty 'MS_Description', 'armazena o nome do Usuario que estornou', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_usuario_estorno'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira que indica o órgao ao qual o patrimônio pertence.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'uo_id_orgao'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave que indica o setor ao qual o patrimônio pertence.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'uo_id_setor'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira que indica fundo do patrimônio.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'uo_id_fundo'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira que indica o convênio do patrimonio.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'co_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para a natureza de Despesa', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'nd_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para o estado de conservação do item.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'ec_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'código da conta contábil de classificação', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'cc_id_classificacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'código da conta contábil atual', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'cc_id_atual'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira para item incorporação do patrimônio.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'ini_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'chave estrangeira que indica o comodante responsável pelo comodato.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'com_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que armazena data/hora em que o cadastro foi realizado.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda a data de edição', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda o usuário que realizou o cadastro', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda o usuário que realizou a edição', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'pa_usuario_alteracao'
go

-- Create indexes for table pat_mobiliario.TB_PATRIMONIO

CREATE INDEX in_pa_num_patrimonio ON pat_mobiliario.TB_PATRIMONIO (pa_numero)
go

CREATE INDEX in_pa_cc_id_classificacao ON pat_mobiliario.TB_PATRIMONIO (cc_id_classificacao)
go

CREATE INDEX in_pa_cc_id_atual ON pat_mobiliario.TB_PATRIMONIO (cc_id_atual)
go

CREATE INDEX in_pa_ini_id ON pat_mobiliario.TB_PATRIMONIO (ini_id)
go

CREATE INDEX in_pa_uo_id_orgao ON pat_mobiliario.TB_PATRIMONIO (uo_id_orgao)
go

CREATE INDEX in_pa_uo_id_setor ON pat_mobiliario.TB_PATRIMONIO (uo_id_setor)
go

CREATE INDEX in_pa_uo_id_fundo ON pat_mobiliario.TB_PATRIMONIO (uo_id_fundo)
go

CREATE INDEX in_pa_co_id ON pat_mobiliario.TB_PATRIMONIO (co_id)
go

CREATE INDEX in_pa_nd_id ON pat_mobiliario.TB_PATRIMONIO (nd_id)
go

CREATE INDEX in_pa_ec_id ON pat_mobiliario.TB_PATRIMONIO (ec_id)
go

CREATE INDEX in_pa_com_id ON pat_mobiliario.TB_PATRIMONIO (com_id)
go

-- Add keys for table pat_mobiliario.TB_PATRIMONIO

ALTER TABLE pat_mobiliario.TB_PATRIMONIO ADD CONSTRAINT pk_patrimonio PRIMARY KEY (pa_id)
go

-- Table pat_mobiliario.TB_RECONHECIMENTO

CREATE TABLE pat_mobiliario.TB_RECONHECIMENTO
(
    re_id Int NOT NULL,
    re_nome Varchar(100) NULL,
    re_execucao_orcamentaria Bit NULL,
    re_nota_fical Bit NULL,
    re_empenho Bit NULL,
    re_situacao Varchar(100) NULL,
    re_dthr_alteracao Datetime NULL,
    re_dthr_cadastro Datetime NULL,
    re_usuario_cadastro Varchar(255) NULL,
    re_usuario_alteracao Varchar(255) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'tabela que armazena os registros de reconhecimento.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RECONHECIMENTO', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'chave primária de reconhecimento', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RECONHECIMENTO', 'COLUMN', 're_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'nome do reconhecimento', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RECONHECIMENTO', 'COLUMN', 're_nome'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo booleano que indica se reconhecimento é do tipo de execução orçamentária', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RECONHECIMENTO', 'COLUMN', 're_execucao_orcamentaria'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo que indica obrigatoriedade da nota fiscal na incorporação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RECONHECIMENTO', 'COLUMN', 're_nota_fical'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo que indica a obrigatoriedade de empenho na incorporação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RECONHECIMENTO', 'COLUMN', 're_empenho'
go
EXEC sp_addextendedproperty 'MS_Description', 'situação de reconhecimento, pode assumir o valor ativo ou inativo', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RECONHECIMENTO', 'COLUMN', 're_situacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda data/hora da última alteração', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RECONHECIMENTO', 'COLUMN', 're_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda data/hora do cadastro', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RECONHECIMENTO', 'COLUMN', 're_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda usuário que realizou o cadastro', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RECONHECIMENTO', 'COLUMN', 're_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'campo de auditoria que guarda usuário que realizou última alteração', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RECONHECIMENTO', 'COLUMN', 're_usuario_alteracao'
go

-- Add keys for table pat_mobiliario.TB_RECONHECIMENTO

ALTER TABLE pat_mobiliario.TB_RECONHECIMENTO ADD CONSTRAINT pk_tb_reconhecimento PRIMARY KEY (re_id)
go

-- Table pat_mobiliario.TB_RESPONSAVEL

CREATE TABLE pat_mobiliario.TB_RESPONSAVEL
(
    res_id Int NOT NULL,
    res_nome Varchar(255) NOT NULL,
    res_dthr_cadastro Datetime NULL,
    res_dthr_alteracao Datetime NULL,
    res_usuario_cadastro Varchar(255) NULL,
    res_usuario_alteracao Varchar(255) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'Tabela referente ao responsável da movimentação', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RESPONSAVEL', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'Número de identificação do responsável.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RESPONSAVEL', 'COLUMN', 'res_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'Nome do responsável.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RESPONSAVEL', 'COLUMN', 'res_nome'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RESPONSAVEL', 'COLUMN', 'res_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da última alteração do registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RESPONSAVEL', 'COLUMN', 'res_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que criou o registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RESPONSAVEL', 'COLUMN', 'res_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'Último usuário a alterar o registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_RESPONSAVEL', 'COLUMN', 'res_usuario_alteracao'
go

-- Add keys for table pat_mobiliario.TB_RESPONSAVEL

ALTER TABLE pat_mobiliario.TB_RESPONSAVEL ADD CONSTRAINT pk_tb_responsavel_id PRIMARY KEY (res_id)
go

-- Table pat_mobiliario.TB_CONFIG_DEPRECIACAO

CREATE TABLE pat_mobiliario.TB_CONFIG_DEPRECIACAO
(
    CD_ID Int NOT NULL,
    CD_VIDA_UTIL_MESES Int NULL,
    CD_PERCENTUAL_RESIDUAL Decimal(5,3) NULL,
    CD_DEPRECIAVEL Bit NOT NULL,
    cc_id Int NULL
)
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave primária de configuração de depreciação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONFIG_DEPRECIACAO', 'COLUMN', 'CD_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Vida útil em meses da configuração conta contábil.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONFIG_DEPRECIACAO', 'COLUMN', 'CD_VIDA_UTIL_MESES'
go
EXEC sp_addextendedproperty 'MS_Description', 'Percentual residual associado a configuração da conta.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONFIG_DEPRECIACAO', 'COLUMN', 'CD_PERCENTUAL_RESIDUAL'
go
EXEC sp_addextendedproperty 'MS_Description', 'Campo booleano que indica se configuração é depreciável ou não.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONFIG_DEPRECIACAO', 'COLUMN', 'CD_DEPRECIAVEL'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para a conta contábil que a configuração esta associada.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_CONFIG_DEPRECIACAO', 'COLUMN', 'cc_id'
go

-- Create indexes for table pat_mobiliario.TB_CONFIG_DEPRECIACAO

CREATE INDEX IN_CONFIG_DEPRE_CC_ID ON pat_mobiliario.TB_CONFIG_DEPRECIACAO (cc_id)
go

-- Add keys for table pat_mobiliario.TB_CONFIG_DEPRECIACAO

ALTER TABLE pat_mobiliario.TB_CONFIG_DEPRECIACAO ADD CONSTRAINT PK_TB_CONFIG_DEPRECIACAO PRIMARY KEY (CD_ID)
go

-- Table pat_mobiliario.TB_DEPRECIACAO

CREATE TABLE pat_mobiliario.TB_DEPRECIACAO
(
    DE_ID Int NOT NULL,
    DE_VALOR_ANTERIOR Decimal(14,3) NOT NULL,
    DE_VALOR_SUBTRAIDO Decimal(14,3) NOT NULL,
    DE_VALOR_POSTERIOR Decimal(14,3) NOT NULL,
    DE_TAXA_APLICADA Decimal(5,3) NOT NULL,
    DE_DTHR_INICIAL Datetime NOT NULL,
    DE_DTHR_FINAL Datetime NOT NULL,
    DE_MES_REFERENCIA Varchar(7) NOT NULL,
    PA_ID BigInt NULL,
    UO_ID_ORGAO Int NULL,
    UO_ID_SETOR Int NULL,
    CC_ID Int NULL,
    DE_DTHR_CADASTRO Datetime NULL,
    DE_DTHR_ALTERACAO Datetime NULL,
    DE_USUARIO_CADASTRO Varchar(255) NULL,
    DE_USUARIO_ALTERACAO Varchar(255) NULL
)
go
EXEC sp_addextendedproperty 'MS_Description', 'Valor liquido anterior a depreciação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DEPRECIACAO', 'COLUMN', 'DE_VALOR_ANTERIOR'
go
EXEC sp_addextendedproperty 'MS_Description', 'Valor subtraido referente a depreciação mensal.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DEPRECIACAO', 'COLUMN', 'DE_VALOR_SUBTRAIDO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Valor liquido após a depreciação mensal.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DEPRECIACAO', 'COLUMN', 'DE_VALOR_POSTERIOR'
go
EXEC sp_addextendedproperty 'MS_Description', 'Taxa de depreciação aplicada.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DEPRECIACAO', 'COLUMN', 'DE_TAXA_APLICADA'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data referente ao início do período depreciado no mês.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DEPRECIACAO', 'COLUMN', 'DE_DTHR_INICIAL'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data referente ao fim do período depreciado no mês.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DEPRECIACAO', 'COLUMN', 'DE_DTHR_FINAL'
go
EXEC sp_addextendedproperty 'MS_Description', 'Mês/Ano de referência para a depreciação. Este mês deve ser idêntico ao mês contido nos campos de_dthr_inicio  e  de_dthr_fim', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DEPRECIACAO', 'COLUMN', 'DE_MES_REFERENCIA'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para patrimônio depreciado.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DEPRECIACAO', 'COLUMN', 'PA_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para órgão referente a depreciação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DEPRECIACAO', 'COLUMN', 'UO_ID_ORGAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para o setor em que o patrimônio está localizado no momento da depreciação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DEPRECIACAO', 'COLUMN', 'UO_ID_SETOR'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para conta contábil referente a depreciação.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DEPRECIACAO', 'COLUMN', 'CC_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Campo de auditoria que guarda data/hora do cadastro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DEPRECIACAO', 'COLUMN', 'DE_DTHR_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Campo de auditoria que guarda data/hora da última alteração.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DEPRECIACAO', 'COLUMN', 'DE_DTHR_ALTERACAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Campo de auditoria que guarda usuário que realizou o cadastro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DEPRECIACAO', 'COLUMN', 'DE_USUARIO_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Campo de auditoria que guarda usuário que realizou última alteração.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'TB_DEPRECIACAO', 'COLUMN', 'DE_USUARIO_ALTERACAO'
go

-- Create indexes for table pat_mobiliario.TB_DEPRECIACAO

CREATE INDEX IN_DEPRECIACAO_PA_ID ON pat_mobiliario.TB_DEPRECIACAO (pa_id)
go

CREATE INDEX IN_DEPRECIACAO_UO_ID_ORGAO ON pat_mobiliario.TB_DEPRECIACAO (uo_id_orgao)
go

CREATE INDEX IN_DEPRECIACAO_UO_ID_SETOR ON pat_mobiliario.TB_DEPRECIACAO (uo_id_setor)
go

CREATE INDEX IN_DEPRECIACAO_CC_ID ON pat_mobiliario.TB_DEPRECIACAO (cc_id)
go

-- Add keys for table pat_mobiliario.TB_DEPRECIACAO

ALTER TABLE pat_mobiliario.TB_DEPRECIACAO ADD CONSTRAINT PK_TB_DEPRECIACAO PRIMARY KEY (DE_ID)
go

-- Table pat_mobiliario.tb_reserva

CREATE TABLE pat_mobiliario.tb_reserva
(
    rv_id bigint NOT NULL,
    rv_codigo Varchar(10) NOT NULL,
    rv_situacao Varchar(20) NOT NULL,
    rv_preenchimento Varchar(10) NOT NULL,
    rv_dthr_criacao Datetime NOT NULL,
    rv_qtd_reservada bigint NULL,
    rv_qtd_restante bigint NULL,
    rv_nro_inicio bigint NULL,
    rv_nro_fim bigint NULL,
    rv_dthr_cadastro Datetime NULL,
    rv_dthr_alteracao Datetime NULL,
    rv_usuario_cadastro Varchar(255) NULL,
    rv_usuario_alteracao Varchar(255) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'Id da tabela reserva.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva', 'COLUMN', 'rv_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'Código da reserva.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva', 'COLUMN', 'rv_codigo'
go
EXEC sp_addextendedproperty 'MS_Description', 'Situação da reserva.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva', 'COLUMN', 'rv_situacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'Tipo de preenchimento automático ou manual.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva', 'COLUMN', 'rv_preenchimento'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva', 'COLUMN', 'rv_dthr_criacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'Quantidade de números reservado.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva', 'COLUMN', 'rv_qtd_reservada'
go
EXEC sp_addextendedproperty 'MS_Description', 'Quantidade de números restantes.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva', 'COLUMN', 'rv_qtd_restante'
go
EXEC sp_addextendedproperty 'MS_Description', 'Número de início da reserva.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva', 'COLUMN', 'rv_nro_inicio'
go
EXEC sp_addextendedproperty 'MS_Description', 'Número final da reserva.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva', 'COLUMN', 'rv_nro_fim'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva', 'COLUMN', 'rv_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da última alteração do registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva', 'COLUMN', 'rv_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que criou o registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva', 'COLUMN', 'rv_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'Último usuário a alterar o registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva', 'COLUMN', 'rv_usuario_alteracao'
go

-- Add keys for table pat_mobiliario.tb_reserva

ALTER TABLE pat_mobiliario.tb_reserva ADD CONSTRAINT PK_tb_reserva PRIMARY KEY (rv_id)
go

-- Table pat_mobiliario.tb_reserva_intervalo

CREATE TABLE pat_mobiliario.tb_reserva_intervalo
(
    ri_id bigint NOT NULL,
    rv_id bigint NOT NULL,
    ri_codigo Varchar(10) NULL,
    ri_situacao Varchar(20) NULL,
    ri_preenchimento Varchar(10) NULL,
    uo_id_orgao Int NULL,
    ri_qtd_reservada bigint NULL,
    ri_nro_inicio bigint NULL,
    ri_nro_fim bigint NULL,
    ri_nro_termo Varchar(20),
    ri_dthr_finalizacao Datetime NULL,
    ri_dthr_cadastro Datetime NULL,
    ri_dthr_alteracao Datetime NULL,
    ri_usuario_cadastro Varchar(255) NULL,
    ri_usuario_alteracao Varchar(255) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'Id da tabela reserva intervalo.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo', 'COLUMN', 'ri_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'Código do intervalo.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo', 'COLUMN', 'ri_codigo'
go
EXEC sp_addextendedproperty 'MS_Description', 'Situação do intervalo da reserva.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo', 'COLUMN', 'ri_situacao'
go
EXEC sp_addextendedproperty 'MS_Description', 'Tipo de preenchimento automático ou manual.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo', 'COLUMN', 'ri_preenchimento'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para o orgão a qual a reserva pertence.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo', 'COLUMN', 'uo_id_orgao'
go
EXEC sp_addextendedproperty 'MS_Description', 'Quantidade de números reservado,', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo', 'COLUMN', 'ri_qtd_reservada'
go
EXEC sp_addextendedproperty 'MS_Description', 'Número de início da reserva.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo', 'COLUMN', 'ri_nro_inicio'
go
EXEC sp_addextendedproperty 'MS_Description', 'Número final da reserva.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo', 'COLUMN', 'ri_nro_fim'
go
EXEC sp_addextendedproperty 'MS_Description', 'Número do termo do intervalo da reserva.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo', 'COLUMN', 'ri_nro_termo'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo', 'COLUMN', 'ri_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da última alteração do registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo', 'COLUMN', 'ri_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que criou o registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo', 'COLUMN', 'ri_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'Último usuário a alterar o registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo', 'COLUMN', 'ri_usuario_alteracao'
go

-- Create indexes for table pat_mobiliario.tb_reserva_intervalo

CREATE INDEX in_uo_id_orgao ON pat_mobiliario.tb_reserva_intervalo (uo_id_orgao)
go

CREATE INDEX in_reserva_reserva_intervalo ON pat_mobiliario.tb_reserva_intervalo (rv_id)
go

-- Add keys for table pat_mobiliario.tb_reserva_intervalo

ALTER TABLE pat_mobiliario.tb_reserva_intervalo ADD CONSTRAINT PK_tb_reserva_intervalo PRIMARY KEY (ri_id)
go

-- Table pat_mobiliario.tb_reserva_intervalo_numero

CREATE TABLE pat_mobiliario.tb_reserva_intervalo_numero
(
    rin_id bigint NOT NULL,
    ri_id bigint NOT NULL,
    rin_numero bigint NULL,
    rin_utilizado Bit NULL DEFAULT 0,
    rin_dthr_cadastro Datetime NULL,
    rin_dthr_alteracao Datetime NULL,
    rin_usuario_cadastro Varchar(255) NULL,
    rin_usuario_alteracao Varchar(255) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'Id da tabela reserva intervalo número.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo_numero', 'COLUMN', 'rin_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para a reserva intervalo a qual o número pertence.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo_numero', 'COLUMN', 'ri_id'
go
EXEC sp_addextendedproperty 'MS_Description', 'Número da tabela.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo_numero', 'COLUMN', 'rin_numero'
go
EXEC sp_addextendedproperty 'MS_Description', 'Valor booleano para indicar se o número foi utilizado ou não.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo_numero', 'COLUMN', 'rin_utilizado'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo_numero', 'COLUMN', 'rin_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da última alteração do registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo_numero', 'COLUMN', 'rin_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que criou o registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo_numero', 'COLUMN', 'rin_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'Último usuário a alterar o registro.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'tb_reserva_intervalo_numero', 'COLUMN', 'rin_usuario_alteracao'
go

-- Create indexes for table pat_mobiliario.tb_reserva_intervalo_numero

CREATE INDEX IN_fk_reserva_intervalo_reserva_intervalo_numero ON pat_mobiliario.tb_reserva_intervalo_numero (ri_id)
go

-- Add keys for table pat_mobiliario.tb_reserva_intervalo_numero

ALTER TABLE pat_mobiliario.tb_reserva_intervalo_numero ADD CONSTRAINT PK_tb_reserva_intervalo_numero PRIMARY KEY (rin_id)
go

-- Create foreign keys (relationships) section -------------------------------------------------


ALTER TABLE pat_mobiliario.TB_CONVENIO ADD CONSTRAINT fk_concedente_convenio FOREIGN KEY (coc_id) REFERENCES pat_mobiliario.TB_CONCEDENTE (coc_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_EMPENHO ADD CONSTRAINT fk_empenho_incorporacao FOREIGN KEY (in_id) REFERENCES pat_mobiliario.TB_INCORPORACAO (in_id) ON UPDATE NO ACTION ON DELETE CASCADE
go
ALTER TABLE pat_mobiliario.TB_CONFIG_CONTA_CONTABIL ADD CONSTRAINT fk_config_conta_contabil FOREIGN KEY (cc_id) REFERENCES comum_siga.tb_conta_contabil (cc_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_INCORPORACAO ADD CONSTRAINT fk_incoporacao_fundo FOREIGN KEY (uo_id_fundo) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_INCORPORACAO ADD CONSTRAINT fk_incoporacao_setor FOREIGN KEY (uo_id_setor) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_INCORPORACAO ADD CONSTRAINT fk_incorporacao_orgao FOREIGN KEY (uo_id_orgao) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_INCORPORACAO ADD CONSTRAINT fk_incoporacao_pessoa FOREIGN KEY (pe_id) REFERENCES comum_siga.tb_pessoa (pe_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_INCORPORACAO ADD CONSTRAINT fk_incorporacao_convenio FOREIGN KEY (co_id) REFERENCES pat_mobiliario.TB_CONVENIO (co_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_INCORPORACAO ADD CONSTRAINT fk_incorporacao_reconhecimento FOREIGN KEY (re_id) REFERENCES pat_mobiliario.TB_RECONHECIMENTO (re_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_INCORPORACAO ADD CONSTRAINT fk_incorporacao_nota_lancto_contabil FOREIGN KEY (nlc_id) REFERENCES pat_mobiliario.TB_NOTA_LANCAMENTO_CONTABIL (nlc_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_INCORPORACAO ADD CONSTRAINT fk_incorporacao_comodante FOREIGN KEY (com_id) REFERENCES pat_mobiliario.TB_COMODANTE (com_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_INCORPORACAO_ITEM ADD CONSTRAINT fk_item_inc_natureza_despesa FOREIGN KEY (nd_id) REFERENCES comum_siga.tb_natureza_despesa (nd_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_INCORPORACAO_ITEM ADD CONSTRAINT fk_item_inc_conta_contabil FOREIGN KEY (cc_id) REFERENCES comum_siga.tb_conta_contabil (cc_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_INCORPORACAO_ITEM ADD CONSTRAINT fk_item_inc_estado_conserv FOREIGN KEY (ec_id) REFERENCES pat_mobiliario.tb_estado_conservacao (ec_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_INCORPORACAO_ITEM ADD CONSTRAINT fk_incorporacao_item_incorp FOREIGN KEY (in_id) REFERENCES pat_mobiliario.TB_INCORPORACAO (in_id) ON UPDATE NO ACTION ON DELETE CASCADE
go
ALTER TABLE pat_mobiliario.TB_PATRIMONIO ADD CONSTRAINT fk_pat_fundo FOREIGN KEY (uo_id_fundo) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_PATRIMONIO ADD CONSTRAINT fk_pat_orgao FOREIGN KEY (uo_id_orgao) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_PATRIMONIO ADD CONSTRAINT fk_pat_setor FOREIGN KEY (uo_id_setor) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_PATRIMONIO ADD CONSTRAINT fk_pat_natureza_despesa FOREIGN KEY (nd_id) REFERENCES comum_siga.tb_natureza_despesa (nd_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_PATRIMONIO ADD CONSTRAINT fk_patrimonio_conta_contabil_atual FOREIGN KEY (cc_id_atual) REFERENCES comum_siga.tb_conta_contabil (cc_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_PATRIMONIO ADD CONSTRAINT fk_patrimonio_conta_contabil_classificacao FOREIGN KEY (cc_id_classificacao) REFERENCES comum_siga.tb_conta_contabil (cc_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_PATRIMONIO ADD CONSTRAINT fk_patrimonio_convenio FOREIGN KEY (co_id) REFERENCES pat_mobiliario.TB_CONVENIO (co_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_PATRIMONIO ADD CONSTRAINT fk_patrimonio_estado_conserv FOREIGN KEY (ec_id) REFERENCES pat_mobiliario.tb_estado_conservacao (ec_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_PATRIMONIO ADD CONSTRAINT fk_patrimonio_inc_item FOREIGN KEY (ini_id) REFERENCES pat_mobiliario.TB_INCORPORACAO_ITEM (ini_id) ON UPDATE NO ACTION ON DELETE CASCADE
go
ALTER TABLE pat_mobiliario.TB_PATRIMONIO ADD CONSTRAINT fk_patrimonio_comodante FOREIGN KEY (com_id) REFERENCES pat_mobiliario.TB_COMODANTE (com_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_MOVIMENTACAO ADD CONSTRAINT fk_movimentacao_orgao_destino FOREIGN KEY (uo_id_orgao_destino) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_MOVIMENTACAO ADD CONSTRAINT fk_movimentacao_orgao_origem FOREIGN KEY (uo_id_orgao_origem) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_MOVIMENTACAO ADD CONSTRAINT fk_movimentacao_setor_destino FOREIGN KEY (uo_id_setor_destino) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_MOVIMENTACAO ADD CONSTRAINT fk_movimentacao_setor_origem FOREIGN KEY (uo_id_setor_origem) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_MOVIMENTACAO ADD CONSTRAINT fk_movimentacao_nl FOREIGN KEY (nlc_id) REFERENCES pat_mobiliario.TB_NOTA_LANCAMENTO_CONTABIL (nlc_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_MOVIMENTACAO ADD CONSTRAINT fk_movimentacao_responsavel FOREIGN KEY (res_id) REFERENCES pat_mobiliario.TB_RESPONSAVEL (res_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_MOVIMENTACAO_ITEM ADD CONSTRAINT fk_movimentacao_item_patrimonio FOREIGN KEY (pa_id) REFERENCES pat_mobiliario.TB_PATRIMONIO (pa_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_MOVIMENTACAO_ITEM ADD CONSTRAINT fk_movimentacao_item_movimentacao FOREIGN KEY (mo_id) REFERENCES pat_mobiliario.TB_MOVIMENTACAO (mo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_LANCAMENTO_CONTABIL ADD CONSTRAINT fk_lc_orgao FOREIGN KEY (uo_id_orgao) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_LANCAMENTO_CONTABIL ADD CONSTRAINT fk_lc_setor FOREIGN KEY (uo_id_setor) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_LANCAMENTO_CONTABIL ADD CONSTRAINT fk_lc_conta_contabil FOREIGN KEY (cc_id) REFERENCES comum_siga.tb_conta_contabil (cc_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_LANCAMENTO_CONTABIL ADD CONSTRAINT fk_lc_incorporacao FOREIGN KEY (in_id) REFERENCES pat_mobiliario.TB_INCORPORACAO (in_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_LANCAMENTO_CONTABIL ADD CONSTRAINT fk_lc_patrimonio FOREIGN KEY (pa_id) REFERENCES pat_mobiliario.TB_PATRIMONIO (pa_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_LANCAMENTO_CONTABIL ADD CONSTRAINT fk_lc_movimentacao FOREIGN KEY (mo_id) REFERENCES pat_mobiliario.TB_MOVIMENTACAO (mo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_DOCUMENTO ADD CONSTRAINT fk_incorporacao_documento FOREIGN KEY (in_id) REFERENCES pat_mobiliario.TB_INCORPORACAO (in_id) ON UPDATE NO ACTION ON DELETE CASCADE
go
ALTER TABLE pat_mobiliario.TB_DOCUMENTO ADD CONSTRAINT fk_movimentacao_documento FOREIGN KEY (mo_id) REFERENCES pat_mobiliario.TB_MOVIMENTACAO (mo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_CONFIG_DEPRECIACAO ADD CONSTRAINT Relationship46 FOREIGN KEY (cc_id) REFERENCES comum_siga.tb_conta_contabil (cc_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_INCORPORACAO_ITEM ADD CONSTRAINT FK_INCORP_ITEM_CONFIG_DEPRE FOREIGN KEY (CD_ID) REFERENCES pat_mobiliario.TB_CONFIG_DEPRECIACAO (CD_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_DEPRECIACAO ADD CONSTRAINT FK_DEPRECIACAO_PATRIMONIO FOREIGN KEY (pa_id) REFERENCES pat_mobiliario.TB_PATRIMONIO (pa_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_DEPRECIACAO ADD CONSTRAINT FK_DEPRECIACAO_ORGAO FOREIGN KEY (uo_id_orgao) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_DEPRECIACAO ADD CONSTRAINT FK_DEPRECIACAO_SETOR FOREIGN KEY (uo_id_setor) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.TB_DEPRECIACAO ADD CONSTRAINT Relationship52 FOREIGN KEY (cc_id) REFERENCES comum_siga.tb_conta_contabil (cc_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.tb_reserva_intervalo ADD CONSTRAINT fk_reserva_reserva_intervalo FOREIGN KEY (rv_id) REFERENCES pat_mobiliario.tb_reserva (rv_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.tb_reserva_intervalo_numero ADD CONSTRAINT fk_reserva_intervalo_reserva_intervalo_numero FOREIGN KEY (ri_id) REFERENCES pat_mobiliario.tb_reserva_intervalo (ri_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
ALTER TABLE pat_mobiliario.tb_reserva_intervalo ADD CONSTRAINT fk_unidadeorg_reserva_intervalo FOREIGN KEY (uo_id_orgao) REFERENCES comum_siga.TB_UNIDADE_ORGANIZACIONAL (uo_id) ON UPDATE NO ACTION ON DELETE NO ACTION
go
