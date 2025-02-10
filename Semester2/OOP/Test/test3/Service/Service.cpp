//
// Created by Titieni Paul on 19.05.2024.
//

#include "Service.h"

#include <utility>

void Service::sterge(const string& nume, const string& prod, int an, int pret) {
    Tractor t{nume,prod,an,pret};
    repo.sterge(t);
}

void Service::modifica(const std::string &nume, const std::string &prod, int an, int pret, int new_pret) {
    Tractor t{nume,prod,an,pret};
    repo.modifica_pret(t,new_pret);
}

vector<Tractor> Service::filtru_producator(const string &prod) {
    vector<Tractor> filtrate;
    for(const auto& tr : get_all()){
        if(tr.get_producator()==prod)
            filtrate.push_back(tr);
    }
    return filtrate;
}

void Service::sortare_pret() {
    repo.sortare_pret();

}
