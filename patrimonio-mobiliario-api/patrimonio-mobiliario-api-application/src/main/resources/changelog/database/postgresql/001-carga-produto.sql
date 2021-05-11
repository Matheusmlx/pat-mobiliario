insert into comum.tb_produto (pr_id,pr_nome,pr_descricao,pr_titulo_img,pr_img_principal,pr_css_default,pl_id) values (410,'patrimonio-mobiliario','Módulo de Patrimônio Mobiliário.','','','default',1);

select setval('comum.seq_produto_atributo', 4000);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginTipo','INTERNO',410);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginAreaInterno','Área do Servidor',410);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginChaveAcessoInterno','Usuário do Compras',410);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginFacebookInterno','false',410);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginEsqueceuSenhaInterno','true',410);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginAutoCadastroInterno','true',410);

insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginAreaExterno','Área do Fornecedor',410);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginChaveAcessoExterno','CPF ou Email',410);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginFacebookExterno','false',410);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginEsqueceuSenhaExterno','false',410);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginAutoCadastroExterno','false',410);

insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pa_tipo, pr_id) values (nextval('comum.seq_produto_atributo'), 'logo', 'repo1:patrimoniomobiliario/logo.svg', 'ARQUIVO', 410);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pa_tipo, pr_id) values (nextval('comum.seq_produto_atributo'), 'logoEmail', 'repo1:patrimoniomobiliario/logoEmail.png', 'ARQUIVO', 410);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pa_tipo, pr_id) values (nextval('comum.seq_produto_atributo'), 'logoMenu', 'repo1:patrimoniomobiliario/logoMenu.svg', 'ARQUIVO', 410);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pa_tipo, pr_id) values (nextval('comum.seq_produto_atributo'), 'logoMenuRetraido', 'repo1:patrimoniomobiliario/logoMenuRetraido.svg', 'ARQUIVO', 410);
