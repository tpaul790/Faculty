
#ifndef LAB1_COLECTIE_H
#define LAB1_COLECTIE_H


#define NULL_TELEM -1
typedef int TElem;

class IteratorColectie;

class Colectie
{
    friend class IteratorColectie;

private:
    /* aici e reprezentarea */
    TElem* lista_dist;
    int cp_dist;
    int lg_dist;
    void redimensionare_dist();


    int* lista_poz;
    int cp_poz;
    int lg_poz;
    void redimensionare_poz();

public:
    //constructorul implicit
    Colectie();

    //adauga un element in colectie
    void adauga(TElem e);

    //sterge o aparitie a unui element din colectie
    //returneaza adevarat daca s-a putut sterge
    bool sterge(TElem e);

    int stergeAparitii(TElem e);

    //verifica daca un element se afla in colectie
    bool cauta(TElem elem) const;

    //returneaza numar de aparitii ale unui element in colectie
    int nrAparitii(TElem elem) const;


    //intoarce numarul de elemente din colectie;
    int dim() const;

    //verifica daca colectia e vida;
    bool vida() const;

    //returneaza un iterator pe colectie
    IteratorColectie iterator() const;

    // destructorul colectiei
    ~Colectie();

};



#endif //LAB1_COLECTIE_H
