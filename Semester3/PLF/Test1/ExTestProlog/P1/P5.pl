%sa se scrie un predicat care pentru o lista creează o lista de perechi 
%(element, frecventa)

/*
creare(l1..ln)	= [], n=0
			= [l1,frecv(l,l1)](+)creare(l2..ln) si elim(l,l1), n>0
*/

creare([],[]) :- !.
creare([H|T],[[H,F]|R]) :-
	frecv([H|T],H,F),
	elim([H|T],H,L),
	creare(L,R).

/*

frecv(l1..ln,e)		=0, n=0
				=1+frecv(l2,,ln, e), e=l1 si n>0
				=frecv(l2..ln,e), e!=l1 si n>0

elim(l1..ln,e)		=[], n=0
				=elim(l2..ln,e), daca e=l1 si n>0
				=l1(+)elim(l2..ln,e), daca e!=l1 si n>0

*/

frecv([],_,0) :- !.
frecv([E|T],E,R) :- !,
	frecv(T,E,R1),
	R is R1+1.
frecv([_|T],E,R) :-
	frecv(T,E,R).

elim([],_,[]) :- !.
elim([E|T],E,R) :- !,
	elim(T,E,R).
elim([H|T],E,[H|R]) :-
	elim(T,E,R).
