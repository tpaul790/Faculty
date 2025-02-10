/*
	Se dau doua numere naturale n si m. Se cere sa se afișeze, in toate modurile posibile, toate numerele de la 1 la n, astfel încât pt orice numere afișate pe poziții consecutive, diferența in modul sa fie >=m
*/

%toate(N:intreg,M:intreg,LRez:list) (i,i,o)
/*
-N este limita superioară a candidaților
-M este limita inferioara a diferenței in modul pentru doua elemente consecutive din soluție
-R este o lista de liste in care fiecare lista reprezintă o soluție a problemei
*/

toate(N,M,LRez) :- U(generare(N,M,R)).

%generare(N:intreg,M:intreg,R:list) (i,i,o)
/*
-N este limita superioară a candidaților
-M este limita inferioara a diferenței in modul pentru doua elemente consecutive din soluție
-R este o lista de N numere distincte din [1,N] a.i. diferenta in modul dintre oricare doua este >=M

Model matematic

generare(n,m)		= generare_aux(n,m,1,candidat(n))
*/

generare(N,M,R) :-
	candidat(N,C),
	generare_aux(N,M,R,1,[C]).




%candidat(N:intreg,C:intreg) (i,o)
/*
-N reprezintă limita superioară a intervalului de candidați
-C reprezintă un candidat, un număr din intervalul [1,N]

Model matematic

1. n
2. candidat(n-1), n>1
*/

candidat(N,N).
candidat(N,C) :-
	N>1,
	N1 is N-1,
	candidat(N1,C).




% generare_aux(N:intreg,M:intreg,R:list,L:intreg,C:list) (i,i,o,i,i)
/*
-N reprezintă limita superioară a intervalului de candidați
-M reprezintă limita inferioara a diferenței in modul intre doua elemente aflate pe poziții consecutive in soluție
-R reprezintă o lista cu N elemente distincte din intervalul [1,N] ce respecta proprietatea ca diferența in modul dintre oricare doua consecutive este >=M
-L reprezintă numărul de elemente adăugate la un moment dat
-C reprezintă variabila colectoare in care vom construi soluțiile

Model matematic

generare_aux(n,m,l,c1..ck)		= c, n=l
							=generare_aux(n,m,l+1,candidat(n)(+) c),
							candidat(n) not((+)) c si
							abs(candidat(n)-c1)>=m 
*/


generare_aux(N,_,Col,N,Col) :- !.
generare_aux(N,M,R,L,[H|T]) :-
	candidat(N,C),
	abs(C-H)>=M,
	not(apare(C,[H|T])),
	L1 is L+1,
	generare_aux(N,M,R,L1,[C|[H|T]]).





%apare(E:intreg,L:lista) (i,i)
/*
-E elementul pe care îl căutăm in lista L
-L lista in care îl căutăm pe E

Model matematic

apare(e,l1..ln)	= true, e=l1
				= apare(e,l2..ln), altfel
*/

apare(E,[E|_]) :- !.
apare(E,[_|T]) :-
	apare(E,T).


/*
Cazuri de testare
toate(4,2,R) => R = [[2, 4, 1, 3], [3, 1, 4, 2]]
toate(2,2,R) => R = []
*/








