%sa se înlocuiască un element cu elementele unei liste date
/*

înlocuire(l1..ln,e,k1..km) 	
		= [], n=0
		= leaga(înlocuire(l2..ln,e,k1..km), n>0 si l1=e
		= l1(+)înlocuire(l2..ln,e,k1..km), n>0 si l1!=e

leaga(l1..ln,r1..rm)
		=r1..rm, n=0
		=l1(+)leaga(l2..ln,r1..rm),n>0

*/

inlocuire([],_,_,[]) :- !.
inlocuire([E|T],E,L,R1) :- !,
	inlocuire(T,E,L,R),
	leaga(L,R,R1).
inlocuire([H|T],E,L,[H|R]) :-
	inlocuire(T,E,L,R).

leaga([],K,K) :- !.
leaga([H|T],K,[H|R]) :-
	leaga(T,K,R).