//
// Created by Titieni Paul on 19.05.2024.
//

#include "Service.h"

bool cmp(Elev e1,Elev e2){
    return e1.get_nume()<e2.get_nume();
}

vector<Elev> Service::sortare_nume() {
    vector<Elev> sortat;
    for(const auto& el : get_all()){
        sortat.push_back(el);
    };

    sort(sortat.begin(),sortat.end(),cmp);
    return sortat;
}

vector<Elev> Service::elevi_inscrisi(const std::string &at) {
    vector<Elev> inscrisi;
    for(auto el : get_all()){
        auto ateliere = el.get_ateliere();
        bool ok= false;
        for(const auto& a : ateliere)
            if(a == at)
                ok=true;
        if(ok)
            inscrisi.push_back(el);
    }
    return inscrisi;
}