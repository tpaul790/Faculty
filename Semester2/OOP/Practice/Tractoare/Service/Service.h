//
// Created by Titieni Paul on 26.06.2024.
//

#ifndef TRACTOARE_SERVICE_H
#define TRACTOARE_SERVICE_H
#include "../Domain/Tractor.h"
#include "../Repo/RepoFile.h"
#include "../Exception.h"
class Service {
private:
    RepoFile& repo;
public:
    Service(RepoFile& r) : repo{r} {};

    void add(int id, const string& denumire, const string& tip, int nrRoti);

    void dec_nrRoti(int id);

    const vector<Tractor>& get_all(){
        return repo.get_all();
    }
};


#endif //TRACTOARE_SERVICE_H
