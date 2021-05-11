-- USUARIO
insert into comum.tb_usuario (us_email, us_nome, us_doc_chave, us_exibir_representacoes, us_telefone, pa_id, id_id, us_dthr_cadastro, us_usuario_cadastro)
values ('integracaopatmob', 'Integração Patrimônio Mobiliário', '37549882630', 0, '556733032700', 30, 1, sysdatetime(), 'Sistema');

-- USUARIO SENHA
insert into hal.tb_usuario_hal (us_id,us_senha)
values ((select us_id from comum.tb_usuario where us_email = 'integracaopatmob'), 'lN0wig/hlaX7v6acYav2MA==');
