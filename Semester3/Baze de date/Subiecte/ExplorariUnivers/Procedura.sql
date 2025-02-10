CREATE PROCEDURE addMisiune @idPlaneta INT, @idExplorator INT, @data DATE, @desc VARCHAR(100) 
AS
BEGIN
    IF(EXISTS(SELECT * FROM Misiuni WHERE idPlaneta = @idPlaneta AND idExplorator = @idExplorator))
    BEGIN
        UPDATE Misiuni
        SET dataMisiunii = @data, descriere = @desc
        WHERE idPlaneta = @idPlaneta AND idExplorator = @idExplorator;
        PRINT 'Misiune actualizata cu succes';
    END
    ELSE
    BEGIN
        INSERT INTO Misiuni VALUES(@idPlaneta,@idExplorator,@data,@desc)
    END
END
