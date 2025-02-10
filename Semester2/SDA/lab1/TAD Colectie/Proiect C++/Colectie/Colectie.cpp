#include "Colectie.h"
#include "IteratorColectie.h"

using namespace std;

void Colectie::redimensionare_dist(){
    TElem* new_list=new TElem[10*cp_dist];
    for(int i=0;i<lg_dist;i++)
        new_list[i]=lista_dist[i];
    cp_dist*=10;
    delete[] lista_dist;
    lista_dist=new_list;
}

void Colectie::redimensionare_poz() {
    TElem* new_list=new TElem[10*cp_poz];
    for(int i=0;i<lg_poz;i++)
        new_list[i]=lista_poz[i];
    cp_poz*=10;
    delete[] lista_poz;
    lista_poz=new_list;
}

Colectie::Colectie() {
    this->cp_dist=10;
    this->lg_dist=0;
    lista_dist=new TElem[cp_dist];

    this->cp_poz=10;
    this->lg_poz=0;
    lista_poz=new int[cp_poz];
}


void Colectie::adauga(TElem elem) {
    IteratorColectie i=iterator();
    bool ok=false;
    while(i.valid() and !ok) {
        if (i.element() == elem)
            ok = true;
        i.urmator();
    }
    if(ok){
        if(lg_poz==cp_poz)
            redimensionare_poz();
        lista_poz[lg_poz++]=lista_poz[i.curent-1];//curent-1 pentru ca in while,cand ok se face true si iteratorul va mai creste o data
    }
    else{
        if(lg_poz==cp_poz)
            redimensionare_poz();
        if(lg_dist==cp_dist)
            redimensionare_dist();
        lista_poz[lg_poz++]=lg_dist;
        lista_dist[lg_dist++]=elem;
    }
}


bool Colectie::sterge(TElem elem) {
    int poz=-1,cnt=0;
    IteratorColectie it=iterator();
    while(it.valid()) {
        if (it.element() == elem)
            poz = it.curent, cnt++;
        it.urmator();
    }
    if(poz==-1)
        return false;
    if(cnt>1){
        for(int i=poz+1;i<lg_poz;i++)
            lista_poz[i-1]=lista_poz[i];
        lg_poz--;
    }
    else{
        int poz_dist=lista_poz[poz];
        for(int i=0;i<lg_poz;i++)
            if(lista_poz[i]>=poz_dist+1 and lista_poz[i]<lg_dist)
                lista_poz[i]--;
        /*
         * Toate elementele din vectorul cu valori distincte care urmeaza dupa valoarea
         * care va fi stearsa vor avea in vectorul de pozitii, pozitia decrementata cu o
         *unitate iar pozitiile care trebuie decrementate sunt [poz elementului sters+1,lg_dist]
        */
        for(int i=poz_dist+1;i<lg_dist;i++)
            lista_dist[i-1]=lista_dist[i];
        lg_dist--;

        for(int i=poz+1;i<lg_poz;i++)
            lista_poz[i-1]=lista_poz[i];
        lg_poz--;
    }
    return true;
}

int Colectie::stergeAparitii(TElem e){
    int poz=-1,cnt=0;
    for(int i=0;i<lg_poz;i++)
        if(lista_dist[lista_poz[i]]==e)
            cnt++,poz=i;
    if(cnt>0){
        int val=lista_poz[poz];//pozitia elementului care va fi sters
        //sterg toate aparitiile pozitiilor sale din vectorul de pozitii
        for(int i=0;i<lg_poz;i++) {
            if (lista_poz[i] == val) {
                for (int j = i + 1; j < lg_poz; j++)
                    lista_poz[j - 1] = lista_poz[j];
                i--;
                lg_poz--;
            }
        }
        for(int i=val+1;i<lg_dist;i++)
            lista_dist[i-1]=lista_dist[i];
        lg_dist--;
        //il sterg din vectorul cu valori distincte
    }
    return cnt;
}


bool Colectie::cauta(TElem elem) const {
    IteratorColectie i=iterator();
    while(i.valid()) {
        if (i.element() == elem)
            return true;
        i.urmator();
    }
    return false;
}

int Colectie::nrAparitii(TElem elem) const {
    IteratorColectie i=iterator();
    int cnt=0;
    while(i.valid()) {
        if (i.element() == elem)
            cnt++;
        i.urmator();
    }
    return cnt;
}

int Colectie::dim() const {
    return lg_poz;
}


bool Colectie::vida() const {
    return (lg_poz==0 and lg_dist==0);
}

IteratorColectie Colectie::iterator() const {
    return  IteratorColectie(*this);
}

Colectie::~Colectie() {
    delete[] lista_dist;
    delete[] lista_poz;
}


