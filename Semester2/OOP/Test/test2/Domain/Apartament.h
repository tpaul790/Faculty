//
// Created by Titieni Paul on 17.05.2024.
//

#ifndef TEST2_APARTAMENT_H
#define TEST2_APARTAMENT_H

#include <string>
#include <utility>

using namespace std;

class Apartament {
private:
    string suprafata;
    string strada;
    string pret;
public:
    Apartament(string sup,string str,string pr) : suprafata(std::move(sup)),strada(std::move(str)),pret(std::move(pr)){};

    Apartament() = default;

    Apartament(const Apartament &ap) {
        suprafata=ap.suprafata;
        pret=ap.pret;
        strada=ap.strada;
    }

    bool operator==(const Apartament &ap) {
        return suprafata==ap.suprafata and pret==ap.pret and strada==ap.strada;
    }

    string get_suprafata(){
        return suprafata;
    }

    string get_strada(){
        return strada;
    }

    string get_pret(){
        return pret;
    }

};


#endif //TEST2_APARTAMENT_H
