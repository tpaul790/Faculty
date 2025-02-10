//
// Created by Titieni Paul on 21.05.2024.
//

#ifndef LAB5_DO_H
#define LAB5_DO_H

#include <iostream>

typedef int TCheie;
typedef int TValoare;

#define NULL_TVALOARE (-1)
#define NULL (-1)

#include <utility>
typedef std::pair<TCheie, TValoare> TElem;

class Iterator;

typedef bool(*Relatie)(TCheie, TCheie);

class DO {
    friend class Iterator;
private:
    /* aici e reprezentarea */
    Relatie rel;
    int cp,size;
    int rad,primLiber;
    TElem* e;
    int* st;
    int* dr;
    int aloca();
    void dealoca(int i);
    int creeazaNod(TElem e);
    void redimensionare();
public:

    // constructorul implicit al dictionarului
    explicit DO(Relatie r);


    // adauga o pereche (cheie, valoare) in dictionar
    //daca exista deja cheia in dictionar, inlocuieste valoarea asociata cheii si returneaza vechea valoare
    // daca nu exista cheia, adauga perechea si returneaza null: NULL_TVALOARE
    TValoare adauga(TCheie c, TValoare v);

    //cauta o cheie si returneaza valoarea asociata (daca dictionarul contine cheia) sau null: NULL_TVALOARE
    TValoare cauta(TCheie c) const;


    //sterge o cheie si returneaza valoarea asociata (daca exista) sau null: NULL_TVALOARE
    TValoare sterge(TCheie c);

    //returneaza numarul de perechi (cheie, valoare) din dictionar
    int dim() const;

    //verifica daca dictionarul e vid
    bool vid() const;

    // se returneaza iterator pe dictionar
    // iteratorul va returna perechile in ordine dupa relatia de ordine (pe cheie)
    Iterator iterator() const;

    // destructorul dictionarului
    ~DO();

    int min_max(int rad);

    int adauga_rec(int poz_r, TElem elem, TValoare& veche);

    int sterge_rec(int poz_r, TCheie c, TValoare& veche);

    void inlocuieste(TCheie c, TValoare ValVeche, TValoare ValNoua);


    //functii auxiliare vizuale
    void afisare_e(){
        for(int i = 0; i < cp ;i++){
            std::cout<<"("<<e[i].first<<","<<e[i].second<<") ";
        }
    }

    void afisare_st(){
        for(int i = 0; i < cp ;i++){
            std::cout<<st[i]<<' ';
        }
    }

    void afisare_dr(){
        for(int i = 0; i < cp ;i++){
            std::cout<<dr[i]<<' ';
        }
    }

    int get_rad(){
        return rad;
    }

    int get_pL(){
        return primLiber;
    }

};



#endif //LAB5_DO_H
