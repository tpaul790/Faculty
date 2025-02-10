//
// Created by Titieni Paul on 17.05.2024.
//

#include "RepoFile.h"

void RepoFile::load_from_file() {
    ifstream fin(file_name);
    string linie;
    string sep=",";
    while(std::getline(fin,linie)){
        vector<string> campuri;
        while(linie.find(sep) != string::npos) {
            auto poz = linie.find(sep);
            auto camp=linie.substr(0,poz);
            campuri.push_back(camp);
            linie.erase(0,poz+1);
        }
        string strada = campuri[0];
        string sup = campuri[1];
        string pret = campuri[2];
        campuri.clear();
        Apartament ap{sup,strada,pret};
        lista_apartamente.push_back(ap);
    }
}

void RepoFile::sterge(const Apartament& ap) {
    auto it = find(lista_apartamente.begin(),lista_apartamente.end(),ap);
    lista_apartamente.erase(it);
}
