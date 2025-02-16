#include "Iterator.h"
#include "Set.h"


Iterator::Iterator(const Set& m) : set(m){
	curent = 0;
}

TElem Iterator::element() const {
	return set.elems[curent];
}

bool Iterator::valid() const {
	return curent < set.size;
}

void Iterator::urmator() {
	curent++;
}

void Iterator::prim() {
	curent = 0;
}

