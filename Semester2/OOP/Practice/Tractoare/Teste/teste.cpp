//
// Created by Titieni Paul on 26.06.2024.
//

#include "teste.h"
#include <assert.h>
#include "../Domain/Tractor.h"
#include "../Repo/RepoFile.h"
#include "../Service/Service.h"
#include "../Exception.h"

void test_domain(){
    Tractor t { 1,"a","a",4};
    assert(t.get_id()==1);
    assert(t.get_denumire()=="a");
    assert(t.get_tip()=="a");
    assert(t.get_nrRoti()==4);
}

void test_repo(){
    RepoFile repo{"test.txt"};
    try {
        Tractor t{10, "fiat", "sof2", 4};
        repo.store(t);
        assert(false);
    }catch (exception){
        assert(true);
    }
    auto tr = repo.get_all().at(0);
    assert(tr.get_id()==6);
}

void test_service(){
        RepoFile repo{"test.txt"};
        Service serv{repo};
        try {
            serv.add(10, "fiat", "sof2", 4);
            assert(false);
        }catch (exception){
            assert(true);
        }
        try {
            serv.add(-1, "", "", 3);
            assert(false);
        }catch (Exception& e){
            assert(true);
        }
        auto tr = repo.get_all().at(0);
        assert(tr.get_id()==6);
};

void test_all(){
    test_domain();
    test_repo();
    test_service();
}