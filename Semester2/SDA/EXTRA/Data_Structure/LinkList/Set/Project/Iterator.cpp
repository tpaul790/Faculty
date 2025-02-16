#include "Iterator.h"
#include "Set.h"


Iterator::Iterator(const Set& m) : set(m){
    curent = set.first;
}

TElem Iterator::element() const {
    return curent->e;
}

bool Iterator::valid() const {
	return curent != nullptr;
}

void Iterator::urmator() {
    curent = curent->next;
}

void Iterator::anterior() {
    curent = curent->previous;
}

void Iterator::prim() {
    curent = set.first;
}

