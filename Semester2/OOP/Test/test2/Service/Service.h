//
// Created by Titieni Paul on 17.05.2024.
//

#ifndef TEST2_SERVICE_H
#define TEST2_SERVICE_H

#include "../Domain/Apartament.h"
#include "../Repo/RepoFile.h"

class Service {
private:
    RepoFile& repo;
public:
    Service(RepoFile& repo) : repo(repo){};

    void sterge(string suprafata, string strada, string pret);

    vector<Apartament> filtrare_pret(const string& pret_min,const string& pret_max);

    vector<Apartament> filtrare_suprafata(const string& sup_min,const string& sup_max);

    const vector<Apartament>& get_all(){
        return repo.get_all();
    }
};


#endif //TEST2_SERVICE_H
