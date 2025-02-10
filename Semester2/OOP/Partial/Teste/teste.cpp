//
// Created by Titieni Paul on 19.05.2024.
//

#include "teste.h"
#include "../Domain/Elev.h"
#include "../Repo/RepoFile.h"
#include "../Service/Service.h"
#include <string>
#include <assert.h>

void test_domain(){
    Elev e{12,"Paul","cnam", {"muzica"}};
    assert(e.get_nrMatricol()==12);
    assert(e.get_nume()=="Paul");
    assert(e.get_scoala()=="cnam");
    assert(e.get_ateliere().size()==1);
    assert(e.get_ateliere()[0]=="muzica");
    e.add_atelier("informatica");
    assert(e.get_ateliere().size()==2);
}

void test_repo(){
    RepoFile repo("test.txt");
    assert(repo.get_all().size()==10);
    auto e = repo.get_all()[0];
    assert(e.get_nrMatricol()==1);
    assert(e.get_nume()=="Paul");
    assert(e.get_scoala()=="CNAM");
    assert(e.get_ateliere().size()==2);
    assert(e.get_ateliere()[0]=="desen");
    assert(e.get_ateliere()[1]=="informatica");
}

void test_service(){
    RepoFile repo("test.txt");
    Service serv{repo};
    assert(serv.get_all().size()==10);

    auto inscrisi = serv.elevi_inscrisi("informatica");
    assert(inscrisi.size()==5);
    assert(inscrisi[0].get_nume()=="Paul");

    auto sortat = serv.sortare_nume();
    assert(sortat[0].get_nume()=="Alex");
    repo.incarca(sortat);

}

void test_all(){
    test_domain();
    test_repo();
    test_service();
}