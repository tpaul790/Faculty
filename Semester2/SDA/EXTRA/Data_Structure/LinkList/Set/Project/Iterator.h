#pragma once
#include "Set.h"

typedef int TElem;

class Iterator{
	friend class Set;
    friend class Nod;
private:
	Iterator(const Set& m);
	const Set& set;
    PNod curent;
public:
		//reseteaza pozitia iteratorului la inceputul containerului
		void prim();

		//muta iteratorul in container
		// arunca exceptie daca iteratorul nu e valid
		void urmator();

        void anterior();

		//verifica daca iteratorul e valid (indica un element al containerului)
		bool valid() const;

		//returneaza valoarea elementului din container referit de iterator
		//arunca exceptie daca iteratorul nu e valid
		TElem element() const;
};

