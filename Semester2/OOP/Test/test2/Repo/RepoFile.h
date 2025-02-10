//
// Created by Titieni Paul on 17.05.2024.
//

#ifndef TEST2_REPOFILE_H
#define TEST2_REPOFILE_H

#include "../Domain/Apartament.h"
#include <utility>
#include <vector>
#include <iostream>
#include <fstream>

class RepoFile {
private:
    vector<Apartament> lista_apartamente;
    string file_name;
    void load_from_file();
public:
    RepoFile(string fn) : file_name(std::move(fn)){
        load_from_file();
    };

    void sterge(const Apartament& ap);

    const vector<Apartament>& get_all(){
        return lista_apartamente;
    }
};


#endif //TEST2_REPOFILE_H
