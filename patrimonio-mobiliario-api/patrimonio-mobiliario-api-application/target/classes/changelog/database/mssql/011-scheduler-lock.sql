CREATE TABLE pat_mobiliario.shedlock (
    name VARCHAR(64) NOT NULL,
    lock_until datetime2 NOT NULL,
    locked_at datetime2 NOT NULL,
    locked_by VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);

GO
EXEC sp_addextendedproperty 'MS_Description', 'Tabela de agendamento de tarefas', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'shedlock', NULL, NULL
GO
EXEC sp_addextendedproperty 'MS_Description', 'Nome do agendamento', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'shedlock', 'COLUMN', 'name'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Limite que pode ser executado pelo agendamento','SCHEMA', 'pat_mobiliario', 'TABLE', 'shedlock', 'COLUMN', 'lock_until'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Tempo que o bloqueio deve ser mantido.','SCHEMA', 'pat_mobiliario', 'TABLE', 'shedlock', 'COLUMN', 'locked_at'
GO
EXEC sp_addextendedproperty 'MS_Description', 'Por que o bloqueio', 'SCHEMA', 'pat_mobiliario', 'TABLE', 'shedlock', 'COLUMN', 'locked_by'
GO
