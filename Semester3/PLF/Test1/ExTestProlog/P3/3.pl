Q/*
[2,4,1,3,5]

back(l1..ln,R)	= aux(l1..ln, [x], 1), x = candidat(l1..ln)

aux(l1..ln,col1..colm,lg)	
				= col, lg>=2
				= aux(l1...ln, e(+)col1..colm,lg+1), e = candidat(l1..ln),
							e<col1
*/
back(L,R) :-
	candidat(L,E),
	aux(L,[E],1,R).

candidat([H|_],H).
candidat([_|T],R) :-	
	candidat(T,R).

aux(_,Col,Lg,Col) :-
	Lg >= 2.
aux(L,[H|T],Lg,R) :-
	candidat(L,E),
	E < H,
	Lg1 is Lg+1,
	aux(L,[E|[H|T]],Lg1,R).