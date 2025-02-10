/*
Un predicat care determina suma a doua numere reprezentate sub forma de lista

sum(l1,l2) = invers(aux(invers(l1),invers(l2),0))

aux(a1..an, b1..bm, f)	= [], n=0 si m = 0 si f=0
						= [f], n=0 si m =0 si f!=0
						= (a1+f)%10(+)aux(a2..an,[],(a1+f)/10),
								m=0 si n>0
						= (b1+f)%10(+)aux([],b2..bm,(b1+f)/10),
								n=0 si m>0
						= (a1+b1+f)%10(+)aux(a2..an,b2..bn,														(a1+b1+f)/10),
								n>0 si m>0

invers(l1..ln) = invAux(l1..ln,[])

invAux(l1..ln, col)	= col, n=0
					= invAux(l2..ln, l1(+)col), n>0
*/

sum(L1,L2,R) :-
	invers(L1,I1),
	invers(L2,I2),
	aux(I1,I2,0,R1),
	invers(R1,R).

aux([],[],0,[]) :- !.
aux([],[],F,[F]).
aux([H|T],[],F,[Cif|R]) :-
	S is H+F,
	Cif is (S mod 10),
	F1 is (S div 10),
	aux(T,[],F1,R).
aux([],[H|T],F,[Cif|R]) :-
	S is H+F,
	Cif is S mod 10,
	F1 is S div 10,
	aux([],T,F1,R).
aux([H1|T1],[H2|T2],F,[Cif|R]) :-
	S is H1+H2+F,
	Cif is S mod 10,
	F1 is S div 10,
	aux(T1,T2,F1,R).	
	
invers(L,R) :- invAux(L,[],R).

invAux([],C,C).
invAux([H|T],C,R) :-
	invAux(T,[H|C],R).
	
