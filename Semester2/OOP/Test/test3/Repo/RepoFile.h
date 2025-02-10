//
// Created by Titieni Paul on 19.05.2024.
//

#ifndef TEST3_REPOFILE_H
#define TEST3_REPOFILE_H

#include <utility>
#include <vector>
#include <fstream>
#include "../Domain/Tractor.h"
#include "../Utils/utils.h"

class RepoAbstract{
public:
    virtual void store(const Tractor& t) = 0;

    virtual void sterge(const Tractor& t) = 0;

    virtual void modifica_pret(const Tractor& t, int pret) = 0;

    virtual void sortare_pret() = 0;

    virtual const vector<Tractor>& get_all() = 0;
};

class RepoFile : public RepoAbstract{
private:
    vector<Tractor> lista_tractorare;
    string file_name;

    /*
     * Functaia incarca tractoarele din fisier in lista
     */
    void load_from_file();

    /*
     * Functia salveaza tractoare din lista in fisier
     */
    void save_to_file();
public:
    /*
     * Constructorul
     */
    RepoFile(string fn):file_name(std::move(fn)){
        load_from_file();
    };

    void store(const Tractor& t) override{
        lista_tractorare.push_back(t);
        save_to_file();
    }

    /*
     * Functia de stergere a unui tractor din lista
     * param: t reprezinta tractorul care va fi sters
     * return:
     */
    void sterge(const Tractor& t) override;

    /*
     * Functia de modificare a unui tractor din lista
     * param: t reprezinta tractorul care va fi modificat
     * param new_pret: reprezinta noul pret al tractorului dat
     * return:
     */
    void modifica_pret(const Tractor& t, int new_pret) override;

    /*
     * Functia sorteaza lista de tractorare crescator dupa pret
     */
    void sortare_pret() override{
        sort(lista_tractorare.begin(),lista_tractorare.end(),cmp);
        save_to_file();
    }

    /*
     * Functia returneaza lista de tractoare
     */
    const vector<Tractor>& get_all() override{
        return lista_tractorare;
    }

};


#endif //TEST3_REPOFILE_H
