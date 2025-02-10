#include "Iterator.h"
#include "LO.h"
#include <exception>
#include <iostream>
using namespace std;

LO::LO(Relatie r) {
    rel=r;
    first= nullptr;
    last= nullptr;
}

// returnare dimensiune
/*
 * BC=WC=AC
 * Complexitatea timp a algoritmului este liniara,Tetha(n)
 */
int LO::dim() const {
    int cnt=0;
    auto it=Iterator(*this);
    while(it.valid())
        cnt++,it.urmator();
    return cnt;
}

// verifica daca LO e vida
/*
 * Complexitate constanta,Tetha(1)
 */
bool LO::vida() const {
    if(first== nullptr and last== nullptr)
        return true;
    return false;
}

// prima pozitie din LO
/*
 * Complexitate timp constanta,Tetha(1)
 */
Iterator LO::prim() const {
    auto it=Iterator(*this);
    return it;
}

// returnare element de pe pozitia curenta
//arunca exceptie daca poz nu e valid
/*
 * Complexitate timp constanta,Tetha(1)
 */
TElement LO::element(Iterator poz) {
    if(!poz.valid())
        throw exception();
    return poz.element();
}

//sterge element de pe o pozitie poz si returneaza elementul sters
//dupa stergere poz e pozitionat pe elementul de dupa cel sters
//arunca exceptie daca poz nu e valid
/*
 * Complexitate timp constanta,Tetha(1)
 */
TElement LO::sterge(Iterator& poz) {
    if(!poz.valid())
        throw exception();
    TElement val=poz.curent->e;
    //tratez cazul in care am un singur element in lista si vreau sa l sterg
    if(poz.curent->precedent== nullptr and poz.curent->urmator== nullptr){
        first= nullptr;
        last= nullptr;
    }
    //sterg elementul de pe prima pozitie
    else if(poz.curent->precedent== nullptr) {
        poz.curent->urmator->precedent = poz.curent->precedent;
        first=poz.curent->urmator;
    }
    //sterg elementul de pe ultima pozitie
    else if(poz.curent->urmator== nullptr){
        poz.curent->precedent->urmator=poz.curent->urmator;
        last=poz.curent->precedent;
    }
    //sterg un element oarecare
    else{
        poz.curent->precedent->urmator=poz.curent->urmator;
        poz.curent->urmator->precedent=poz.curent->precedent;
    }
    Pnod p=poz.curent;
    poz.curent=poz.curent->urmator;
    delete p;
    return val;
}


// cauta element si returneaza prima pozitie pe care apare (sau iterator invalid)
/*
 * BC: Lista este vida,elementul cautat este chiar primul,primul element din lista nu se afla in relatie cu elementul cautat
 *     sau elementul cautat nu se afla in relatie cu ultimul element. In acest caz complexitatea
 *     timp este constanta,Tetha(1)
 * WC: Elementul cautat este chiar ultimul din lista, in acest caz complexitatea este liniara, Tetha(n) deoarece se parcurge toata lista
 * AC: Elementul cautat poate fi primul,al doilea,...,ultimul element din lista, iar probabilitatea ca elemntul cautat
 *     sa se afle pe o anumita pozitie este 1/n deci complexitatea medie este T(n)=n(n+1)/2*1/n=(n+1)/2 care apartine lui Tetha(n)
 * Complexitatea generala a algoritmului de cautare este O(n).
 */
Iterator LO::cauta(TElement e) const{
    auto it = Iterator(*this);
    it.curent= nullptr;
    if(first == nullptr or !rel(first->e,e) or !rel(e,last->e))
        return it;
    Pnod p=first;
    while(p != nullptr and p->e!=e and !rel(e,p->e))
        p=p->urmator;
    if(p != nullptr and p->e==e)
        it.curent=p;
    return it;
}

// adaugare element in LO
/*
 * BC: Elementul adaugat este primul din lista, se adauga pe prima sau pe ultima pozitie in raport cu relatia de ordine.
 *     In acest caz complexitatea timp este constanta,Tetha(1).
 * WC: Elementul se adauga pe penultima pozitie din lista, T(n)=n-1 care apartine lui Tetha(n).
 * AC: Elementul se adauga pe pozitia 2,3,...,n-1=>T(n)=(n-2)(n+1)/2*1/n care apartine lui Tetha(n) unde 1/n din
 *     formula reprezinta probabilitatea ca elemntul sa fie adaugat pe o anumita pozitie.
 * Complexitatea generala a algoritmului este O(n).
 */
void LO::adauga(TElement e) {
    Pnod nou = new Nod(e, nullptr, nullptr);
    if(first == nullptr){
        //e primul nod
        first=nou;
        last=nou;
    }
    else{
        Pnod p=first;
        //adaug pe prima pozitie
        if(rel(e,p->e)){
            nou->precedent= nullptr;
            nou->urmator=p;
            p->precedent=nou;
            first=nou;
        }
        //adaugare pe ultima pozitie
        else if (rel(last->e,e)) {
            last->urmator=nou;
            nou->urmator= nullptr;
            nou->precedent=last;
            last=nou;
        }
        //adaugare undeva in lista,nici pe prima nici pe ultima pozitie
        else{
            while (!rel(e, p->e))
                p = p->urmator;
            //adaugare undeva in lista
            nou->precedent = p->precedent;
            p->precedent->urmator = nou;
            nou->urmator = p;
            p->precedent = nou;
        }
    }
}
/*
BC: cand k e negativ sau mai mare decat dimeniunea T(n)=1 care apartine lui Tetha(1)
WC: Complexitatea timp a functiei este dependenta de complexitatea functiei de stergere care este constanta.
    In wc se face o parcurgere completa a listei si pentru elementele care se sterg se apeleaza
    functia de stergere deci complexitatea este Tetha(n)
AC: Deoarece in medie se k reprezinta o valoare potrivita, se efectueaza n pasi deci Tetha(n)
Complexitatea generala este O(n)

 */
int LO::eliminaDinKInK(int k){
    int cnt=0;
    if(k<0)
        throw exception();
    if(k>dim())
        return 0;
    int poz=0;
    Iterator it=prim();
    while(it.valid()){
        poz++;
        if(poz%k==0)
            sterge(it),cnt++;
        else
            it.urmator();
    }
    return cnt;
}

//destructor
/*
 * Complexitate timp liniara deoarece parcurge toata lista,Tetha(n)
 */
LO::~LO() {
    while(first != nullptr){
        Pnod p=first;
        first=first->urmator;
        delete p;
    }
}

/*
 * Complexitate timp constanta,Tetha(1)
 */
Nod::Nod(TElement e, Pnod precedent, Pnod urmator) {
    this->e=e;
    this->precedent=precedent;
    this->urmator=urmator;
}

/*
 * Complexitate timp constanta,Tetha(1)
 */
TElement Nod::get_elem() {
    return this->e;
}

/*
 * Complexitate timp constanta,Tetha(1)
 */
Pnod Nod::get_prec() {
    return this->precedent;
}

/*
 * Complexitate timp constanta,Tetha(1)
 */
Pnod Nod::get_urm() {
    return this->urmator;
}


