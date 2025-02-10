#ifndef LAB6_SERVICE_H
#define LAB6_SERVICE_H

#include <string>
#include "../Repo/repo.h"
#include "../Cos/cos.h"
#include "../Undo/undo.h"

class Service{
private:
    RepoAbstract& repo;
    vector<unique_ptr<ActiuneUndo>> undo_list;
    CosProduse& cos;
public:
    /*
     * Constructorul service ului
     * param:
     *  1:repo, reprezinta un obiect de tip repo
     */
    explicit Service(RepoAbstract& rep, CosProduse& cos):repo{rep}, cos{cos}{}

    Service(const Service& ot)=delete;

    /*
     * Functia de adaugare a unui produs in service
     * param:
     *  1: nume, reprezinta numele produsului care se adauga
     *  2: tip, reprezinta tipul produsului care se adauga
     *  3: pret, reprezinta pretul produsului care se adauga
     *  4: producator, reprezinta producatorul produsului care se adauga
     * return:-
     */
    void add(const string& nume,const string& tip,int pret,const string& producator);

    /*
     * Functia de stergere a unui produs in service
     * param:
     *  1: nume, reprezinta numele produsului care se sterge
     *  2: tip, reprezinta tipul produsului care se sterge
     *  3: pret, reprezinta pretul produsului care se sterge
     *  4: producator, reprezinta producatorul produsului care se sterge
     * return:-
     */
    void sterge_service(const string& nume,const string& tip,int pret,const string& producator);

    /*
     * Functia de modificare a unui produs in service
     * param:
     *  1: nume, reprezinta numele produsului care se modifica
     *  2: tip, reprezinta tipul produsului care se modifica
     *  3: pret, reprezinta pretul produsului care se modifica
     *  4: producator, reprezinta producatorul produsului care se modifica
     *  5: nume, reprezinta noul nume
     *  6: tip, reprezinta noul tip
     *  7: pret, reprezinta noul pret
     *  8: producator, reprezinta noul producator
     * return:-
     */
    void modifica_service(const string& nume,const string& tip,int pret,const string& producator,const string& nume_nou,const string& tip_nou,int pret_nou,const string& producator_nou);

    /*
     * Functia de cautare a unui produs in service
     * param:
     *  1: nume, reprezinta numele produsului care se cauta
     *  2: tip, reprezinta tipul produsului care se cauta
     *  3: pret, reprezinta pretul produsului care se cauta
     *  4: producator, reprezinta producatorul produsului care se cauta
     * return:-
     */
    void cauta_service(const string& nume,const string& tip,int pret,const string& producator) const;

    /*
     * Functia de filtrare a produselor dupa pret
     * param:
     *  -1: pret, reprezinta pretul dupa care se filtreaza
     * return: returneaza o lista cu toate produsele care au pretul dat
     */
    vector<Produs> filtrare_pret(const int& pret) const;

    /*
     * Functia de filtrare a produselor dupa nume
     * param:
     *  -1: nume, reprezinta numele dupa care se filtreaza
     * return: returneaza o lista cu toate produsele care au numele dat
     */
    vector<Produs> filtrare_nume(const string& nume) const;

    /*
     * Functia de filtrare a produselor dupa producator
     * param:
     *  -1: producator, reprezinta producatorul dupa care se filtreaza
     * return: returneaza o lista cu toate produsele care au producatorul dat
     */
    vector<Produs> filtrare_producator(const string& producator) const;

    /*
     * Functia de adaugare a unui produs in cos
     * nume: numele produsului care se adauga
     * return:-
     */
    void adauga_cos(const string& nume);

    /*
     * Functia de golire a cosului
     */
    void goleste_cos();

    /*
     * Functia de populare a cosului cu produse random din repo
     */
    void populare_cos(int dim);

    /*
     * Functia generica de sortare dupa un singur criteriu
     * param:
     *  -1: fct, reprezinta pointer catre functia de comparare
     *  -2: ord, ordinea dupa care se sorteaza(1-cresc,2-descresc)
     * return:-
     */
    void sortare_generica(std::function<bool(const Produs&, const Produs&)> compara_functie);

    /*
     * Functia reprezinta un geter al listei de produse
     * param:-
     * return: returneaza lista de produse
     */
    const vector<Produs>& get_all() const;

    /*
     * Functia returneaza lista produselor din cos
     * return: lista produselor din cos
     */
    const vector<Produs>& get_cos();

    /*
     * Functia returneaza produsul total al produselor din cos
     */
    const int& get_total(){
        return cos.get_total();
    }

    /*
     * Functia salveaza lista produselor din cos in fisierul dat ca paramentru
     * param: file_name reprezinta numele
     */
    void save_to_file(string file_name);

    /*
     * Functia creaza un dictionar pentru produsele din repo cu cheie tip si valoare frecv
     * return: returneaza dictionarul creat
     */
    unordered_map<string,int> get_map() const;

    void undo();

    vector<Produs> get_cpy(){
        return repo.get_cpy();
    }
};

#endif //LAB6_SERVICE_H
