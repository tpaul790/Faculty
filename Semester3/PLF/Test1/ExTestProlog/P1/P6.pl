%sa se șteargă toate elementele care apar de mai multe ori

/*
main(l1..ln)		=[], n=0
				=main(elim(l,l1)), frecv(l,l1) > 1
				=l1(+)main(elim(l,l1)), frecv(l,l1) = 1


frecv(l1..ln,e)		=0, n=0
				=1+frecv(l2,,ln, e), e=l1 si n>0
				=frecv(l2..ln,e), e!=l1 si n>0

elim(l1..ln,e)		=[], n=0
				=elim(l2..ln,e), daca e=l1 si n>0
				=l1(+)elim(l2..ln,e), daca e!=l1 si n>0

*/
main([],[]) :- !.
main([H|T],R) :- 
	frecv([H|T],H,F),
	F > 1,!,
	elim([H|T],H,L),
	main(L,R).
main([H|T],[H|R]) :-
	main(T,R).

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

%sa se elimine toate aparițiile elementului maxim dintr o lista de numere
/*
maxim(l) = elim(max(l))

max(l1..ln) = maxAux(l1..ln,l1).

maxAux(l1..ln,m) 	=m, n=0
				=maxAux(l2..ln,l1), l1>m
				=maxAux(l2..ln.m), m>l1
*/
maxim(L,R) :- 
	max(L,M),
	elim(L,M,R).

max([H|T],R) :- maxAux([H|T],H,R).

maxAux([],M,M) :- !.
maxAux([H|T],M,R) :-
	H > M,!,
	M1 = H,
	maxAux(T,M1,R).
maxAux([_|T],M,R) :-
	maxAux(T,M,R).
	


















