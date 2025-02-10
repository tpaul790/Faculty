CREATE PROCEDURE addAchizitie 
    @idStudent INT,
    @idCafea INT,
    @cantitate FLOAT,
    @cost FLOAT
AS
BEGIN
    IF(EXISTS(SELECT * FROM Achizitii WHERE idStudent = @idStudent AND idCafea = @idCafea))
    BEGIN
        UPDATE Achizitii 
        SET cost = @cost, cantitate = @cantitate
        WHERE idStudent = @idStudent AND idCafea = @idCafea;
        PRINT 'Achizitie actualizata cu succes'
    END
    ELSE
    BEGIN
        INSERT INTO Achizitii VALUES(@idStudent,@idCafea,@cantitate,@cost);
        PRINT 'Achiztia a fost adaugata cu succes'
    END
END

select * from Achizitii;
EXECUTE addAchizitie 2,1,42,53; --- pentru add
EXECUTE addAchizitie 2,3,256,90; --- pentru update