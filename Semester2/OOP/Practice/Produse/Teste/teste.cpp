//
// Created by Titieni Paul on 24.06.2024.
//

#include "teste.h"
#include <assert.h>
#include "../Domain/Produs.h"
#include "../Repo/RepoFile.h"
#include "../Service/Service.h"

void test_domain(){
    Produs p1{1,"a","a",1.2};
    assert(p1.get_id()==1);
    assert(p1.get_nume()=="a");
    assert(p1.get_tip()=="a");
    assert(p1.get_pret()==1.2);
}

void test_load(){
    RepoFile repo{"test.txt"};
    assert(repo.get_all().size()==10);
    assert(repo.get_all().at(0).get_pret()==3.2);
}

void test_store(){
    Produs p1{1,"a","a",1.2};
    RepoFile repo{"test2.txt"};
    try{
        repo.store(p1);
        assert(false);
    }catch (exception){
        assert(true);
    }
    assert(repo.get_all().size()==11);
}

void test_repo(){
    test_load();
    test_store();
}

void test_add_map(){
    RepoFile repo{"test3.txt"};
    Service serv{repo};
    try {
        serv.add(1, "a", "a", 1.2);
        serv.add(2, "abcde", "a", 1.2);
        assert(false);
    }catch (exception){
        assert(true);
    }
    assert(serv.get_all().size()==2);
    try {
        serv.add(-1, "", "", 0.2);
        serv.add(2, "abcde", "a", 1.2);
        assert(false);
    }catch (Exception& e){
        assert(true);
    }
    auto map = serv.get_map();
    assert(map.at("a")==1);
    assert(map.at("abcde")==2);
}

void test_service(){
    test_add_map();
}

void test_all(){
    test_domain();
    test_repo();
    test_service();
}