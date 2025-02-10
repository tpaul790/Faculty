%sa se sorteze o lista cu păstrarea dublurilor
/*
sortare(l1..ln) 	= [], n=0
				= plaseaza(sortare(l2..ln),l1), n>0

plasează(l1..ln,e)	= [e], n=0
				= e(+)l1..ln, e<=l1
				= l1(+)plasează(l2..ln,e), e>l1
*/

sortare([],[]) :- !.
sortare([H|T],R) :-
	sortare(T,R1),
	plaseaza(R1,H,R).


plaseaza([],E,[E]) :- !.
plaseaza([H|T],E,[E|[H|T]]) :-
	E =< H, !.
plaseaza([H|T],E,[H|R]) :-
	plaseaza(T,E,R).
	
