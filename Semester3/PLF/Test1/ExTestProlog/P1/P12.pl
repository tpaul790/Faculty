%sa se obțină sublista din de la n pana la m
/*

sub(l,n,m) = subAux(l,1,n,m).

					=[], x=0 sau p=m+1
subAux(l1..lx,p,n,m)	=subAux(l2..lx,p+1,n,m), p<n
					=l1(+)subAux(l2..lx,p+1,n,m),p>=n si p<=m
*/

sub(L,N,M,R) :- subAux(L,1,N,M,R).

subAux([],_,_,_,[]) :- !.
subAux(_,P,_,M,[]) :-
	P =:= M+1.
subAux([_|T],P,N,M,R) :-
	P < N,!,
	P1 is P+1,
	subAux(T,P1,N,M,R).
subAux([H|T],P,N,M,[H|R]) :-
	P =< M,
	P1 is P+1,
	subAux(T,P1,N,M,R).