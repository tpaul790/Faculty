//
// Created by Titieni Paul on 09.05.2024.
//

#ifndef TEST1_PROCESORSERVICE_H
#define TEST1_PROCESORSERVICE_H

#include "../Repo/ProcesorInFileRepo.h"

class ProcesorService {
private:
    ProcesorInFileRepo repo;
public:
    ProcesorService(ProcesorInFileRepo repo) : repo(std::move(repo)){};

    Procesor cauta(const string& nume){
        return repo.cauta(nume);
    }

    const vector<Procesor>& get_all(){
        return repo.get_all();
    }
};


#endif //TEST1_PROCESORSERVICE_H
