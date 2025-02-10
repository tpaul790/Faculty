#include "Multime.h"
#include "IteratorMultime.h"

#include <iostream>

/*
 * Timp constant, Tetha(10)
 */
Multime::Multime() : cp{10},prim{-1},primLiber{0}, size{0}{
    urm=new int[cp];
    e=new TElem[cp];
    for(int i=0;i<cp-1;i++)
        urm[i]=i+1;
    urm[cp-1]=-1;
}

/*
 * Functia de adaugare are o complexitate echivalenta cu functia de cautare
 * ,a carei complexitate este descrisa mai jos, deoarece depinde de ea
 */
bool Multime::adauga(TElem elem) {
    if(cauta(elem))
        return false;
    int nou = creeazaNod(elem);
    urm[nou]=prim;
    prim=nou;
    size++;
    return true;
}

/*
 * BC: multimea este vida sau sterg primul element, in acest caz complexitatea este constanta, Tetha(1)
 * WC: nu exita element in multime sau este chiar ultimul, complexitate liniara, Tetha(n)
 * AC: in medie elementul pe care doresc sa l sterg poate sa apara pe pozitia 1,2,..,n iar
 *     probabilitatea sa se afle pe un din aceste pozitii este 1/n deci T(n)=1/n*n(n+1)/2
 *     care apartine lui Tetha(n)
 * In concluzie, complexitatea generala a algoritmului este O(n),unde n reprezinta numarul de elemente din multime
 */
bool Multime::sterge(TElem elem) {
    if(vida())
        return false;
    int p=prim;
    if(e[p]==elem){
        prim=urm[p];
        dealoca(p);
        size--;
        return true;
    }
    while (urm[p]!=-1 and e[urm[p]] != elem)
        p = urm[p];
    if(urm[p]==-1)
        return false;
    int s=urm[p];   // nodul pe care il sterg
    urm[p]=urm[s];
    dealoca(s);
    size--;
    return true;
}

/*
 * BC: lista este vida sau elementul cautat este chiar primul din colectie,Tetha(1)
 * WC: elementul nu se afla in colectie sau este chiar ultimul,Tetha(n)
 * AC: in general un element se poate afla pe pozitia 1,2,..,n iar probabilitatea
 *     ca un element sa se afle pe una dintre aceste pozitii este 1/n deci T(n)=1/n*n(n+1)/2=(n+1)/2 care
 *     apartine de Tetha(n)
 * In concluzie complexitatea generala a algoritmului este O(n)
 */
bool Multime::cauta(TElem elem) const {
    int p=prim;
    while(p!=-1){
        if(e[p]==elem)
            return true;
        p=urm[p];
    }
    return false;
}

/*
 * //Complexitate liniara, Tetha(n), deoarece face o parcurgere completa a multimii
 * Complexitate timp constanta,Tetha(1)
 */
int Multime::dim() const {
//    if(vida())
//        return 0;
//    int cnt=0;
//    int p=prim;
//    while(p!=-1){
//        cnt++;
//        p=urm[p];
//    }
//    return cnt;
    return size;
}

/*
 * Timp constant, Tetha(1)
 */
bool Multime::vida() const {
    if(prim==-1)
        return true;
    return false;
}

/*
 * Timp constant, Tetha(1)
 */
Multime::~Multime() {
    delete[] e;
    delete[] urm;
}


/*
 * Timp constant, Tetha(1)
 */
IteratorMultime Multime::iterator() const {
    return IteratorMultime(*this);
}

/*
 * Timp constant, Tetha(1)
 */
int Multime::aloca() {
    int i=primLiber;
    primLiber=urm[primLiber];
    return i;
}

/*
 * Timp constant, Tetha(1)
 */
void Multime::dealoca(int i) {
    urm[i]=primLiber;
    primLiber=i;
}

/*
 * Complexitate timp constanta amortizata datorita redimensionarii, Tetha(1)
 */
int Multime::creeazaNod(TElem elem) {
    if(primLiber==-1)
        redimensionare();
    int i=aloca();
    e[i]=elem;
    urm[i]=-1;
    return i;
}

/*
 * Complexitate timp liniara, T(n)=n+2*n=3*n care aparine lui Tehta(n)
 */
void Multime::redimensionare() {
    TElem* new_e=new TElem[2*cp];
    int* new_urm=new int[2*cp];
    for(int i=0;i<cp;i++)
        new_e[i]=e[i],new_urm[i]=urm[i];
    primLiber=cp;
    for(int i=cp;i<2*cp-1;i++)
        new_urm[i]=i+1;
    new_urm[2*cp-1]=-1;
    cp=2*cp;
    delete[] e;
    e=new_e;
    delete[] urm;
    urm=new_urm;
}

int Multime::diferentaMaxMin() const {
    if(vida())
        return -1;
    int n=dim();
    TElem max=e[0],min=e[0];
    for(int i=0;i<n;i++){
        if(e[i]>max)
            max=e[i];
        if(e[i]<min)
            min=e[i];
    }
    return max-min;
}

//int Multime::get_cp() {
//    return cp;
//}
//
//void Multime::see_all_e() {
//    for(int i=0;i<cp;i++)
//        std::cout<<e[i]<<' ';
//}
//
//void Multime::see_all_urm() {
//    for(int i=0;i<cp;i++)
//        std::cout<<urm[i]<<' ';
//}
