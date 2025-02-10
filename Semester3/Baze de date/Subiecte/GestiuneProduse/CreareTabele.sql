CREATE TABLE Clienti(
    idClient INT IDENTITY(1,1),
    nume VARCHAR(100),
    prenume VARCHAR(100),
    gen VARCHAR(50),
    dataNasterii DATE,
    CONSTRAINT PK_Clienti PRIMARY KEY(idClient)
);

CREATE TABLE Favorite(
    idFavorit INT IDENTITY(1,1),
    denumire VARCHAR(100),
    pret FLOAT,
    reducere FLOAT,
    idClient INT,
    CONSTRAINT PK_Favorite PRIMARY KEY(idFavorit),
    CONSTRAINT FK_Favorite_Clienti FOREIGN KEY(idClient) REFERENCES Clienti(idClient)
)

CREATE TABLE Locatii(
    idLocatie INT IDENTITY(1,1),
    localitate VARCHAR(100),
    strada VARCHAR(100),
    numar INT,
    codPostal VARCHAR(20),
    CONSTRAINT PK_Locatii PRIMARY KEY(idLocatie)
);

CREATE TABLE Magazine(
    idMagazin INT IDENTITY(1,1),
    denumire VARCHAR(100),
    anDeschidere INT,
    idLocatie INT,
    CONSTRAINT PK_Magazine PRIMARY KEY(idMagazin),
    CONSTRAINT FK_Magazine_Locatii FOREIGN KEY(idLocatie) REFERENCES Locatii(idLocatie)
)

CREATE TABLE Cumparaturi(
    idClient INT,
    idMagazin INT,
    dataCumparaturii DATE,
    pretAchitat FLOAT,
    CONSTRAINT PK_Cumparaturi PRIMARY KEY(idClient, idMagazin),
    CONSTRAINT FK_Cumparaturi_Clienti FOREIGN KEY(idClient) REFERENCES Clienti(idClient),
    CONSTRAINT FK_Cumparaturi_Magazine FOREIGN KEY(idMagazin) REFERENCES Magazine(idMagazin)
)