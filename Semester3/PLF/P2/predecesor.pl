%predecesor_list(L:list,R:list)
%(i,i),(i,o)

/*
Model matematic
				=[], n=0
predecesor_list(l1..ln)	=l1 (+) predecesor_list(l2..ln), n>=1 si number(l1)
				=predecesor(l1) (+) predecesor_list(l2..ln), n>=1 si is_list(l1)

L - lista eteregoena in care sublistele vor fi înlocuite cu lista care reprezintă predecesorul ei
R - lista eterogenă in care sublistele sunt înlocuite cu lista care reprezintă predecesorul ei
*/

predecesor_list([],[]).
predecesor_list([H|T],[H|R]) :-
	number(H),!, 			%daca nu E number,sigur e lista
	predecesor_list(T,R).
predecesor_list([H|T],[L|R]) :-
	predecesor(H,L),
	predecesor_list(T,R).

/*
Cazuri de testare
predecesor_list([],R) => R = []
predecesor_list([[1,2,9],1,2,[9,9]],R) => R = [[1,3,0],1,2,[1,0,0]]
predecesor_list([1,2,3],[1,2,3]) => true
predecesor_list([[1,2,3]],[[1,2,3]]) => false
predecesor_list([[1,2,3]],R) => R = [[1,2,4]]
*/





%predecesor(L:list,R:list)
%(i,i),(i,o)

/*
Model matematic	
predecesor(l1..ln)=invers(predecesor_aux(0,1,invers(l1..ln)))

L - lista sub forma căreia se reperzinta numărul
R - lista in care vom reprezenta predecesorul numărului reprezentat de L
*/

predecesor(L,R) :- 
	invers(L,L1),
	predecesor_aux(1,L1,R1),
	invers(R1,R).

/*
Cazuri de testare
predecesor([],R) => R = []
predecesor([1,2,3],R) => R = [1,2,4]
predecesor([1,2,9],R) => R = [1,3,0]
predecesor([9,9],R) => R = [1,0,0]
predecesor([9,9],[1,0,0]) => true
predecesor([1,2,3],[2,3,4]) => false
*/





%predecesor_aux(C:int,L:list,R:list)
/*
C - carry flag, tine minte cifra de transport
L - numărul reprezentat in forma de lista
R - predecesorul numărului lui L reprezentat sub forma de lista
Model matematic
					=[], n=0 si c=0
					=[1], n=0 si c=1
predecesor_aux(l1..ln,c)		=0 (+) predecesor_aux(l2..ln,1,f), l1=9 si c=1
					=l1+1 (+) predecesor_aux(l2,..ln,0,f), l1<9 si c=1
					=l1 (+) predecesor_aux(l2..ln,c,f), c=0

*/

predecesor_aux(0,[],[]).
predecesor_aux(1,[],[1]).
predecesor_aux(1,[9|T],[0|R]) :-
	predecesor_aux(1,T,R).
predecesor_aux(1,[H|T],[H1|R]) :-
	H < 9,
	H1 is H+1,
	predecesor_aux(0,T,R).
predecesor_aux(0,[H|T],[H|R]) :-
	predecesor_aux(0,T,R).
	




%invers(L:list,R:list)
%(i,i),(i,o)
/*
Model matematic
invers(l1...ln)=invers_aux(l1..ln,(/)).  (/) - multimea vida

L - lista pe care vrem sa o inversam
R - lista R inverata
*/

invers(L,R) :- invers_aux([],L,R).

/*
Cazuri de testare
invers([],R) => R=[]
invers([1,2,3],R) => R = [3,2,1]
invers([1,2,3],[1,2,3]) => true
invers([1,2],[1,2]) => false
*/






%invers_aux(C:list,L:list,R:list)
/*
Model matematic
			=Col, n=0
invers_aux(l1..ln,Col)	=invers_aux(l2..ln,l1 (+) Col) altfel

C - variabila colectoare
L - lista pe care doresc sa o inversez
R - lista L inversată
*/

invers_aux(C,[],C).
invers_aux(C,[H|T],R) :-
		invers_aux([H|C],T,R).


























