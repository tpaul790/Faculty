#include "IteratorColectie.h"
#include "Colectie.h"
#include <exception>

IteratorColectie::IteratorColectie(const Colectie& c): col(c) {
    curent=0;
}

void IteratorColectie::prim() {
    curent=0;
}

void IteratorColectie::urmator() {
    curent++;
}

bool IteratorColectie::valid() const {
    return curent<col.dim();
}

TElem IteratorColectie::element() const {
    return col.lista_dist[col.lista_poz[curent]];
}
