CREATE OR ALTER PROCEDURE addNota @idUtilizator INT, @idRestaurant INT, @nota FLOAT
AS
BEGIN
    IF(EXISTS(SELECT * FROM Note WHERE idUtilizator = @idUtilizator AND idRestaurant = @idRestaurant))
    BEGIN
        UPDATE Note SET nota = @nota WHERE idUtilizator = @idUtilizator AND idRestaurant = @idRestaurant;
        PRINT 'Nota a fost actualizata cu succes'
    END
    ELSE
    BEGIN
        INSERT INTO Note VALUES(@idUtilizator,@idRestaurant,@nota);
        PRINT 'Nota a fost acordata cu succes restaurantului'
    END
END

EXEC addNota 1,2,8;
EXEC addNota 1,4,9
SELECT * FROM Note;