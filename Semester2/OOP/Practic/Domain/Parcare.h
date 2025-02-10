//
// Created by Titieni Paul on 27.06.2024.
//

#ifndef PRACTIC_PARCARE_H
#define PRACTIC_PARCARE_H
#include <string>

using namespace std;

class Parcare {
private:
    string adresa,stare;
    int linii,coloane;
public:
    Parcare(string ad, int l, int c, string st) : adresa{std::move(ad)}, linii{l},coloane{c},stare{std::move(st)} {};

    int get_linii() const{
        /*
         * Functia returneaza numarul de linii al parcarii
         */
        return linii;
    }

    int get_coloane() const{
        /*
         * Functia returneaza numarul de coloane al parcarii
         */
        return coloane;
    }

    string get_adresa() const{
        /*
         * Functia returneaza adresa parcarii
         */
        return adresa;
    }

    string get_stare() const{
        /*
         * Functia returneaza starea parcarii
         */
        return stare;
    }

    void set_coloane(int nou) {
        /*
         * Functia seteaza numarul de coloane ale parcarii curente cu nou
         */
        coloane = nou;
    }

    void set_linii(int nou){
        /*
         * Functia seteaza numarul de linii ale parcarii curente cu nou
         */
        linii=nou;
    }

    void set_stare(string nou){
        /*
         * Functia seteaza starea parcarii curente cu nou
         */
        stare=nou;
    }

    bool operator==(const Parcare& ot){
        /*
         * Operatorul = returneaza true daca doua parcari au aceeasi adresa
         */
        return ot.get_adresa()==adresa;
    }

};


#endif //PRACTIC_PARCARE_H
