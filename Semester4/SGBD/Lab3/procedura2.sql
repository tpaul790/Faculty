use Laborator3
go;

CREATE OR ALTER PROCEDURE AddStudentCourseGrade2
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
	DECLARE @idS INT;
	DECLARE @idC INT;
	--TRANZACTIA 1
	
	BEGIN TRY
			BEGIN TRAN
				IF (dbo.validateString(@studentName) = 0)
					THROW 50000, 'Invalid student name', 1;
				IF (dbo.validateString(@university) = 0)
					THROW 50001, 'Invalid university', 1;
				IF (dbo.validareEmail(@email) = 0)
					THROW 50002, 'Invalid email', 1;
				IF (dbo.validateString(@specialization) = 0)
					THROW 50003, 'Invalid specialization', 1;
		
				INSERT INTO LogTable(info, time) VALUES ('Student successfully validated', GETDATE());

				INSERT INTO Students(name, email, university, specialization) 
				VALUES (@studentName, @email, @university, @specialization);

				SET @idS = SCOPE_IDENTITY();

			COMMIT TRAN
				INSERT INTO LogTable(info, time) VALUES ('Student successfully inserted', GETDATE());
				SELECT 'Student successfully inserted' as Tranzaction1;
		END TRY

		BEGIN CATCH
			IF @@TRANCOUNT > 0
				ROLLBACK TRAN;

			BEGIN TRY
				INSERT INTO LogTable(info, time) VALUES ('An error occurs when saving the student', GETDATE());
				INSERT INTO LogTable(info, time) VALUES ('First transaction rolled back', GETDATE());
			END TRY
			BEGIN CATCH
				-- Dacă logarea eșuează, ignorăm 
			END CATCH;

			SELECT 
				ERROR_MESSAGE() AS ErrorMessage,
				'First Transaction Rolled back' AS TransactionStatus;
		END CATCH

	--TRANSACTIA 2
	BEGIN TRAN
		BEGIN TRY
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

			SET @idC = SCOPE_IDENTITY();

			INSERT INTO LogTable(info, time) VALUES ('Course successfully inserted', GETDATE());

			COMMIT TRAN
			INSERT INTO LogTable(info, time) VALUES ('Course successfully inserted', GETDATE());
			SELECT 'Course successfully inserted' as Tranzaction2;
		END TRY

		BEGIN CATCH
			IF @@TRANCOUNT > 0
			ROLLBACK TRAN;

			-- Logăm eroarea într-o tranzacție nouă, sigură
			BEGIN TRY
				INSERT INTO LogTable(info, time) VALUES ('An error occurs when saving the course', GETDATE());
				INSERT INTO LogTable(info, time) VALUES ('Second transaction rolled back', GETDATE());
			END TRY
			BEGIN CATCH
				-- Dacă chiar și logarea eșuează, ignorăm 
			END CATCH;

			SELECT 
				ERROR_MESSAGE() AS ErrorMessage,
				'Second Transaction Rolled back' AS TransactionStatus;
		END CATCH

		--daca sau inserat studentul si cursul atunci pot insera nota
		IF @idS IS NOT NULL AND @idC IS NOT NULL
		BEGIN
			BEGIN TRY
				BEGIN TRAN;

				INSERT INTO Grades(idS, idC, grade) 
				VALUES (@idS, @idC, @grade);

				COMMIT TRAN;

				INSERT INTO LogTable(info, time) VALUES ('Grade successfully inserted', GETDATE());
				SELECT 'Grade successfully inserted' as Tranzaction3;
			END TRY
			BEGIN CATCH
				IF @@TRANCOUNT > 0
					ROLLBACK TRAN;

				BEGIN TRY
					INSERT INTO LogTable(info, time) VALUES ('An error occurs when saving the grade', GETDATE());
					INSERT INTO LogTable(info, time) VALUES ('Third transaction rolled back', GETDATE());
				END TRY
				BEGIN CATCH
					-- Ignorăm eventualele erori de log
				END CATCH;

				SELECT 
					ERROR_MESSAGE() AS ErrorMessage,
					'Third Transaction Rolled back' AS TransactionStatus;
			END CATCH
		END
		ELSE
		BEGIN
			INSERT INTO LogTable(info, time) VALUES ('Cannot insert grade because student or course insert failed', GETDATE());
			SELECT 'Cannot insert grade: Missing student or course' as ErrorMessage;
		END
END


--valid
exec AddStudentCourseGrade2 'Paul','ptitieni9@gmail.com','UBB-FMI','Informatica','SGBD','Sisteme de gestiune a bazelor de date',6,120,10;
select * from LogTable;

--invalid student
exec AddStudentCourseGrade2 'Paul','ptitieni9gmail.com','UBB-FMI','Informatica','SGBD','Sisteme de gestiune a bazelor de date',6,120,10;
select * from LogTable;

--invalid course
exec AddStudentCourseGrade2 'Paul','ptitieni9@gmail.com','UBB-FMI','Informatica','SGBD','Sisteme de gestiune a bazelor de date',-6,120,10;
select * from LogTable;

--invalid student & course
exec AddStudentCourseGrade2 'Paul','ptitieni9gmail.com','UBB-FMI','Informatica','SGBD','Sisteme de gestiune a bazelor de date',-6,120,10;
select * from LogTable;


select * from Students
select * from Courses
select * from Grades

delete from Students where id > 26;
delete from Courses
delete from Grades
delete from LogTable;