//
// Created by Titieni Paul on 25.06.2024.
//

#ifndef MASINI_MASINA_H
#define MASINI_MASINA_H
#include <string>
#include <utility>
using namespace std;

class Masina {
private:
    int an;
    string id,tip,denumire;
public:
    Masina(string i,string d,int a,string t) : id{std::move(i)}, denumire{std::move(d)}, tip{std::move(t)}, an{a}{};

    const string& get_id() const{
        /*
         * Functia returneaza numarul de inmatriculare al masinii
         */
        return id;
    }

    const string& get_tip() const{
        /*
         * Functia returneaza tipul al masinii
         */
        return tip;
    }

    const string& get_denumire() const{
        /*
         * Functia returneaza denumirea al masinii
         */
        return denumire;
    }

    const int& get_an() const{
        /*
         * Functia returneaza anul al masinii
         */
        return an;
    }

    void set_an(int nou){
        /*
         * Functia seteaza anul masinii la valoarea parametrului 'nou'
         */
        an = nou;
    }

    bool operator==(const Masina& ot){
        return id == ot.get_id();
    }
};


#endif //MASINI_MASINA_H
