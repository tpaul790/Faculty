-----------------------------Validari---------------------------------
CREATE FUNCTION existaDepozit(@id INT)
RETURNS BIT
AS
BEGIN
    IF(EXISTS(SELECT * FROM Depozite WHERE idDepozit = @id))
        RETURN 1;
    RETURN 0;
END
go;

CREATE FUNCTION existaAdresa(@id INT)
RETURNS BIT
AS
BEGIN
    IF(EXISTS(SELECT * FROM Adresa WHERE idAdresa = @id))
        RETURN 1;
    RETURN 0;
END
go;

CREATE FUNCTION existaClient(@id INT)
RETURNS BIT
AS
BEGIN
    IF(EXISTS(SELECT * FROM Clienti WHERE idClient = @id))
        RETURN 1;
    RETURN 0;
END
go;

CREATE FUNCTION existaLivrare(@id INT)
RETURNS BIT
AS
BEGIN
    IF(EXISTS(SELECT * FROM Livrari WHERE idLivrare = @id))
        RETURN 1;
    RETURN 0;
END
go;

CREATE FUNCTION existaColet(@id INT)
RETURNS BIT
AS
BEGIN
    IF(EXISTS(SELECT * FROM Colet WHERE idColet = @id))
        RETURN 1;
    RETURN 0;
END

go;
CREATE FUNCTION validareTelefon(@telefon VARCHAR(15))
RETURNS BIT
AS
BEGIN
    IF(LEN(@telefon) = 10)
        RETURN 1;
    RETURN 0;
END

go;
CREATE FUNCTION validareEmail(@email VARCHAR(100))
RETURNS BIT
AS
BEGIN
    IF(@email LIKE '%@gmail.com')
        RETURN 1;
    RETURN 0;
END
go;

CREATE FUNCTION pozitivInt(@nr INT)
RETURNS BIT
AS
BEGIN
    IF(@nr >= 0)
        RETURN 1;
    RETURN 0;
END
go;

CREATE FUNCTION pozitivBigInt(@nr BIGINT)
RETURNS BIT
AS
BEGIN
    IF(@nr >= 0)
        RETURN 1;
    RETURN 0;
END
go;

CREATE FUNCTION pozitivFloat(@nr FLOAT)
RETURNS BIT
AS
BEGIN
    IF(@nr >= 0)
        RETURN 1;
    RETURN 0;
END
go;


