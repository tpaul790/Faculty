%sa se scrie un predicat care substituie intr o lista un element cu reprezentarea acestuia
%sub forma de lista

/*
nrToList(x) = nrToListAux(x,[]).
				=col, x<10
nrToListAux(x.col)	=nrToList(x/10,x%10(+)col)

*/
nrToList(X,R) :- nrToListAux(X,[],R).

nrToListAux(X,C,[X|C]) :-
	X =< 9,!.
nrToListAux(X,C,R) :-
	Cif is X mod 10,
	X1 is X div 10,
	nrToListAux(X1,[Cif|C],R).

/*
			=[], n=0
subs(l1..ln,e) 	=nrToList(e)(+)subs(l2..ln,e), l1 = e si n>0
			=l1(+)subs(l2..ln,e), l1!=e si n>0

*/

subs([],_,[]) :- !.
subs([E|T],E,[L|R]) :- !,
	nrToList(E,L),
	subs(T,E,R).
subs([H|T],E,[H|R]) :-
	subs(T,E,R).


%sa se elimine elementul de pe poziția a n-a
/*
elimin(l,n) = eliminaAux(l,n,1)

eliminaAux(l1...lx,n,p)		= [], x=0
					= l1(+)subs(l2..lx,n,p), n != p si x>0
					= subs(l2..lx,n,p), n = p si x>0
*/

elimin(L,N,R) :- eliminAux(L,N,0,R).

eliminAux([],_,_,[]) :- !.
eliminAux([_|T],N,N,R) :- !,
	P1 is N+1,
	eliminAux(T,N,P1,R).
eliminAux([H|T],N,P,[H|R]) :-
	P1 is P+1,
	eliminAux(T,N,P1,R).







