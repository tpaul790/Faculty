//
// Created by Titieni Paul on 02.05.2024.
//

#ifndef LAB4_ITERATORMD_H
#define LAB4_ITERATORMD_H

#include "MD.h"

class MD;

class IteratorMD
{
    friend class MD;

private:

    //constructorul primeste o referinta catre Container
    //iteratorul va referi primul element din container
    IteratorMD(const MD& c);

    //contine o referinta catre containerul pe care il itereaza
    const MD& md;

    /* aici e reprezentarea  specifica a iteratorului */
    int curent;

    void deplasare();

public:

    //reseteaza pozitia iteratorului la inceputul containerului
    void prim();

    //muta iteratorul in container
    // arunca exceptie daca iteratorul nu e valid
    void urmator();

    //verifica daca iteratorul e valid (indica un element al containerului)
    bool valid() const;

    //returneaza valoarea elementului din container referit de iterator
    //arunca exceptie daca iteratorul nu e valid
    TElem element() const;
};




#endif //LAB4_ITERATORMD_H
