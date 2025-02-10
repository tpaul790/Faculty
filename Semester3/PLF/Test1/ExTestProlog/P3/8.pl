/*

back() = aux(4,[x],1), x = candidat([1,X])

aux(n, col, lg)	= col, lg = n
				= aux(n, e(+)col,lg+1),
					cntX(e(+)col) <= 2, lg<n e = candidat([1,X,2])

candiadat(l1..ln)
1. l1
2. candidat(l2..ln)

cntX(l1..ln)  = 0, n=0
			= 1+cntX(l2..ln), l1 = 'X' si n>0
			= cntX(l2..ln), l1 != 'X' si n>0
*/

back(R) :-
	candidat([1,'X'],E),
	aux(4,[E],1,R).

aux(N,Col,N,Col) :- !.
aux(N,Col,Lg,R) :-
	candidat([1,'X',2],E),
	cntX([E|Col],NrX),
	NrX =< 2,
	Lg1 is Lg+1,
	Lg < N,
	aux(N,[E|Col],Lg1,R).

candidat([H|_],H).
candidat([_|T],R) :-
	candidat(T,R).

cntX([],0) :- !.
cntX(['X'|T],R) :- !,
	cntX(T,R1),
	R is R1+1.
cntX([_|T],R) :-
	cntX(T,R).