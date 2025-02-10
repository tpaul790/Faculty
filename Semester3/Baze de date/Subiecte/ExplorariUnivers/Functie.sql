CREATE FUNCTION planeteExploratori(@roca VARCHAR(100))
RETURNS TABLE
AS
    RETURN SELECT P.nume as NumePlaneta,COUNT(E.idExplorator) AS NrExploratori FROM
    Planete P INNER JOIN Misiuni M ON M.idPlaneta = P.idPlaneta 
    INNER JOIN Exploratori E ON E.idExplorator = M.idExplorator
    WHERE P.roca = @roca
    GROUP BY P.idPlaneta,P.nume
go;

select * from planeteExploratori('piatra');