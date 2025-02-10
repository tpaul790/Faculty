%sa se adune doua numere reprezentate sub forma de lista
/*
Modele matematice

sum(l1..ln,k1..km) = invers(sumAux(invers(l1..ln),invers(k1..km),0)

						=[c], n=0 si m=0 si c=1
sumAux(a1..an,b1..bn,c)		=[a1+b1+c], n=1 si m=1 si a1+b1+c<=9
						=a1+c..an , m=0 si n>0 si a1+c<=9
						=(a1+c)%10(+)sumAux(a2..an,[],(a1+c)/10),
								n>0 si m=0 si a1+c>9
				 		=b1+c..bm, n=0 si m>0 si a1+c<=9
						=(b1+c)%10(+)sumAux([],b2..bn,(b1+c)/10),
								n=0 si m>0 si b1+c>9
						=(a1+b1+c)%10(+)sumAux(a2..an,b2..bn,											(a1+b1)/10),m>0 si n>0



invers(l1..ln) = inversAux(l1..ln,[]).

inversAux(l1..ln,col)	= col, n=0
					= inversAux(l2..ln,l1(+)col), n>0

Specificatii

sum(A,B,R) (i,i,o)
A- primul număr reprezentat sub forma de lista
B- cel de-al doilea număr reprezentat sub forma de lista
R- numărul reprezentat sub forma de lista care reprezintă suma lui A si B

sumAux(A,B,Cf,R) (i,i,i,o)
A- primul număr reprezentat sub forma de lista
B- cel de-al doilea număr reprezentat sub forma de lista
Cf- cifra de transport găsită in urma adunării a doua cifre din A si B
R- numărul reprezentat sub forma de lista care reprezintă suma lui A si B

invers(L,R) (i,o)
L - lista pe care vreau sa o inversez
R - lista L inversată

inversAux(L,Col,R) (i,i,o)
L - lista pe care vreau sa o inversez
Col - variabila in care colectez soluția
R - lista L inversată
*/

sum(L,K,R) :-
	invers(L,L1),
	invers(K,K1),
	sumAux(L1,K1,0,R1),
	invers(R1,R).


sumAux([],[],1,[1]) :- !.

sumAux([H1],[H2],Cf,[R]) :-
	R is (H1+H2+Cf),
	R =< 9,!.

sumAux([H|T],[],Cf,[H1|T]) :-
	H1 is H+Cf,
	H1 =< 9,!.

sumAux([H|T],[],Cf,[Cif|R]) :-
	Cif1 is (H+Cf),
	Cif1 > 9,!,
	Cif is Cif1 mod 10,
	Cf1 is (H+Cf) div 10,
	sumAux(T,[],Cf1,R).

sumAux([],[H|T],Cf,[H1|T]) :-
	H1 is H+Cf,
	H1 =< 9,!.

sumAux([],[H|T],Cf,[Cif|R]) :-
	Cif1 is (H+Cf),
	Cif1 > 9,!,
	Cif is Cif1 mod 10,
	Cf1 is (H+Cf) div 10,
	sumAux([],T,Cf1,R).

sumAux([A|Ta],[B|Tb],Cf,[Cif|R]) :-
	Cif is (A+B+Cf) mod 10,
	Cf1 is (A+B+Cf) div 10,
	sumAux(Ta,Tb,Cf1,R).



invers(L,R) :- inversAux(L,[],R).

inversAux([],C,C) :- !.
inversAux([H|T],C,R) :-
	inversAux(T,[H|C],R).



find([E],E) :- !.
find([H|T],M) :-
	find(T,M),
	M =< H, !.
find([H|_],H).

