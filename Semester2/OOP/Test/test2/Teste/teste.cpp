//
// Created by Titieni Paul on 17.05.2024.
//
#include "../Domain/Apartament.h"
#include "../Repo/RepoFile.h"
#include "../Service/Service.h"
#include <assert.h>

void test_domain(){
    Apartament a1{"50m^2","Hasdeu","300 euro"};
    assert(a1.get_pret()=="300 euro");
    assert(a1.get_strada()=="Hasdeu");
    assert(a1.get_suprafata()=="50m^2");
}

void test_repo(){
    RepoFile repo("test");
    assert(repo.get_all().size()==5);
    auto a = repo.get_all()[1];
    assert(a.get_strada()=="Intre Lacuri");
    repo.sterge(a);
    assert(repo.get_all().size()==4);
    auto a2 = repo.get_all()[1];
    assert(a2.get_strada()=="Gheorgheni");
}

void test_service(){
    RepoFile repo("test");
    Service serv{repo};
    assert(serv.get_all().size()==5);
    auto a = serv.get_all()[1];
    assert(a.get_strada()=="Intre Lacuri");
    assert(a.get_suprafata()=="50m^2");
    assert(a.get_pret()=="700 euro");
    serv.sterge("50m^2" ,"Intre Lacuri","700 euro");
    assert(repo.get_all().size()==4);
    auto a2 = repo.get_all()[1];
    assert(a2.get_strada()=="Gheorgheni");
}

void test_all(){
    test_domain();
    test_repo();
    test_service();
}