--- Trigger para replicar data de alteração de empenho em incorporação referenciada
CREATE OR REPLACE FUNCTION atualiza_incorporacao_empenho() RETURNS TRIGGER AS $incorporacao_empenho$
BEGIN
    UPDATE pat_mobiliario.tb_incorporacao SET in_dthr_alteracao = now() where in_id=NEW.in_id;
    RETURN NEW;
END;
$incorporacao_empenho$ LANGUAGE plpgsql;


CREATE TRIGGER incorporacao_empenho
    AFTER INSERT OR UPDATE ON pat_mobiliario.tb_empenho
    FOR EACH ROW EXECUTE PROCEDURE atualiza_incorporacao_empenho();


--- Trigger para replicar data de alteração de empenho em incorporação referenciada ao deletar
CREATE OR REPLACE FUNCTION atualiza_incorporacao_empenho_delete() RETURNS TRIGGER AS $incorporacao_empenho$
BEGIN
    UPDATE pat_mobiliario.tb_incorporacao SET in_dthr_alteracao = now() where in_id=OLD.in_id;
    RETURN OLD;
END;
$incorporacao_empenho$ LANGUAGE plpgsql;


CREATE TRIGGER incorporacao_empenho_delete
    BEFORE DELETE ON pat_mobiliario.tb_empenho
    FOR EACH ROW EXECUTE PROCEDURE atualiza_incorporacao_empenho_delete();

--- Trigger para replicar data de alteração de item em incorporação referenciada
CREATE OR REPLACE FUNCTION atualiza_incorporacao_item() RETURNS TRIGGER AS $incorporacao_item$
BEGIN
    UPDATE pat_mobiliario.tb_incorporacao SET in_dthr_alteracao = now() where in_id=NEW.in_id;
    RETURN NEW;
END;
$incorporacao_item$ LANGUAGE plpgsql;

CREATE TRIGGER incorporacao_item
    AFTER INSERT OR UPDATE ON pat_mobiliario.tb_incorporacao_item
    FOR EACH ROW EXECUTE PROCEDURE atualiza_incorporacao_item();

--- Trigger para replicar data de alteração de item em incorporação referenciada ao o item de incorporação

CREATE OR REPLACE FUNCTION atualiza_incorporacao_item_delete() RETURNS TRIGGER AS $incorporacao_item$
BEGIN
    UPDATE pat_mobiliario.tb_incorporacao SET in_dthr_alteracao = now() where in_id=OLD.in_id;
    RETURN OLD;
END;
$incorporacao_item$ LANGUAGE plpgsql;

CREATE TRIGGER incorporacao_item_delete
    BEFORE DELETE ON pat_mobiliario.tb_incorporacao_item
    FOR EACH ROW EXECUTE PROCEDURE atualiza_incorporacao_item_delete();

