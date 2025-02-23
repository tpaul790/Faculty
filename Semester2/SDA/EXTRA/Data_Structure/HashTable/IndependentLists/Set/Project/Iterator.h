#pragma once
#include "Set.h"

typedef int TElem;

class Iterator{
	friend class Set;
    friend class Nod;
private:
	Iterator(const Set& m);
	const Set& set;
    int poz;
    PNod curent;
    void deplasare();
public:

		void prim();

		void urmator();

		bool valid() const;

		TElem element() const;
};

