USE CompanieCurierat;

go;
CREATE OR ALTER VIEW viewClientDepozitColet AS
    SELECT C.nume AS NumeClient, D.nume AS NumeDepozit, Co.idColet FROM
    Clienti C INNER JOIN Colet Co ON C.idClient = Co.idClient 
    INNER JOIN Tranzit T ON T.idColet = Co.idColet 
    INNER JOIN Depozite D ON D.idDepozit = T.idDepozit;
GO;

SELECT * FROM viewClientDepozitColet ORDER BY NumeClient;
CREATE NONCLUSTERED INDEX idx_nume_Clienti ON Clienti(nume);
CREATE NONCLUSTERED INDEX idx_idClient_Colet ON Colet(idClient);
CREATE NONCLUSTERED INDEX idx_idColet_Tranzit ON Tranzit(idColet);

SELECT * FROM viewClientDepozit WHERE NumeDepozit LIKE 'FedLex%';



GO;
CREATE OR ALTER VIEW viewSoferi AS
    SELECT nume FROM Soferi WHERE vechime > 5;

GO;
SELECT * FROM viewSoferi ORDER BY nume;

CREATE NONCLUSTERED INDEX idx_categoriePermis_Soferi ON Soferi(categoriePermis);
CREATE NONCLUSTERED INDEX idx_vechime_Soferi ON Soferi(vechime) INCLUDE (nume);
DROP INDEX idx_vechime_Soferi ON Soferi;
