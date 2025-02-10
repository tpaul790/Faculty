CREATE PROCEDURE goToVersion @version INT AS
BEGIN
    IF @version > 5 OR @version < 0
        BEGIN
            PRINT 'Nu putem ajunge la aceasta versiune!'
        END
    ELSE
        BEGIN
            DECLARE @curent INT;
            SELECT @curent = versiune FROM Versiune;

            IF @version = @curent
                BEGIN
                    PRINT 'Suntem deja la aceasta versiune!'
                    GOTO out;
                END

            DECLARE @cnt INT = 1;
            IF @curent > @version
                BEGIN
                    SET @cnt = -1;
                    SET @curent += 1;
                END

            WHILE @curent <> @version
                BEGIN
                    SET @curent += @cnt;
                    IF @curent = @version AND @cnt = -1
                    BEGIN
                        CONTINUE
                    END
                    EXEC doProc @curent,@cnt;
                END
    
            UPDATE Versiune SET versiune = @version;
            PRINT 'Versiune modificata cu succes'
        END
    out:
END
GO



SELECT * from Versiune;
EXEC goToVersion 0;



GO
CREATE PROCEDURE doProc @numar INT, @cnt INT AS
BEGIN
    IF @numar > 5 OR @numar < 0
        BEGIN
            PRINT 'Nu exista aceasta versiune!'
        END
    IF @cnt <> 1 AND @cnt <> -1
        BEGIN
            PRINT 'Contorul este invalid!'
        END

    IF @numar = 1
    BEGIN
        IF @cnt = 1
        BEGIN
            EXEC coletChangeGreutateType;
        END
        ELSE
        BEGIN
            EXEC coletChangeGreutateTypeBack;
        END
    END

    IF @numar = 2
    BEGIN
        IF @cnt = 1
        BEGIN
            EXEC clientSetDefaultValueForTip;
        END
        ELSE
        BEGIN
            EXEC clientDropDefaultValueForTip;
        END
    END

    IF @numar = 3
    BEGIN
        IF @cnt = 1
        BEGIN
            EXEC createTableFacturi;
        END
        ELSE
        BEGIN
            EXEC dropTableFacturi;
        END
    END
    IF @numar = 4
    BEGIN
        IF @cnt = 1
        BEGIN
            EXEC addColumnTotalPlataFacturi;
        END
        ELSE
        BEGIN
            EXEC dropColumnTotalPlataFacturi;
        END
    END
    IF @numar = 5
    BEGIN
        IF @cnt = 1
        BEGIN
            EXEC addConstraintColetFacturi;
        END
        ELSE
        BEGIN
            EXEC dropConstraintColetFacturi;
        END
    END
END

EXEC goToVersion 2;