%predicat care face produsul dintre un număr reprezentat sub forma de lista 
%si o cifra data
/*

prod(l1..ln,cif) = invers(prodAux(invers(l1..ln),0,cif)

prodAux(l1..ln,cf,cif)	
		= [cf], n=0 si cf>0
		= [l1*cif+cf], n=1 si l1*cif+cf<=9
		= (l1*cif+cf)%10(+)prodAux(l2..ln,l1*cif+cf)/10,cif), n>0

invers(l1..ln) = inversAux(l1..ln,[])

inversAux(l1..ln,col)	= col, n=0
					= inversAux(l2..ln,l1(+)col), n>0

*/

prod(L,C,R) :-
	invers(L,L1),
	prodAux(L1,0,C,R1),
	invers(R1,R).

prodAux([],Cf,_,[Cf]) :- Cf>0.
prodAux([H],Cf,Cif,[R]) :-
	R is H*Cif+Cf,
	R =< 9.
prodAux([H|T],Cf,Cif,[C|R]) :-
	C is (H*Cif+Cf) mod 10,
	Cf1 is (H*Cif+Cf) div 10,
	prodAux(T,Cf1,Cif,R).

invers(L,R) :- inversAux(L,[],R).

inversAux([],C,C). :- !.
inversAux([H|T],C,R) :-
	inversAux(T,[H|C],R).
	