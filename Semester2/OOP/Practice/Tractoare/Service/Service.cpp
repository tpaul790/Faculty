//
// Created by Titieni Paul on 26.06.2024.
//

#include "Service.h"

string validare(const Tractor& tr){
    string ers;
    if(tr.get_id()<=0)
        ers.append("Id inavlid! ");
    if(tr.get_denumire().empty())
        ers.append("Denumire inavlida! ");
    if(tr.get_tip().empty())
        ers.append("Tip inavlid! ");
    if(tr.get_nrRoti()<2 or tr.get_nrRoti()>16 or tr.get_nrRoti()%2==1)
        ers.append("Numar invalid de roti! ");
    return ers;
}

void Service::add(int id, const string &denumire, const string &tip, int nrRoti) {
    Tractor tr{id,denumire,tip,nrRoti};
    auto ers = validare(tr);
    if(!ers.empty())
        throw Exception(ers);
    repo.store(tr);
}

void Service::dec_nrRoti(int id) {
    repo.dec(id);
}
