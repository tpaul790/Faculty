%conversie la mulțime in ordine inversa

/*

set(l) = setAux(invers(l),[])

					= col ,n=0
setAux(l1..ln, col)	= setAux(l2..ln,col), n>0 si member(l1,col)
					= setAux(l2..ln,l1(+)col), n>0 si not(member..)

invers(l1..ln) = inversAux(l1..ln,[]).

member(l1..ln,e)	=true, e = l1
				=member(l2..ln,e),altfel

inversAux(l1..ln,col)	=col, n=0
					=inversAux(l2..ln,l1(+)col), altfel

*/

set(L,R) :- invers(L,L1),setAux(L1,[],R).

setAux([],C,C) :- !.
setAux([H|T],C,R) :-
	member(C,H),!,
	setAux(T,C,R).
setAux([H|T],C,R) :-
	setAux(T,[H|C],R).

invers(L,R) :- inversAux(L,[],R).

member([E|_],E) :- !.
member([_|T],E) :- 
	member(T,E).

inversAux([],C,C) :- !.
inversAux([H|T],C,R) :-
	inversAux(T,[H|C],R).
