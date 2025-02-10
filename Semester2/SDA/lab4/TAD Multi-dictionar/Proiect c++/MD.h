//
// Created by Titieni Paul on 02.05.2024.
//

#ifndef LAB4_MD_H
#define LAB4_MD_H

#include<vector>
#include<utility>
#include<iostream>
#define MAX 262144
#define NIL std::make_pair(-30001,-30001)
#define STERS std::make_pair(-30002,-30002)
using namespace std;

typedef int TCheie;
typedef int TValoare;

typedef std::pair<TCheie, TValoare> TElem;


class IteratorMD;

class MD
{
    friend class IteratorMD;

private:
    /* aici e reprezentarea */
    int m,size;
    TElem e[MAX];
    int d(TCheie c, int i) const;

public:
    // constructorul implicit al MultiDictionarului
    MD();

    // adauga o pereche (cheie, valoare) in MD
    void adauga(TCheie c, TValoare v);

    //cauta o cheie si returneaza vectorul de valori asociate
    vector<TValoare> cauta(TCheie c) const;

    //sterge o cheie si o valoare
    //returneaza adevarat daca s-a gasit cheia si valoarea de sters
    bool sterge(TCheie c, TValoare v);

    //returneaza numarul de perechi (cheie, valoare) din MD
    int dim() const;

    //verifica daca MultiDictionarul e vid
    bool vid() const;

    // se returneaza iterator pe MD
    IteratorMD iterator() const;

    void goleste();

    // destructorul MultiDictionarului
    ~MD();

    //functie auxiliara pentru a vedea multidictionarul
    void get_md(){
        for(int i=0;i<1024;i++)
            if(e[i]==NIL)
                std::cout<<"NIL"<<' ';
            else if(e[i]==STERS)
                std::cout<<"STERS"<<' ';
            else std::cout<<"("<<e[i].first<<","<<e[i].second<<")"<<' ';
    }
};

#endif //LAB4_MD_H
