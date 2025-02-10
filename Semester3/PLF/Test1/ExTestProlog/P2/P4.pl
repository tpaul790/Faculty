/*
int(a1..an,b1..bm)	= elimDubs(a1..an), m=0
				= elimDubs(b1..bm), n=0
				= a1(+)int(deplasare(a2..an,a1),deplasare(b2..bm,b1)), 										n>0 si m>0 si a1 = b1
				= a1(+)int(deplasasre(a2..an,a1), b1..bm), n>0 si m>0 si a1<b1
				= b1(+)int(a1..an,deplasare(b2..bm,b1)), n>0 si m>0 si b1<a1

elimDubs(l1..ln)		= [], n=0
				= elimDubs(l2..ln), n>0 si apare(l2..ln,l1)
				= l1(+)elimDubs(l2..ln), n>0 si not(apare(l2..ln,l1))

apare(l1..ln, e)		= false, n=0
				= true, n>0 si l1 = e
				= apare(l2..ln,e).

deplasare(l1..ln,e)	= [], n=0
				= l1..ln, n>0 si l1 != e
				= deplasare(l2..ln,e), n>0 si l1 = e
				
*/
int([],[],[]) :- !.
int([],B,R) :-
	elimDubs(B,R).
int(A,[],R):-
	elimDubs(A,R).
int([H1|T1],[H2|T2],[H1|R]) :-
	H1 == H2,
	deplasare(T1,H1,A),
	deplasare(T2,H2,B),
	int(A,B,R).
int([H1|T1],[H2|T2],[H1|R]) :-
	H1 < H2,
	deplasare(T1,H1,A),
	int(A,[H2|T2],R).
int([H1|T1],[H2|T2],[H2|R]) :-
	H2 < H1,
	deplasare(T2,H2,B),
	int([H1|T1],B,R).

elimDubs([],[]).
elimDubs([H|T],R) :-
	apare(T,H),!,
	elimDubs(T,R).
elimDubs([H|T],[H|R]) :-
	elimDubs(T,R).

apare([E|_],E) :- !.
apare([_|T],E) :-
	apare(T,E).

deplasare([],_,[]).
deplasare([E|T],E,R) :- !,
	deplasare(T,E,R).
deplasare([H|T],_,[H|T]).