%a)Cel mai mare multiplu comun al unei liste de numere

/*
			=a,b=0
cmmdc(a,b)	=cmmdc(b,a%b), altfel

*/
cmmdc(A,0,A) :- !.
cmmdc(A,B,R) :-
	X is A mod B,
	cmmdc(B,X,R).

/*
cmmmc(a,b) = a/cmmdc(a,b)*b
*/
cmmmc(A,B,R) :- 
	cmmdc(A,B,C),
	R is A div C * B.

/*
					=-1, n=0
cmmmcList(l1..ln)	=l1, n=1
					=cmmmc(l1,l2)(+)cmmmcList(l3..ln), n>1
*/
cmmmcList([],-1) :- !.
cmmmcList([H],H) :- !.
cmmmcList([A,B|T],R) :-
	cmmmc(A,B,C),
	cmmmcList([C|T],R).




%insereaza un element după cele care se afla pe poziții %puteri ale lui 2

/*
		=true,x=1
put2(x)	=put2(x/2), x par
		=false, altfel
*/

put2(1) :- !.
put2(X) :-
	X mod 2 =:= 0,
	X1 is X div 2,
	put2(X1).

/*
insereaza(l1..ln,e)	= insereaza_aux(l,e,1).

						=[], n=0
insereaza_aux(l1..ln,e,p)	=l1(+)e(+)insereaza_aux(l2..ln,e,p+1),
								put2(p) = true si n>0
						=l1(+)insereaza_aux(l2..ln,e,p+1)
								put2(p) = false si n>0				
*/	

insereaza(L,E,R) :- insereaza_aux(L,E,1,R).

insereaza_aux([],_,_,[]) :- !.
insereaza_aux([H|T],E,P,[H|R]) :-
	not(put2(P)),!,
	P1 is P+1,
	insereaza_aux(T,E,P1,R).
insereaza_aux([H|T],E,P,[H|[E|R]]) :-
	P1 is P+1,
	insereaza_aux(T,E,P1,R).











	