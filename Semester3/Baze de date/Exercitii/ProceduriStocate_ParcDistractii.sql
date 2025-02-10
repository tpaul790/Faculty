---procedura stocata care adauga o sectiune noua in tabelul sectiuni --
CREATE PROCEDURE adaugaSectiune @nume VARCHAR(100), @descriere VARCHAR(100)
AS
BEGIN
    INSERT INTO Sectiuni VALUES(@nume, @descriere);
    SELECT * FROM Sectiuni;
END
go;

/*
procedura care insereaza o inregistrare noua in tabelul categorii, daca exista deja categoria data, se afiseaza
un mesaj si nu se adauga
*/
CREATE OR ALTER PROCEDURE adaugaCategorie @nume VARCHAR(100)
AS
BEGIN
    IF(EXISTS(select * from CategoriiVizitatori WHERE nume = @nume))
        RAISERROR('Exista deja aceasta categorie!',16,1)
    ELSE
    BEGIN
        INSERT INTO CategoriiVizitatori VALUES(@nume);
        SELECT * FROM CategoriiVizitatori;
    END
END
EXEC adaugaCategorie 'someri';
go;
/*
procedura care insereaza o inregistrare noua in tabelul atractii, daca sectiunea data nu exista in tabelul sectiuni, atunci 
se adauga sectiunea si pe urma se adauga atractia
*/

CREATE OR ALTER PROCEDURE adaugaAtractie @nume VARCHAR(100), @descriere VARCHAR(100), @varsta_min INT, @nume_sectiune VARCHAR(100)
AS
BEGIN
    IF(NOT EXISTS(SELECT * FROM Sectiuni where nume = @nume_sectiune))
        INSERT INTO Sectiuni VALUES(@nume_sectiune,'DESCRIERE LIPSA');
    DECLARE @idSectiune INT;
    SELECT @idSectiune = idSectiune FROM Sectiuni WHERE nume = @nume_sectiune;
    INSERT INTO Atractii VALUES(@nume, @descriere, @varsta_min,@idSectiune);
    SELECT * FROM Sectiuni;
    SELECT * FROM Atractii;
END

EXEC adaugaAtractie 'a10','atractia10',8,'s7';
GO;

/*
o procedura care verifica daca exista un vizitator cu adresa de email data, daca exista se va returna codul acestuia,
altfel se va genera un mesaj de eroare
*/
CREATE OR ALTER PROCEDURE verificaVizitator @email VARCHAR(100), @idVizitator INT OUTPUT
AS
BEGIN
    IF(EXISTS(SELECT * FROM Vizitatori WHERE email = @email))
        SELECT @idVizitator = idVizitator FROM Vizitatori WHERE email = @email;
    ELSE
        RAISERROR ('Nu exista niciun vizitator cu acest email!',16,1)
END
DECLARE @id INT;
set @id = 0;
EXECUTE verificaVizitator 'dd', @idVizitator = @id OUTPUT;
PRINT @id;
go;

/*
o procedura stocata care adauga o nota in tabelul note dupa ce verifica ca exista vizitatorul si atractie precum si 
validitatea valorii pentru nota, afiseaza mesaje de eroare in caz de invaliditate
*/
CREATE OR ALTER PROCEDURE adaugaNota @idVizitator INT, @idAtractie INT, @nota INT
AS
BEGIN
    IF(EXISTS(SELECT * FROM Vizitatori WHERE idVizitator = @idVizitator) AND EXISTS(SELECT * FROM Atractii WHERE idAtractie = @idAtractie))
        IF(1 <= @nota AND @nota <= 10)
        BEGIN
            INSERT INTO Note VALUES(@idVizitator, @idAtractie, @nota);
            SELECT * FROM Note;
        END
        ELSE
            RAISERROR('Nota nu este valida!', 16, 1);
    ELSE
        RAISERROR('Nu exista vizitaroul sau atractia data!', 16, 1)
END

EXEC adaugaNota 1,5,8;
go;

/*
o procedura care actualizeaza adresa de email a unui vizitator cu id ul dat
*/
CREATE OR ALTER PROCEDURE actualizareEmailVizitator @idVizitator INT, @email VARCHAR(100)
AS
BEGIN
    UPDATE Vizitatori SET email = @email WHERE idVizitator = @idVizitator;
    SELECT * FROM Vizitatori;
END

EXEC actualizareEmailVizitator 1,'gmail';
GO;

/*
o procedura stocata care returneaza numele vizitatorului, adresa de email si numarul total de note pentru toti vizitatorii
care au dat cel putin o nota unei atractii
*/
CREATE OR ALTER PROCEDURE veziVizitatoriNumarNote 
AS
BEGIN
    SELECT V.nume, V.email,COUNT(N.idVizitator) as numerNote FROM
    Vizitatori V INNER JOIN Note N ON V.idVizitator = N.idVizitator
    GROUP BY V.idVizitator,V.nume, V.email;
END

EXEC veziVizitatoriNumarNote;
go;

/*
o procedura care primeste numele unei atractii si o sterge daca nu exista note atribuite acesteia, in cazul in care
atractia a primit note, atunci se va afisa un mesaj de eroare iar atractia nu se va sterge
*/
CREATE OR ALTER PROCEDURE stergeAtractie @nume VARCHAR(100)
AS
BEGIN
    DECLARE @id INT;
    SET @id = 0;
    SELECT @id = idAtractie FROM Atractii WHERE nume = @nume;
    IF(@id = 0)
    BEGIN
        RAISERROR('Nu exista o atractie cu acest nume', 16, 1);
        RETURN
    END
    IF(EXISTS(SELECT * FROM Note WHERE idAtractie = @id))
    BEGIN
        RAISERROR('Nu se poate sterge aceasta atractie deoarece a primit note', 16, 1);
        RETURN
    END
    DELETE FROM Atractii WHERE idAtractie = @id;
    SELECT * FROM Atractii;
END 

EXEC stergeAtractie 'a10';