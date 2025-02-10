//
// Created by Titieni Paul on 26.06.2024.
//

#ifndef TRACTOARE_TRACTOR_H
#define TRACTOARE_TRACTOR_H
#include <string>
using namespace std;

class Tractor {
private:
    int id,nrRoti;
    string denumire,tip;
public:
    Tractor(int i,string d,string t,int nr) : id{i}, denumire{d}, tip{t}, nrRoti{nr} {};

    int get_id() const{
        return id;
    }

    string get_denumire() const{
        return denumire;
    }

    string get_tip() const{
        return tip;
    }

    int get_nrRoti() const{
        return nrRoti;
    }

    void set_nrRoti(int nou){
        nrRoti=nou;
    }
};


#endif //TRACTOARE_TRACTOR_H
