#include "Set.h"
#include "Iterator.h"
#include <iostream>

using namespace std;

Set::Set() {
    m = 10;
    size = 0;
    l = new PNod[m];
    for(int i = 0 ; i < m; i++)
        l[i] = nullptr;
}


bool Set::adauga(TElem elem) {
    if(cauta(elem))
        return false;
    int poz = d(hash(elem));
    PNod p = new Nod(elem);
    p->urm = l[poz];
    l[poz] = p;
    size ++;
    return true;
}


bool Set::sterge(TElem elem) {
    int poz = d(hash(elem));
    PNod p = l[poz];
    //lista independenta este vida
    if(p == nullptr)
        return false;
    //este primul din lista independenta
    if(p->e == elem){
        l[poz] = p->urm;
        delete p;
        size --;
        return true;
    }
    //este undeva in lista sau nu este deloc
    while(p->urm != nullptr and p->urm->e != elem){
        p = p->urm;
    }
    if(p->urm == nullptr)
        return false;
    PNod del = p->urm;
    p->urm = p->urm->urm;
    delete del;
    size --;
    return true;
}


bool Set::cauta(TElem elem) const {
    int poz = d(hash(elem));
    if(l[poz] != nullptr){
        PNod p = l[poz];
        while(p != nullptr){
            if(p->e == elem)
                return true;
            p = p->urm;
        }
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
    for(int i = 0; i < m; i++){
        if(l[i] != nullptr){
            PNod p = l[i];
            while(p != nullptr){
                PNod aux = p;
                p = p->urm;
                delete aux;
            }
        }
    }
    delete l;
}

int Set::d(int k) const {
    return k%m;
}

int Set::hash(int k) const {
    return abs(k);
}






