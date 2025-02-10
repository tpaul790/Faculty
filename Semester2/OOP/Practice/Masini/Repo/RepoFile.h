//
// Created by Titieni Paul on 25.06.2024.
//

#ifndef MASINI_REPOFILE_H
#define MASINI_REPOFILE_H
#include "../Domain/Masina.h"
#include <vector>
#include <fstream>
#include <algorithm>

class RepoFile {
private:
    vector<Masina> masini;
    string file_name;
    void load_from_file();
    /*
     * Functia incarca datele din fisierul file_name in vectorul de masini
     */
    void save_to_file();
    /*
     * Functia salveaza datele din vectorul de masini in fisierul file_name
     */
public:
    RepoFile(string fn) : file_name{std::move(fn)} {
        load_from_file();
    };

    void inc_an(int val){
        /*
         * Functia incrementeaza valoarea aniilor fiecarei masini cu valoarea data ca parametru
         */
        for(auto& m : masini)
            m.set_an(m.get_an()+val);
        save_to_file();
    }

    void sterge(const Masina& m){
        auto it = std::find(masini.begin(),masini.end(),m);
        masini.erase(it);
        save_to_file();
    }

    const vector<Masina>& get_all(){
        /*
         * Functia sorteaza vectorul de masini crescator dupa id si returneaza o referinta de tip const
         * catre acesta
         */
        sort(masini.begin(),masini.end(),[](const Masina& m1, const Masina& m2){return m1.get_id()<m2.get_id();});
        return masini;
    }

    vector<Masina> get_copy(){
        return masini;
    }

    void set_masini(vector<Masina> nou){
        masini=nou;
    }
};


#endif //MASINI_REPOFILE_H
