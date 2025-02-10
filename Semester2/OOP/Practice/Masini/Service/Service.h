//
// Created by Titieni Paul on 25.06.2024.
//

#ifndef MASINI_SERVICE_H
#define MASINI_SERVICE_H
#include <utility>

#include "../Domain/Masina.h"
#include "../Repo/RepoFile.h"
#include <stack>
#include <vector>

class Service {
private:
    RepoFile& repo;
    stack<vector<Masina>> undo;
    stack<vector<Masina>> redo;
public:
    Service(RepoFile& r) : repo{r} {};

    void inc_an(int val){
        undo.push(repo.get_copy());
        repo.inc_an(val);
    }

    void sterge(const string& id, const string& denumire, int an, const string& tip){
        undo.push(repo.get_copy());
        repo.sterge(Masina{std::move(id),denumire,an,tip});
    }

    const vector<Masina>& get_all(){
        /*
         * Functia  returneaza o referinta de tip const a vectorului de masini
         */
        return repo.get_all();
    }

    void do_undo(){
        if(!undo.empty()){
;           redo.push(repo.get_copy());
            repo.set_masini(undo.top());
            undo.pop();
        } else
            throw exception();
    }

    void do_redo(){
        if(!redo.empty()){
            repo.set_masini(redo.top());
            redo.pop();
        }
        else
            throw exception();
    }
};


#endif //MASINI_SERVICE_H
