//
// Created by Titieni Paul on 24.06.2024.
//

#ifndef PRODUSE_PRODUS_H
#define PRODUSE_PRODUS_H

#include <string>
using namespace std;

class Produs {
private:
    int id;
    string nume,tip;
    double pret;
public:
    Produs(int id,string n,string t,double p) : id{id}, nume{n}, tip{t}, pret{p} {};

    int get_id() const{
        /*
         * Functia returneaza id ul produsului curent
         */
        return id;
    }

    string get_nume() const{
        /*
         * Functia returneaza numele ul produsului curent
         */
        return nume;
    }

    string get_tip() const{
        /*
         * Functia returneaza tipul ul produsului curent
         */
        return tip;
    }

    double get_pret() const{
        /*
         * Functia returneaza pretul ul produsului curent
         */
        return pret;
    }
};


#endif //PRODUSE_PRODUS_H
