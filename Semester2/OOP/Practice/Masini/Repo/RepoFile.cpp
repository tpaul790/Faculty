//
// Created by Titieni Paul on 25.06.2024.
//

#include "RepoFile.h"

void RepoFile::load_from_file() {
    ifstream fin(file_name);
    while(!fin.eof()){
        string id,tip,denumire;
        int an;
        fin>>id>>denumire>>an>>tip;
        if(fin.eof())
            break;
        masini.push_back(Masina{id,denumire,an,tip});
    }
    fin.close();
}

void RepoFile::save_to_file() {
    ofstream fout(file_name);
    for(const auto& m : masini){
        string id = m.get_id();
        string tip = m.get_tip();
        string denumire = m.get_denumire();
        int an = m.get_an();
        fout<<id<<' '<<denumire<<' '<<an<<' '<<tip<<endl;
    }
    fout.close();
}
