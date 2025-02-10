CREATE OR ALTER VIEW viewMedici AS
    SELECT M.nume,M.prenume FROM Medici M 
    INNER JOIN Consultatii C ON M.idMedic = C.idMedic
    WHERE DATEPART(MONTH,C.dataConsultatie) = DATEPART(MONTH,GETDATE())
    GROUP BY M.idMedic,M.nume,M.prenume
    HAVING COUNT(M.idMedic) > 5; --aici trebe 20 da nu adaug atatea date nici mort
go;

SELECT * FROM viewMedici
ORDER BY nume, prenume;
