create table Students(
	id int identity(1,1),
	name varchar(100),
	email varchar(100),
	university varchar(100),
	specialization varchar(100),
	constraint pk_Students primary key(id)
);

create table Courses(
	id int identity(1,1),
	name varchar(100),
	description varchar(100),
	credits int,
	hours int,
	constraint pk_Courses primary key(id)
	);

create table Grades(
	idS int,
	idC int,
	grade int,
	constraint pk_Grades primary key(idS,idC),
	constraint fk_Sudents_Grades foreign key(idS) references Students(id) on delete cascade,
	constraint fk_Courses_Grades foreign key(idC) references Courses(id) on delete cascade
);

create table LogTable(
	id int identity(1,1),
	info varchar(100),
	time datetime,
	constraint pk_Log primary key(id)
);

