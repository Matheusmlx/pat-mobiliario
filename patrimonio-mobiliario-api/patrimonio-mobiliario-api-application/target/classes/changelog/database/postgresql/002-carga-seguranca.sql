-- MODULOS E FUNCOES --


-- MÓDULO PATRIMONIO --
insert into comum.tb_modulo (mo_id,mo_nome,mo_descricao,pr_id,mo_dthr_cadastro,mo_usuario_cadastro)
values (nextval('comum.seq_modulo'),'Patrimonio','Modulo de acesso às telas de gerenciamento do Patrimonio', 410, current_timestamp(6),'Sistema');

insert into hal.tb_funcao (fu_id,fu_nome,fu_descricao,mo_id,pl_id,fu_dthr_cadastro,fu_usuario_cadastro)
values (nextval('hal.seq_funcao'),'Mobiliario.Normal','Função de acesso normal às telas de gerenciamento do Patrimonio', currval('comum.seq_modulo'), 1, current_timestamp(6),'Sistema');

insert into hal.tb_funcao (fu_id,fu_nome,fu_descricao,mo_id,pl_id,fu_dthr_cadastro,fu_usuario_cadastro)
values (nextval('hal.seq_funcao'),'Mobiliario.Retroativo','Função de acesso para cadastro de ativações retroativas.', currval('comum.seq_modulo'), 1, current_timestamp(6),'Sistema');

insert into hal.tb_funcao (fu_id,fu_nome,fu_descricao,mo_id,pl_id,fu_dthr_cadastro,fu_usuario_cadastro)
values (nextval('hal.seq_funcao'),'Mobiliario.Consulta','Função de acesso de consulta.', currval('comum.seq_modulo'), 1, current_timestamp(6),'Sistema');

insert into hal.tb_funcao (fu_id,fu_nome,fu_descricao,mo_id,pl_id,fu_dthr_cadastro,fu_usuario_cadastro)
values (nextval('hal.seq_funcao'),'Mobiliario.Leilao','Função de acesso para cadastro de ativações retroativas.', currval('comum.seq_modulo'), 1, current_timestamp(6),'Sistema');

insert into hal.tb_funcao (fu_id,fu_nome,fu_descricao,mo_id,pl_id,fu_dthr_cadastro,fu_usuario_cadastro)
values (nextval('hal.seq_funcao'),'Mobiliario.Movimentacoes','Função de acesso para movimentação de patrimônios.', currval('comum.seq_modulo'), 1, current_timestamp(6),'Sistema');

-- MÓDULO CONFIGURAÇÔES --
insert into comum.tb_modulo (mo_id,mo_nome,mo_descricao,pr_id,mo_dthr_cadastro,mo_usuario_cadastro)
values (nextval('comum.seq_modulo'),'Configurações','Modulo de acesso às telas de configurações do sistema', 410, current_timestamp(6),'Sistema');
insert into hal.tb_funcao (fu_id,fu_nome,fu_descricao,mo_id,pl_id,fu_dthr_cadastro,fu_usuario_cadastro)
values (nextval('hal.seq_funcao'),'Mobiliario.Configuracao','Função de acesso de edição a tela de gerenciamento de configurações', currval('comum.seq_modulo'), 1, current_timestamp(6),'Sistema');

-- PERFIS --

-- SERVIDOR PATRIMONIO --
insert into comum.tb_perfil(pf_id,pf_nome,pf_descricao,pf_tipo,pr_id,pf_dthr_cadastro,pf_usuario_cadastro)
values (nextval('comum.seq_perfil'),'Servidor','Usuário específico para gerenciar patrimônios.','INTERNO',410,current_timestamp(6),'Sistema');

insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Consulta'),(select pf_id from comum.tb_perfil where pf_nome = 'Servidor' and pr_id = 410), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Normal'),(select pf_id from comum.tb_perfil where pf_nome = 'Servidor' and pr_id = 410), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Movimentacoes'),(select pf_id from comum.tb_perfil where pf_nome = 'Servidor' and pr_id = 410), 1,'S');


-- GESTOR --

insert into comum.tb_perfil(pf_id,pf_nome,pf_descricao,pf_tipo,pr_id,pf_dthr_cadastro,pf_usuario_cadastro)
values (nextval('comum.seq_perfil'),'Gestor','O usuário gestor terá acesso a todas as funcionalidades operacionais do sistema, exceto cadastros retroativos e  configurações básicas.','INTERNO',410,current_timestamp(6),'Sistema');

insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Consulta'),(select pf_id from comum.tb_perfil where pf_nome = 'Gestor' and pr_id = 410), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Normal'),(select pf_id from comum.tb_perfil where pf_nome = 'Gestor' and pr_id = 410), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Retroativo'),(select pf_id from comum.tb_perfil where pf_nome = 'Gestor' and pr_id = 410), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Movimentacoes'),(select pf_id from comum.tb_perfil where pf_nome = 'Gestor' and pr_id = 410), 1,'S');



-- LEILAO --
insert into comum.tb_perfil(pf_id,pf_nome,pf_descricao,pf_tipo,pr_id,pf_dthr_cadastro,pf_usuario_cadastro)
values (nextval('comum.seq_perfil'),'Leilao','Usuário capaz de realizar em sistema apenas as alienações de bens inservíveis para a unidade, mandando esses bens para leilão.','INTERNO',410,current_timestamp(6),'Sistema');

insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Leilao'),(select pf_id from comum.tb_perfil where pf_nome = 'Leilao' and pr_id = 410), 1,'S');

-- CONSULTOR --
insert into comum.tb_perfil(pf_id,pf_nome,pf_descricao,pf_tipo,pr_id,pf_dthr_cadastro,pf_usuario_cadastro)
values (nextval('comum.seq_perfil'),'Consultor','Usuário auditor/contador que vai apenas visualizar todos os cadastros, sem poder gerenciar nenhum deles, e vai poder gerar relatórios para esses bens.','INTERNO',410,current_timestamp(6),'Sistema');

insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Mobiliario.Consulta'),(select pf_id from comum.tb_perfil where pf_nome = 'Consultor' and pr_id = 410), 1,'S');

-- ADMINISTRADOR --
insert into comum.tb_perfil(pf_id,pf_nome,pf_descricao,pf_tipo,pr_id,pf_dthr_cadastro,pf_usuario_cadastro)
values (nextval('comum.seq_perfil'),'Administrador','Usuário com permissão para todas as funcionalidades, com permissão para configuração de sistema, geralmente algum funcionário da AZ que fica no cliente.','INTERNO',410,current_timestamp(6),'Sistema');

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


