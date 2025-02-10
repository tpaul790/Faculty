%cmmdc(A:int,B:int,R:int)
%A este primul numar
%B este al doilea număr
%R va reprezenta cmmdc dintre A si B

/*
Model matematic
		=a, b=0
cmmdc(a,b)	=b, a=0
		=cmmdc(a-b,b), a>=b
		=cmmdx(b-a,a), b>a
*/
%(i,i,o),(i,i,i)-deterministe.

cmmdc(A,0,A).
cmmdc(0,B,B).
cmmdc(A,B,A) :- 
	A == B.
cmmdc(A,B,R) :-
	A>B,
	A1 is A-B,
	cmmdc(A1,B,R).
cmmdc(A,B,R) :-
	B>A,
	B1 is B-A,
	cmmdc(B1,A,R).




%cmmmc(A:integer,B:integer,R:integer).
%A este primul numar
%B este al doilea număr
%R va reprezenta cmmmc dintre A si B
/*
Model matematic

cmmmc(a,b)	=-1, cmmdc(a,b)=0
		=a*b/cmmdc(a,b), altfel
*/
%(i,i,i),(i,i,o)-deterministe.
cmmmc(A,B,R) :-
	cmmdc(A,B,R1),
	R is A*B/R1.




%cmmmc_l(L:list,R:intiger).
%L este lista de numere
%R este cmmmc ul elementelor din lista
/*
Model matematic
			=-1, n=0
cmmmc_l(l1,l2,...,ln)	=l1, n=1
			=cmmmc_l(cmmmc(l1,l2)(+)l3,l4,...,ln), altfel
*/
%(i,i),(i,o)-deterministe

cmmmc_l([],-1).
cmmmc_l([H|T],H) :-
	T == [].
cmmmc_l([A,B|T],R) :-
	cmmmc(A,B,R1),
	cmmmc_l([R1|T],R).

/*
Cazuri de testare:
cmmmc_l([],X) => X=-1
cmmmc_l([5],X) => X=5
cmmmc_l([3,6,9],X) => X=18
cmmmc_l([3,3,3],X) => X=3
cmmmc_l([],-1) => true
cmmmc_l([5],6) => false
cmmmc_l([3,6,9],18) => true
cmmmc_l([1,2,3],7) => false
*/