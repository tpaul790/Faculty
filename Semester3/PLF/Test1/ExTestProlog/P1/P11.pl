%sa se verifice daca o lista are aspect de vale

/*
vale(l1..ln) = false, n=1 sau n=0
			 = valeAux(l1..ln,0), l1>l2

					=valeAux(l2..ln,0), l1>l2 si f=0
valeAux(l1..ln,f)	=valeAux(l2..ln,1), l1<l2 si f=0
					=valeAux(l2..ln,1), l1<l2 si f=1

*/

vale([A,B|T]) :- A>B, valeAux([B|T],0).

valeAux([_],1) :- !.
valeAux([A,B|T],0) :-
	A>B,
	valeAux([B|T],0).
valeAux([A,B|T],0) :-
	A<B,
	valeAux([B|T],1).
valeAux([A,B|T],1) :-
	A<B,
	valeAux([B|T],1).




%suma alternantă a elementelor unei liste
/*
sum(l) = sumAux(l,-1)

sumAux(l1..ln,s) = l1+sumAux(l2..ln,-1), s=1 si n>0
				= l1-sumAux(l2..ln,1), s=-1 si n>0
*/

sum(L,R) :- sumAux(L,1,0,R).

sumAux([],_,S,S) :- !.
sumAux([H|T],1,S,R) :-
	S1 is S+H,
	sumAux(T,-1,S1,R).
sumAux([H|T],-1,S,R) :-
	S1 is S-H,
	sumAux(T,1,S1,R).












