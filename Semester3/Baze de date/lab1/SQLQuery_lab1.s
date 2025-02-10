CREATE TABLE Soferi(
    idSofer INT IDENTITY(1,1),
    nume VARCHAR(30),
    vechime INT,
    categoriePermis VARCHAR(10),
    salariu INT,
    CONSTRAINT PK_Soferi PRIMARY KEY(idSofer),
    CONSTRAINT CK_Vechime_Sofer CHECK(vechime>=0 AND vechime<=46)
);


CREATE TABLE Clienti(
    idClient INT IDENTITY(1,1),
    nume VARCHAR(30),
    email VARCHAR(100),
    telefon VARCHAR(12),
    tipClient VARCHAR(8),
    CONSTRAINT PK_Clienti PRIMARY KEY(idClient)
);


CREATE TABLE Adresa(
    idAdresa INT IDENTITY(1,1),
    judet VARCHAR(50),
    localitate VARCHAR(50),
    strada VARCHAR(50),
    numar INT,
    bloc VARCHAR(30),
    apartament INT,
    CONSTRAINT PK_Adresa PRIMARY KEY(idAdresa)
);


CREATE TABLE Masini(
    nrInmatriculare VARCHAR(7),
    marca VARCHAR(12),
    capacitateCuti INT,
    combustibil VARCHAR(15),
    kilometraj INT,
    idSofer INT,
    CONSTRAINT PK_Masini PRIMARY KEY (nrInmatriculare),
    CONSTRAINT FK_Sofer_Masini FOREIGN KEY(idSofer) REFERENCES Soferi(idSofer) ON UPDATE CASCADE ON DELETE SET NULL
);


CREATE TABLE Livrari(
    idLivrare INT IDENTITY(1,1),
    idSofer INT,
    idAdresaLivrare INT,
    dataLivrare DATE,
    statusLivrare VARCHAR(20),
    CONSTRAINT PK_Livrari PRIMARY KEY(idLivrare),
    CONSTRAINT FK_Sofer_Livrari FOREIGN KEY(idSofer) REFERENCES Soferi(idSofer) ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT FK_Adresa_Livrari FOREIGN KEY(idAdresaLivrare) REFERENCES Adresa(idAdresa) ON UPDATE CASCADE ON DELETE SET NULL
);


CREATE TABLE Colet(
    idColet INT IDENTITY(1,1),
    idClient INT,
    idLivrare INT,
    greutate INT,
    volum FLOAT,
    dataExpedierii DATE,
    stare VARCHAR(20),
    CONSTRAINT PK_Colet PRIMARY KEY(idColet),
    CONSTRAINT FK_Client_Colet FOREIGN KEY (idClient) REFERENCES Clienti(idClient) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_Livrare_Colet FOREIGN KEY (idLivrare) REFERENCES Livrari(idLivrare) ON UPDATE CASCADE ON DELETE SET NULL
);


CREATE TABLE Depozite(
    idDepozit INT IDENTITY(1,1),
    nume VARCHAR(30),
    capacitateCuti INT,
    telefon VARCHAR(12),
    idAdresaDepozit INT,
    CONSTRAINT PK_Depozite PRIMARY KEY(idDepozit),
    CONSTRAINT FK_Adresa_Depozit FOREIGN KEY(idAdresaDepozit) REFERENCES Adresa(idAdresa) ON UPDATE CASCADE ON DELETE SET NULL
);


CREATE TABLE Tranzit(
    idColet INT,
    idDepozit INT,
    dataIntrare DATE,
    dataIesire DATE,
    CONSTRAINT PK_Tranzit PRIMARY KEY(idColet,idDepozit)
);


CREATE TABLE Trasee(
    idTraseu INT IDENTITY(1,1),
    idSofer INT,
    lungime INT,
    durata INT,
    idDepozit INT, --depozitul din care plec si in care ma intorc
    CONSTRAINT PK_Trasee PRIMARY KEY(idTraseu),
    CONSTRAINT FK_Sofer_Trasee FOREIGN KEY(idSofer) REFERENCES Soferi(idSofer) ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT FK_Depozit_Trasee FOREIGN KEY(idDepozit) REFERENCES Depozite(idDepozit) ON UPDATE CASCADE ON DELETE SET NULL,
);

CREATE TABLE Retur(
    idRetur INT IDENTITY(1,1),
    idColet INT,
    dataCererii DATE,
    statusRetur VARCHAR(20),
    CONSTRAINT PK_Retur PRIMARY KEY(idRetur),
    CONSTRAINT FK_Colet_Retur FOREIGN KEY(idColet) REFERENCES Colet(idColet) ON UPDATE CASCADE ON DELETE CASCADE
);