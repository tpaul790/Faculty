%succesroul unui număr reprezentat sub forma de lista
/*

suc(l1..ln)	= [] ,n=0
			= invers(sucAux(invers(l1..ln),1), n>0

sucAux(l1..ln,cf)	= [cf], n=0 si cf=1
				= [l1+cf], n=1 si l1+cf<=9
				= (l1+cf)%10(+)sucAux(l2..ln,(l1+cf)/10), n>0

invers(l1..ln) = inversAux(l1..ln,[])

inversAux(l1..ln,col) 	= col, n=0
					= inversAux(l2..ln,l1(+)col), n>0

*/

suc([],[]) :- !.
suc(L,R) :-
	invers(L,L1),
	sucAux(L1,1,R1),
	invers(R1,R).

sucAux([],1,[1]) :- !.
sucAux([H],Cf,[R]) :-
	R is H+Cf,
	R =< 9,!.
sucAux([H|T],Cf,[C|R]) :-
	C is (H+Cf) mod 10,
	Cf1 is (H+Cf) div 10,
	sucAux(T,Cf1,R).

invers(L,R) :- inversAux(L,[],R).

inversAux([],C,C) :- !.
inversAux([H|T],C,R) :-
	inversAux(T,[H|C],R).
