CREATE OR ALTER PROCEDURE addOprire @idRuta INT, @idStatie INT, @oraSosire TIME, @oraPlecare TIME
AS
BEGIN
    IF(EXISTS(SELECT * FROM Opriri WHERE idStatie = @idStatie AND idRuta = @idRuta))
    BEGIN
        UPDATE Opriri 
        SET oraPlecare = @oraPlecare, oraSosire = @oraSosire 
        WHERE idRuta = @idRuta AND idStatie = @idStatie;
        PRINT 'Oprirea a fost modificata succes!'
    END
    ELSE
    BEGIN
        INSERT INTO Opriri VALUES(@idRuta,@idStatie,@oraSosire,@oraPlecare)
        PRINT 'Oprirea a fost adaugata cu succes!'
    END
END

SELECT * FROM Opriri;

EXECUTE addOprire 2,1,'11:15','11:20';
EXECUTE addOprire 5,1,'10:15','10:27';