//
// Created by Titieni Paul on 23.06.2024.
//

#include "RepoFile.h"
#include <fstream>

void RepoFile::load_from_file() {
    ifstream fin(file_name);
    while(!fin.eof()){
        int id;
        string titlu;
        string artist;
        int rank;
        fin>>id;
        fin>>titlu;
        fin>>artist;
        fin>>rank;
        if (fin.eof())
            break;
        melodii.push_back(Melodie{id,titlu,artist,rank});
    }
    sort(melodii.begin(),melodii.end(),[](const Melodie& m1,const Melodie& m2){
        return m1.get_rank()<m2.get_rank();
    });
    fin.close();
}

void RepoFile::save_to_file() {
    ofstream fout(file_name);
    for(const auto& m : melodii){
        int id = m.get_id();
        string titlu = m.get_titlu();
        string artist = m.get_artist();
        int rank = m.get_rank();
        fout<<id<<' '<<titlu<<' '<<artist<<' '<<rank<<endl;
    }
    fout.close();
}

void RepoFile::modifica(int id, string nou_t, int nou_r) {
    bool gasit = false;
    for(auto& m : melodii){
        if(m.get_id()==id){
            m.set_titlu(nou_t);
            m.set_rank(nou_r);
            gasit = true;
        }
    }
    if(!gasit)
        throw std::exception();
    save_to_file();
}

void RepoFile::sterge(int id) {
    auto it = melodii.begin();
    while((*it).get_id()!=id)
        it++;
    auto artist = (*it).get_artist();
    int cnt=0;
    for(const auto& m : melodii)
        if(m.get_artist()==artist)
            cnt++;
    if(cnt>1)
        melodii.erase(it);
    else{
        throw exception();
    }
    save_to_file();
}

Melodie RepoFile::find(int id) {
    for(auto m : melodii)
        if(m.get_id()==id){
            return m;
        }
    throw exception();
}

