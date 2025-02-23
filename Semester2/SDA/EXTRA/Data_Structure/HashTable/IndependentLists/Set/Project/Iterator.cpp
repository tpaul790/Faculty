#include "Iterator.h"
#include "Set.h"


Iterator::Iterator(const Set& m) : set(m){
    poz = 0;
    deplasare();
}

TElem Iterator::element() const {
    return curent->e;
}

bool Iterator::valid() const {
	return curent != nullptr and poz < set.m;
}

void Iterator::urmator() {
    if(curent->urm == nullptr){
        poz ++;
        deplasare();
    }else{
        curent = curent->urm;
    }
}

void Iterator::prim() {
    poz = 0;
    deplasare();
}

void Iterator::deplasare() {
    while(poz < set.m and set.l[poz] == nullptr)
        poz ++;
    if(poz == set.m){
        curent = nullptr;
    }else
        curent = set.l[poz];
}

