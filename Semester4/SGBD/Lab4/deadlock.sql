--deadlock

create or alter procedure transaction1
as
begin
	set nocount on
	begin try
		--set deadlock_priority high;
		begin tran
			insert into LogTable values('Update the first student',GETDATE())
			update Students set name = 'TEST1' where id = 16
			insert into LogTable values('First student successfully updated',GETDATE())

			waitfor delay '00:00:05'

			insert into LogTable values('Update the second student',GETDATE())
			update Students set name = 'TEST1' where id = 26
			insert into LogTable values('Second student successfully updated',GETDATE())
		commit tran
		select 'Transaction1 was successfully' as Message
	end try
	begin catch
		if @@TRANCOUNT > 0
			rollback tran
		insert into LogTable values('An error ocures in transaction1',GETDATE())
		select ERROR_MESSAGE() as Error,'Rolled Back' as StatusTransaction1;
		throw;
	end catch
end
go;


create or alter procedure transaction2
as
begin
	set nocount on
	begin try
		--set deadlock_priority high;
		begin tran
			insert into LogTable values('Update the second student',GETDATE())
			update Students set name = 'TEST2' where id = 26
			insert into LogTable values('Second student successfully updated',GETDATE())

			waitfor delay '00:00:05'

			insert into LogTable values('Update the first student',GETDATE())
			update Students set name = 'TEST2' where id = 16
			insert into LogTable values('First student successfully updated',GETDATE())
		commit tran
		select 'Transaction2 was successfully' as Message
	end try
	begin catch
		if @@TRANCOUNT > 0
			rollback tran
		insert into LogTable values('An error ocures in transaction2',GETDATE())
		select ERROR_MESSAGE() as Error,'Rolled Back' as StatusTransaction1;
		throw;
	end catch
end

select * from Students

exec transaction1