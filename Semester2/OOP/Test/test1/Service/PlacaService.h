//
// Created by Titieni Paul on 09.05.2024.
//

#ifndef TEST1_PLACASERVICE_H
#define TEST1_PLACASERVICE_H
#include <utility>

#include "../Repo/PlacaInFileRepo.h"

class PlacaService {
private:
    PlacaInFileRepo repo;
public:
    PlacaService(PlacaInFileRepo repo) : repo(std::move(repo)){};

    void add(string nume,int nrT,int pret){
        PlacaDeBaza p{nume,nrT,pret};
        repo.store(p);
    }

    PlacaDeBaza cauta(string nume){
        return repo.cauta(nume);
    }

    vector<PlacaDeBaza> filtrare_sP(int sP);

    const vector<PlacaDeBaza>& get_all(){
        return repo.get_all();
    }
};


#endif //TEST1_PLACASERVICE_H
