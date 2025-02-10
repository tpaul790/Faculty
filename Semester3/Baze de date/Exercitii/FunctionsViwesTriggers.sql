-- o functie care returneaza id ul unei categorii cu numele dat
CREATE OR ALTER FUNCTION codCategorie(@nume VARCHAR(100))
RETURNS INT
AS
BEGIN
    DECLARE @cod INT;
    SET @cod = 0;
    IF(EXISTS(SELECT idCategorie FROM CategoriiVizitatori WHERE nume = @nume))
    BEGIN
        SELECT @cod = idCategorie FROM CategoriiVizitatori WHERE nume = @nume
        RETURN @cod;
    END
    return -1;
END
go;

-- un trigger care impiedica realizarea operatiilor de stergere pe tabelul categorii, aifsand un mesaj corespunzator
CREATE TRIGGER deleteCategorii ON CategoriiVizitatori
INSTEAD OF DELETE
AS
BEGIN
    RAISERROR('Nu se pot face stergeri momentan',16,1);
END

delete from CategoriiVizitatori;
go;

-- view care afiseaza toate categoriile cu nume pensionari sau copii --
CREATE VIEW viewCategorii
AS 
SELECT * FROM CategoriiVizitatori WHERE nume in ('copii', 'pensionari');
go;

-- view care afiseaza toate sectiunile al caror nume incepe cu s --
CREATE VIEW viewSectiuni 
AS
SELECT * FROM Sectiuni WHERE nume LIKE 's%';
go;

/*
 o functie inline table valued care returneaza toate inregistrarile din tabelul sectiuni al caror nume se termina cu o litera
 data ca parametru si are cel putin doua caractere
*/
CREATE OR ALTER FUNCTION SectiuniThatEndWith(@char CHAR)
RETURNS TABLE
AS
    RETURN SELECT * FROM Sectiuni WHERE nume LIKE '_'+@char;
go;

-- un view care afiseza numele vizitatorului, al atractiei si nota oferita de catre vizitator atractiei respective --
CREATE VIEW viewNote AS
    SELECT V.nume AS numeVizitator, A.nume as numeAtractie, N.nota FROM
    Vizitatori V INNER JOIN Note N ON V.idVizitator = N.idVizitator
    INNER JOIN Atractii A ON A.idAtractie = N.idAtractie;
go;