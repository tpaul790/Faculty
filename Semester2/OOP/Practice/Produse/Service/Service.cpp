//
// Created by Titieni Paul on 24.06.2024.
//

#include "Service.h"

void Service::add(int id, string nume, string tip, double pret) {
    Produs p{id,nume,tip,pret};
    auto ers = validare(p);
    if(!ers.empty())
        throw Exception{ers};
    repo.store(p);
    notify();
}

const map<string,int>& Service::get_map() {
    for(const auto& p : repo.get_all()) {
        int cnt=0;
        for(const auto& e : p.get_nume())
            if (e == 'a' or e == 'e' or e == 'i' or e == 'o' or e == 'u' or e == 'A' or e == 'E' or e == 'I' or e == 'O' or e == 'U')
                cnt++;
        dict[p.get_nume()]=cnt;
    }
    return dict;
}