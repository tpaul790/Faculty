//
// Created by Titieni Paul on 19.05.2024.
//

#ifndef TEST3_TRACTOR_H
#define TEST3_TRACTOR_H

#include <string>
#include <utility>

using namespace std;

class Tractor {
private:
    string nume,producator;
    int an,pret;
public:
    Tractor(string nume, string prod, int an, int pret) : nume(std::move(nume)), producator(std::move(prod)),an(an),pret(pret){};

    bool operator==(const Tractor& ot){
        return nume==ot.nume and producator==ot.producator and an==ot.an and pret==ot.pret;
    }

    const string& get_nume() const{
        return nume;
    }

    const string& get_producator() const{
        return producator;
    }

    int get_an() const{
        return an;
    }

    int get_pret() const{
        return pret;
    }

    void set_pret(int new_pret){
        pret=new_pret;
    }
};


#endif //TEST3_TRACTOR_H
