//
// Created by Titieni Paul on 19.05.2024.
//

#include "RepoFile.h"

void RepoFile::load_from_file() {
    std::ifstream fin(file_name);
    while (!fin.eof()) {
        string nume;
        string scoala;
        int nrM;
        string ateliere_str;
        fin >> nrM;
        fin >> nume;
        fin >> scoala;
        fin >> ateliere_str;
        vector<string> ateliere;
        string sep = ",";
        auto poz = ateliere_str.find(sep);
        while(poz != string::npos){
            poz = ateliere_str.find(sep);
            auto at = ateliere_str.substr(0,poz);
            ateliere.push_back(at);
            ateliere_str.erase(0,poz+sep.length());
        }
        Elev e(nrM, nume, scoala, ateliere);
        lista_elevi.push_back(e);
    }
    fin.close();
}

void RepoFile::incarca(const vector<Elev>& nou) {
    lista_elevi.clear();
    for(const auto& el : nou)
        lista_elevi.push_back(el);
}