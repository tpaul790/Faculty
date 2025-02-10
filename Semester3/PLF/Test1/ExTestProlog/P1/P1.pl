%a)Sa se scrie un predicat care întoarce diferența a doua mulțimi
/*
Model matematic	(i,i,o)
					=[], n=0
dif(l1..ln,k1..km)		=l1(+)dif(l2..ln,k), l1 nu apartine lui k
					=dif(l2..ln,k), l1 aparține lui k
dif(L:lista,K:lista,R:lista)
L - reprezintă prima lista din scăderea mulțimilor
K - reprezintă cea de-a doua lista din diferența mulțimilor
R - reprezintă lista rezultat si anume L\K
*/

dif([],_,[]):-!.
dif([H|T],K,R):-
	membru(H,K),
	!,
	dif(T,K,R).
dif([H|T],K,[H|R]):-
	dif(T,K,R).



/*
Model matematic. (i,i)
				=true, e=l1
membru(e,l1..ln)	=membru(e,l2..ln), altfel

membru(E:intreg,L:lista)
E - reprezintă elementul pe care îl cautăm in lista L
L - reprezintă lista in care îl căutăm pe E
*/

membru(E,[E|_]):-!.
membru(E,[_|T]):-
	membru(E,T).





%b) Sa se scrie un predicat care adăuga după fiecare element par %valoarea 1
/*
Model matematic
				=[], n=0
adauga(l1..ln)	=l1(+)adăuga(l2..ln), n>0 si l1 impar
				=l1(+)1(+)adăuga(l2..ln), n>0 si l1 par
adăuga(L:lista,R:lista)
L - reprezintă lista de elemente pentru care dorim sa facem înserări
R - reprezintă lista rezultat si anume lista L in care după fiecare număr par s-a adăugat valoarea 1
*/

adauga([],[]) :- !.
adauga([H|T],[H|R]) :-
	H mod 2 =:= 1,
	!,
	adauga(T,R).
adauga([H|T],[H|[1|R]]) :-
	adauga(T,R).
















