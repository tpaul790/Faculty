#ifndef LAB2_4_DOMAIN_H
#define LAB2_4_DOMAIN_H

typedef struct{
    int id;
    char* tip;
    char* producator;
    char* model;
    int pret;
    int cantitate;
}Produs;

/*
    Functia creaza un produs cu campurile date
    parametrii:
        -id: id-ul produsului,id>0
        -tip: tipul produsului(tractor,telefon,laptop,televizor)
        -producator: producatorul produsului(asus,fiat,apple)
        -model: modelul produsului(orice sir de caractere)
        -pret: pretul produsului,pret>=0
        -cantitate: cantitatea produsului,cantitate>0
    return: functia returneaza produsul creat
*/
Produs* creare_produs(int, char* ,char* ,char* ,int ,int);

/*
    Functia elibereaza memoria alocata pe heap de catre un pointer al unui produs dat
    parametrii:
        -p: pointer catre produsul pe care dorim sa l eliberam,p!=NULL
    return:
*/
void distruge_produs(Produs* p);

/*
    Functia returneaza id ul unui produs dat:
        -p: produsul caruia ii vrem id ul
    return: id ul produsului dat,>0
 */
int get_id(Produs*);

/*
    Functia returneaza tipul unui produs dat:
        -p: produsul caruia ii vrem tipul
    return: tipul produsului dat(tractor,telefon,televizor,laptop)
 */
char* get_tip(Produs*);

/*
    Functia returneaza producatorul unui produs dat:
        -p: produsul caruia ii vrem producatorul
    return: producatorul produsului dat(fiat,apple,assus,hp)
 */
char* get_producator(Produs*);

/*
    Functia returneaza modelul unui produs dat:
        -p: produsul caruia ii vrem modelul
    return: modelul produsului dat(orice sir de caractere)
 */
char* get_model(Produs*);

/*
    Functia returneaza cantitatea unui produs dat:
        -p: produsul caruia ii vrem cantitatea
    return: cantitatea produsului dat,>0
 */
int get_pret(Produs*);

/*
    Functia returneaza cantitatea unui produs dat:
        -p: produsul caruia ii vrem cantitatea
    return: cantitatea produsului dat,>0
 */
int get_cantitate(Produs*);

/*
    Functia seteaza pretul unui produs dat cu o alta pret dat
    parametrii:
        -p: produsul caruia ii va fi setat pretul
        -new_pret: noua cantitate a produsului:new_pret>=0
 */
void set_pret(Produs* p, int new_pret);

/*
    Functia seteaza cantitatea unui produs dat cu o alta cantitate data
    parametrii:
        -p: produsul caruia ii va fi setata cantitatea
        -new_cant: noua cantitate a produsului:new_cant>0
 */
void set_cantitate(Produs* p,int new_cant);

#endif //LAB2_4_DOMAIN_H
