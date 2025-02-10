//
// Created by Titieni Paul on 23.06.2024.
//

#ifndef MELODII1_SERVICE_H
#define MELODII1_SERVICE_H
#include "../Repo/RepoFile.h"
#include "../Domain/Melodie.h"
class Service {
private:
    RepoFile& repo;
public:

    Service(RepoFile& r) : repo{r} {};

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

    int get_nr_rank(int rank);

    const vector<Melodie>& get_all(){
        return repo.get_all();
    }
};


#endif //MELODII1_SERVICE_H
