/*
back(L)	= aux(L,size(L),[X],X), X = candidat(L)

aux(L,n,col,s)	= col, s = n			
			= aux(L,n,X(+)col,s+X),
				X=candidat(L),X<col1,s+X<=n
candidat(l1..ln)
1.l1
2.candidat(l2..ln)

size(l1..ln) = 0, n=0
		 = 1+size(l2..ln), n>0 
*/

back(L,R) :-
	size(L,N),
	candidat(L,E),
	aux(L,N,[E],E,R).

aux(_,N,Col,N,Col) :- !.
aux(L,N,[H|T],S,R) :-
	candidat(L,E),
	E < H,
	S1 is S+E,
	S1 =< N,
	aux(L,N,[E|[H|T]],S1,R).

candidat([H|_],H).
candidat([_|T],R) :-
	candidat(T,R).

size([],0).
size([_|T],R) :-
	size(T,R1),
	R is R1+1.