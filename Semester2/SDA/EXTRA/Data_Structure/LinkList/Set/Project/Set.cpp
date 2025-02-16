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

Nod::Nod(TElem e) {
    this->e = e;
    next = nullptr;
    previous = nullptr;
}

Set::Set() {
    first = nullptr;
    last = nullptr;
    size = 0;
//    rel = nullptr;
}

Set::Set(Relation relation) {
    first = nullptr;
    last = nullptr;
    size = 0;
//    rel = relation;
}

bool Set::adauga(TElem elem) {
    PNod nod = new Nod(elem);
    //adaugam in lista vida
    if(vida()){
        first = nod;
        last = nod;
        size ++;
        return true;
    }

    //adaugam la inceput
    if(rel(elem,first->e)){
        if(first->e == elem)
            return false;
        nod->next = first;
        first->previous = nod;
        first = nod;
        size ++;
        return true;
    }

    //adaugam la final
    if(rel(last->e,elem)){
        if(last->e == elem)
            return false;
        last->next = nod;
        nod->previous = last;
        last = nod;
        size ++;
        return true;
    }

    //adaugam undeva in lista
    PNod ant = first;
    while(rel(ant->next->e,elem))
        ant = ant->next;
    if(ant->e == elem)
        return false;
    nod->next = ant->next;
    nod->previous = ant;
    ant->next->previous = nod;
    ant->next = nod;
    size ++;
    return true;
}


bool Set::sterge(TElem elem) {
    //lista vida
    if(first == nullptr)
        return false;

    //sterg primul element
    if(elem == first->e){
        PNod p = first;
        first = first->next;
        if(first != nullptr)
            first->previous = nullptr;
        delete p;
        size --;
        return true;
    }

    //sterg ultimul element
    if(elem == last->e){
        PNod p = last;
        last = last->previous;
        if(last != nullptr)
            last->next = nullptr;
        delete p;
        size --;
        return true;
    }

    //sterg un nod oarecare != first si != last
    PNod p = first->next;
    while(p != nullptr and p->e != elem)
        p = p->next;
    if(p == nullptr)
        return false;
    p->previous->next = p->next;
    p->next->previous = p->previous;
    size --;
    delete p;
    return true;
}


bool Set::cauta(TElem elem) const {
    Iterator it = iterator();
    while(it.valid() and it.element() != elem)
        it.urmator();
    return it.valid();
}


int Set::dim() const {
    return size;
}

bool Set::vida() const {
	return first == nullptr;
}

Iterator Set::iterator() const {
	return Iterator(*this);
}


Set::~Set() {
    while(first != nullptr){
        PNod p = first;
        first = first->next;
        delete p;
    }
}


PNod Set::getFirst() {
    return first;
}

PNod Set::getLast() {
    return last;
}

TElem Nod::getValue(){
    return e;
}






