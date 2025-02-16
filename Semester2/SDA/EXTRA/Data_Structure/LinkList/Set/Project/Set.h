#pragma once

class Iterator;
class Nod;

typedef int TElem;

typedef bool(*Relation)(TElem, TElem);

typedef Nod* PNod;

bool rel(TElem, TElem);

class Nod{
    friend class Set;
    friend class Iterator;
private:
    TElem e;
    PNod next;
    PNod previous;
public:
    Nod(TElem e);

    TElem getValue();
};

class Set {
	friend class Iterator;
    friend  class Nod;
private:
    PNod first, last;
    int size;
//    Relation rel;
public:
        PNod getFirst();

        PNod getLast();

		//constructorul implicit
		Set();

        Set(Relation relation);

		//adauga un element in multime
		//returneaza adevarat daca elementul a fost adaugat (nu exista deja in multime)
		bool adauga(TElem e);

		//sterge un element din multime
		//returneaza adevarat daca elementul a existat si a fost sters
		bool sterge(TElem e);

		//verifica daca un element se afla in multime
		bool cauta(TElem elem) const;


		//intoarce numarul de elemente din multime;
		int dim() const;

		//verifica daca multimea e vida;
		bool vida() const;

		//returneaza un iterator pe multime
		Iterator iterator() const;

		// destructorul multimii
		~Set();

};

