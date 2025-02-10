CREATE FUNCTION functieStudenti(@n INT)
RETURNS TABLE
AS
RETURN SELECT S.prenume,S.nume FROM Studenti S
       INNER JOIN Achizitii A ON S.idStudent = A.idStudent
       GROUP BY S.idStudent,S.prenume,S.nume
       HAVING COUNT(A.idCafea) <= @n;
go;

select * from Achizitii;
select * from functieStudenti(3);