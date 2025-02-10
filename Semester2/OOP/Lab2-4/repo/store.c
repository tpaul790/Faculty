#include <string.h>
#include <stdlib.h>
#include "../domain/domain.h"
#include "store.h"

Lista creare_lista(){
    Lista l;

    l.lista_produse=(Produs**) malloc(2*sizeof(Produs*));

    l.lg=0;
    l.cp=2;

    return l;
}

void distruge_lista(Lista* l){
    for(int i=0;i<l->lg;i++)
        distruge_produs(l->lista_produse[i]);
    free(l->lista_produse);
}

void redimensionare(Lista* l){
    Produs** l_prods=l->lista_produse;

    l->lista_produse=(Produs**) malloc(2*l->cp*sizeof(Produs*));
    l->cp*=2;

    for(int i=0;i<l->lg;i++)
        l->lista_produse[i]=l_prods[i];

    free(l_prods);
}

Produs* find(Lista* list, int id) {
    Produs* p = NULL;
    for (int i = 0; i < list->lg; i++)
        if (get_id(list->lista_produse[i]) == id)
            p = list->lista_produse[i];
    return p;
}

void adaugare_produs(Lista* lista,Produs* p){
    int id=get_id(p),cant=get_cantitate(p);
    Produs* p_local=find(lista,id);
    if(p_local!=NULL){
        int new_cant=p_local->cantitate+cant;
        set_cantitate(p_local,new_cant);
    }
    else{
        if(lista->lg==lista->cp)
            redimensionare(lista);
        lista->lista_produse[lista->lg]=p;
        lista->lg++;
    }
}
