//
// Created by Titieni Paul on 23.06.2024.
//

#include "Service.h"

#include <utility>

void Service::modifica(int id, string nou_t,int nou_r){
    repo.modifica(id,std::move(nou_t),nou_r);
}

void Service::sterge(int id) {
    repo.sterge(id);
}

int Service::get_nr_rank(int rank) {
    int cnt = 0;
    for(const auto& m : repo.get_all())
        if(m.get_rank()==rank)
            cnt++;
    return cnt;
}