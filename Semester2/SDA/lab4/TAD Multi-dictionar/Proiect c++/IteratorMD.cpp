#include "IteratorMD.h"
#include "MD.h"

using namespace std;

/*
 * BC: curent se afla pe un element valid, complexitate constanta, tetha(1)
 * WC: curent este pozitionat pe pozitia 0 iar tabela este vida,
 *     complexitate liniara deoarece se va face o intreaga parcurgere a acesteia, tetha(n)
 * AC: in medie, probabilitatea ca un element valid sa se afle pe una din pozitiile
 *     1,2,..,n este 1/n, de aici rezulta o complexitate liniara, tetha(n)
 * In concluzie, complexitatea generala a algoritmului este O(n)
 */
void IteratorMD::deplasare() {
    while(curent<md.m and (element()==NIL or element()==STERS))
        curent++;
}

/*
 *Complexitate echivalenta cu cea a algoritmului de deplasare, O(n)
 */
IteratorMD::IteratorMD(const MD& _md): md(_md) {
    curent=0;
    deplasare();
}

/*
 * Complexitate timp constanta
 */
TElem IteratorMD::element() const{
    if(!valid())
        throw std::exception();
    return md.e[curent];
}

/*
 * Complexitate timp constanta
 */
bool IteratorMD::valid() const {
    return (curent<md.m);
}

/*
 *Complexitate echivalenta cu cea a algoritmului de deplasare, O(n)
 */
void IteratorMD::urmator() {
    if(!valid())
        throw std::exception();
    curent++;
    deplasare();
}

/*
 *Complexitate echivalenta cu cea a algoritmului de deplasare, O(n)
 */
void IteratorMD::prim() {
    curent=0;
    deplasare();
}

