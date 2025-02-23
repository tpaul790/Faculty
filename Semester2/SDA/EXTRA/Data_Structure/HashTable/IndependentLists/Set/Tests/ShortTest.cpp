#include "../Project/Set.h"
#include <assert.h>
#include "ShortTest.h"
#include "../Project/Iterator.h"
#include <iostream>

void testAll() { //apelam fiecare functie sa vedem daca exista
    Set m;
    assert(m.vida() == true);
    assert(m.dim() == 0); //adaug niste elemente
    assert(m.adauga(5)==true);
    assert(m.adauga(1)==true);
    assert(m.adauga(10)==true);
    assert(m.adauga(7)==true);
    assert(m.adauga(1)==false);
    assert(m.adauga(10)==false);
    assert(m.adauga(-3)==true);
    assert(m.dim() == 5);
    assert(m.cauta(10) == true);
    assert(m.cauta(16) == false);
    assert(m.sterge(1) == true);
    assert(m.sterge(6) == false);
    assert(m.dim() == 4);


    Iterator im = m.iterator();
    im.prim();
    int s = 0;
    while (im.valid()) {
        TElem e = im.element();
        s += e;
        im.urmator();
    }
    assert(s == 19);

    std::cout<<"Short test passed!\n";
}
