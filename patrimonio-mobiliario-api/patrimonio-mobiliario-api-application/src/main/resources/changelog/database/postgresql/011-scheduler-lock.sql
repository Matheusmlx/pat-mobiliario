CREATE TABLE pat_mobiliario.shedlock (
    name VARCHAR(64) NOT NULL,
    lock_until TIMESTAMP NOT NULL,
    locked_at TIMESTAMP NOT NULL,
    locked_by VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);

comment on table "pat_mobiliario"."shedlock" is 'Tabela de agendamento de tarefas';
comment on column "pat_mobiliario"."shedlock"."name" is 'Nome do agendamento';
comment on column "pat_mobiliario"."shedlock"."lock_until" is 'Limite que pode ser executado pelo agendamento';
comment on column "pat_mobiliario"."shedlock"."locked_at" is 'Tempo que o bloqueio deve ser mantido.';
comment on column "pat_mobiliario"."shedlock"."locked_by" is 'Por que o bloqueio';
