//
// Created by Titieni Paul on 19.05.2024.
//

#ifndef TEST3_SERVICE_H
#define TEST3_SERVICE_H

#include "../Repo/RepoFile.h"


class Service {
private:
    RepoFile& repo;
public:
    Service(RepoFile& repo) : repo(repo){};

    void store(const string& nume,const string& prod, int an, int pret){
        Tractor tr{nume,prod,an,pret};
        repo.store(tr);
    }

    void sterge(const string& nume,const string& prod, int an, int pret);

    void modifica(const string& nume, const string& prod, int an, int pret, int new_pret);

    vector<Tractor> filtru_producator(const string& prod);

    void sortare_pret();

    const vector<Tractor>& get_all(){
        return repo.get_all();
    }
};


#endif //TEST3_SERVICE_H
