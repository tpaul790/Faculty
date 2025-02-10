%sa se sorteze un șir cu eliminarea duplicatelor
/*

sortare(l1..ln)		= [], n=0
					= min(l1..ln)(+) sortare(elim(l2..ln,l1)), 										n>0

min(l1..ln) = minAux(l2..ln,l1), n>0

minAux(l1..ln,min)	= min, n=0
					= minAux(l2..ln,min), min<l1
					= minAux(l2..ln,l1), l1<min

elim(l1..ln,e)	= [], n=0
				= elim(l2..ln,e), l1 = e
				= l1(+)elim(l2..ln,e), l1 != e

sort(L,R)

min(L,R),minAux(L,Col,R).

elim(L,E,R).

*/

sortare([],[]) :- !.
sortare([H|T],[M|R]) :-
	min([H|T],M),
	elim([H|T],M,L1),
	sortare(L1,R).

min([H|T],R) :- minAux(T,H,R).

minAux([],M,M) :- !.
minAux([H|T],M,R) :-
	M<H,!,
	minAux(T,M,R).
minAux([H|T],_,R) :-
	minAux(T,H,R).

elim([],_,[]) :- !.
elim([E|T],E,R) :- !,
	elim(T,E,R).
elim([H|T],E,[H|R]) :-
	elim(T,E,R).






	 