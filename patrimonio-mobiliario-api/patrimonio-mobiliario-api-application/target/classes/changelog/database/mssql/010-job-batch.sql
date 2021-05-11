-- Table pat_mobiliario.BATCH_JOB_INSTANCE
CREATE TABLE pat_mobiliario.BATCH_JOB_INSTANCE  (
	JOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY ,
	VERSION BIGINT NULL,
	JOB_NAME VARCHAR(100) NOT NULL,
	JOB_KEY VARCHAR(32) NOT NULL,
	constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
)
GO

EXEC sp_addextendedproperty 'MS_Description', 'Tabela referente a instancia da JobBatch', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_INSTANCE', NULL, NULL
GO
EXEC sp_addextendedproperty 'MS_Description', 'Chave primaria da tabela BATCH_JOB_INSTANCE', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_INSTANCE', 'COLUMN', 'JOB_INSTANCE_ID'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Número da versão da batch', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_INSTANCE', 'COLUMN', 'VERSION'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Nome da Job.', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_INSTANCE', 'COLUMN', 'JOB_NAME'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Indentificador da Job', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_INSTANCE', 'COLUMN', 'JOB_KEY'
GO

-- Table pat_mobiliario.BATCH_JOB_EXECUTION
CREATE TABLE pat_mobiliario.BATCH_JOB_EXECUTION  (
	JOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
	VERSION BIGINT NULL,
	JOB_INSTANCE_ID BIGINT NOT NULL,
	CREATE_TIME DATETIME NOT NULL,
	START_TIME DATETIME DEFAULT NULL ,
	END_TIME DATETIME DEFAULT NULL ,
	STATUS VARCHAR(10) NULL,
	EXIT_CODE VARCHAR(2500) NULL,
	EXIT_MESSAGE VARCHAR(2500) NULL,
	LAST_UPDATED DATETIME NULL,
	JOB_CONFIGURATION_LOCATION VARCHAR(2500) NULL,
	constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
	references pat_mobiliario.BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
)
GO

EXEC sp_addextendedproperty 'MS_Description', 'Tabela referente a instancia da JobBatch em execução', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION', NULL, NULL
GO
EXEC sp_addextendedproperty 'MS_Description', 'Chave primaria da tabela BATCH_JOB_EXECUTION', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION', 'COLUMN', 'JOB_EXECUTION_ID'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Número da versão da batch', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION', 'COLUMN', 'VERSION'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira da tabela BATCH_JOB_INSTANCE','SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION', 'COLUMN', 'JOB_INSTANCE_ID'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da criação da execution.','SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION', 'COLUMN', 'CREATE_TIME'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora do inicio da job.','SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION', 'COLUMN', 'START_TIME'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora do fim da job.','SCHEMA','pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION', 'COLUMN', 'END_TIME'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Estatus da job','SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION', 'COLUMN', 'STATUS'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Retorno da execucao da job','SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION', 'COLUMN', 'EXIT_CODE'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Mensagem de retorno da finalização da Job','SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION', 'COLUMN', 'EXIT_MESSAGE'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da ultima alteração da job','SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION', 'COLUMN', 'LAST_UPDATED'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Configuração da Job','SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION', 'COLUMN', 'JOB_CONFIGURATION_LOCATION'
GO

CREATE INDEX in_job_instance_id ON pat_mobiliario.BATCH_JOB_EXECUTION (JOB_INSTANCE_ID)
GO

-- Table pat_mobiliario.BATCH_JOB_EXECUTION_PARAMS
CREATE TABLE pat_mobiliario.BATCH_JOB_EXECUTION_PARAMS  (
	JOB_EXECUTION_ID BIGINT NOT NULL ,
	TYPE_CD VARCHAR(6) NOT NULL ,
	KEY_NAME VARCHAR(100) NOT NULL ,
	STRING_VAL VARCHAR(250) NULL,
	DATE_VAL DATETIME DEFAULT NULL ,
	LONG_VAL BIGINT NULL,
	DOUBLE_VAL DOUBLE PRECISION NULL,
	IDENTIFYING CHAR(1) NOT NULL ,
	constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
	references pat_mobiliario.BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
)
GO

EXEC sp_addextendedproperty 'MS_Description', 'Tabela referente a os parametros da  instancia da JobBatch em execução', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION_PARAMS', NULL, NULL
GO
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira da tabela BATCH_JOB_EXECUTION', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION_PARAMS', 'COLUMN', 'JOB_EXECUTION_ID'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Tipo da referencia', 'SCHEMA', 'pat_mobiliario' ,'TABLE', 'BATCH_JOB_EXECUTION_PARAMS', 'COLUMN', 'TYPE_CD'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Nome do parametro', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION_PARAMS', 'COLUMN', 'KEY_NAME'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Valor do Parametro de String', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION_PARAMS', 'COLUMN', 'STRING_VAL'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Valor do Parametro da Data/Hora', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION_PARAMS', 'COLUMN', 'DATE_VAL'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Valor Long', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION_PARAMS', 'COLUMN', 'LONG_VAL'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Valor Double', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION_PARAMS', 'COLUMN', 'DOUBLE_VAL'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Identificação do parametro', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION_PARAMS', 'COLUMN', 'IDENTIFYING'
GO

CREATE INDEX in_job_instance_execution_id ON pat_mobiliario.BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
GO
-- Table pat_mobiliario.BATCH_STEP_EXECUTION
CREATE TABLE pat_mobiliario.BATCH_STEP_EXECUTION  (
	STEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
	VERSION BIGINT NOT NULL,
	STEP_NAME VARCHAR(100) NOT NULL,
	JOB_EXECUTION_ID BIGINT NOT NULL,
	START_TIME DATETIME NOT NULL ,
	END_TIME DATETIME DEFAULT NULL ,
	STATUS VARCHAR(10) NULL,
	COMMIT_COUNT BIGINT NULL,
	READ_COUNT BIGINT NULL,
	FILTER_COUNT BIGINT NULL,
	WRITE_COUNT BIGINT NULL,
	READ_SKIP_COUNT BIGINT NULL,
	WRITE_SKIP_COUNT BIGINT NULL,
	PROCESS_SKIP_COUNT BIGINT NULL,
	ROLLBACK_COUNT BIGINT NULL,
	EXIT_CODE VARCHAR(2500) NULL,
	EXIT_MESSAGE VARCHAR(2500) NULL,
	LAST_UPDATED DATETIME NULL,
	constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
	references pat_mobiliario.BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
)
GO

EXEC sp_addextendedproperty 'MS_Description', 'Tabela referente a(s) etapas da execução da Batch', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', NULL, NULL
GO
EXEC sp_addextendedproperty 'MS_Description', 'Chave primaria da tabela BATCH_STEP_EXECUTION', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'STEP_EXECUTION_ID'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Versão da Step', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'VERSION'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Nome da etapa de execution', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'STEP_NAME'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para a tabela de JOB_EXECUTION', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'JOB_EXECUTION_ID'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora do inicio da etapa', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'START_TIME'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora do fim da etapa', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'END_TIME'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Status da step', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'STATUS'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Número de commits', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'COMMIT_COUNT'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Número de leituras realizadas', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'READ_COUNT'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Número de filtros realizados', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'FILTER_COUNT'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Número de escritas realizadas', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'WRITE_COUNT'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Número de leituras puladas', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'READ_SKIP_COUNT'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Número de escritas puladas', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'WRITE_SKIP_COUNT'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Número de processos pulados', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'PROCESS_SKIP_COUNT'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Número de rollbacks', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'ROLLBACK_COUNT'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Retorno da execucao da etapa', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'EXIT_CODE'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Mensagem de retorno da finalização da step', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'EXIT_MESSAGE'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da ultima alteração da step', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION', 'COLUMN', 'LAST_UPDATED'
GO

CREATE INDEX in_batch_step_execution ON pat_mobiliario.BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
GO
-- Table pat_mobiliario.BATCH_STEP_EXECUTION_CONTEXT
CREATE TABLE pat_mobiliario.BATCH_STEP_EXECUTION_CONTEXT  (
	STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
	SHORT_CONTEXT VARCHAR(2500) NOT NULL,
	SERIALIZED_CONTEXT TEXT NULL,
	constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
	references pat_mobiliario.BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
)
GO

EXEC sp_addextendedproperty 'MS_Description', 'Tabela referente ao contexto da execução da step', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION_CONTEXT', NULL, NULL
GO
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para tabela de SETP_EXECUTION', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION_CONTEXT', 'COLUMN', 'STEP_EXECUTION_ID'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Contexto da step em execução', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION_CONTEXT', 'COLUMN', 'SHORT_CONTEXT'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Número do serial da step', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_STEP_EXECUTION_CONTEXT', 'COLUMN', 'SERIALIZED_CONTEXT'
GO
CREATE INDEX in_batch_step_execution_context ON pat_mobiliario.BATCH_STEP_EXECUTION_CONTEXT (STEP_EXECUTION_ID)
GO
-- Table pat_mobiliario.BATCH_JOB_EXECUTION_CONTEXT
CREATE TABLE pat_mobiliario.BATCH_JOB_EXECUTION_CONTEXT  (
	JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
	SHORT_CONTEXT VARCHAR(2500) NOT NULL,
	SERIALIZED_CONTEXT TEXT NULL,
	constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
	references pat_mobiliario.BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
)
GO

CREATE INDEX in_batch_job_execution_context ON pat_mobiliario.BATCH_JOB_EXECUTION_CONTEXT(JOB_EXECUTION_ID)
GO
EXEC sp_addextendedproperty 'MS_Description', 'Tabela referente ao contexto da execução da JOB', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION_CONTEXT', NULL, NULL
GO
EXEC sp_addextendedproperty 'MS_Description', 'Chavé primaria da tabela de BATCH_JOB_EXECUTION_CONTEXT', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION_CONTEXT', 'COLUMN', 'JOB_EXECUTION_ID'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Contexto da job', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION_CONTEXT', 'COLUMN', 'SHORT_CONTEXT'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Número do serial da job', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'BATCH_JOB_EXECUTION_CONTEXT', 'COLUMN', 'SERIALIZED_CONTEXT'
GO

CREATE TABLE pat_mobiliario.BATCH_STEP_EXECUTION_SEQ (ID BIGINT IDENTITY);
CREATE TABLE pat_mobiliario.BATCH_JOB_EXECUTION_SEQ (ID BIGINT IDENTITY);
CREATE TABLE pat_mobiliario.BATCH_JOB_SEQ (ID BIGINT IDENTITY);
