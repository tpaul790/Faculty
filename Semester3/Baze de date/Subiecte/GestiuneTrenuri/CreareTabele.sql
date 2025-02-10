CREATE TABLE Tipuri(
    idTip INT IDENTITY(1,1),
    descriere VARCHAR(100),
    CONSTRAINT PK_Tipuri PRIMARY KEY(idTip)
);

CREATE TABLE Trenuri(
    idTren INT IDENTITY(1,1),
    nume VARCHAR(100),
    idTip INT,
    CONSTRAINT PK_Trenuri PRIMARY KEY(idTren),
    CONSTRAINT FK_Trenuri_Tipuri FOREIGN KEY(idTip) REFERENCES Tipuri(idTip)
);

CREATE TABLE Rute(
    idRuta INT IDENTITY(1,1),
    nume VARCHAR(100),
    idTren INT,
    CONSTRAINT PK_Rute PRIMARY KEY(idRuta),
    CONSTRAINT FK_Rute_Trenuri FOREIGN KEY(idTren) REFERENCES Trenuri(idTren)
);

CREATE TABLE Statii(
    idStatie INT IDENTITY(1,1),
    nume VARCHAR(100),
    CONSTRAINT PK_Statii PRIMARY KEY(idStatie)
);

CREATE TABLE Opriri(
    idRuta INT,
    idStatie INT,
    oraSosire TIME,
    oraPlecare TIME,
    CONSTRAINT PK_Opriri PRIMARY KEY(idRuta, idStatie),
    CONSTRAINT FK_Opriri_Rute FOREIGN KEY(idRuta) REFERENCES Rute(idRuta),
    CONSTRAINT FK_Opriri_Statii FOREIGN KEY(idStatie) REFERENCES Statii(idStatie)
);