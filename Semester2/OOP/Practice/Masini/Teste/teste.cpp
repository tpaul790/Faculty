//
// Created by Titieni Paul on 25.06.2024.
//

#include "teste.h"
#include "assert.h"
#include "../Domain/Masina.h"
#include "../Repo/RepoFile.h"
#include "../Service/Service.h"

void test_domain(){
    Masina m{"tif", "audi", 2001,"sedan"};
    assert(m.get_id()=="tif");
    assert(m.get_denumire()=="audi");
    assert(m.get_an()==2001);
    assert(m.get_tip()=="sedan");
    m.set_an(2010);
    assert(m.get_an()==2010);
}

void test_repo(){
    RepoFile repo{"test.txt"};
    assert(repo.get_all().size()==10);
    auto m = repo.get_all().at(2);
    assert(m.get_id()=="BN86TIF");
    assert(m.get_denumire()=="Audi");
    assert(m.get_an()==2016);
    assert(m.get_tip()=="suv");
    repo.inc_an(1);
    repo.inc_an(-1);
}

void test_service(){
    RepoFile repo{"test.txt"};
    Service serv{repo};
    assert(serv.get_all().size()==10);
    auto m = serv.get_all().at(2);
    assert(m.get_id()=="BN86TIF");
    assert(m.get_denumire()=="Audi");
    assert(m.get_an()==2016);
    assert(m.get_tip()=="suv");
    serv.inc_an(1);
    serv.inc_an(-1);
//    serv.sterge("BN23CIR","Mercedes",2014,"suv");
};


void test_all(){
    test_domain();
    test_repo();
    test_service();
}