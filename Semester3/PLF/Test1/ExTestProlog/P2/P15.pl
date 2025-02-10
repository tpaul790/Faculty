/*

secv(l1..ln) =	aux(l1..ln,[],[])

aux(l1..ln, col1..colm, max1..maxk)	
					= max, n=0 si k>m
					= col, n-0 si m>k
					= aux(l2..ln,l1(+)col1..colm, max),n>0,l1 par,m=0
					= aux(l2..ln,l1(+)col1..colm, max),n>0,l1 par,m>0,
												col1+2=l1
					= aux(l2..ln,[],max), n>0, l1 impar;col1+2!=l1,k>m
					= aux(l2..ln,[],col), n>0, l1 impar;col1+2!=l1, m>k
				
*/

secv(L,R) :- aux(L,[],[],R).

aux([],C,M,C) :-
	size(C,S1),
	size(M,S2),
	S1>S2,!.

aux([],_,M,M).

aux([H|T],[],M,R) :-
	H mod 2 =:= 0,
	aux(T,[H],M,R).

aux([H|T],[C1|T1],M,R) :-
	H mod 2 =:= 0,
	C1+2 =:= H,
	aux(T,[H|[C1|T1]],M,R).

aux([H|T],[C1|T1],M,R) :-
	size([C1|T1],S1),
	size(M,S2),
	H mod 2 =:= 0,
	C1+2 =\= H,
	S1 > S2,
	aux(T,[],[C1|T1],R).

aux([H|T],[C1|T1],M,R) :-
	size([C1|T1],S1),
	size(M,S2),
	H mod 2 =:= 0,
	C1+2 =\= H,
	S1 =< S2,
	aux(T,[],[C1|T1],R).

aux([H|T],C,M,R) :-
	size(C,S1),
	size(M,S2),
	H mod 2 =:= 1,
	S1 > S2,
	aux(T,[],C,R).

aux([H|T],C,M,R) :-
	size(C,S1),
	size(M,S2),
	H mod 2 =:= 1,
	S1 =< S2,
	aux(T,[],M,R).

size([],0).
size([_|T],R) :-
	size(T,R1),
	R is R1 + 1.