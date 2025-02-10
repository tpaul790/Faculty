/*
back(L,n)	= aux(L,n,[x],1), x = candidat(L)

aux(L, n, col1..colm,lg)
	= col1..colm, n = lg
	= aux(L,n,x(+)col1..colm,lg+1), x = candidat(L), x < col1, lg<n

candidat(l1..ln)=
1.l1
2.candidat(l2..ln)
*/

back(L,N,R) :- 
	candidat(L,E),
	aux(L,N,[E],1,R).

aux(_,N,Col,N,Col) :- !.
aux(L,N,[H|T],Lg,R) :-
	candidat(L,E),
	E < H,
	Lg < N,
	Lg1 is Lg+1,
	aux(L,N,[E|[H|T]],Lg1,R).

candidat([H|_],H).
candidat([_|T],R) :-
	candidat(T,R).