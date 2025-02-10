solutii(L,R) :- findall(R1,back(L,R1),R).

back(L,R) :-
	candidat(L,X),
	lungime(L,N),
	aux(L,N,[X],1,R).

aux(_,N,C,N,C):-!.
aux(L,N,[H|T],M,R) :-
	candidat(L,X),
	M<N, M1 is M+1,
	D is X-H,
	abs(D,D1),
	D1=<3,
	\+ candidat([H|T],X),
	aux(L,N,[X|[H|T]],M1,R).

candidat([H|_],H).
candidat([_|T],R) :- candidat(T,R).

lungime([],0):-!.
lungime([_|T],R):-
	lungime(T,R1),
	R is R1+1.