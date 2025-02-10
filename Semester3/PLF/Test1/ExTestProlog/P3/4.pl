/*

back(n,m) = aux(n,m,[x],1), x = candidat(n)

aux(n,m,col1..colz,lg)	
				= col, n = lg
				= aux(n,m,x(+)col1..colz,lg+1), 
    			      x nu apare in col,|col1-x|>=m,lg<m, x = candidat(n)

candidat(n)=
1. n
2. candidat(n-1), n>1

apare(l1..ln,e) 	= true, l1 = e
				= apare(l2..ln, e)

dif(a,b)	= a-b, a>=b
		= b-a, a<b

*/

back(N,M,R) :-
	candidat(N,E),
	aux(N,M,[E],1,R).

aux(N,_,Col,N,Col) :- !.
aux(N,M,[H|T],Lg,R) :-
	candidat(N,E),
	\+ apare([H|T],E),
	dif(H,E,X),
	X >= M,
	Lg1 is Lg+1,
	aux(N,M,[E|[H|T]],Lg1,R).

candidat(N,N).
candidat(N,R) :-
	N > 1,
	N1 is N-1,
	candidat(N1,R).

apare([H|_],H) :- !.
apare([_|T],R) :-
	apare(T,R).

dif(A,B,R) :-
	A >= B,!,
	R is A-B.
dif(A,B,R) :-
	R is B-A.