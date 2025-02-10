/*
    Modifica tipul de date pentru greutatea unui colet din int in bigint
*/
CREATE PROCEDURE coletChangeGreutateType AS
BEGIN
    ALTER TABLE Colet 
    ALTER COLUMN greutate bigint;
    PRINT 'S-a modificat tipul coloanei greutate din int in bigint!'
END
GO
/*
    Modifica tipul de date pentru greutatea unui colet din bigint inapoi in int
*/
CREATE PROCEDURE coletChangeGreutateTypeBack AS
BEGIN
    ALTER TABLE Colet 
    ALTER COLUMN greutate int;
    PRINT 'S-a modificat tipul coloanei greutatea inapoi in int!'
END
GO




/*
    Adauga o constrangere default in tabelul Clienti prin care se seteaza tipul clientul ca persoana fizica
*/
CREATE PROCEDURE clientSetDefaultValueForTip AS
BEGIN
    ALTER TABLE Clienti
    ADD CONSTRAINT DF_tipClient DEFAULT 'fizic' FOR tipClient;
    PRINT 'Constrangere default pentru tipClient adaugata cu succes!'
END
GO
/*
    Sterge constrangerea default din tabelul Clienti prin care se seteaza tipul clientul ca persoana fizica
*/
CREATE PROCEDURE clientDropDefaultValueForTip AS
BEGIN
    ALTER TABLE Clienti
    DROP CONSTRAINT DF_tipClient;
    PRINT 'Constrangere default pentru tipClient stearsa cu succes!'
END
GO




/*
    Creeaza tabelul Facturi care se afla in relatie de 1-1 cu tabelul Colet
*/
CREATE PROCEDURE createTableFacturi AS
BEGIN
    CREATE TABLE Facturi(
        idFactura INT,
        firma VARCHAR(100),
        nrFactura INT,
        CONSTRAINT PK_Facturi PRIMARY KEY(idFactura, firma),
    );
    PRINT 'Tabelul Facturi a fost creat cu succes!'
END
GO


/*
    Sterge tabelul Facturi care se afla in relatie de 1-1 cu tabelul Colet
*/
CREATE PROCEDURE dropTableFacturi AS
BEGIN
    DROP TABLE Facturi; --merge, nush dc il sublineaza cu rosu
    PRINT 'Tabelul Facturi a fost sters cu succes!'
END
GO



/*
    Adauga campul totalPlata in tabelul Facturi
*/
CREATE PROCEDURE addColumnTotalPlataFacturi AS
BEGIN
    ALTER TABLE Facturi 
    ADD totalPlata FLOAT;
    PRINT 'Coloana totalPlata adaugata cu succes in tabelul Facturi!'
END
GO
/*
    Sterge campul totalPlata din tabelul Facturi
*/
CREATE PROCEDURE dropColumnTotalPlataFacturi AS
BEGIN
    ALTER TABLE Facturi 
    DROP COLUMN totalPlata;
    PRINT 'Coloana totalPlata sterasa cu succes din tabelul Facturi!'
END
GO



/*
    Adauga constrangerea de cheie straina in tabelul Facturi care refera tabelul Colet
*/
CREATE PROCEDURE addConstraintColetFacturi AS
BEGIN
    ALTER TABLE Facturi
    ADD CONSTRAINT FK_Colet_Facturi FOREIGN KEY(idFactura) REFERENCES Colet(idColet);
    PRINT 'Constrangerea de cheie straina referita de tabela Colet in tabela Facturi a fost adaugata cu succes!'
END
GO
/*
    Sterge constrangerea de cheie straina din tabelul Facturi care refera tabelul Colet
*/
CREATE PROCEDURE dropConstraintColetFacturi AS
BEGIN
    ALTER TABLE Facturi
    DROP CONSTRAINT FK_Colet_Facturi;
    PRINT 'Constrangerea de cheie straina referita de tabela Colet in tabela Facturi a fost stearsa cu succes!'
END
GO

