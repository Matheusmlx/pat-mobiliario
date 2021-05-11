-- Autogenerated: do not edit this file

create table "pat_mobiliario"."batch_job_instance"
(
    "job_instance_id" bigint not null primary key,
    "version"         bigint,
    "job_name"        Varchar(100) not null,
    "job_key"         Varchar(32)  not null,
    constraint job_inst_un unique ("job_name", "job_key")
);

comment on table "pat_mobiliario"."batch_job_instance" is 'Tabela referente a instancia da JobBatch.';
comment on column "pat_mobiliario"."batch_job_instance"."job_instance_id" is 'Chave primaria da tabela BATCH_JOB_INSTANCE';
comment on column "pat_mobiliario"."batch_job_instance"."version" is 'Número da versão da batch';
comment on column "pat_mobiliario"."batch_job_instance"."job_name" is 'Nome da Job.';
comment on column "pat_mobiliario"."batch_job_instance"."job_key" is 'Indentificador da Job';

create table "pat_mobiliario"."batch_job_execution"
(
    "job_execution_id"           bigint        NOT NULL PRIMARY KEY,
    "version"                    bigint,
    "job_instance_id"            bigint        NOT NULL,
    "create_time"                TIMESTAMP     NOT NULL,
    "start_time"                 TIMESTAMP DEFAULT NULL,
    "end_time"                   TIMESTAMP DEFAULT NULL,
    "status"                     VARCHAR(10),
    "exit_code"                  VARCHAR(2500),
    "exit_message"               VARCHAR(2500),
    "last_updated"               TIMESTAMP,
    "job_configuration_location" VARCHAR(2500) NULL,
    constraint job_inst_exec_fk foreign key ("job_instance_id")
        references pat_mobiliario.BATCH_JOB_INSTANCE ("job_instance_id")
);

comment on table "pat_mobiliario"."batch_job_execution" is 'Tabela referente a instancia da JobBatch em execução';
comment on column "pat_mobiliario"."batch_job_execution"."job_execution_id" is 'Chave primaria da tabela BATCH_JOB_EXECUTION';
comment on column "pat_mobiliario"."batch_job_execution"."version" is 'Número da versão da batch';
comment on column "pat_mobiliario"."batch_job_execution"."job_instance_id" is 'Chave estrangeira da tabela BATCH_JOB_INSTANCE';
comment on column "pat_mobiliario"."batch_job_execution"."create_time" is 'Data/Hora da criação da execution.';
comment on column "pat_mobiliario"."batch_job_execution"."start_time" is 'Data/Hora do inicio da job.';
comment on column "pat_mobiliario"."batch_job_execution"."end_time" is 'Data/Hora do fim da job.';
comment on column "pat_mobiliario"."batch_job_execution"."status" is 'Estatus da job';
comment on column "pat_mobiliario"."batch_job_execution"."exit_code" is 'Retorno da execucao da job';
comment on column "pat_mobiliario"."batch_job_execution"."exit_message" is 'Mensagem de retorno da finalização da Job';
comment on column "pat_mobiliario"."batch_job_execution"."last_updated" is 'Data/Hora da ultima alteração da job';
comment on column "pat_mobiliario"."batch_job_execution"."job_configuration_location" is 'Configuração da Job';

create index "in_job_instance_id" on "pat_mobiliario"."batch_job_execution" ("job_instance_id");

CREATE TABLE pat_mobiliario.batch_job_execution_params
(
    "job_execution_id" BIGINT       NOT NULL,
    "type_cd"          VARCHAR(6)   NOT NULL,
    "key_name"         VARCHAR(100) NOT NULL,
    "string_val"       VARCHAR(250),
    "date_val"         TIMESTAMP DEFAULT NULL,
    "long_val"         BIGINT,
    "double_val"       DOUBLE PRECISION,
    "identifying"      CHAR(1)      NOT NULL,
    constraint JOB_EXEC_PARAMS_FK foreign key ("job_execution_id")
        references pat_mobiliario.batch_job_execution ("job_execution_id")
);
comment on table "pat_mobiliario"."batch_job_execution_params" is 'Tabela referente a os parametros da  instancia da JobBatch em execução';
comment on column "pat_mobiliario"."batch_job_execution_params"."job_execution_id" is 'Chave estrangeira da tabela BATCH_JOB_EXECUTION';
comment on column "pat_mobiliario"."batch_job_execution_params"."type_cd" is 'Tipo da referencia';
comment on column "pat_mobiliario"."batch_job_execution_params"."key_name" is 'Nome do parametro';
comment on column "pat_mobiliario"."batch_job_execution_params"."string_val" is 'Valor do Parametro de String';
comment on column "pat_mobiliario"."batch_job_execution_params"."date_val" is 'Valor do Parametro da Data/Hora';
comment on column "pat_mobiliario"."batch_job_execution_params"."long_val" is 'Valor Long';
comment on column "pat_mobiliario"."batch_job_execution_params"."double_val" is 'Valor Double';
comment on column "pat_mobiliario"."batch_job_execution_params"."identifying" is 'Identificação do parametro';

create index "in_job_instance_execution_id" on "pat_mobiliario"."batch_job_execution" ("job_execution_id");

CREATE TABLE pat_mobiliario.batch_step_execution
(
    "step_execution_id"  BIGINT       NOT NULL PRIMARY KEY,
    "version"            BIGINT       NOT NULL,
    "step_name"          VARCHAR(100) NOT NULL,
    "job_execution_id"   BIGINT       NOT NULL,
    "start_time"         TIMESTAMP    NOT NULL,
    "end_time"           TIMESTAMP DEFAULT NULL,
    "status"             VARCHAR(10),
    "commit_count"       BIGINT,
    "read_count"         BIGINT,
    "filter_count"       BIGINT,
    "write_count"        BIGINT,
    "read_skip_count"    BIGINT,
    "write_skip_count"   BIGINT,
    "process_skip_count" BIGINT,
    "rollback_count"     BIGINT,
    "exit_code"          VARCHAR(2500),
    "exit_message"       VARCHAR(2500),
    "last_updated"       TIMESTAMP,
    constraint job_exec_step_fk foreign key ("job_execution_id")
        references pat_mobiliario.batch_job_execution ("job_execution_id")
);

comment on table "pat_mobiliario"."batch_step_execution" is 'Tabela referente a(s) etapas da execução da Batch';
comment on column "pat_mobiliario"."batch_step_execution"."step_execution_id" is 'Chave primaria da tabela BATCH_STEP_EXECUTION';
comment on column "pat_mobiliario"."batch_step_execution"."version" is 'Versão da Step';
comment on column "pat_mobiliario"."batch_step_execution"."step_name" is 'Nome da etapa de execution';
comment on column "pat_mobiliario"."batch_step_execution"."job_execution_id" is 'Chave estrangeira para a tabela de JOB_EXECUTION';
comment on column "pat_mobiliario"."batch_step_execution"."start_time" is 'Data/Hora do inicio da etapa';
comment on column "pat_mobiliario"."batch_step_execution"."end_time" is 'Data/Hora do fim da etapa';
comment on column "pat_mobiliario"."batch_step_execution"."status" is 'Status da step';
comment on column "pat_mobiliario"."batch_step_execution"."commit_count" is 'Número de commits';
comment on column "pat_mobiliario"."batch_step_execution"."read_count" is 'Número de leituras realizadas';
comment on column "pat_mobiliario"."batch_step_execution"."filter_count" is 'Número de filtros realizados';
comment on column "pat_mobiliario"."batch_step_execution"."write_count" is 'Número de escritas realizadas';
comment on column "pat_mobiliario"."batch_step_execution"."read_skip_count" is 'Número de leituras puladas';
comment on column "pat_mobiliario"."batch_step_execution"."write_skip_count" is 'Número de escritas puladas';
comment on column "pat_mobiliario"."batch_step_execution"."process_skip_count" is 'Número de processos pulados';
comment on column "pat_mobiliario"."batch_step_execution"."rollback_count" is 'Número de rollbacks';
comment on column "pat_mobiliario"."batch_step_execution"."exit_code" is 'Retorno da execucao da etapa';
comment on column "pat_mobiliario"."batch_step_execution"."exit_message" is 'Mensagem de retorno da finalização da step';
comment on column "pat_mobiliario"."batch_step_execution"."last_updated" is 'Data/Hora da ultima alteração da step';

create index "in_batch_step_execution" on "pat_mobiliario"."batch_job_execution" ("job_execution_id");

CREATE TABLE pat_mobiliario.batch_step_execution_context
(
    "step_execution_id"  BIGINT        NOT NULL PRIMARY KEY,
    "short_context"      VARCHAR(2500) NOT NULL,
    "serialized_context" TEXT,
    constraint step_exec_ctx_fk foreign key ("step_execution_id")
        references pat_mobiliario.batch_step_execution ("step_execution_id")
);

comment on table "pat_mobiliario"."batch_step_execution_context" is 'Tabela referente ao contexto da execução da step';
comment on column "pat_mobiliario"."batch_step_execution_context"."step_execution_id" is 'Chave estrangeira para tabela de SETP_EXECUTION';
comment on column "pat_mobiliario"."batch_step_execution_context"."short_context" is 'Contexto da step em execução';
comment on column "pat_mobiliario"."batch_step_execution_context"."serialized_context" is 'Número do serial da step';

create index "in_batch_step_execution_context" on "pat_mobiliario"."batch_step_execution_context" ("step_execution_id");

CREATE TABLE pat_mobiliario.batch_job_execution_context
(
    "job_execution_id"   BIGINT        NOT NULL PRIMARY KEY,
    "short_context"      VARCHAR(2500) NOT NULL,
    "serialized_context" TEXT,
    constraint job_exec_ctx_fk foreign key ("job_execution_id")
        references pat_mobiliario.batch_job_execution ("job_execution_id")
);

comment on table "pat_mobiliario"."batch_job_execution_context" is 'Tabela referente ao contexto da execução da JOB';
comment on column "pat_mobiliario"."batch_job_execution_context"."job_execution_id" is 'Chavé primaria da tabela de BATCH_JOB_EXECUTION_CONTEXT';
comment on column "pat_mobiliario"."batch_job_execution_context"."short_context" is 'Contexto da job';
comment on column "pat_mobiliario"."batch_job_execution_context"."serialized_context" is 'Número do serial da job';

create index "in_batch_job_execution_context" on "pat_mobiliario"."batch_job_execution_context" ("job_execution_id");

CREATE SEQUENCE pat_mobiliario.batch_step_execution_seq MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE pat_mobiliario.batch_job_execution_seq MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE pat_mobiliario.batch_job_seq MAXVALUE 9223372036854775807 NO CYCLE;
