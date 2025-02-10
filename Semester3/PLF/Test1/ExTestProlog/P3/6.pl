/*
Paranteze 1 = ( si 2 = )

sols(n) = sol(invers(back(n)))

%se începe cu o paranteza deschisă (1= '(')
back(n)	= aux(n,[1],1,0)

%se pot adauga n/2 paranteze deshise si n/2 închise
%
aux(n, col,lg1 , lg2)
	= col, lg1+lg2 = n
	1.aux(n,1(+)col,lg1+1,lg2), lg1<n/2
	2.aux(n,2(+)col,lg1,lg2), lg2<n/2 si lg2<lg1


*/
sols(N) :-	
	back(N,R1),
	invers(R1,R),
	sol(R).

sol([]) :-
	nl.
sol([H|T]) :-
	H == 1,!,
	write('('),
	sol(T).
sol([_|T]) :-
	write(')'),
	sol(T).

back(N,R) :-
	aux(N,[1],1,0,R).

aux(N,Col,Lg1,Lg2,Col) :-
	Lg1+Lg2 =:= N,!.
aux(N,Col,Lg1,Lg2,R) :-
	H is N div 2,
	Lg1 < H,
	Lg is Lg1+1,
	aux(N,[1|Col],Lg,Lg2,R).
aux(N,Col,Lg1,Lg2,R) :-
	H is N div 2,
	Lg2 < H,
	Lg2 < Lg1,
	Lg is Lg2+1,
	aux(N,[2|Col],Lg1,Lg,R).

invers(L,R) :- inv(L,[],R).

inv([],Col,Col) :- !.
inv([H|T],Col,R) :-
	inv(T,[H|Col],R).




