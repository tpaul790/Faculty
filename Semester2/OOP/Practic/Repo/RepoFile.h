
#ifndef PRACTIC_REPOFILE_H
#define PRACTIC_REPOFILE_H
#include <vector>
#include <fstream>
#include "../Domain/Parcare.h"

class RepoFile {
private:
    string file_name;
    vector<Parcare> parcari;
    void load_from_file();
    /*
     * Functia incarca datele din fisier in lista de obiecte
     */
    void save_to_file();
    /*
     * Functia salveaza datele din lista de obiecte in fisier
     */
public:
    RepoFile(string fn) : file_name{std::move(fn)}{
        load_from_file();
    };
    /*
     * Constructorul care primeste fisierul
     */

    void store(const Parcare& p);
    /*
     * Functia adauga in lista de parcari o parcare data daca aceasta nu exista deja
     * Arunca exceptie daca parcarea exista
     */

    void modifica(Parcare p, int nrl,int nrc,const string& str);
    /*
     * Functia modifica nr de linii,coloane si starea unei parcari date
     * Arunca exceptie daca parcarea nu exista
     */

    const vector<Parcare>& get_all();
    /*
     * Functia returneaza vectorul de parcari
     */
};


#endif //PRACTIC_REPOFILE_H
