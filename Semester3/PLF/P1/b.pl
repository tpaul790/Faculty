%put2(A:intiger,R:intiger).
%A reprezintă numărul pentru care verific proprietatea
%R va fi 0 daca numărul nu respecta proprietatea si 1 in caz contrar
/*
Model matematic
	= true, a=1
put2(a)	= false, a%2=1
	= put2(a/2), a%2=0
*/
%(i,o),(i,i)-determinist

put2(1,1) :- !.
put2(A,0) :-
	A mod 2 =:= 1.
put2(A,R) :-
	A mod 2 =:= 0,
	A1 is A div 2,
	put2(A1,R).
/*
Cazuri de testare
put2(1,X) => X=1
put2(2,X) => X=1
put2(5,X) => X=0
put2(1024,X) => X=1
put2(2,1) => true
put2(511,1) => false
put2(511,0) => true
*/



%adaugare(L:list,V:intiger,P:intiger,R:list).
%L reprezintă lista de numere
%V va reprezenta valoarea care se adăuga
%P este poziția curenta
%R reprezintă lista rezultat
/*
Model matematic
			=[], n=0
adaugare(l1...ln,v,p)	=l1+adaugare(l2...ln,v,p+1),put2(p)=false
			=l1+v+adăugare(l2...ln,v,p+1),put2(p)=true.
*/

adaugare([],_,_,[]):-!.
adaugare([H|T],V,P,[H|R]) :-
	Pu is P+1,
	put2(P,P1),
	P1 is 0,
	adaugare(T,V,Pu,R).
adaugare([H|T],V,P,[H,V|R]) :-
	Pu is P+1,
	P1 is 1,
	put2(P,P1),
	adaugare(T,V,Pu,R).





%adaugare_f(L:list,V:intiger,R:list)
%L reprezintă lista de numere
%V va reprezenta valoarea care se adăuga
%R reprezintă lista rezultat
/*
Model matematic
adaugare_f(L,V) = adaugare(L,V,1)
*/

(i,i,i),(i,i,o),(i,o,i),(o,i,i) - deterministe.
adaugare_f(L,V,R) :- adaugare(L,V,1,R).
	
/*
Cazuri de testare
adaugare_f([],3,X) => X=[]
adaugare_f([1,2,3],5,[1,5,2,5,3]) => true
adaugare_f([1,2,3],5,[1,2,3]) => false
adaugare_f([1,2,3],5,X) => X=[1,5,2,5,3]
adaugare_f([1,2,3],X,[1,5,2,5,3]) => X=5
adaugare_f(X,5,[1,5,2,5,3]) => x=[1,2,3]
*/
	








