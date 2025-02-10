CREATE TABLE Tipuri(
    idTip INT IDENTITY(1,1),
    nume VARCHAR(100),
    descriere VARCHAR(100),
    CONSTRAINT PK_Tipuri PRIMARY KEY(idTip)
);

CREATE TABLE Orase(
    idOras INT IDENTITY(1,1),
    nume VARCHAR(100),
    CONSTRAINT PK_Oras PRIMARY KEY(idOras)
)

CREATE TABLE Restaurante(
    idRestaurant INT IDENTITY(1,1),
    nume VARCHAR(100),
    adresa VARCHAR(100),
    telefon VARCHAR(15),
    idOras INT,
    idTip INT,
    CONSTRAINT PK_Restaurante PRIMARY KEY(idRestaurant),
    CONSTRAINT FK_Restaurante_Orase FOREIGN KEY(idOras) REFERENCES Orase(idOras),
    CONSTRAINT FK_Restaurante_Tipuri FOREIGN KEY(idTip) REFERENCES Tipuri(idTip)
)

CREATE TABLE Utilizatori(
    idUtilizator INT IDENTITY(1,1),
    nume VARCHAR(100),
    email VARCHAR(100),
    parola VARCHAR(100),
    CONSTRAINT PK_Utilizatori PRIMARY KEY(idUtilizator)
)

CREATE TABLE Note(
    idUtilizator INT,
    idRestaurant INT,
    nota FLOAT,
    CONSTRAINT PK_Note PRIMARY KEY(idUtilizator, idRestaurant),
    CONSTRAINT FK_Note_Utilizatori FOREIGN KEY(idUtilizator) REFERENCES Utilizatori(idUtilizator),
    CONSTRAINT FK_Note_Restaurante FOREIGN KEY(idRestaurant) REFERENCES Restaurante(idRestaurant)
)