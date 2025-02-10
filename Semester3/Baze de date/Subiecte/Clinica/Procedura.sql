CREATE PROCEDURE addConsultatie 
    @idMedic INT, 
    @idPacient INT, 
    @dataConsultatie DATE, 
    @oraConsultatie TIME,
    @diagnostic VARCHAR(100),
    @observatii VARCHAR(100)
AS
BEGIN
    IF(EXISTS(SELECT * FROM Consultatii WHERE idMedic = @idMedic AND idPacient = @idPacient AND dataConsultatie = @dataConsultatie))
    BEGIN
        UPDATE Consultatii 
        SET oraConsultatiei = @oraConsultatie, observatii = @observatii, diagnostic = @diagnostic
        WHERE idMedic = @idMedic AND idPacient = @idPacient AND dataConsultatie = @dataConsultatie;
        PRINT 'Consultatie actualizata cu succes'
    END
    ELSE
    BEGIN
        INSERT INTO Consultatii VALUES(@idMedic,@idPacient,@dataConsultatie,@oraConsultatie,@diagnostic,@observatii);
        PRINT 'Consultatie adaugata cu succes'
    END
END


SELECT * FROM Consultatii;
EXECUTE addConsultatie 3,1,'2023-12-01','09:30','plomba','default'

create database x;