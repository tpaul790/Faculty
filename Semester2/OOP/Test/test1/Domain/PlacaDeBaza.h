//
// Created by Titieni Paul on 09.05.2024.
//

#ifndef TEST1_PLACADEBAZA_H
#define TEST1_PLACADEBAZA_H
#include <string>

using namespace std;

class PlacaDeBaza {
private:
    string nume;
    int socluProcesor,pret;
public:
    PlacaDeBaza(string nume,int sP,int p) : nume(std::move(nume)),socluProcesor(sP),pret(p){};

    string get_nume() const{
        return nume;
    }

    int get_socluProcesor() const{
        return socluProcesor;
    }

    int get_pret() const{
        return pret;
    }
};


#endif //TEST1_PLACADEBAZA_H
