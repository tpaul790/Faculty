#ifndef LAB2_4_STORE_H
#define LAB2_4_STORE_H

#include "../domain/domain.h"

typedef struct{
    Produs** lista_produse;
    int lg;
    int cp;
}Lista;


/*
    Funtctia creaza o lista vida
    return: functia returneaza lista creata
*/
Lista creare_lista();

/*
    Functia elibereaza memoria alocata pe heap de catre un pointer al unei liste date
    parametrii:
        -l: pointer catre lista pe care dorim sa o eliberam
    return:
 */
void distruge_lista(Lista* l);

/*
    Functia redimensioneaza o lista data
    parametrii:
        l-lista care dorim sa fie redimensionata
    return: o lista noua care reprezinta prima lista redimensionata
 */
void redimensionare(Lista* l);

/*
    Functia cauta produsul cu id-ul dat intr o lista data
    parametrii:
        -list: adresa listei in care se cauta
        -id: id ul produsul care se cauta,id>0
    return: functia returneaza adresa produsului cu id ul dat daca acesta exista in lista
            sau pointerul null in caz contrar
*/
Produs* find(Lista* list, int id);

/*
    Functia adauga un produs dat intr o lista data
    parametrii:
        lista: adresa listei in care se va adauga produsul
        p: produsul care se va adauga
    return:
*/
void adaugare_produs(Lista* lista,Produs* p);

#endif //LAB2_4_STORE_H
