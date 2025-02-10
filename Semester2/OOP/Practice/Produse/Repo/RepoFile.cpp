#include <fstream>
#include "RepoFile.h"

void RepoFile::load_from_file() {
    ifstream fin(file_name);
    while(!fin.eof()){
        int id;
        string tip,nume;
        double pret;
        fin>>id;
        fin>>nume;
        fin>>tip;
        fin>>pret;
        if(fin.eof())
            break;
        produse.push_back(Produs{id,nume,tip,pret});
    }
    fin.close();
}

void RepoFile::save_to_file() {
    ofstream fout(file_name);
    for(const auto& p : produse){
        auto id = p.get_id();
        auto nume = p.get_nume();
        auto tip = p.get_tip();
        auto pret = p.get_pret();
        fout<<id<<' '<<nume<<' '<<tip<<' '<<pret<<"\n";
    }
    fout.close();
}

void RepoFile::store(const Produs &p1) {
    for(const auto& p : produse)
        if(p.get_id()==p1.get_id())
            throw exception();
    produse.push_back(p1);
    save_to_file();
}

const vector<Produs> &RepoFile::get_all() {
    sort(produse.begin(),produse.end(),[](const Produs& p1,const Produs& p2){return p1.get_pret()<p2.get_pret();});
    return produse;
}
