CREATE OR ALTER FUNCTION functieUtilizator(@email VARCHAR(100))
RETURNS TABLE
AS
    RETURN SELECT T.nume as NumeTip ,R.nume as NumeRestaurant,R.telefon,O.nume as NumeOras,N.nota,U.nume as NumeUtilizator, U.email
    FROM Utilizatori U INNER JOIN Note N ON U.idUtilizator = N.idUtilizator
    INNER JOIN Restaurante R ON R.idRestaurant = N.idRestaurant
    INNER JOIN Orase O ON O.idOras = R.idOras
    INNER JOIN Tipuri T ON T.idTip = R.idTip
    WHERE U.email = @email;
go;

SELECT * FROM functieUtilizator('ptitieni9@gmail.com');
update Utilizatori set email = '' where idUtilizator = 1

create database GestiuneProduse