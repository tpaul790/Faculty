CREATE TABLE Sortimente(
    idSortiment INT IDENTITY(1,1),
    denumire VARCHAR(100),
    descriere VARCHAR(100),
    CONSTRAINT pk_Sortimente PRIMARY KEY(idSortiment)
);

CREATE TABLE Cafele(
    idCafea INT IDENTITY(1,1),
    denumire VARCHAR(100),
    gramaj FLOAT,
    pret FLOAT,
    idSortiment INT,
    CONSTRAINT pk_Cafele PRIMARY KEY(idCafea),
    CONSTRAINT fk_Cafele_Sortimente FOREIGN KEY(idSortiment) REFERENCES Sortimente(idSortiment)
);

CREATE TABLE Prajituri(
    idPrajitura INT IDENTITY(1,1),
    denumire VARCHAR(100),
    gramaj FLOAT,
    idCafea INT,
    CONSTRAINT pk_Prajituri PRIMARY KEY(idPrajitura),
    CONSTRAINT fk_Prajituri_Cafele FOREIGN KEY(idCafea) REFERENCES Cafele(idCafea)
);

CREATE TABLE Studenti(
    idStudent INT IDENTITY(1,1),
    nume VARCHAR(100),
    prenume VARCHAR(100),
    gen VARCHAR(30),
    dataNasterii DATE,
    CONSTRAINT pk_Studenti PRIMARY KEY(idStudent)
);

CREATE TABLE Achizitii(
    idStudent INT,
    idCafea INT,
    cantitate FLOAT,
    cost FLOAT,
    CONSTRAINT pk_Achizitii PRIMARY KEY(idStudent,idCafea),
    CONSTRAINT fk_Achizitii_Studenti FOREIGN KEY(idStudent) REFERENCES Studenti(idStudent),
    CONSTRAINT fk_Achizitii_Cafele FOREIGN KEY(idCafea) REFERENCES Cafele(idCafea)
);