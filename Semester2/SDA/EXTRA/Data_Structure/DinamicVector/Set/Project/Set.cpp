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

void Set::resize() {
    capacity *= 2;
    int* newArray = new int[capacity];
    for(int i = 0; i < size; i++)
        newArray[i] = elems[i];
    delete elems;
    elems = newArray;
}

void Set::unresize() {
    capacity /= 2;
    int* newArray = new int[capacity];
    for(int i = 0; i < size; i++)
        newArray[i] = elems[i];
    delete elems;
    elems = newArray;
}

Set::Set() {
	this->capacity = 5;
    this->size = 0;
    this->elems = new int[capacity];
}


bool Set::adauga(TElem elem) {
    if(size == capacity)
        resize();
	int poz = 0; //pozitia pe care trebuie inserat
    while(poz < size and !rel(elem,elems[poz]))
        poz++;
    if(poz != size and elems[poz] == elem)
        return false;
    for(int i = size-1; i >= poz; i--)
        elems[i+1] = elems[i];
    elems[poz] = elem;
    size++;
    return true;
}


bool Set::sterge(TElem elem) {
    if(capacity >= 10 and size <= capacity/2)
        unresize();
	int poz = -1; //pozitia elementului de sters
    for(int i = 0; i < size and poz == -1; i++)
        if(elems[i] == elem)
            poz = i;
    if(poz == -1)
        return false;
    for(int i = poz+1; i < size; i++)
        elems[i-1] = elems[i];
    size --;
    return true;
}


bool Set::cauta(TElem elem) const {
	int st = 0, dr = size - 1;
    while(st <= dr){
        int mij = (st+dr)/2;
        if(elems[mij] == elem)
            return true;
        else if(elems[mij] < elem) {
            st = mij + 1;
        }else
            dr = mij - 1;
    }
    return false;
}


int Set::dim() const {
    return size;
}



bool Set::vida() const {
	return size == 0;
}

Iterator Set::iterator() const {
	return Iterator(*this);
}


Set::~Set() {
	delete elems;
}






