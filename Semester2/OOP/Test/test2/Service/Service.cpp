//
// Created by Titieni Paul on 17.05.2024.
//

#include "Service.h"

#include <utility>

void Service::sterge(string suprafata, string strada, string pret) {
    Apartament a{std::move(suprafata),std::move(strada),std::move(pret)};
    repo.sterge(a);
}

vector<Apartament> Service::filtrare_pret(const std::string& pret_min, const std::string& pret_max) {
    vector<Apartament> filtrate;
    for(auto ap : get_all()){
        if(ap.get_pret().size()==pret_max.size() and ap.get_pret().size()==pret_min.size()) {
            if (ap.get_pret() >= pret_min and ap.get_pret() <= pret_max)
                filtrate.push_back(ap);
        }
        else if(ap.get_pret().size()<pret_max.size() and ap.get_pret().size()==pret_min.size()) {
            if (ap.get_pret() >= pret_min)
                filtrate.push_back(ap);
        }
        else if(ap.get_pret().size()==pret_max.size() and ap.get_pret().size()>pret_min.size()){
            if (ap.get_pret() <= pret_max)
                filtrate.push_back(ap);
        }
        else if(ap.get_pret().size()<pret_max.size() and ap.get_pret().size()>pret_min.size()) {
            filtrate.push_back(ap);
        }
    }
    return filtrate;
}

vector<Apartament> Service::filtrare_suprafata(const std::string& sup_min, const std::string& sup_max) {
    vector<Apartament> filtrate;
    for(auto ap : get_all()){
        if(ap.get_suprafata().size()==sup_max.size() and ap.get_suprafata().size()==sup_min.size()) {
            if (ap.get_suprafata() >= sup_min and ap.get_suprafata() <= sup_max)
                filtrate.push_back(ap);
        }
        else if(ap.get_suprafata().size()<sup_max.size() and ap.get_suprafata().size()==sup_min.size()) {
            if (ap.get_suprafata() >= sup_min)
                filtrate.push_back(ap);
        }
        else if(ap.get_suprafata().size()==sup_max.size() and ap.get_suprafata().size()>sup_min.size()){
            if (ap.get_suprafata() <= sup_max)
                filtrate.push_back(ap);
        }
        else if(ap.get_suprafata().size()<sup_max.size() and ap.get_suprafata().size()>sup_min.size()) {
                filtrate.push_back(ap);
        }
    }
    return filtrate;
}