%sa se dubleze numerele prime dintr o lista
/*

dublare(l1..ln)	
			= [], n=0
			= l1(+)dublare(l2..ln), !prim(l1) si n>0
			= l1(+)l1(+)dublare(l2..ln), prim(l1) si n>0

nrdiv(n)	= nrdivAux(n,n)

nrdivAux(n,d)	= 0, d=0 sau n=0
			= 1+nrdiv(n,d-1), n%d = 0
			= nrdiv(n,d-1), n%d != 0

prim(n)	= true, nrDiv(n) = 2

*/


dublare([],[]) :- !.
dublare([H|T],[H|R]) :-
	not(prim(H)),!,
	dublare(T,R).
dublare([H|T],[H|[H|R]]) :-
	dublare(T,R).

nrdiv(N,R) :- nrdivAux(N,N,R).

nrdivAux(0,_,0) :- !.
nrdivAux(_,0,0) :- !.
nrdivAux(N,D,R) :-
	N mod D =:= 0,!,
	D1 is D-1,
	nrdivAux(N,D1,R1),
	R is R1+1.
nrdivAux(N,D,R) :-
	D1 is D-1,
	nrdivAux(N,D1,R).

prim(N) :- nrdiv(N,R), R=2.