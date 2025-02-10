#include "IteratorMultime.h"
#include "Multime.h"
#include <exception>

IteratorMultime::IteratorMultime(const Multime& m) : multime(m){
    curent=multime.prim;
}

/*
 * Timp de executie constant,Tetha(1)
 */
void IteratorMultime::prim() {
    curent=multime.prim;
}

/*
 * Timp de executie constant,Tetha(1)
 */
void IteratorMultime::urmator() {
    if(!valid())
        throw std::exception();
    curent=multime.urm[curent];
}

/*
 * Timp de executie constant,Tetha(1)
 */
TElem IteratorMultime::element() const {
    if(!valid())
        throw std::exception();
    return multime.e[curent];
}

/*
 * Timp de executie constant,Tetha(1)
 */
bool IteratorMultime::valid() const {
    if(curent != -1)
        return true;
    return false;
}
