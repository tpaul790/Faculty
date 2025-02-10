USE CompanieCurierat;
go;

---------------------------------CRUD pentru Depozite-------------------------------------
CREATE PROCEDURE CRUD_Depozite
        @nume VARCHAR(100),
        @capacitateCutii INT,
        @telefon VARCHAR(15),
        @adresa INT
AS
BEGIN
    IF(dbo.pozitivInt(@capacitateCutii) = 1 AND dbo.validareTelefon(@telefon) = 1 AND dbo.existaAdresa(@adresa) = 1)
    BEGIN
        INSERT INTO Depozite VALUES(@nume, @capacitateCutii, @telefon, @adresa);

        SELECT * FROM Depozite;

        UPDATE Depozite SET capacitateCuti = 13242 WHERE nume = 'FedLex';

        DELETE FROM Depozite WHERE capacitateCuti = 0;

        PRINT('CRUD realizat cu succes pe tabela Depozite');
    END
    ELSE
    BEGIN
        PRINT('Datele nu sunt valide, mai incearca!');
    END
END

EXECUTE dbo.CRUD_Depozite 'FedLex3', 0,'0763196231',4;  --valid--
EXECUTE dbo.CRUD_Depozite 'FedLex',-1,'063196',102;         --invalid--



---------------------------------CRUD pentru Colet-------------------------------------
go;
CREATE PROCEDURE CRUD_Colet
    @client INT,
    @livrare INT,
    @greutate BIGINT,
    @volum FLOAT,
    @dataExpedierii DATE,
    @stare VARCHAR(20)
AS
BEGIN
    IF(dbo.existaClient(@client) = 1 AND dbo.existaLivrare(@livrare) = 1 AND dbo.pozitivBigInt(@greutate) = 1 AND dbo.pozitivFloat(@volum) = 1)
    BEGIN
        INSERT INTO Colet VALUES(@client, @livrare, @greutate, @volum, @dataExpedierii, @stare);

        SELECT * FROM Colet;

        UPDATE Colet SET stare='livrat' WHERE dataExpedierii<'2024-01-01';

        DELETE FROM Colet WHERE greutate = 0 AND volum = 0;

        PRINT('CRUD realizat cu succes pe tabela Depozite');
    END
    ELSE
    BEGIN
        PRINT('Datele nu sunt valide, mai incearca!');
    END
END
DELETE from Colet where idColet BETWEEN 2010 and 2011;
EXECUTE dbo.CRUD_Colet 1, 2, 12, 0.3, '2023-03-02', 'In livrare';       ---valid---
EXECUTE dbo.CRUD_Colet 112, 231, 12, 0.3, '2024-03-02', 'In livrare';  ---invalid---



---------------------------------CRUD pentru Tranzit-------------------------------------
go;
CREATE OR ALTER PROCEDURE CRUD_Tranzit 
    @colet INT,
    @depozit INT,
    @dataIntrare DATE,
    @dataIesire DATE
AS
BEGIN
    IF(dbo.existaColet(@colet) = 1 AND dbo.existaDepozit(@depozit) = 1 AND dbo.existaTranzit(@colet, @depozit) = 0)
    BEGIN
        INSERT INTO Tranzit VALUES(@colet, @depozit, @dataIntrare, @dataIesire);

        SELECT * from Tranzit;

        UPDATE Tranzit SET dataIntrare = dataIesire WHERE dataIntrare > dataIesire;

        DELETE FROM Tranzit WHERE dataIesire <= '2020-01-01';

        PRINT('CRUD realizat cu succes pe tabela Depozite');
    END
    ELSE
    BEGIN
        PRINT('Datele nu sunt valide, mai incearca!');
    END
END

EXECUTE dbo.CRUD_Tranzit 1, 9, '2019-10-11', '2019-10-12';    ---valid---
EXECUTE dbo.CRUD_Tranzit 12, 533, '2019-10-11', '2019-10-12'; ---invalid---




---------------------------------CRUD pentru Soferi-------------------------------------
go;
CREATE PROCEDURE CRUD_Soferi
    @nume VARCHAR(30),
    @vechime INT,
    @categoriePermis VARCHAR(10),
    @salariu INT
AS
BEGIN
    IF(dbo.pozitivInt(@vechime) = 1 AND dbo.pozitivInt(@salariu) = 1)
    BEGIN
        INSERT INTO Soferi VALUES(@nume, @vechime, @categoriePermis, @salariu);

        SELECT * FROM Soferi;

        UPDATE Soferi SET categoriePermis = 'B' WHERE categoriePermis NOT IN ('B','C','D','E');

        DELETE FROM Soferi WHERE vechime < 2;

        PRINT('CRUD realizat cu succes pe tabela Depozite');
    END
    ELSE
    BEGIN
        PRINT('Datele nu sunt valide, mai incearca!');
    END
END

EXECUTE CRUD_Soferi 'Mircea', 1, 'L',12000;     ---valid---
EXECUTE CRUD_Soferi 'Mircea', -23, 'dfa', -123; ---invalid---



---------------------------------CRUD pentru Clienti-------------------------------------
go;
CREATE PROCEDURE CRUD_Clienti
    @nume VARCHAR(35),
    @email VARCHAR(100),
    @telefon VARCHAR(15),
    @tipClient VARCHAR(12)
AS
BEGIN
    IF(dbo.validareEmail(@email) = 1 AND dbo.validareTelefon(@telefon) = 1)
    BEGIN
        INSERT INTO Clienti VALUES(@nume, @email, @telefon, @tipClient);

        SELECT * FROM Clienti;

        UPDATE Clienti SET tipClient = 'fizic' WHERE tipClient NOT IN ('fizic', 'juridic');

        DELETE FROM Clienti WHERE nume LIKE 'BOT_%';

        PRINT('CRUD realizat cu succes pe tabela Depozite');
    END
    ELSE
    BEGIN
        PRINT('Datele nu sunt valide, mai incearca!');
    END
END

DELETE FROM Clienti WHERE idClient BETWEEN 2011 AND 3000;
EXECUTE CRUD_Clienti 'TPaul', 'tpaul@gmail.com','0763196245','fizic';   ---valid---
EXECUTE CRUD_Clienti 'TPaul', 'email', '3210', 'client';                ---invalid--