%sa se șteargă secvențele de elemente consecutive
/*

sterge(l1..ln)	= [], n=0
				= [l1], n=1
				= [], l1+1=l2 si n=2
				= l1..ln, l1+1!=l2 si n=2
				= sterge(l2..ln), l1=l2-1 si l2=l3-1 si n>2
				= sterge(l3..ln), l1=l2-1 si l2!=l3-1 si n>2
				= l1(+)șterge(l2..ln), altfel
*/

sterge([],[]) :- !.
sterge([H],[H]) :- !.
sterge([A,B],[A,B]) :- 
	A =\= B-1,!.
sterge([_,_],[]) :- !.
sterge([A,B,C|T],R) :-
	A =:= B-1,
	B =:= C-1,!,
	sterge([B,C|T],R).
sterge([A,B,C|T],R) :-
	A =:= B-1,
	B =\= C-1,!,
	sterge([C|T],R).
sterge([A,B,C|T],[A|R]) :-
	sterge([B,C|T],R).
	

/*
elim(L) = aux(L,0)

aux(l1..ln,f)	= [], n=0
			= [l1], n=1 si f=0
			= [], n=1 si f=1
			= l1(+)aux(l2..ln,0),n>1 si l1 != l2-1 si f=0
			= aux(l2..ln,0), n>1 si l1 != l2-1 si f=1
			= aux(l2..ln,1), n>1 si l1 = l2-1
*/

elim(L,R) :- aux(L,0,R).

aux([],_,[]).
aux([H],0,[H]).
aux([_],1,[]).
aux([A,B|T],0,[A|R]) :-
	A =\= B-1,
	aux([B|T],0,R).
aux([A,B|T],1,R) :-
	A =\= B-1,
	aux([B|T],0,R).
aux([A,B|T],_,R) :-
	A =:= B-1,
	aux([B|T],1,R).