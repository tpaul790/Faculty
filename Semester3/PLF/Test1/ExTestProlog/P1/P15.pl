%verifica daca are număr par de elemente fără sa le numere
/*
			=true, n=0
verif(l1..ln)	=false, n=1
			=verif(l3..ln), n>1

*/

verif([_,_]) :- !.
verif([_,_|T]) :-
	verif(T).