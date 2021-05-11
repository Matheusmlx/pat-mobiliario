-- Procedure para atualizar data de alteração da incorporação
CREATE PROCEDURE atualizar_incorporacao @incorporacao INT
AS
BEGIN
    UPDATE pat_mobiliario.tb_incorporacao SET in_dthr_alteracao = SYSDATETIME() WHERE pat_mobiliario.tb_incorporacao.in_id = @incorporacao
END
GO

-- Trigger para replicar data de alteração de empenho em incorporação referenciada
CREATE TRIGGER incorporacao_empenho ON pat_mobiliario.tb_empenho
AFTER INSERT, UPDATE
AS
BEGIN
    DECLARE @incorporacao_id INT
    SELECT @incorporacao_id = in_id FROM Inserted
    EXECUTE atualizar_incorporacao @incorporacao_id
END
GO

-- Trigger para replicar data de alteração de empenho em incorporação referenciada ao deletar
CREATE TRIGGER incorporacao_empenho_delete ON pat_mobiliario.tb_empenho
AFTER DELETE
AS
BEGIN
    DECLARE @incorporacao_id INT
    SELECT @incorporacao_id = in_id FROM Deleted
    EXECUTE atualizar_incorporacao @incorporacao_id
END
GO

-- Trigger para replicar data de alteração de item em incorporação referenciada
CREATE TRIGGER incorporacao_item_incorporacao ON pat_mobiliario.tb_incorporacao_item
AFTER INSERT, UPDATE
AS
BEGIN
    DECLARE @incorporacao_id INT
    SELECT @incorporacao_id = in_id FROM Inserted
    EXECUTE atualizar_incorporacao @incorporacao_id
END
GO

-- Trigger para replicar data de alteração de item em incorporação referenciada ao o item de incorporação
CREATE TRIGGER incorporacao_item_incorporacao_delete ON pat_mobiliario.tb_incorporacao_item
AFTER DELETE
AS
BEGIN
    DECLARE @incorporacao_id INT
    SELECT @incorporacao_id = in_id FROM Deleted
    EXECUTE atualizar_incorporacao @incorporacao_id
END
GO
