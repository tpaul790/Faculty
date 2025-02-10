//
// Created by Titieni Paul on 26.06.2024.
//

#include "RepoFile.h"

void RepoFile::load_from_file() {
    ifstream fin(file_name);
    while(!fin.eof()){
        int id,nr;
        string denumire,tip;
        fin>>id>>denumire>>tip>>nr;
        if(fin.eof())
            break;
        tractoare.push_back(Tractor{id,denumire,tip,nr});
    }
    fin.close();
}

void RepoFile::save_to_file() {
    ofstream fout(file_name);
    for(const auto& t : tractoare){
        auto id = t.get_id();
        auto nr = t.get_nrRoti();
        auto tip = t.get_tip();
        auto denumire = t.get_denumire();
        fout<<id<<' '<<denumire<<' '<<tip<<' '<<nr<<endl;
    }
    fout.close();
}

void RepoFile::store(const Tractor &tr) {
    for(const auto& t : tractoare)
        if(t.get_id() == tr.get_id())
            throw exception();
    tractoare.push_back(tr);
    save_to_file();
}

const vector<Tractor> &RepoFile::get_all() {
    sort(tractoare.begin(),tractoare.end(),[](const Tractor& t1, const Tractor& t2){return t1.get_denumire()<t2.get_denumire();});
    return tractoare;
}

void RepoFile::dec(int id) {
    for(auto& t : tractoare)
        if(t.get_id() == id)
            t.set_nrRoti(t.get_nrRoti()-2);
}
