%sa se sorteze o lista cu eliminarea dublurilor
/*
sortare(l1..ln) 	= [], n=0
			= plaseaza(sortare(l2..ln),l1), n>0

plasează(l1..ln,e)	= [e], n=0
				= e(+)l1..ln, e<l1
				= l1..ln, e=l1
				= l1(+)plasează(l2..ln,e), e>l1
*/

sortare2([],[]) :- !.
sortare2([H|T],R) :-
	sortare2(T,R1),
	plaseaza2(R1,H,R).


plaseaza2([],E,[E]) :- !.
plaseaza2([H|T],E,[E|[H|T]]) :-
	E < H,!.
plaseaza2([E|T],E,[E|T]) :- !.
plaseaza2([H|T],E,[H|R]) :-
	plaseaza2(T,E,R).
	

%Metoda 2, mai neeficienta dar mai ușoară de înțeles

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

sortare3([],[]) :- !.
sortare3([H|T],[M|R]) :-
	min([H|T],M),
	elim([H|T],M,L1),
	sortare3(L1,R).

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






	 