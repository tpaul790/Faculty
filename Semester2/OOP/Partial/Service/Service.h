//
// Created by Titieni Paul on 19.05.2024.
//

#ifndef PARTIAL_SERVICE_H
#define PARTIAL_SERVICE_H

#include "../Repo/RepoFile.h"


class Service {
private:
    RepoFile& repo;
public:
    /*
     * constructor
     */
    Service(RepoFile& r) : repo(r){};

    /*
     * Functia de sortare dupa nume
     */
    vector<Elev> sortare_nume();

    /*
     * Functia returneaza un vector cu toti elevii inscrisi la un atelier dat
     * param at: atelierul dupa care se cauta
     */
    vector<Elev> elevi_inscrisi(const string& at);

    /*
     * Functia incarca toti elevii dintr un vector dat
     */
    void incarca(const vector<Elev>& nou) {
        repo.incarca(nou);
    }

    /*
     * returneaza lista de elevi
     */
    const vector<Elev>& get_all(){
        return repo.get_all();
    }
};


#endif //PARTIAL_SERVICE_H
