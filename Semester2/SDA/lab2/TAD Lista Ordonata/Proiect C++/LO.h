#ifndef LAB2_LO_H
#define LAB2_LO_H


class Iterator;

typedef int TComparabil;
typedef TComparabil TElement;

typedef bool (*Relatie)(TElement, TElement);

#define NULL_TELEMENT -1

class Nod;

typedef Nod *Pnod;

class Nod{
private:
    friend class LO;
    TElement e;
    Pnod precedent,urmator;
public:
    Nod(TElement e, Pnod precedent, Pnod urmator);

    TElement get_elem();

    Pnod get_prec();

    Pnod get_urm();
};

class LO {
private:
    friend class Iterator;
private:
    /* aici reprezentarea */
    Pnod first,last;
    Relatie rel;
public:
    // constructor
    LO (Relatie r);

    // returnare dimensiune
    int dim() const;

    // verifica daca LO e vida
    bool vida() const;

    // prima pozitie din LO
    Iterator prim() const;

    // returnare element de pe pozitia curenta
    //arunca exceptie daca poz nu e valid
    static TElement element(Iterator poz) ;

    // adaugare element in LO a.i. sa se pastreze ordinea intre elemente
    void adauga(TElement e);

    // sterge element de pe o pozitie poz si returneaza elementul sters
    //dupa stergere poz e pozitionat pe elementul de dupa cel sters
    //arunca exceptie daca poz nu e valid
    TElement sterge(Iterator& poz);

    // cauta element si returneaza prima pozitie pe care apare (sau iterator invalid)
    Iterator cauta(TElement e) const;

    int eliminaDinKInK(int k);

    //destructor
    ~LO();

};


#endif //LAB2_LO_H
