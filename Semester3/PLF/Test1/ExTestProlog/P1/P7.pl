%predicat care face combinari de n luate cate 2 

/*

main(l1..ln) 	= [],n=0
			= leaga(findall(perechi(l2..ln,l1)),main(l2..ln))

leaga(l1..ln,k1..kn)	= k1..kn, n=0
				= l1(+)leaga(l2..ln), n>0

perechi(l1..ln,e)
1.[e,l1], n>0
2.perechi(l2..ln),n>0

*/

main([],[]) :- !.
main([H|T],R) :-
	findall(Per,perechi(T,H,Per),Rez),
	main(T,R1),
	leaga(Rez,R1,R).


leaga([],K,K) :- !.
leaga([H|T],K,[H|R]) :-
	leaga(T,K,R).

perechi([H|_],E,[E,H]).
perechi([_|T],E,R) :-
	perechi(T,E,R).