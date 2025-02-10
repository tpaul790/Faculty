//
// Created by Titieni Paul on 09.05.2024.
//
#include "../Domain/Procesor.h"
#include "../Domain/PlacaDeBaza.h"
#include "../Repo/ProcesorInFileRepo.h"
#include "../Repo/PlacaInFileRepo.h"
#include "../Service/ProcesorService.h"
#include "../Service/PlacaService.h"
#include <assert.h>

void test_domain(){
    Procesor p1{"AMD Ryzen",3,124,300};
    assert(p1.get_nume()=="AMD Ryzen");
    assert(p1.get_nrTreduri()==3);
    assert(p1.get_socluProcesor()==124);
    assert(p1.get_pret()==300);

    PlacaDeBaza placa1{"Geforce",125,250};
    assert(placa1.get_nume()=="Geforce");
    assert(placa1.get_socluProcesor()==125);
    assert(placa1.get_pret()==250);
}

void test_repo(){
    ProcesorInFileRepo repo{"test_proc"};
    assert(repo.get_all().size()==2);
    PlacaInFileRepo repo2{"test_placi"};
    assert(repo2.get_all().size()==2);
    try{
        ProcesorInFileRepo repo3{"test"};
        assert(false);
    }catch (exception&){
        assert(true);
    }

    try{
        PlacaDeBaza p{"gtx", 1, 304};
        repo2.store(p);
        assert(false);
    }catch (exception&){
        assert(true);
    }

    try{
        PlacaInFileRepo repo3{"test"};
        assert(false);
    }catch (exception&){
        assert(true);
    }
}

void test_service(){
    ProcesorInFileRepo repo{"test_proc"};
    ProcesorService serv{repo};
    assert(serv.get_all().size()==2);
    PlacaInFileRepo repo2{"test_placi"};
    PlacaService serv2{repo2};
    serv2.add("a",1,1);
    assert(serv2.get_all().size()==3);

    auto list=serv2.filtrare_sP(1);
    assert(list.size()==2);
    for(const auto& p : list)
        assert(p.get_socluProcesor()==1);
}

void test_all(){
    test_domain();
    test_repo();
    test_service();
}