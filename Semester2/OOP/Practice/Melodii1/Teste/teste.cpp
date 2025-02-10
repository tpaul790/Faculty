//
// Created by Titieni Paul on 23.06.2024.
//

#include "teste.h"
#include <assert.h>
#include "../Domain/Melodie.h"
#include "../Repo/RepoFile.h"
#include "../Service/Service.h"

void test_domain(){
    Melodie m1{1,"a","a",7};
    assert(m1.get_id()==1);
    assert(m1.get_titlu()=="a");
    assert(m1.get_artist()=="a");
    assert(m1.get_rank()==7);
    m1.set_rank(10);
    m1.set_titlu("b");
    assert(m1.get_titlu()=="b");
    assert(m1.get_rank()==10);
}

void test_repo(){
    RepoFile repo{"test.txt"};
    assert(repo.get_all().size()==10);
    auto m = repo.get_all().at(0);
    repo.modifica(1,"soare",7);
    auto m1=repo.find(1);
    assert(m1.get_titlu()=="soare");
    assert(m1.get_rank()==7);
    try{
        repo.sterge(3);
        assert(false);
    }catch(exception){
        assert(true);
    }
}

void test_service(){
    RepoFile repo{"test.txt"};
    Service serv{repo};
    assert(serv.get_all().size()==10);
    auto m = serv.get_all().at(0);
    serv.modifica(1,"luna",9);
    auto m1=repo.find(1);
    assert(m1.get_titlu()=="luna");
    assert(m1.get_rank()==9);
    try{
        repo.sterge(3);
        assert(false);
    }catch(exception){
        assert(true);
    }
}


void test_all(){
    test_domain();
    test_repo();
    test_service();
}