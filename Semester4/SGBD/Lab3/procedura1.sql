use Laborator3
go;

CREATE OR ALTER PROCEDURE AddStudentCourseGrade1 
	@studentName VARCHAR(100), 
    @email VARCHAR(100), 
    @university VARCHAR(100), 
    @specialization VARCHAR(100),
	@courseName VARCHAR(100), 
    @description VARCHAR(100), 
    @credits INT, 
    @hours INT,
	@grade INT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRAN;

		--validare student
		IF (dbo.validateString(@studentName) = 0)
			THROW 50000, 'Invalid student name', 1;
		IF (dbo.validateString(@university) = 0)
			THROW 50001, 'Invalid university', 1;
		IF (dbo.validareEmail(@email) = 0)
			THROW 50002, 'Invalid email', 1;
		IF (dbo.validateString(@specialization) = 0)
			THROW 50003, 'Invalid specialization', 1;
		
		--inserare student
		INSERT INTO LogTable(info, time) VALUES ('Student successfully validated', GETDATE());

		INSERT INTO Students(name, email, university, specialization) 
		VALUES (@studentName, @email, @university, @specialization);

		DECLARE @idS INT = SCOPE_IDENTITY();

		INSERT INTO LogTable(info, time) VALUES ('Student successfully inserted', GETDATE());

		--validare curs
		IF (dbo.validateString(@courseName) = 0)
			THROW 50004, 'Invalid course name', 1;
		IF (dbo.validateString(@description) = 0)
			THROW 50005, 'Invalid description', 1;
		IF (dbo.validateInt(@credits) = 0)
			THROW 50006, 'Invalid number of credits', 1;
		IF (dbo.validateInt(@hours) = 0)
			THROW 50007, 'Invalid number of course hours', 1;

		--inserare curs
		INSERT INTO LogTable(info, time) VALUES ('Course successfully validated', GETDATE());

		INSERT INTO Courses(name, description, credits, hours) 
		VALUES (@courseName, @description, @credits, @hours);

		DECLARE @idC INT = SCOPE_IDENTITY();

		INSERT INTO LogTable(info, time) VALUES ('Course successfully inserted', GETDATE());
		
		--inserare nota
		INSERT INTO Grades(idS, idC, grade) 
		VALUES (@idS, @idC, @grade);

		INSERT INTO LogTable(info, time) VALUES ('Grade successfully inserted', GETDATE());

		--commit tranzactie
        COMMIT TRAN;
		SELECT 'Transaction successfully committed' as SuccesMessage
        INSERT INTO LogTable(info, time) VALUES ('Transaction successfully committed', GETDATE());
    END TRY
    BEGIN CATCH
		--rollback daca apare eroare
        IF @@TRANCOUNT > 0
            ROLLBACK TRAN;

        BEGIN TRY
            INSERT INTO LogTable(info, time) VALUES ('An error occurs', GETDATE());
            INSERT INTO LogTable(info, time) VALUES ('Transaction rolled back', GETDATE());
        END TRY
        BEGIN CATCH
            -- Dacă chiar și logarea eșuează, ignorăm 
        END CATCH;

        SELECT 
            ERROR_MESSAGE() AS ErrorMessage,
            'Rolled back' AS TransactionStatus;
    END CATCH
END

--valid
exec AddStudentCourseGrade1 'Paul','ptitieni9@gmail.com','UBB-FMI','Informatica','SGBD','Sisteme de gestiune a bazelor de date',6,120,10;
select * from LogTable;

--invalid email
exec AddStudentCourseGrade1 'Paul','ptitieni9gmail.com','UBB-FMI','Informatica','SGBD','Sisteme de gestiune a bazelor de date',6,120,10;
select * from LogTable;

select * from Students
select * from Courses
select * from Grades

delete from Students where id > 26
delete from Courses
delete from Grades
delete from LogTable;