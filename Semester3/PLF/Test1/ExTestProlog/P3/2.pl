/*
back(n)	= aux(n,[X],X), X = candiat(prime(n))

aux(n, col1..colm, s)	
			= col, s = n
			= aux(n,X(+)col1..colm,s+X), 
					X = candidat(prime(n)),s+X<=n,X<col1
candidat(l1..ln) =
1. l1
2. candidat(l2..ln)

prime(n) = paux(n,2)

paux(n,k)	= [], k > n
			= k(+)paux(n,k+1), prim(k)
			= paux(n, k+1), altfel
prim(n) 	= true, nr(n) = 2
		= false, altfel

nr(n) = nraux(n, 1)

nraux(n, k) 	= 1, k=n
			= 1+nraux(n,k+1), n%k=0
			= nraux(n,k+1), altfel
*/

back(N,R) :-
	prime(N,L),
	candidat(L,E),
	aux(N,L,[E],E,R).

aux(N,_,Col,N,Col) :- !.
aux(N,L,[H|T],S,R) :-
	candidat(L,E),
	S+E =< N,
	E < H,
	S1 is S+E,
	aux(N,L,[E|[H|T]],S1,R).

candidat([H|_],H).
candidat([_|T],R) :-
	candidat(T,R).

prime(N,L) :- paux(N,2,L).

paux(N,K,[]) :- K > N,!.
paux(N,K,[K|L]) :-
	prim(K),!,
	K1 is K+1,
	paux(N,K1,L).
paux(N,K,L) :-
	K1 is K+1,
	paux(N,K1,L).

prim(N) :- 
	nr(N,R),
	R == 2.

nr(N,R) :- nraux(N,1,R).

nraux(N,N,1) :- !.
nraux(N,K,R) :-
	K1 is K+1,
	N mod K =:= 0,!,
	nraux(N,K1,R1),
	R is R1+1.
nraux(N,K,R) :-
	K1 is K+1,
	nraux(N,K1,R).
	
	







