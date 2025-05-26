--nonrepetable reads
create or alter procedure addTestStudent
as
begin
	set nocount on;
    begin try
        begin tran

		waitfor delay '00:00:04'
        insert into Students (name,email,university,specialization) values('test','test@gmail.com','test','test');
        commit tran -- transaction is ended here

        insert into LogTable(info,time) values('Test student successfully added',GETDATE());
        select 'Add transaction was successfully' as Message;
    end try
    begin catch
        if (@@TRANCOUNT > 0)
            rollback tran

        insert into LogTable(info,time) values('An error occurred',GETDATE());
        select ERROR_MESSAGE() as Error, 'Transaction Rolled Back' as StatusTransaction
    end catch
end
go;

create or alter procedure nonRepetableReads
as
begin
	set nocount on;	
    set transaction isolation level read committed
	--solution: 
	--set transaction isolation level repetable read
    begin try
        begin tran
		insert into LogTable values('Read data before add',GETDATE());
        select * from Students
        waitfor delay '00:00:04'
        select * from Students
		insert into LogTable values('Read data after committed insertion',GETDATE());
        commit tran
    end try
    begin catch
        if (@@TRANCOUNT > 0)
            rollback tran

        select ERROR_MESSAGE() as Error, 'Transaction Rolled Back' as StatusTransaction
		insert into LogTable values('An error ocure in nonRepetableRead transaction',GETDATE());
    end catch
end