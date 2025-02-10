#include "MD.h"
#include "IteratorMD.h"
#include <exception>
#include <iostream>
#include <cmath>

using namespace std;

int hashCode(TCheie c){
    return abs(c);
}

/*
 * Complexitate timp liniara,Tetha(m);
 */
MD::MD() {
    m=MAX;
    size=0;
    for(int i=0;i<m;i++)
        e[i]=NIL;
}

// Folosim conventia m=numarul de elemente din tabela

/*
 * BC: prima pozitie pe care o returneaza functia de dispersie este liber,
 *     in acest caz complexitatea este constanta,Tetha(1);
 * WC: functia de dispersie returneaza o pozitie libera doar cand i=m-1,
 *     in acest caz complexitatea este liniara deoarece se parcurge toata tabela
 * AC: functia de dispersie poate returna o pozitie valida dupa 1,2,3,...,m pasi
 *     iar probabilitea ca functia sa returneze o pozitie este 1/m => complexitate
 *     liniara,Tetha(m);
 * In concluzie complexitatea medie a functiei este O(n);
 */
void MD::adauga(TCheie c, TValoare v) {
    int i=0;
    bool gasit = false;
    do{
        int j=d(c,i);
        if(e[j] == NIL or e[j] == STERS){
            size++;
            e[j] = std::make_pair(c,v);
            gasit = true;
        }else
            i++;
    }while(!gasit and i<m);
}

/*
 * BC: elementul care dorim a fi sters se afla pe prima pozitie pe care o returneaza
 *     functia de dispersie,in acest caz complexitatea este constanta,tetha(1);
 * WC: elementul pe care dorim sa l stergem nu exista in tabela sau se afla pe ultima
 *     pozitie returnata de functia de dispersie,in acest caz complexitatea este liniara,
 *     tetha(m).
 * AC: elementul pe care dorim sa l stergem se poate afla pe pozitia 1,2,3,..,m iar probabilitatea
 *     sa se afle pe una dintre aceste pozitii este 1/m,in acest caz complexitatea este liniara,tetha(m);
 * In concluzie complexitatea medie a functiei este O(n);
 */
bool MD::sterge(TCheie c, TValoare v) {
    if(size==0)
        return false;
    int i=0;
    bool sters=false;
    do {
        int j=d(c,i);
        if(e[j]!=NIL and e[j].second==v and e[j].first==c) {
            e[j]=STERS;
            sters=true;
            size--;
        }
        else if(e[j]==NIL)
            break;
        else
            i++;
    }while(!sters and i<m);
    return sters;
}

/*
 * BC: elementul pe care il caut se afla pe prima pozitie returnata de functia de dispersie
 *     in acest caz complexitatea este constanta,tetha(1)
 * WC: elementul nu se afla in tabela sau se afla pe ultima pozitie returnata de functia de dispersie,
 *     in acest caz complexitatea este liniara, tetha(n);
 * AC: in medie elementul se poate afla pe poz 1,2,3,...,m (considerand ca 1,2,3,...,m sunt pozitii returnate
 *     de fucntia de dispersie, chiar daca dispersia patratica nu le afiseaza in aceasta ordine) iar probabilitatea
 *     ca elementul sa se afle pe o pozitie valida este 1/m => complexitate liniara O(n).
 * In concluzie, complexitatea generala a functiei este O(n).
 */
vector<TValoare> MD::cauta(TCheie c) const {
    vector<TValoare> val;
    int i=0;
    bool stop=false;
    do{
        int j=d(c,i);
        if(e[j]!=NIL and e[j].first==c)
            val.push_back(e[j].second);
        else
            if(e[j]==NIL)
                stop = true;
        i++;
    }while(i<m and !stop);
    return val;
}

/*
 * Complexitate timp constanta
 */
int MD::dim() const {
    return size;
}

/*
 * Complexitate timp constanta
 */
bool MD::vid() const {
    return size==0;
}

/*
 * Complexitate timp constanta
 */
IteratorMD MD::iterator() const {
    return IteratorMD(*this);
}


MD::~MD() {
    /*nu avem memorie de eliberat*/
}

/*
 * Complexitate timp constanta
 */
int MD::d(TCheie c, int i) const {
    return(int)(hashCode(c)%m+0.5*i+0.5*i*i)%m;
}

void MD::goleste() {
    if(size!=0) {
        auto it = iterator();
        while (it.valid()) {
            auto c = it.element().first;
            auto v = it.element().second;
            sterge(c, v);
            it.urmator();
        }
    }
}
