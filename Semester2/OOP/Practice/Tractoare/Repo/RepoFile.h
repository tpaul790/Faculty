//
// Created by Titieni Paul on 26.06.2024.
//

#ifndef TRACTOARE_REPOFILE_H
#define TRACTOARE_REPOFILE_H
#include <fstream>
#include "../Domain/Tractor.h"
#include <vector>
class RepoFile {
private:
    string file_name;
    vector<Tractor> tractoare;
    void load_from_file();
    /*
     * Functia incarca datele din fisier
     */
    void save_to_file();
    /*
     * Functia incarca datele in fisier
     */
public:
    RepoFile(string fn) : file_name{std::move(fn)} {
        load_from_file();
    };

    void store(const Tractor& tr);
    /*
     * Functia adauga tractorul dat in lista daca nu exista deja
     */

    void dec(int id);

    const vector<Tractor>& get_all();
    /*
     * Functia returneaza lista de tractoare sortata dupa denumire
     */
};


#endif //TRACTOARE_REPOFILE_H
