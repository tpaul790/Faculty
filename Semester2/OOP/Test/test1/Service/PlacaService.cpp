//
// Created by Titieni Paul on 09.05.2024.
//

#include "PlacaService.h"

vector<PlacaDeBaza> PlacaService::filtrare_sP(int sP) {
    vector<PlacaDeBaza> list_filtrate;
    for(auto& p : repo.get_all())
        if(p.get_socluProcesor()==sP)
            list_filtrate.push_back(p);
    return list_filtrate;
}