#include <stdlib.h>
#include <string.h>
#include "domain.h"

Produs* creare_produs(int id, char* tip,char* producator,char* model,int pret,int cantitate){

    Produs* p=(Produs*)malloc(sizeof(Produs));

    p->tip=(char*)malloc(21*sizeof(char));
    strcpy(p->tip,tip);

    p->producator=(char*)malloc(21*sizeof(char));
    strcpy(p->producator,producator);

    p->model=(char*)malloc(21*sizeof(char));
    strcpy(p->model,model);

    p->id=id;
    p->pret=pret;
    p->cantitate=cantitate;
    return p;
}

void distruge_produs(Produs* p){
    free(p->model);
    free(p->producator);
    free(p->tip);
    free(p);
}

int get_id(Produs* p){
    return p->id;
}

char* get_tip(Produs* p){
    return p->tip;
}

char* get_producator(Produs* p){
    return p->producator;
}

char* get_model(Produs* p){
    return p->model;
}

int get_pret(Produs* p){
    return p->pret;
}

int get_cantitate(Produs* p){
    return p->cantitate;
}

void set_pret(Produs* p, int new_pret){
    p->pret=new_pret;
}

void set_cantitate(Produs* p,int new_cant){
    p->cantitate=new_cant;
}