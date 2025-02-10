CREATE TABLE Pacienti(
    idPacient INT IDENTITY(1,1),
    nume VARCHAR(100),
    prenume VARCHAR(100),
    adresa VARCHAR(100),
    CONSTRAINT pk_Pacienti PRIMARY KEY(idPacient)
);

CREATE TABLE Specializari(
    denumire VARCHAR(100),
    CONSTRAINT pk_Specializari PRIMARY KEY(denumire)
);

CREATE TABLE Medici(
    idMedic INT IDENTITY(1,1),
    nume VARCHAR(100),
    prenume VARCHAR(100),
    specializare VARCHAR(100),
    CONSTRAINT pk_Medici PRIMARY KEY(idMedic),
    CONSTRAINT fk_Medici_Specializari FOREIGN KEY(specializare) REFERENCES Specializari(denumire)
);

CREATE TABLE Diagnostice(
    denumire VARCHAR(100),
    descriere VARCHAR(100),
    CONSTRAINT pk_Diagnostice PRIMARY KEY(denumire)
);

CREATE TABLE Consultatii(
    idMedic INT,
    idPacient INT,
    dataConsultatie DATE,
    oraConsultatiei TIME,
    diagnostic VARCHAR(100),
    observatii VARCHAR(100),
    CONSTRAINT pk_Consultatie PRIMARY KEY(idMedic,idPacient,dataConsultatie),
    CONSTRAINT fk_Consultatii_Medici FOREIGN KEY(idMedic) REFERENCES Medici(idMedic),
    CONSTRAINT fk_Consultatii_Pacienti FOREIGN KEY(idPacient) REFERENCES Pacienti(idPacient),
    CONSTRAINT fk_COnsultatii_Diagnostice FOREIGN KEY(diagnostic) REFERENCES Diagnostice(denumire)
);
