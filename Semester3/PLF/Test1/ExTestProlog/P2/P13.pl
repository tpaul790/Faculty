%sa se insereze după fiecare element dintr o lista, divizorii săi

/*

inserare(l1..ln)	= [], n=0
				= leaga(l1(+)div(l1),inserare(l2..ln)), n>0

leaga(l1..ln,k1..km) = k1..km, n=0
				= l1(+)leaga(l2..ln,k1..kn), n>0

div(n) = divAux(n,2)

divAux(n,d)	= [], d=n-1 sau n=0
			= d(+)divAux(n,d+1), n%d=0
			= divAux(n,d+1), n%d !=0 

*/

inserare([],[]) :- !.
inserare([H|T],R) :-
	inserare(T,R1),
	div(H,D),
	leaga([H|D],R1,R). 

leaga([],K,K) :- !.
leaga([H|T],K,[H|R]) :-
	leaga(T,K,R).


div(N,R) :- divAux(N,2,R).

divAux(0,_,[]) :- !.
divAux(N,D,[]) :- D =:= N-1,!.
divAux(N,D,[D|R]) :-
	N mod D =:= 0,!,
	D1 is D+1,
	divAux(N,D1,R).
divAux(N,D,R) :-
	D1 is D+1,
	divAux(N,D1,R).