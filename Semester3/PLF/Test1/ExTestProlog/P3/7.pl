/*
back(L,n)	= aux(L,n,[x],1), x = candidat(L)

aux(L, n, col1..colm,lg)
	= col1..colm, n = lg
	= aux(L,n,x(+)col1..colm,lg+1), 
			x = candidat(L), x nu apare in col, lg<n

candidat(l1..ln)=
1.l1
2.candidat(l2..ln)

apare(l1..ln,e)	= true, l1 = e
				= apare(l2..ln,e), l1 != e
*/

all(L,N,Rez) :-
	findall(R,back(L,N,R),Rez).

back(L,N,R) :- 
	candidat(L,E),
	aux(L,N,[E],1,R).

aux(_,N,Col,N,Col) :- !.
aux(L,N,Col,Lg,R) :-
	candidat(L,E),
	\+ apare(Col,E),
	Lg < N,
	Lg1 is Lg+1,
	aux(L,N,[E|Col],Lg1,R).

candidat([H|_],H).
candidat([_|T],R) :-
	candidat(T,R).

apare([H|_],H) :- !.
apare([_|T],R) :-
	apare(T,R).