%sa se determine o lista cu pozițiile elementului maxim dintr o lista
/*

poz(l1..ln)	= [], n=0
			= pozAux(l1..ln,1,max(l1..ln)),n>0

pozAux(l1..ln,p,max)	= [], n=0
					= p(+) pozAux(l2..ln,p+1,max), l1 = max si n>0
					= pozAux(l2..ln,p+1,max), l1 != max si n>0

max(l1..ln)	= maxAux(l2..ln,l1)

maxAux(l1..ln,m)	= m, n=0
				= maxAux(l2..ln,l1), l1>=m si n>0
				= maxAux(l2..ln,m), l1<m si n>0

poz(L,R)			(i,o)
pozAux(L,P,M,R)	(i,i,i,o)
max(L,R)			(i,o)
maxAux(L,Col,R)	(i,i,o)

*/

poz([],[]) :- !.
poz(L,R) :- max(L,M), pozAux(L,1,M,R).

pozAux([],_,_,[]) :- !.
pozAux([H|T],P,M,[P|R]) :- 
	H = M,!,
	P1 is P+1,
	pozAux(T,P1,M,R).
pozAux([_|T],P,M,R) :- 
	P1 is P+1,
	pozAux(T,P1,M,R).

max([H|T],R) :- maxAux(T,H,R).

maxAux([],M,M) :- !.
maxAux([H|T],M,R) :-
	H >= M,!,
	maxAux(T,H,R).
maxAux([_|T],M,R) :-
	maxAux(T,M,R).



