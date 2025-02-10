//
// Created by Titieni Paul on 24.06.2024.
//

#ifndef PRODUSE_SERVICE_H
#define PRODUSE_SERVICE_H
#include "../Domain/Produs.h"
#include "../Repo/RepoFile.h"
#include "../Validari/validare.h"
#include "../Exception/Exception.h"
#include <map>
#include "../Observer/Observable.h"

class Service : public Observable{
private:
    RepoFile& repo;
    map<string,int> dict;
public:
    Service(RepoFile& repo) : repo{repo} {};

    void add(int id, string nume, string tip, double pret);

    const map<string,int>& get_map();

    const vector<Produs>& get_all(){
        return repo.get_all();
    }
};


#endif //PRODUSE_SERVICE_H
