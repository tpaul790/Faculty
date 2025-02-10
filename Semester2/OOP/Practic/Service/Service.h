
#ifndef PRACTIC_SERVICE_H
#define PRACTIC_SERVICE_H
#include "../Repo/RepoFile.h"
#include "../Observer/Observer.h"

void validare(const Parcare& p);

class Service : public Observable{
private:
    RepoFile& repo;
public:
    Service(RepoFile& r) : repo{r} {};

    void add(const string& adr, int nrl, int nrc, const string& st){
        /*
         * Functia creeaza un obiect parcare cu campurile date, valideaza obiectul
         * si il adauga in lista daca acesta este valid
         */
        Parcare p{adr, nrl,nrc,st};
        validare(p);
        repo.store(p);
    }

    void modifica(const string& adr, int nrl, int nrc, const string& stare){
        /*
         * Functia modifica nr linii,coloane si starea parcarii cu adresa data daca
         * datele introduse sunt valide
         */
        Parcare p1 {adr, nrl,nrc, stare};
        validare(p1);
        bool gasit = false;
        for(const auto& p : repo.get_all())
            if(p.get_adresa() == adr){
                repo.modifica(p,nrl,nrc,stare);
                gasit = true;
                break;
            }
        if(!gasit)
            throw exception();
    }

    string generare(int nrl, int nrc){
        /*
         * Functia creeaza radom o stare pentru o parcare cu numar de linii si coloane date
         */
        if(nrl<1 or nrl>5 or nrc<1 or nrc>5)
            throw exception();
        string st;
        for(int i = 0;i<nrl*nrc;i++){
            int r = random()%10;
            if(r%2==0)
                st.append("X");
            else
                st.append("-");
        }
        return st;
    }

    const vector<Parcare>& get_all(){
        /*
         * Functia returneaza lista de parcari
         */
        return repo.get_all();
    }
};


#endif //PRACTIC_SERVICE_H
