#include "repo.h"
#include "../Exceptii/exceptions.h"
#include <fstream>

void ProdusRepo::store(const Produs& p){
    std::for_each(lista_produse.begin(),lista_produse.end(),[&p](const Produs& prod){
        if(prod==p)
            throw Exception("Exista deja acest produs!");
    });
    lista_produse.push_back(p);
}

void ProdusRepo::sterge(const Produs& p){
    bool exista=false;
    std::for_each(lista_produse.begin(),lista_produse.end(),[&exista,&p](const Produs& prod){
        if(prod==p)
            exista=true;
    });
    if(!exista)
        throw Exception("Nu exista acest produs!");
    auto it=std::find(lista_produse.begin(),lista_produse.end(),p);
    lista_produse.erase(it);
}

const vector<Produs>& ProdusRepo::get_all() const {
    return lista_produse;
}

void ProdusRepo::modifica(const Produs &p, const Produs &nou) {
    bool exista=false;
    std::for_each(lista_produse.begin(),lista_produse.end(),[&exista,&p,&nou](Produs& prod){
        if(prod==p) {
            exista = true;
            prod.set_nume(nou.get_nume());
            prod.set_tip(nou.get_tip());
            prod.set_pret(nou.get_pret());
            prod.set_producator(nou.get_producator());
        }
    });
    if(!exista)
        throw Exception("Nu exista acest produs!");
}

bool ProdusRepo::cauta(const Produs& produs) {
    for (const auto& p : lista_produse)
        if(p.get_nume()==produs.get_nume() and p.get_producator()==produs.get_producator() and p.get_tip()==produs.get_tip() and p.get_pret()==produs.get_pret())
            return true;
    throw Exception("Nu exista acest produs");
}

void ProdusRepo::set_all(const vector<Produs>& nou) {
    lista_produse.clear();
    for(const auto& prod : nou)
        lista_produse.push_back(prod);
}


void ProdusRepoFile::save_to_file() {
    std::ofstream fout(file_name);
//    if(!fout.is_open())
//        throw Exception("Nu s-a putut deschide fisierul: "+file_name);
    for(const auto& p : ProdusRepo::get_all()){
        fout<<p.get_nume()<<" ";
        fout<<p.get_tip()<<" ";
        fout<<p.get_pret()<<" ";
        fout<<p.get_producator()<<" ";
        fout<<"\n";
    }
    fout.close();
}

void ProdusRepoFile::load_from_file() {
    std::ifstream fin(file_name);
//    if(!fin.is_open())
//        throw Exception("Nu s-a putut deschide fisierul: "+file_name);
    while (!fin.eof()) {
        string nume;
        string tip;
        int pret;
        string producator;
        fin >> nume;
        fin >> tip;
        fin >> pret;
        fin >> producator;
        if (fin.eof())
            break;
        Produs p(nume, tip, pret, producator);
        ProdusRepo::store(p);
    }
    fin.close();
}
