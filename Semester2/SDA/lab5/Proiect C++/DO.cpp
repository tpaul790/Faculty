#include "Iterator.h"
#include "DO.h"
#include <exception>

/*
 * Complextiatea constructorului este liniara, Tetha(cp).
 */
DO::DO(Relatie r) : rel(r), cp(10), rad(-1), primLiber(0), size(0){
    e = new TElem[cp];
    st = new int[cp];
    dr = new int[cp];
    for(int i = 0; i < cp-1; i++){
        st[i] = i+1;
    }
    st[cp-1] = NULL;
}

//adauga o pereche (cheie, valoare) in dictionar
//daca exista deja cheia in dictionar, inlocuieste valoarea asociata cheii si returneaza vechea valoare
//daca nu exista cheia, adauga perechea si returneaza null
/*
 * Complexitatea functiei este ecvhivalenta cu cea a metodei adauga_rec().
 */
TValoare DO::adauga(TCheie c, TValoare v) {
    TValoare veche = NULL_TVALOARE;
    rad = adauga_rec(rad, std::make_pair(c,v),veche);
    if(veche == NULL_TVALOARE)
        size++;
    return veche;
}

//cauta o cheie si returneaza valoarea asociata (daca dictionarul contine cheia) sau null
/*
 * BC: cheia cautata se afla chiar in nodul radacina. In acest caz complexitatea
 *      este constanta,tetha(1).
 * WC: cheia nu se afla in arbore sau se afla intr un nod frunza. In acest caz
 *      complexitatea este tehta(h), unde h este inaltimea arborelui.
 * AC: in medie, cheia se poate afla intr un nod alfat pe nivelul 0,1,2,...,h
 *      iar probabilitatea sa se afle intr unul din aceste noduri este 1/h, deci
 *      T(n)=h(h+1)/2*h=(h+1)/2 care apartine lui tehta(h).
 * In concluzie, complexitatea generala a functiei este O(h).
 *
 */
TValoare DO::cauta(TCheie c) const {
    auto p = rad;
    while(p != NULL and e[p].first != c){
        if(rel(c, e[p].first))
            p = st[p];
        else
            p = dr[p];
    }
    if(p == NULL)
        return p;
    return e[p].second;
}

//sterge o cheie si returneaza valoarea asociata (daca exista) sau null
/*
 * Complexitatea functiei este echivalenta cu cea a metodei sterge_rec().
 */
TValoare DO::sterge(TCheie c) {
   TValoare veche = NULL_TVALOARE;
   rad = sterge_rec(rad,c,veche);
   if(veche != NULL_TVALOARE)
       size--;
   return veche;
}

//returneaza numarul de perechi (cheie, valoare) din dictionar
/*
 * Complexitatea constanta, Tetha(1)
 */
int DO::dim() const {
    return size;
}

//verifica daca dictionarul e vid
/*
 * Complexitatea constanta, Tetha(1)
 */
bool DO::vid() const {
    return size == 0;
}

/*
 * Complexitatea constanta, Tetha(1)
 */
Iterator DO::iterator() const {
    return  Iterator(*this);
}

/*
 * Complexitatea constanta, Tetha(1)
 */
DO::~DO() {
    delete[] e;
    delete[] st;
    delete[] dr;
}

/*
 * Complexitatea constanta, Tetha(1)
 */
int DO::aloca() {
    int i = primLiber;
    primLiber = st[primLiber];
    return i;
}

/*
 * Complexitatea constanta, Tetha(1)
 */
void DO::dealoca(int i) {
    st[i] = primLiber;
    primLiber = i;
}

/*
 * Complexitatea constanta amortizata, Tetha(1)
 */
int DO::creeazaNod(TElem elem) {
    if(primLiber == -1)
        redimensionare();
    int i = aloca();
    e[i]=elem;
    st[i]=NULL;
    dr[i]=NULL;
    return i;
}

/*
 * Complexitate liniara Tetha(cp).
 */
void DO::redimensionare() {
    auto new_e = new TElem[2*cp];
    auto new_st = new int[2*cp];
    auto new_dr = new int[2*cp];
    for(int i = 0; i < cp; i++){
        new_e[i] = e[i];
        new_st[i] = st[i];
        new_dr[i] = dr[i];
    }
    primLiber = cp;
    for(int i = cp; i < 2*cp-1; i++){
        new_st[i] = i+1;
    }
    new_st[2*cp-1] = NULL;
    cp *= 2;
    delete[] e;
    e = new_e;
    delete[] st;
    st = new_st;
    delete[] dr;
    dr = new_dr;
}

/*
 * Complexitate tetha(h), unde h este inaltimea arborelui
 */
int DO::min_max(int r) {
    auto p = r;
    while(st[p] != NULL)
        p=st[p];
    return p;
}

/*
 * Complexitate tehta(h).
 */
int DO::adauga_rec(int r, TElem elem,TValoare& veche) {
    if(r == NULL)
        r = creeazaNod(elem);
    else{
        if(elem.first == e[r].first){
            veche = e[r].second;
            if(veche == NULL_TVALOARE)//exista posibilitatea ca valoare asociata unei chei sa fie chiar -1
                size--;//iar in cazul in care schimbam valoarea acestei chei, in functia principala size va creste
                        //si de acea este nevoie de un size-- pentru a echilibra situatia
            e[r].second = elem.second;
        }
        else if(rel(elem.first, e[r].first)){
            st[r] = adauga_rec(st[r], elem, veche);
        }
        else{
            dr[r] = adauga_rec(dr[r],elem, veche);
        }
    }
    return r;
}

/*
 * Complexitate tehta(h).
 */
int DO::sterge_rec(int r, TCheie c, TValoare &veche) {
    if(r == NULL)
        return NULL;
    else{
        if(c == e[r].first) {
            veche = e[r].second;
            if(veche == NULL_TVALOARE) //exista posibilitatea ca, cheia sa fie chiar -1
                size--;//si din aceasta cauza nu se va scadea size ul in functia principala
            int temp;
            if (st[r] != NULL and dr[r] != NULL) {
                temp = min_max(dr[r]);
                e[r] = e[temp];
                dr[r] = sterge_rec(dr[r], e[r].first, veche);
                return r;
            } else {
                temp = r;
                int repl;
                if (st[r] == NULL)
                    repl = dr[r];
                else
                    repl = st[r];
                dealoca(temp);
                return repl;
            }
        }
        else if(rel(c, e[r].first)){
            st[r] = sterge_rec(st[r], c,veche);
            return r;
        }
        else  {
            dr[r] = sterge_rec(dr[r], c, veche);
            return r;
        }
    }
}

void DO::inlocuieste(TCheie c, TValoare ValVeche, TValoare ValNoua) {
    auto p = rad;
    while(p != NULL and e[p].first != c){
        if(rel(c, e[p].first))
            p = st[p];
        else
            p = dr[p];
    }
    if(p != NULL and e[p].second == ValVeche)
        e[p].second = ValNoua;
}
