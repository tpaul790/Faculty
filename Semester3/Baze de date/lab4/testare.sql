USE CompanieCurierat;
DBCC CHECKIDENT ('TestRuns', RESEED, 0);
DELETE FROM Views;
/*
Tabelele din Tables
    1. Soferi - un camp ca si cheie primara si nicio cheie straina
    2. Masini - un camp ca si cheie primara si alt camp ca si cheie straina
    3. Retur  - doua campuri care reprezinta cheia primara
*/

INSERT INTO Tables(Name) VALUES
    ('soferi'),('masini'),('facturi');

SELECT * from Tables;
go;

/*
View urile din Views
    1. ViewSoferi       - foloseste un select pe o singura tabela
    2. ViewSoferiMasini - foleste un select pe doua tabele
    3. ViewLivrariJudet - folosete un select pe doua tabele si clauza group by
*/

-- tot despre toti soferii
CREATE VIEW ViewSoferi AS SELECT * from Soferi;
go;
SELECT * FROM ViewSoferi;
go;
DROP VIEW ViewSoferi;
go;

-- nume soferului si masina/masinile pe care o/le conduce
CREATE VIEW ViewMasini AS
    SELECT S.nume, M.nrInmatriculare, M.marca FROM Soferi S
    INNER JOIN Masini M ON S.idSofer = M.idSofer;
go;
SELECT * FROM ViewMasini;
go;
DROP VIEW ViewMasini;
go;


-- numele clientului si nr de facturi ale fiecaruia ---
CREATE VIEW ViewFacturi AS
    SELECT C.nume, COUNT(F.idFactura) AS nrFacturi FROM Clienti C
    INNER JOIN Colet Co ON C.idClient = Co.idColet 
    INNER JOIN Facturi F ON F.idFactura = Co.idColet
    GROUP BY (C.nume);
go;
    
SELECT * from ViewFacturi;
go;
DROP VIEW ViewFacturi;
go;


--adaug view urile in Views
INSERT INTO Views(Name) VALUES
    ('ViewSoferi'), ('ViewMasini') , ('ViewFacturi');
SELECT * FROM Views;
go;

DBCC CHECKIDENT ('Views', RESEED, 0);
DELETE FROM Views;


--adaug teste in Tests--
INSERT INTO Tests(Name) VALUES
    ('DIV_soferi'), ('DIV_masini'),('DIV_facturi')
SELECT * FROM Tests;
go;

DBCC CHECKIDENT ('Tests', RESEED, 0);
DELETE FROM Tests;


-- fac legatura dintre tabele si teste
INSERT INTO TestTables(TestID, TableID, NoOfRows, [Position]) VALUES
    (1,1,10,1),(2,2,8,2),(3,3,12,3);
INSERT INTO TestTables(TestID, TableID, NoOfRows, [Position]) VALUES
    (2,1,10,1),(3,1,7,2);
SELECT * from TestTables;
go;

DELETE FROM Views;


-- fac legatura dintre views si teste
INSERT INTO TestViews(TestID, ViewID) VALUES
    (1,1),(2,2),(3,3);
SELECT * FROM TestViews;
go;

DELETE FROM TestViews;
go;


--- creez procedura stocata care adauga nrOfRows intregistrari in tabelul soferi ---

CREATE PROCEDURE insert_soferi @nrOfRows INT AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @idsofer INT = (SELECT MAX(S.idSofer) FROM Soferi S);
    SET @idsofer = @idsofer + 1;
    DECLARE @nume VARCHAR(30);
    DECLARE @vechime INT;
    DECLARE @categoriePermis VARCHAR(10);
    DECLARE @salariu INT;
    DECLARE @n INT = 0;

    WHILE(@n < @nrOfRows)
    BEGIN
        SET @nume = 'Test_Sofer_' + CONVERT(VARCHAR(10), @idsofer);
        SET @vechime = @idsofer % 10;
        SET @categoriePermis = 'B';
        SET @salariu = @idsofer * 1000;
        INSERT INTO Soferi VALUES(@nume, @vechime, @categoriePermis, @salariu);
        SET @idsofer = @idsofer + 1;
        SET @n = @n + 1;
    END

    PRINT 'S-au adaugat '+CONVERT(VARCHAR(10), @nrOfRows)+' soferi in tabel';
END

EXEC insert_soferi 5;
SELECT * FROM Soferi;

--- creez procedura stocata care sterge inregistrari test din tabelul soferi ---
go;
CREATE PROCEDURE delete_soferi AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM Soferi
    WHERE nume LIKE 'Test_Sofer_%';
    PRINT 'S-au sters toate inregistrarile test din tabelul soferi'
END

EXEC delete_soferi;


--- creez procedura stocata care adauga nrOfRows masini in tabel
go;
CREATE PROCEDURE insert_masini @nrOfRows INT AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @nrIntmatriculare VARCHAR(10);
    DECLARE @marca VARCHAR(15) = 'dacia';
    DECLARE @capacitateCuti INT;
    DECLARE @combustibil VARCHAR(15) = 'diesel';
    DECLARE @kilometraj INT;
    DECLARE @idSofer INT = (SELECT TOP 1 idSofer FROM Soferi);
    DECLARE @n INT = 0;

    WHILE(@n < @nrOfRows)
    BEGIN
        SET @n = @n + 1;
        SET @nrIntmatriculare = 'Test_' + CONVERT(VARCHAR(5), @n);
        SET @capacitateCuti = @n * 10;
        SET @kilometraj = @n *10000;
        INSERT INTO Masini VALUES(@nrIntmatriculare, @marca, @capacitateCuti, @combustibil, @kilometraj, @idSofer);
    END

    PRINT 'S-au adaugat '+CONVERT(VARCHAR(10), @nrOfRows)+' masini in tabel!'
END

EXEC insert_masini 5;
SELECT * FROM Masini;

--- creez procedura stocata care sterge toate masinile test din tabel
go;
CREATE PROCEDURE delete_masini AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM Masini 
    WHERE nrInmatriculare LIKE 'Test_%'
    PRINT 'S-au sters toate masinile test din tabel'
END

EXEC delete_masini;

SELECT * from Tranzit;


--- creez procedura care adauga nrOfRows inregistrari in tabelul facturi ---
go;
CREATE PROCEDURE insert_facturi @nrOfRows INT AS
BEGIN
    SET NOCOUNT ON;
    --- inserez o adresa,un client, o livrare de test pentru a putea crea un colet de test ----
    INSERT INTO Adresa VALUES('Test','Test','Test',1,null,null);
    DECLARE @idAdresaTest INT = (SELECT idAdresa FROM Adresa WHERE judet = 'Test');

    INSERT INTO Clienti VALUES('Test','Test','0','Test');
    DECLARE @idClientTest INT = (SELECT idClient FROM Clienti WHERE nume = 'Test');

    --- in tabelul soferi vor fi deja adaugati soferi de test ---
    DECLARE @idSofer INT = (SELECT TOP 1 idSofer FROM Soferi WHERE nume LIKE 'Test_Sofer_%');
    INSERT INTO Livrari VALUES(@idSofer, @idAdresaTest, '3000-12-31','Test');
    DECLARE @idLivrare INT = (SELECT idLivrare FROM Livrari WHERE idAdresaLivrare = @idAdresaTest AND idSofer = @idSofer);
    

    INSERT INTO Colet VALUES(@idClientTest, @idLivrare, 0, 0, '3000-12-31','Test');

    DECLARE @idFactura INT = (SELECT idColet FROM Colet WHERE idClient = @idClientTest AND idLivrare = @idAdresaTest);
    DECLARE @firma VARCHAR(100);
    DECLARE @nrFactura INT = 11;
    DECLARE @totalPlata INT = 1200;
    DECLARE @n INT = 0;

    WHILE(@n < @nrOfRows)
    BEGIN
        SET @n = @n + 1;
        SET @firma = 'Test_firma_'+CONVERT(VARCHAR(10), @n);
        INSERT INTO Facturi VALUES(@idFactura, @firma, @nrFactura, @totalPlata);
    END

    PRINT 'S-au inserat '+CONVERT(VARCHAR(10), @nrOfRows)+' facturi in tabel'
END

EXEC insert_facturi 5;
EXEC delete_facturi;
select * from Facturi;

--- creez procedura stocata care sterge toate facturile test ---
go;
CREATE PROCEDURE delete_facturi AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM Facturi 
    WHERE firma LIKE 'Test_firma_%';

    --- trebuie sa sterg tot ce am adaugat pentru a creea facturile ---
    DECLARE @idAdresaTest INT = (SELECT idAdresa FROM Adresa WHERE judet = 'Test');
    DECLARE @idClientTest INT = (SELECT idClient FROM Clienti WHERE nume = 'Test');
    DECLARE @idSofer INT = (SELECT TOP 1 idSofer FROM Soferi WHERE nume LIKE 'Test_Sofer_%');
    DECLARE @idLivrare INT = (SELECT idLivrare FROM Livrari WHERE idAdresaLivrare = @idAdresaTest AND idSofer = @idSofer);

    DELETE FROM Colet WHERE idClient = @idClientTest AND idLivrare = @idLivrare;
    DELETE FROM Livrari WHERE idAdresaLivrare = @idAdresaTest AND idSofer = @idSofer;
    DELETE FROM Clienti WHERE idClient = @idClientTest;
    DELETE FROM Adresa WHERE idAdresa = @idAdresaTest;
    --soferul va fi sters de apelul functiei delete_generic ---
    PRINT 'S-au sters toate inregistrarile test din tabelul facturi'
END

DROP PROC insert_facturi;
DROP PROC delete_facturi;


--- procedura generica de inserare --- 
go;
CREATE PROCEDURE inserat_generic @idTest INT AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @tabel NVARCHAR(50);
    DECLARE @test NVARCHAR(50) = (SELECT Name FROM Tests WHERE TestID = @idTest);
    DECLARE @nrOfRows INT;
    DECLARE @procedura VARCHAR(100);

    DECLARE cursorTabel CURSOR FAST_FORWARD FOR
    SELECT Tab.Name, T.NoOfRows FROM Tables Tab
    INNER JOIN TestTables T ON Tab.TableID = T.TableID
    WHERE T.TestID = @idTest
    ORDER BY T.[Position];

    OPEN cursorTabel;

    FETCH NEXT FROM cursorTabel INTO @tabel, @nrOfRows;
    WHILE(@@FETCH_STATUS = 0)
    BEGIN
        SET @procedura = 'insert_' + @tabel;
        EXEC @procedura @nrOfRows;
        FETCH NEXT FROM cursorTabel INTO @tabel, @nrOfRows;
    END

    CLOSE cursorTabel;
    DEALLOCATE cursorTabel;
END

EXEC inserat_generic 3;
EXEC delete_generic 3;
DROP PROC inserat_generic;
EXEC delete_masini;
SELECT * from Masini;


--- stergere generica ---
go;
CREATE PROCEDURE delete_generic @idTest INT AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @tabel NVARCHAR(50);
    DECLARE @test NVARCHAR(50) = (SELECT Name FROM Tests WHERE TestID = @idTest);
    DECLARE @procedura VARCHAR(100);

    DECLARE cursorTabel CURSOR FAST_FORWARD FOR
    SELECT Tab.Name FROM Tables Tab
    INNER JOIN TestTables T ON Tab.TableID = T.TableID
    WHERE T.TestID = @idTest
    ORDER BY T.[Position] DESC;

    OPEN cursorTabel;

    FETCH NEXT FROM cursorTabel INTO @tabel;
    WHILE(@@FETCH_STATUS = 0)
    BEGIN
        SET @procedura = 'delete_' + @tabel;
        EXEC @procedura;
        FETCH NEXT FROM cursorTabel INTO @tabel;
    END

    CLOSE cursorTabel;
    DEALLOCATE cursorTabel;
END

EXEC delete_generic 2;
drop proc delete_generic;


--- procedura generica care afiseaza view ul testat ---
go;
CREATE PROCEDURE select_view_generic @idTest INT AS
BEGIN
    DECLARE @viewName NVARCHAR(50) = (SELECT V.Name FROM Views V 
                                     INNER JOIN TestViews T ON T.ViewID = V.ViewID
                                     WHERE T.TestID = @idTest)
    DECLARE @statement NVARCHAR(100) = 'SELECT * FROM ' + @viewName;
    EXECUTE (@statement);
END

drop procedure select_view_generic;

EXEC select_view_generic 3;
select * from ViewSoferi;


--- procedura care executa un test si completeaza tabelele de run
go;
CREATE PROCEDURE run_test @idTest INT AS
BEGIN
    DECLARE @stTabel DATETIME = GETDATE();
    EXECUTE inserat_generic @idTest;
    EXECUTE delete_generic @idTest;
    DECLARE @intermediar DATETIME = GETDATE();

    EXECUTE select_view_generic @idTest;
    DECLARE @sfView DATETIME = GETDATE();

    DECLARE @test NVARCHAR = (SELECT Name FROM Tests WHERE TestID = @idTest);
    INSERT INTO TestRuns VALUES(@test, @stTabel, @sfView);
     DECLARE @testRunId INT = (SELECT TOP 1 TestRunId FROM TestRuns
                              WHERE [Description] = @test 
                              ORDER BY TestRunID DESC)

    DECLARE @viewId INT = (SELECT ViewId FROM TestViews WHERE TestID = @idTest);
    INSERT INTO TestRunViews VALUES(@testRunId, @viewId, @intermediar, @sfView);

    DECLARE  cursorTabId CURSOR FAST_FORWARD FOR
        SELECT TableID FROM TestTables T 
        WHERE TestID = @idTest;
    
    OPEN cursorTabId;


    DECLARE @idTab INT;
    FETCH NEXT FROM cursorTabId INTO @idTab;

    WHILE(@@FETCH_STATUS = 0)
    BEGIN
        INSERT INTO TestRunTables VALUES(@testRunId ,@idTab, @stTabel, @intermediar);
        FETCH NEXT FROM cursorTabId INTO @idTab;
    END

    CLOSE cursorTabId;
    DEALLOCATE cursorTabId;

    PRINT 'TEST WAS SUCCESFULLY COMPLETED IN '+CONVERT(VARCHAR(10), DATEDIFF(millisecond,@stTabel, @sfView))+ ' MILISECONDS';
END


EXEC run_test 2;

SELECT * FROM Tables;
SELECT * FROM TestRuns;
SELECT * FROM TestRunTables;
SELECT * FROM TestRunViews;
SELECT * FROM Tests;
SELECT * FROM TestTables;
SELECT * FROM TestViews;
SELECT * FROM Views;

DELETE FROM TestRuns;
