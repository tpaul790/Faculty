--Tema lab2: interogari

---1) Numele soferului si nr de inamatriculare al masinii precum si data in care a finalizat livrari
SELECT S.nume, M.nrInmatriculare, L.dataLivrare
FROM Soferi S INNER JOIN Masini M on S.idSofer = M.idSofer
INNER JOIN Livrari L ON S.idSofer = L.idSofer
WHERE statusLivrare = 'Livrat';

--2) Numele soferilor care opereaza in B-N
SELECT S.Nume,L.statusLivrare
FROM Soferi S INNER JOIN Livrari L ON S.idSofer = L.idSofer
INNER JOIN Adresa A on L.idAdresaLivrare = A.idAdresa
WHERE A.judet = 'B-N';

--3) Numarul de soferi din fiecare judet pentru care suma salariilor este mai mare de 16000
SELECT  A.judet, COUNT(A.judet) as SoferiPeJudet, SUM(S.salariu) as TotalBani
FROM Soferi S INNER JOIN Livrari L ON S.idSofer = L.idSofer
INNER JOIN Adresa A on L.idAdresaLivrare = A.idAdresa
GROUP BY A.judet 
HAVING Sum(S.salariu) > 14000;

--4) Clientii care au facut retururi iar acestea au fost finalizate
SELECT DISTINCT(C.idClient), C.nume, C.email
FROM Clienti C INNER JOIN Colet Co ON C.idClient = Co.idClient
INNER JOIN Retur R ON R.idColet = Co.idColet
WHERE R.statusRetur = 'Finalizat';

--5) Masinile care parcurg trasee mai lungi de 100km si soferii lor
SELECT S.nume,M.nrInmatriculare,M.marca,T.lungime
FROM Masini M INNER JOIN Soferi S ON M.idSofer = S.idSofer
INNER JOIN Trasee T ON S.idSofer = T.idSofer
WHERE T.lungime > 100;

--6) Media kilometrajului pentru masinile ale caror soferi sunt profesionisti, grupate dupa combustibil si a caror medie e <100000
SELECT M.combustibil,AVG(M.kilometraj) as MedieKilometraj
FROM Soferi S INNER JOIN Masini M ON M.idSofer = S.idSofer
WHERE S.categoriePermis IN ('C','D','E')
GROUP BY M.combustibil
HAVING AVG(M.kilometraj)<100000;

--7) Clientii si adresele lor / le cunoastem adresele doar clientiilor care au efectuat comenzi
SELECT DISTINCT(C.idClient),C.nume,A.judet,A.localitate
FROM Clienti C LEFT JOIN Colet Co ON C.idClient = Co.idClient
LEFT JOIN Livrari L ON L.idLivrare = Co.idLivrare
LEFT JOIN Adresa A ON A.idAdresa = L.idAdresaLivrare;

--8) Numarul de colete comandate din fiecare judet
SELECT A.judet,COUNT(A.judet) AS ColetePeJudet
FROM Colet Co INNER JOIN Livrari L ON Co.idLivrare = L.idLivrare
INNER JOIN Adresa A ON L.idAdresaLivrare = A.idAdresa
GROUP BY A.judet

--9) Depozitele si greutatile in colete gazduite de ele
SELECT D.idDepozit,D.nume,SUM(C.greutate) as GreutateColete
FROM Colet C INNER JOIN Tranzit T ON C.idColet = T.idColet
INNER JOIN Depozite D ON D.idDepozit = T.idDepozit
GROUP BY D.idDepozit,D.nume;

--10) Clientii si numarul de depozite diferite prin care le-au trecut coletele in timpul tranzitului
SELECT C.nume,C.email,COUNT(DISTINCT(D.idDepozit)) as NumarDepozite
FROM Clienti C INNER JOIN Colet Co ON C.idClient = Co.idClient
INNER JOIN Tranzit T ON T.idColet = Co.idColet
INNER JOIN Depozite D ON D.idDepozit = T.idDepozit
GROUP BY C.nume,C.email;
