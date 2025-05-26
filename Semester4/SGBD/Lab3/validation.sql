create function validateString(@str varchar(100)) 
returns bit 
as
begin
	if(LEN(@str) >= 4)
		return 1;
	return 0;
end;
go;

create function validareEmail(@email VARCHAR(100))
RETURNS BIT
AS
BEGIN
    IF(@email LIKE '%@gmail.com')
        RETURN 1;
    RETURN 0;
END
go;

create function validateInt(@nr int)
returns bit
as
begin
	if(@nr > 0)
		return 1;
	return 0;
end