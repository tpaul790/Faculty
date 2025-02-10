#ifndef LAB6_COS_H
#define LAB6_COS_H

#include "../Domain/produs.h"
#include <vector>
#include <random>
#include <algorithm>
#include <chrono>
#include "../Exceptii/exceptions.h"
#include "../Observer/Observer.h"
#include <fstream>

using namespace std;

class CosProduse : public Observable{
private:
    vector<Produs> cos;
    int total;
public:
    /*
     * Constructor
     */
    CosProduse(){
        total=0;
    };

    /*
     * Functia de adaugare a unui produs in cos
     * pre: p produs valid
     * post: se adauga produsul in cos
     * p: produsul care se adauga
     * return:
     */
    void store(const Produs& p) {
        cos.push_back(p);
        total+=p.get_pret();
        notify();
    }

    /*
     * Functia de golire a cosului
     * post: cosul este gol
     * param:
     * return:
     */
    void goleste(){
        if(cos.empty())
            throw Exception("Cosul este deja gol!");
        cos.clear();
        total=0;
        notify();
    }

    /*
     * Functia de populare a cosului cu produse random din repo
     * pre: dim numar natural,v vectorul din care se adauga produse random
     * post: cosul o sa aiba dim produse in plus
     * dim: numarul de produse care se adauga
     * v: vectorul din care se adauga produse random
     * return:-
     */
    void populare(int dim,vector<Produs> v);

    /*
     * Functia salveaza produsele din cos intr un fisier dat
     * post: In fisierul file name vor fi scrise produsele din cos
     * param: file_name reprezinta numele fisierului in care se face adaugarea
     * return:
     */
    void save_to_file(string file_name);

    /*
     * Functia returneaza lista produselor din cos
     * post: se returneaza lista produselor din cos
     * return: lista produselor din cos
     */
    const vector<Produs>& get_all(){
        return cos;
    }

    /*
     * Functia returneaza pretul total al produselor din cos
     * post: se returneaza un nr natural
     * return: pretul total al produselor din cos
     */
    const int& get_total() const{
        return total;
    }
};


#endif //LAB6_COS_H
