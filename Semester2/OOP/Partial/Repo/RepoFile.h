//
// Created by Titieni Paul on 19.05.2024.
//

#ifndef PARTIAL_REPOFILE_H
#define PARTIAL_REPOFILE_H

#include "../Domain/Elev.h"
#include <fstream>

class RepoFile {
private:
    vector<Elev> lista_elevi;
    string file_name;
    /*
     * Funcita incarca datele dintr un fisier in lista de elevi
     */
    void load_from_file();
public:
    /*
     * Constructorul clasei
     */
    RepoFile(string fn) : file_name(std::move(fn)){
        load_from_file();
    }

    void incarca(const vector<Elev>& nou);

    /*
     * Functia returneaza lista de elevi
     */
    const vector<Elev>& get_all(){
        return lista_elevi;
    }
};


#endif //PARTIAL_REPOFILE_H
