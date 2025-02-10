CREATE OR ALTER VIEW viewClienti
AS
    SELECT C.prenume as NumeClient FROM
    Clienti C INNER JOIN Favorite F ON C.idClient = F.idClient
    GROUP BY C.idClient,C.prenume
    HAVING COUNT(F.idFavorit) <= 3;
go;

SELECT * FROM viewClienti;
