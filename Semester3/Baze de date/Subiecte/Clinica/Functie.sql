CREATE OR ALTER FUNCTION veziMedici(@ora TIME)
RETURNS TABLE
AS
RETURN SELECT M.prenume, M.nume FROM Medici M 
    INNER JOIN Consultatii C ON M.idMedic = C.idMedic
    WHERE C.oraConsultatiei = @ora AND
    EXISTS(SELECT * FROM Medici M 
        INNER JOIN Consultatii C ON M.idMedic = C.idMedic
        GROUP BY M.idMedic,M.nume,M.prenume,C.dataConsultatie
        HAVING COUNT(C.oraConsultatiei) > 1)
    GROUP BY M.idMedic,M.prenume, M.nume

go;
SELECT * from Consultatii;
SELECT * FROM veziMedici('9:00');
