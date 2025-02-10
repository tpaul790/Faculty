CREATE TABLE Sectiuni(
    idSectiune INT IDENTITY(1,1),
    nume VARCHAR(100),
    descriere VARCHAR(100),
    CONSTRAINT PK_Sectiuni PRIMARY KEY(idSectiune)
);

CREATE TABLE Atractii(
    idAtractie INT IDENTITY(1,1),
    nume VARCHAR(100),
    descriere VARCHAR(100),
    varsta_minima INT,
    idSectiune INT,
    CONSTRAINT PK_Atracie PRIMARY KEY(idAtractie),
    CONSTRAINT FK_Atrcatii_Sectiuni FOREIGN KEY(idSectiune) REFERENCES Sectiuni(idSectiune)
);

CREATE TABLE CategoriiVizitatori(
    idCategorie INT IDENTITY(1,1),
    nume VARCHAR(100),
    CONSTRAINT PK_CategoriiVizitatori PRIMARY KEY(idCategorie)
);

CREATE TABLE Vizitatori(
    idVizitator INT IDENTITY(1,1),
    nume VARCHAR(100),
    email VARCHAR(100),
    idCategorie INT,
    CONSTRAINT PK_Vizitatori PRIMARY KEY(idVizitator),
    CONSTRAINT FK_Vizitaori_CategoriiVizitatori FOREIGN KEY (idCategorie) REFERENCES CategoriiVizitatori(idCategorie)
);

CREATE TABLE Note(
    idVizitator INT,
    idAtractie INT,
    nota INT,
    CONSTRAINT PK_Note PRIMARY KEY(idVizitator, idAtractie),
    CONSTRAINT FK_Note_Vizitatori FOREIGN KEY(idVizitator) REFERENCES Vizitatori(idVizitator),
    CONSTRAINT FK_Note_Atractii FOREIGN KEY(idAtractie) REFERENCES Atractii(idAtractie),
    CONSTRAINT CK_Note CHECK (1 <= nota AND nota <= 10)
);

INSERT into Sectiuni VALUES
    ('s1', 'sectiune 1'),
    ('s2', 'sectiune 2'),
    ('s3', 'sectiune 3'),
    ('s4', 'sectiune 4'),
    ('s4', 'sectiune 5'),
    ('s5', 'sectiune 6');

insert into Atractii VALUES
    ('a1','atractie1',10,1),
    ('a2','atractie2',11,2),
    ('a3','atractie3',8,1),
    ('a4','atractie4',12,3),
    ('a5','atractie5',7,1),
    ('a6','atractie6',9,5),
    ('a7','atractie7',10,6),
    ('a8','atractie8',13,1);
insert into Atractii VALUES
    ('a9','atractie9',18,null);

insert INTO CategoriiVizitatori VALUES
    ('pensionari'),
    ('copii'),
    ('adulti');

INSERT into Vizitatori VALUES
    ('Paul','email',3),
    ('Florina','email',3),
    ('Emilia','email',1),
    ('Andrei','email',2),
    ('Mihai','email',1),
    ('Florian','email',1),
    ('Ionut','email',3);
insert into Vizitatori VALUES
    ('Ionela','email',null);

INSERT INTO Note VALUES
    (1,1,9),
    (1,2,8),
    (2,5,10),
    (2,1,5),
    (3,2,8),
    (5,4,10),
    (7,5,8),
    (2,6,9);

---Interogari----
--O interogare care afiseaza toate inregistrarile din tabelul categorii al caror nume este egal cu pensionari sau copii--
select * from CategoriiVizitatori WHERE nume in ('pensionari', 'copii');

--O interogare care afiseaza toate sectiunile al caror nume incepe cu s---
select * from Sectiuni WHERE nume LIKE 's%';

--O interogare care afiseaza toate sectiunile al caror nume se termina cu o cifra si are cel putin 2 caractere --
select * from Sectiuni WHERE nume LIKE '%_[0123456789]';

--O interogare care afiseaza toti vizitatorii care nu au dat niciodata note unei atractii---
select * from Vizitatori 
WHERE idVizitator not in (select idVizitator from Note);

--O interogare care afiseaza numele vizitatorului, nota si numele atractiei evalutate
select V.nume as NumeVizitator, A.nume as NumeAtractie, N.nota
from Vizitatori V INNER JOIN Note N on V.idVizitator = N.idVizitator
INNER JOIN Atractii A on A.idAtractie = N.idAtractie;

--O interogare care afiseaza numele vizitatorului si numarul de note pe care le-a dat (trebuie sa apara toti vizitatorii)--
SELECT V.nume as NumeVizitator, COUNT(N.idVizitator) as NumarNote FROM
Vizitatori V LEFT JOIN Note N on V.idVizitator = N.idVizitator
GROUP by V.idVizitator,V.nume

--O interogare care afiseaza valorile distincte ale notelor date atractiilor--
SELECT DISTINCT nota from Note;

/*
O interogare care afiseaza numele sectiunii, numele si descrierea atractiilor pentru toate sectiunile care au cel putin
o atractie asociata(se vor include si atractile care nu apartin nici unei sectiuni)
*/
select S.nume as numeSectiune, A.nume as numeAtractie, A.descriere FROM
Sectiuni S RIGHT JOIN Atractii A on S.idSectiune = A.idSectiune;

--O interogare care afiseaza numele si varsta minima a atractiilor care au primit cel putin 2 note--
select A.nume, A.varsta_minima from Atractii A 
where A.idAtractie IN (
    select A.idAtractie from 
    Atractii A INNER JOIN Note N on A.idAtractie = N.idAtractie
    GROUP BY A.idAtractie
    HAVING COUNT(N.idAtractie) >= 2
    );

/*
O interogare care afiseaza numele categoriei, numele vizitatorului, nota, numele si descrierea atractiei pentru
toate categoriile care au numele diferit de 'adult' si au vizitatori asociati care au dat cel putin o nota unei atractii
*/
select C.nume as numeCategorie, V.nume as numeVizitator, N.nota, A.nume as numeAtractie, A.descriere FROM
CategoriiVizitatori C INNER JOIN Vizitatori V on C.idCategorie = V.idCategorie 
INNER JOIN Note N on V.idVizitator = N.idVizitator 
INNER JOIN Atractii A on A.idAtractie = N.idAtractie
WHERE C.nume <> 'adulti';

--O interogarea care afiseaza numele unei atractii si nota maxima primita---
select A.nume as numeAtractie, MAX(N.nota) as notaMaxima FROM
Atractii A INNER JOIN Note N on A.idAtractie = N.idAtractie
group by A.idAtractie,A.nume;

--O interogarea care afiseaza numele unei atractii si nota minima primita---
select A.nume as numeAtractie, MIN(N.nota) as notaMaxima FROM
Atractii A INNER JOIN Note N on A.idAtractie = N.idAtractie
group by A.idAtractie,A.nume;

--O interogare care afiseaza numele si adresa de email a vizitatoriilor care nu apartin niciun categorii--
select V.nume, V.email from Vizitatori V
where V.idCategorie is NULL;