//
// Created by Titieni Paul on 19.05.2024.
//

#include "RepoFile.h"
#include <iostream>

void RepoFile::load_from_file() {
    ifstream fin(file_name);
    string line;
    string sep=",";
    while(std::getline(fin,line)){
        auto poz = line.find(sep);
        vector<string> campuri;
        while(poz != string::npos){
            poz=line.find(sep);
            string camp = line.substr(0,poz);
            campuri.push_back(camp);
            line.erase(0,poz+sep.length());
        }
        auto nume = campuri[0];
        auto producator = campuri[1];
        auto an = to_int(campuri[2]);
        auto pret = to_int(campuri[3]);
        Tractor t{nume,producator,an,pret};
        lista_tractorare.push_back(t);
        campuri.clear();
    }
}

void RepoFile::save_to_file() {
    ofstream fout(file_name);
    for(const auto& t : lista_tractorare){
        auto nume = t.get_nume();
        auto prod = t.get_producator();
        auto an = to_string(t.get_an());
        auto pret = to_string(t.get_pret());
        string afis;
        afis.append(nume);
        afis.append(",");
        afis.append(prod);
        afis.append(",");
        afis.append(an);
        afis.append(",");
        afis.append(pret);
        afis.append(",");
        fout<<afis<<"\n";
    }
}

void RepoFile::sterge(const Tractor &t) {
    auto it = std::find(lista_tractorare.begin(),lista_tractorare.end(),t);
    lista_tractorare.erase(it);
    save_to_file();
}

void RepoFile::modifica_pret(const Tractor &t, int new_pret) {
    auto it = std::find(lista_tractorare.begin(),lista_tractorare.end(),t);
    it->set_pret(new_pret);
    save_to_file();
}
