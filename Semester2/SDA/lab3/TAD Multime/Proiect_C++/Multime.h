//
// Created by Titieni Paul on 18.04.2024.
//

#ifndef LAB3_MULTIME_H
#define LAB3_MULTIME_H

#define NULL_TELEM -1
typedef int TElem;

class IteratorMultime;

class Multime {
    friend class IteratorMultime;

private:
    int cp;
    int prim,primLiber;
    TElem* e;
    int* urm;
    int size;
    int aloca();
    void dealoca(int i);
    int creeazaNod(TElem elem);
    void redimensionare();

public:
    //constructorul implicit
    Multime();

    //adauga un element in multime
    //returneaza adevarat daca elementul a fost adaugat (nu exista deja in multime)
    bool adauga(TElem e);

    //sterge un element din multime
    //returneaza adevarat daca elementul a existat si a fost sters
    bool sterge(TElem e);

    //verifica daca un element se afla in multime
    bool cauta(TElem elem) const;


    //intoarce numarul de elemente din multime;
    int dim() const;

    //verifica daca multimea e vida;
    bool vida() const;

    //returneaza un iterator pe multime
    IteratorMultime iterator() const;

    int diferentaMaxMin() const;

//    //astea s in plus
//    int get_cp();
//
//    void see_all_e();
//
//    void see_all_urm();

    // destructorul multimii
    ~Multime();
};


#endif //LAB3_MULTIME_H
