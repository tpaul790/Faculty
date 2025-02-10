//
// Created by Titieni Paul on 19.05.2024.
//
#include <iostream>
#include "teste.h"
#include <assert.h>
#include "../Domain/Tractor.h"
#include "../Repo/RepoFile.h"
#include "../Service/Service.h"
#include "../Utils/utils.h"

void test_domain(){
    Tractor t{"a","a",1,1};
    assert(t.get_nume()=="a");
    assert(t.get_producator()=="a");
    assert(t.get_pret()==1);
    assert(t.get_an()==1);
    t.set_pret(5);
    assert(t.get_pret()==5);
}

void test_repo(){
    RepoFile repo{"test1.txt"};
    assert(repo.get_all().size()==5);
    auto t = repo.get_all()[0];
    repo.sterge(t);
    assert(repo.get_all().size()==4);
    repo.store(t);
    repo.modifica_pret(t,7999);

    //repo.sortare_pret();

}

void test_service(){
    RepoFile repo{"test2.txt"};
    Service serv{repo};
    assert(serv.get_all().size()==5);
    serv.sterge("GT991","Ford",1998,9000);
    serv.store("GT991","Ford",1998,9000);
    serv.modifica("GT991","Ford",1998,9000,7900);
    serv.modifica("GT991","Ford",1998,7900,9000);
    serv.sortare_pret();
    assert(serv.get_all()[0].get_pret()==7890);

    auto filtrate = serv.filtru_producator("Fiat");
    for(const auto& tr : filtrate)
        assert(tr.get_producator()=="Fiat");
    assert(filtrate.size()==2);

}

void test_utils(){
    string nrstr = "123";
    auto nr = to_int(nrstr);
    assert(nr == 123);
}


void test_all(){
    test_domain();
    test_repo();
    test_utils();
    test_service();
}