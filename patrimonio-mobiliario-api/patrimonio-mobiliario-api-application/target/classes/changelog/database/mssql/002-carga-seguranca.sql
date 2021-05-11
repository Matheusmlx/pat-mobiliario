-- MODULOS E FUNCOES --


-- MÓDULO PATRIMONIO --
insert into comum.tb_modulo (mo_nome,mo_descricao,pr_id,mo_dthr_cadastro,mo_usuario_cadastro)
values ('Patrimonio','Modulo de acesso às telas de gerenciamento do Patrimonio', 410, sysdatetime(),'Sistema');

insert into hal.tb_funcao (fu_nome,fu_descricao,mo_id,pl_id,fu_dthr_cadastro,fu_usuario_cadastro)
values ('Mobiliario.Normal','Função de acesso normal às telas de gerenciamento do Patrimonio', (select mo_id from comum.tb_modulo where mo_nome = 'Patrimonio' and pr_id = 410), 1, sysdatetime(),'Sistema');

insert into hal.tb_funcao (fu_nome,fu_descricao,mo_id,pl_id,fu_dthr_cadastro,fu_usuario_cadastro)
values ('Mobiliario.Retroativo','Função de acesso para cadastro de ativações retroativas.', (select mo_id from comum.tb_modulo where mo_nome = 'Patrimonio' and pr_id = 410), 1, sysdatetime(),'Sistema');

insert into hal.tb_funcao (fu_nome,fu_descricao,mo_id,pl_id,fu_dthr_cadastro,fu_usuario_cadastro)
values ('Mobiliario.Consulta','Função de acesso de consulta.', (select mo_id from comum.tb_modulo where mo_nome = 'Patrimonio' and pr_id = 410), 1, sysdatetime(),'Sistema');

insert into hal.tb_funcao (fu_nome,fu_descricao,mo_id,pl_id,fu_dthr_cadastro,fu_usuario_cadastro)
values ('Mobiliario.Leilao','Função de acesso para cadastro de ativações retroativas.', (select mo_id from comum.tb_modulo where mo_nome = 'Patrimonio' and pr_id = 410), 1, sysdatetime(),'Sistema');

insert into hal.tb_funcao (fu_nome,fu_descricao,mo_id,pl_id,fu_dthr_cadastro,fu_usuario_cadastro)
values ('Mobiliario.Movimentacoes','Função de acesso para movimentação de patrimônios.', (select mo_id from comum.tb_modulo where mo_nome = 'Patrimonio' and pr_id = 410), 1, sysdatetime(),'Sistema');

-- MÓDULO CONFIGURAÇÔES --
insert into comum.tb_modulo (mo_nome,mo_descricao,pr_id,mo_dthr_cadastro,mo_usuario_cadastro)
values ('Configurações','Modulo de acesso às telas de configurações do sistema', 410, sysdatetime(),'Sistema');
insert into hal.tb_funcao (fu_nome,fu_descricao,mo_id,pl_id,fu_dthr_cadastro,fu_usuario_cadastro)
values ('Mobiliario.Configuracao','Função de acesso de edição a tela de gerenciamento de configurações', (select mo_id from comum.tb_modulo where mo_nome = 'Patrimonio' and pr_id = 410), 1, sysdatetime(),'Sistema');

-- PERFIS --

-- SERVIDOR PATRIMONIO --
insert into comum.tb_perfil(pf_nome,pf_descricao,pf_tipo,pr_id,pf_dthr_cadastro,pf_usuario_cadastro)
values ('Servidor','Usuário específico para gerenciar patrimônios.','INTERNO',410,sysdatetime(),'Sistema');

insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Consulta'),(select pf_id from comum.tb_perfil where pf_nome = 'Servidor' and pr_id = 410), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Normal'),(select pf_id from comum.tb_perfil where pf_nome = 'Servidor' and pr_id = 410), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Movimentacoes'),(select pf_id from comum.tb_perfil where pf_nome = 'Servidor' and pr_id = 410), 1,'S');


-- GESTOR --

insert into comum.tb_perfil(pf_nome,pf_descricao,pf_tipo,pr_id,pf_dthr_cadastro,pf_usuario_cadastro)
values ('Gestor','O usuário gestor terá acesso a todas as funcionalidades operacionais do sistema, exceto cadastros retroativos e  configurações básicas.','INTERNO',410,sysdatetime(),'Sistema');

insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Consulta'),(select pf_id from comum.tb_perfil where pf_nome = 'Gestor' and pr_id = 410), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Normal'),(select pf_id from comum.tb_perfil where pf_nome = 'Gestor' and pr_id = 410), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Retroativo'),(select pf_id from comum.tb_perfil where pf_nome = 'Gestor' and pr_id = 410), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Movimentacoes'),(select pf_id from comum.tb_perfil where pf_nome = 'Gestor' and pr_id = 410), 1,'S');



-- LEILAO --
insert into comum.tb_perfil(pf_nome,pf_descricao,pf_tipo,pr_id,pf_dthr_cadastro,pf_usuario_cadastro)
values ('Leilao','Usuário capaz de realizar em sistema apenas as alienações de bens inservíveis para a unidade, mandando esses bens para leilão.','INTERNO',410,sysdatetime(),'Sistema');

insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Leilao'),(select pf_id from comum.tb_perfil where pf_nome = 'Leilao' and pr_id = 410), 1,'S');

-- CONSULTOR --
insert into comum.tb_perfil(pf_nome,pf_descricao,pf_tipo,pr_id,pf_dthr_cadastro,pf_usuario_cadastro)
values ('Consultor','Usuário auditor/contador que vai apenas visualizar todos os cadastros, sem poder gerenciar nenhum deles, e vai poder gerar relatórios para esses bens.','INTERNO',410,sysdatetime(),'Sistema');

insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Consulta'),(select pf_id from comum.tb_perfil where pf_nome = 'Consultor' and pr_id = 410), 1,'S');

-- ADMINISTRADOR --
insert into comum.tb_perfil(pf_nome,pf_descricao,pf_tipo,pr_id,pf_dthr_cadastro,pf_usuario_cadastro)
values ('Administrador','Usuário com permissão para todas as funcionalidades, com permissão para configuração de sistema, geralmente algum funcionário da AZ que fica no cliente.','INTERNO',410,sysdatetime(),'Sistema');

insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Consulta'),(select pf_id from comum.tb_perfil where pf_nome = 'Administrador' and pr_id = 410), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Normal'),(select pf_id from comum.tb_perfil where pf_nome = 'Administrador' and pr_id = 410), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Retroativo'),(select pf_id from comum.tb_perfil where pf_nome = 'Administrador' and pr_id = 410), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Leilao'),(select pf_id from comum.tb_perfil where pf_nome = 'Administrador' and pr_id = 410), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Configuracao'),(select pf_id from comum.tb_perfil where pf_nome = 'Administrador' and pr_id = 410), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Movimentacoes'),(select pf_id from comum.tb_perfil where pf_nome = 'Administrador' and pr_id = 410), 1,'S');


