#ifndef LAB6_REPO_H
#define LAB6_REPO_H

#include "../Domain/produs.h"
#include "random"
#include <utility>
#include <vector>
#include <map>
#include <utility>
#include <cstdlib>
using std::vector;

class RepoAbstract{
public:
    virtual void store(const Produs& p) = 0;

    virtual void sterge(const Produs& p) = 0;

    virtual void modifica(const Produs& p,const Produs& nou) = 0;

    virtual bool cauta(const Produs& p) = 0;

    virtual const vector<Produs>& get_all() const = 0;

    virtual vector<Produs> get_cpy() const  = 0;

    virtual void set_all(const vector<Produs>& nou) = 0;
};

class ProdusRepo : public RepoAbstract{
private:
    vector<Produs> lista_produse;
    friend class Service;

public:
    ProdusRepo(const ProdusRepo& ot)=delete;

    ProdusRepo()=default;

    /*
     * Functia de stocare a unui produs in lista de produse
     * pram:
     *  -1: p, reprezinta produsul care se stocheaza
     * return:-
     */
    void store(const Produs& p) override;

    /*
     * Functia de stergere a unui produs din lista de produse
     * pram:
     *  -1: p, reprezinta produsul care se sterge
     * return:-
     */
    void sterge(const Produs& p) override;

    /*
     * Functia de modificare a unui produs de pe o poz data
     * pram:
     *  -1: p, reprezinta produsul care se stocheaza
     *  -3: poz, pozitia din lista e produsuli dat
     * return:-
     */
    void modifica(const Produs& p,const Produs& nou) override;

    /*
     * Functia de cautare a unui produs din lista de produse,functia arunca o exceptie daca produsul nu exista
     * iar daca nu exista,functia nu face nimic
     * pram:
     *  -1: p, reprezinta produsul care se cauta
     * return:-
     */
    bool cauta(const Produs& p) override;

    /*
     * Functia returneaza lista curenta de produse
     * param:-
     * return: lista curenta de produse
     */
    const vector<Produs>& get_all() const override;

    vector<Produs> get_cpy() const override{
        return lista_produse;
    }

    void set_all(const vector<Produs>& nou) override;

};

class ProdusRepoFile : public ProdusRepo{
private:
    string file_name;
    void save_to_file();
    void load_from_file();
public:

    /*
     * Constructorul pentru repo file
     */
    explicit ProdusRepoFile(string file_name) : ProdusRepo() ,file_name(std::move(file_name)){
        load_from_file();
    }

    /*
     * Functia de store pentru repo file
     * p: produsul care se stocheaza in lista si in fisier
     * return:-
     */
    void store(const Produs& p) override{
        ProdusRepo::store(p);
        save_to_file();
    }

    /*
     * Functia de stergere pentru repo file
     * p: produsul care se sterge din lista si din fisier
     */
    void sterge(const Produs& p) override{
        ProdusRepo::sterge(p);
        save_to_file();
    }

    void modifica(const Produs& p, const Produs& nou) override{
        ProdusRepo::modifica(p,nou);
        save_to_file();
    }

    void set_all(const vector<Produs>& nou) override{
        ProdusRepo::set_all(nou);
        save_to_file();
    }
};

#endif //LAB6_REPO_H
