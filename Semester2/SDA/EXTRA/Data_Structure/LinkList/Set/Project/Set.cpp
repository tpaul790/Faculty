#include "Set.h"
#include "Iterator.h"
#include <iostream>

using namespace std;

//o posibila relatie
bool rel(TElem e1, TElem e2) {
	if (e1 <= e2) {
		return true;
	}
	else {
		return false;
	}
}

Set::Set() {
}


bool Set::adauga(TElem elem) {
    return false;
}


bool Set::sterge(TElem elem) {
    return true;
}


bool Set::cauta(TElem elem) const {
    return false;
}


int Set::dim() const {
    return -1;
}



bool Set::vida() const {
	return false;
}

Iterator Set::iterator() const {
	return Iterator(*this);
}


Set::~Set() {
}






