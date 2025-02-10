%sa se determine predecesorul unui număr reprezentat ca lista
/*

pred(l1..ln) = invers(predAux(invers(l1..ln),-1))

predAux(l1..ln,f)	= [], n=0
 				= [], n=1 si f = -1 si l1=1
				= l1..ln, f = 0
				= 9(+)predAux(l2..ln,-1), n>0 si f=-1 si l1=0
				= l1+f(+)predAux(l2..ln,0), n>0 si l1+f>=0

				
*/

pred(L,R) :- 
	invers(L,L1),
	predAux(L1,-1,R1),
	invers(R1,R).

predAux([],_,[]) :- !.
predAux([1],-1,[]) :- !.
predAux(L,0,L) :- !.
predAux([0|T],-1,[9|R]) :- !,
	predAux(T,-1,R).
predAux([H|T],F,[C|R]) :-
	C is H+F,
	predAux(T,0,R).

invers(L,R) :- inversAux(L,[],R).

inversAux([],C,C) :- !.
inversAux([H|T],C,R) :-
	inversAux(T,[H|C],R).