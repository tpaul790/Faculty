//
// Created by Titieni Paul on 24.06.2024.
//

#ifndef PRODUSE_REPOFILE_H
#define PRODUSE_REPOFILE_H
#include "../Domain/Produs.h"
#include <utility>
#include <vector>
#include <string>

class RepoFile {
private:
    string file_name;
    vector<Produs> produse;
    void load_from_file();
    /*
     * Functia incarca datele din fisierul file_name in lista de produse
     */
    void save_to_file();
    /*
     * Functia salveaza toate produsele din lista in fisierul file_name
     */
public:
    RepoFile(string fn) : file_name{std::move(fn)} {
        load_from_file();
    };

    void store(const Produs& p1);

    const vector<Produs>& get_all();
};


#endif //PRODUSE_REPOFILE_H
