//
// Created by Titieni Paul on 20.05.2024.
//

#ifndef PARTIAL_ELEV_H
#define PARTIAL_ELEV_H

#include <string>
#include <utility>
#include <vector>

using namespace std;

class Elev {
private:
    int nrMatricol;
    string scoala;
    string nume;
    vector<string> ateliere;
public:
    /*
     * Constructorul
     */
    Elev(int nrM,string nume,string sc,vector<string> atel) : nrMatricol(nrM), scoala(std::move(sc)),nume(std::move(nume)),ateliere(std::move(atel)){};

    /*
     * Functia returneaza numarul matricol al elevului
     */
    int get_nrMatricol() const{
        return nrMatricol;
    }

    /*
     * Functia returneaza scoala elevului
     */
    const string& get_scoala(){
        return scoala;
    }

    /*
     * Functia returneaza numele elevului
     */
    const string& get_nume() {
        return nume;
    }

    /*
     * Functia returneaza vectorul de ateliere al elevului
     */
    const vector<string>& get_ateliere(){
        return ateliere;
    }

    /*
     * Functia adauga un atelier dat elevului
     * param at: atelierul care se adauga
     */
    void add_atelier(const string& at){
        ateliere.push_back(at);
    }

};


#endif //PARTIAL_ELEV_H
