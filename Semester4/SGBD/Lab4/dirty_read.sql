use Laborator3
go;
--dirty reads
create or alter procedure updateStudentName(@id int, @newName varchar(100))
as
begin
	set nocount on;
    begin try
        begin tran

        update Students set name = @newName where id = @id
        waitfor delay '00:00:04'
        rollback tran -- transaction is ended here

        insert into LogTable(info,time) values('Student name update was attempted but rolled back',GETDATE());
        select 'Update transaction was attempted but rolled back' as Message;
    end try
    begin catch
        if (@@TRANCOUNT > 0)
            rollback tran

        insert into LogTable(info,time) values('An error occurred',GETDATE());
        select ERROR_MESSAGE() as Error, 'Transaction Rolled Back' as StatusTransaction
    end catch
end
go;

create or alter procedure dirtyRead
as
begin
	set nocount on;	
    set transaction isolation level read uncommitted
	--solution: 
	--set transaction isolation level read committed
    begin try
        begin tran
		insert into LogTable values('Read data after update',GETDATE());
        select * from Students
        waitfor delay '00:00:08'
        select * from Students
		insert into LogTable values('Read data after rollback',GETDATE());
        commit tran
    end try
    begin catch
        if (@@TRANCOUNT > 0)
            rollback tran

        select ERROR_MESSAGE() as Error, 'Transaction Rolled Back' as StatusTransaction
		insert into LogTable values('An error ocure in dirtyRead transaction',GETDATE());
    end catch
end

exec updateStudentName 16,'Antonio'
