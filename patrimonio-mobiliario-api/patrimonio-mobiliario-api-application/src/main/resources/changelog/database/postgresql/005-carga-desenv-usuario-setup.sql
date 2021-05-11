

-- USUARIO
insert into comum.tb_usuario (us_id, us_email, us_email_contato, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, us_situacao, id_id, pa_id, us_tipo_usuario)
values
    (nextval('comum.seq_usuario'), 'servidor', 'servidor@azi.com.br', '61486745210', 'Servidor', false, '33233232', 'ATIVO', 1, 30, 'INTERNO'),
    (nextval('comum.seq_usuario'), 'gestor', 'servidor@azi.com.br', '61486745210', 'Gestor', false, '33233232', 'ATIVO', 1, 30, 'INTERNO'),
    (nextval('comum.seq_usuario'), 'leilao', 'servidor@azi.com.br', '61486745210', 'Leilao', false, '33233232', 'ATIVO', 1, 30, 'INTERNO'),
    (nextval('comum.seq_usuario'), 'consultor', 'consultor@azi.com.br', '93548959059', 'Consultor', false, '33233232', 'ATIVO', 1, 30, 'INTERNO');

-- USUARIO SENHA
insert into hal.tb_usuario_hal (us_id, us_senha)
values
  ((select us_id from comum.tb_usuario where us_email = 'servidor'), 'UTr6YlcHzuQ=' ),
  ((select us_id from comum.tb_usuario where us_email = 'gestor'), 'UTr6YlcHzuQ=' ),
  ((select us_id from comum.tb_usuario where us_email = 'leilao'), 'UTr6YlcHzuQ=' ),
  ((select us_id from comum.tb_usuario where us_email = 'consultor'), 'UTr6YlcHzuQ=' );

-- DOMINIO
insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id)
select nextval('hal.seq_dominio'), 'ESTRUTURA_ORGANIZACIONAL', uo_id, (select us_id from comum.tb_usuario where us_email = 'admin')
from comum_siga.tb_unidade_organizacional
where uo_cod_hierarquia like '0002.%';

insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id)
select nextval('hal.seq_dominio'), 'ESTRUTURA_ORGANIZACIONAL', uo_id, (select us_id from comum.tb_usuario where us_email = 'servidor')
from comum_siga.tb_unidade_organizacional
where uo_cod_hierarquia like '0002.%';

insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id)
select nextval('hal.seq_dominio'), 'ESTRUTURA_ORGANIZACIONAL', uo_id, (select us_id from comum.tb_usuario where us_email = 'gestor')
from comum_siga.tb_unidade_organizacional
where uo_cod_hierarquia like '0002.%';

insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id)
select nextval('hal.seq_dominio'), 'ESTRUTURA_ORGANIZACIONAL', uo_id, (select us_id from comum.tb_usuario where us_email = 'leilao')
from comum_siga.tb_unidade_organizacional
where uo_cod_hierarquia like '0002.%';

insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id)
select nextval('hal.seq_dominio'), 'ESTRUTURA_ORGANIZACIONAL', uo_id, (select us_id from comum.tb_usuario where us_email = 'consultor')
from comum_siga.tb_unidade_organizacional
where uo_cod_hierarquia like '0002.%';

-- DOMÍNIO PERFIL
insert into hal.tb_dominio_perfil (dm_id, pf_id)
values ((select dm_id from hal.tb_dominio where dm_tipo_cliente = 'DEFAULT' and us_id = (select us_id from comum.tb_usuario where us_email = 'admin')),
(select pf_id from comum.tb_perfil where pf_nome = 'Administrador' and pr_id = 410));

insert into hal.tb_dominio_perfil (dm_id, pf_id)
select dm_id, (select pf_id from comum.tb_perfil where pf_nome = 'Administrador' and pr_id = 410)
from hal.tb_dominio
where dm_tipo_cliente = 'ESTRUTURA_ORGANIZACIONAL'
  and us_id = (select us_id from comum.tb_usuario where us_email = 'admin')
  and dm_id_cliente in (select uo_id from comum_siga.tb_unidade_organizacional where uo_cod_hierarquia like '0002.%');

insert into hal.tb_dominio_perfil (dm_id, pf_id)
select dm_id, (select pf_id from comum.tb_perfil where pf_nome = 'Servidor' and pr_id = 410)
from hal.tb_dominio
where dm_tipo_cliente = 'ESTRUTURA_ORGANIZACIONAL'
and us_id = (select us_id from comum.tb_usuario where us_email = 'servidor')
and dm_id_cliente in (select uo_id from comum_siga.tb_unidade_organizacional where uo_cod_hierarquia like '0002.%');

insert into hal.tb_dominio_perfil (dm_id, pf_id)
select dm_id, (select pf_id from comum.tb_perfil where pf_nome = 'Gestor' and pr_id = 410)
from hal.tb_dominio
where dm_tipo_cliente = 'ESTRUTURA_ORGANIZACIONAL'
and us_id = (select us_id from comum.tb_usuario where us_email = 'gestor')
and dm_id_cliente in (select uo_id from comum_siga.tb_unidade_organizacional where uo_cod_hierarquia like '0002.%');

insert into hal.tb_dominio_perfil (dm_id, pf_id)
select dm_id, (select pf_id from comum.tb_perfil where pf_nome = 'Leilao' and pr_id = 410)
from hal.tb_dominio
where dm_tipo_cliente = 'ESTRUTURA_ORGANIZACIONAL'
and us_id = (select us_id from comum.tb_usuario where us_email = 'leilao')
and dm_id_cliente in (select uo_id from comum_siga.tb_unidade_organizacional where uo_cod_hierarquia like '0002.%');

insert into hal.tb_dominio_perfil (dm_id, pf_id)
select dm_id, (select pf_id from comum.tb_perfil where pf_nome = 'Consultor' and pr_id = 410)
from hal.tb_dominio
where dm_tipo_cliente = 'ESTRUTURA_ORGANIZACIONAL'
and us_id = (select us_id from comum.tb_usuario where us_email = 'consultor')
and dm_id_cliente in (select uo_id from comum_siga.tb_unidade_organizacional where uo_cod_hierarquia like '0002.%');
