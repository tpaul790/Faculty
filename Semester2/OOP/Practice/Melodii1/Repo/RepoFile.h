//
// Created by Titieni Paul on 23.06.2024.
//

#ifndef MELODII1_REPOFILE_H
#define MELODII1_REPOFILE_H
#include "../Domain/Melodie.h"
#include <utility>
#include <vector>
#include <string>
using namespace std;

class RepoFile {
private:
    string file_name;
    vector<Melodie> melodii;
    void load_from_file();
    void save_to_file();
public:
    RepoFile(string fn) : file_name{std::move(fn)} {
        load_from_file();
    };

    void modifica(int id, string nou_t,int nou_r);
    /*
    * Functia modifica titlul si rank ul melodiei cu id ul dat
    * param: id, id ul melodiei pe care o modificam
    * param2: nou_t, noul titlu al melodiei
    * param3: nou_r, noul rank al melodiei
    */


    void sterge(int id);
    /*
     * Functia sterge melodia cu id ul dat daca nu este singura piesa a artistului respectiv
     * param1:id, id ul melodiei care se sterge
     */

    Melodie find(int id);
    /*
     * Functia returneaza melodia cu id ul dat sau arunca exceptie daca nu exista
     */

    const vector<Melodie>& get_all(){
        /*
         * Functia returneaza lista de melodii sortate crescator dupa rank
         */
        sort(melodii.begin(),melodii.end(),[](const Melodie& m1,const Melodie& m2){
            return m1.get_rank()<m2.get_rank();
        });
        return melodii;
    }
};


#endif //MELODII1_REPOFILE_H
