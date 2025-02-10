CREATE TABLE Roci(
    denumire VARCHAR(100),
    duritate VARCHAR(100),
    anAparitie INT,
    CONSTRAINT pk_Roci PRIMARY KEY(denumire)
);

CREATE TABLE Exploratori(
    idExplorator INT IDENTITY(1,1),
    nume VARCHAR(100),
    gen VARCHAR(10),
    dataNasterii DATE,
    CONSTRAINT pk_Exploratori PRIMARY KEY(idExplorator)
);

CREATE TABLE Planete(
    idPlaneta INT IDENTITY(1,1),
    nume VARCHAR(100),
    descriere VARCHAR(100),
    dSoare INT,
    dPamant INT,
    roca VARCHAR(100),
    CONSTRAINT pk_Planete PRIMARY KEY(idPlaneta),
    CONSTRAINT fk_Planete_Roci FOREIGN KEY(roca) REFERENCES Roci(denumire)
);

CREATE TABLE Sateliti(
    denumire VARCHAR(100),
    marime INT,
    specific VARCHAR(100),
    idPlaneta INT,
    CONSTRAINT pk_Sateliti PRIMARY KEY(denumire),
    CONSTRAINT fk_Sateliti_Planete FOREIGN KEY(idPlaneta) REFERENCES Planete(idPlaneta)
);

CREATE TABLE Misiuni(
    idPlaneta INT,
    idExplorator INT,
    dataMisiunii DATE,
    descriere VARCHAR(100),
    CONSTRAINT pk_Misiuni PRIMARY KEY(idPlaneta,idExplorator),
    CONSTRAINT fk_Misiuni_Planete FOREIGN KEY(idPlaneta) REFERENCES Planete(idPlaneta),
    CONSTRAINT fk_Misiuni_Exploratori FOREIGN KEY(idExplorator) REFERENCES Exploratori(idExplorator)
);
