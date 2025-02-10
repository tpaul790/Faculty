CREATE VIEW viewSateliti AS
    SELECT P.nume as NumePlaneta, S.denumire as DenumireSatelit FROM
    Planete P INNER JOIN Sateliti S ON P.idPlaneta = S.idPlaneta;
go;

INSERT INTO Roci VALUES
    ('piatra',12,1980),('vulcanica',12,1980),('gazoasa',12,1980),('lipicioaas',12,1980);

INSERT INTO Planete VALUES
    ('Pamant','desc',1,2,'piatra'),('Marte','desc',1,2,'piatra'),('Jupiter','desc',1,2,'piatra');


INSERT INTO Sateliti VALUES
    ('voyager1',12,'studiu',2),('voyager2',12,'studiu',2),('voyager3',12,'studiu',3),('voyager4',12,'studiu',2),
    ('voyager5',12,'studiu',4),('voyager6',12,'studiu',3),('voyager7',12,'studiu',3),('voyager8',12,'studiu',4);

select * from viewSateliti order by NumePlaneta;