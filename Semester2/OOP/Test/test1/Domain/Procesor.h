//
// Created by Titieni Paul on 09.05.2024.
//

#ifndef TEST1_PROCESOR_H
#define TEST1_PROCESOR_H
#include <string>
#include <utility>

using namespace std;

class Procesor {
private:
    string nume;
    int numarThreduri,socluProcesor,pret;
public:
    Procesor(string nume,int nrT,int sP,int p) : nume(std::move(nume)),numarThreduri(nrT),socluProcesor(sP),pret(p){};

    string get_nume(){
        return nume;
    }

    int get_nrTreduri(){
        return numarThreduri;
    }

    int get_socluProcesor(){
        return socluProcesor;
    }

    int get_pret(){
        return pret;
    }
};


#endif //TEST1_PROCESOR_H
