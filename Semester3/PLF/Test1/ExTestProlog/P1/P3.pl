%sa se scrie un predicat care transorma o lista intr o mulțime

/*
	toSet(l)=invers(toSetAux(l,[])) 

*/

toSet(L,R) :-
	toSetAux(L,[],R1),
	invers(R1,R).

/*
						=col, n=0
	toSetAux(l1...ln,col)	=toSetAux(l2..ln,l1(+)col),
							membru(l1,col) = false si n>0
						=toSetAux(l2..ln,col),
							membru(l1,col) = true si n>0
*/

toSetAux([],C,C) :- !.
toSetAux([H|T],C,R) :-
	not(membru(C,H)),!,
	toSetAux(T,[H|C],R).
toSetAux([_|T],C,R) :-
	toSetAux(T,C,R).

/*
				=true, e=l1
membru(l1..ln,e)	=membru(l2..ln,e), e != l1
				=false, altfel
	
*/

membru([E|_],E) :- !.
membru([_|T],E) :-
	membru(T,E).

/*
	invers(l)=inversAux(l,[]).

	
	inversAux(l1..ln,col)	=col, n=0
						=inversAux(l2..ln,l1(+)col), n>0
*/

invers(L,R) :- inversAux(L,[],R).

inversAux([],C,C) :- !.
inversAux([H|T],C,R) :-
	inversAux(T,[H|C],R).




%sa se descompună o lista intr o lista de forma,[lista de numere impare, lista pare] 

/*

descompunere(l,p,i) = descompunereAux(l,[],[],0,0)

descompunereAux(l1..ln,cp,ci,nrp,nri) 
	=[cp,ci],nr,nri daca n=0
	=descompunereAux(l2..ln,l1(+)cp,ci,nrp+1,nri), l1 par si n>0
	=descompunereAux(l2..ln,cp, l1(+)ci,nrp,nri+1), l1 impar si n>0
*/

descompunere(L,P,I,R) :- descompunereAux(L,[],[],R,0,0,P,I).

descompunereAux([],CP,CI,[CP,CI],NRP,NRI,NRP,NRI) :- !.
descompunereAux([H|T],CP,CI,R,NRP,NRI,P,I) :-
	H mod 2 =:= 0, !,
	NRP1 is NRP+1,
	descompunereAux(T,[H|CP],CI,R,NRP1,NRI,P,I).
descompunereAux([H|T],CP,CI,R,NRP,NRI,P,I) :-
	NRI1 is NRI+1,
	descompunereAux(T,CP,[H|CI],R,NRP,NRI1,P,I).











		
