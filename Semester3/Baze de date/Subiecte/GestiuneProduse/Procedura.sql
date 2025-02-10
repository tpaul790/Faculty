CREATE PROCEDURE addCumparaturi @idClient INT, @idMagazin INT, @data DATE, @pret FLOAT
AS
BEGIN
    IF(EXISTS(SELECT * FROM Cumparaturi WHERE idClient = @idClient AND idMagazin = @idMagazin))
    BEGIN
        UPDATE Cumparaturi 
        SET dataCumparaturii = @data, pretAchitat = @pret 
        WHERE idClient = @idClient AND idMagazin = @idMagazin;
        PRINT 'Datele cumparaturii au fost actualizate cu succes'
    END
    ELSE
    BEGIN
        INSERT INTO Cumparaturi VALUES (@idClient,@idMagazin,@data,@pret);
        PRINT 'Cumparatura a fost adaugata cu succes'
    END
END

select * from Cumparaturi;
EXECUTE addCumparaturi 1,1,'2024-12-21',132
EXECUTE addCumparaturi 1,3,'2024-12-21',432
