use Laborator3
go;
--nonrepetable reads
create or alter procedure updateStudentName2(@id int, @newName varchar(100))
as
begin
	set nocount on;
    begin try
        begin tran

		waitfor delay '00:00:04'
        update Students set name = @newName where id = @id
        commit tran -- transaction is ended here

        select 'Update transaction was successfully' as Message;
    end try
    begin catch
        if (@@TRANCOUNT > 0)
            rollback tran

        insert into LogTable(info,time) values('An error occurred',GETDATE());
        select ERROR_MESSAGE() as Error, 'Transaction Rolled Back' as StatusTransaction
    end catch
end
go;

create or alter procedure nonRepeatableReads
as
begin
	set nocount on;	
    set transaction isolation level read committed
	--solution: 
	--set transaction isolation level repeatable read
    begin try
        begin tran
		insert into LogTable values('Read data before update',GETDATE());
        select * from Students
        waitfor delay '00:00:04'
        select * from Students
		insert into LogTable values('Read data after committed update',GETDATE());
        commit tran
    end try
    begin catch
        if (@@TRANCOUNT > 0)
            rollback tran

        select ERROR_MESSAGE() as Error, 'Transaction Rolled Back' as StatusTransaction
		insert into LogTable values('An error ocure in nonRepeatableRead transaction',GETDATE());
    end catch
end

exec updateStudentName2 16,'Antonio'