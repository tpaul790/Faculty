

#include "teste.h"

#include "../Repo/RepoFile.h"
#include "../Service/Service.h"

void test_domain() {
    Parcare p{"a",1,2,"a"};
    assert(p.get_adresa()=="a");
    assert(p.get_stare()=="a");
    assert(p.get_linii()==1);
    assert(p.get_coloane()==2);
    p.set_stare("b");
    p.set_coloane(5);
    p.set_linii(7);
    assert(p.get_stare()=="b");
    assert(p.get_linii()==7);
    assert(p.get_coloane()==5);
}

void test_repo(){
    RepoFile repo{"test.txt"};
    assert(repo.get_all().size()==6);
    try{
        Parcare p1{"garii" ,4 ,2 ,"XX-X--X-"};
        repo.store(p1);
        assert(false);
    }catch (exception){
        assert(true);
    }

    try{
        Parcare p2{"mare" ,4 ,2 ,"XX-X--X-"};
        repo.modifica(p2,1,2,"fdsa");
        assert(false);
    }catch (exception){
        assert(true);
    }
}

void test_service(){
    RepoFile repo{"test.txt"};
    Service serv{repo};
    assert(serv.get_all().size()==6);
    try{
        serv.add("garii" ,4 ,2 ,"XX-X--X-");
        assert(false);
    }catch (exception){
        assert(true);
    }

    try{
        serv.modifica("mare" ,4 ,2 ,"XX-X--X-");
        assert(false);
    }catch (exception){
        assert(true);
    }

    auto str = serv.generare(3,3);
    assert(str.size()==9);
    for(const auto& el : str)
        if(el != 'X' and el != '-')
            assert(false);
}

void test_validare(){
    Parcare p2{"mare" ,4 ,2 ,"XX-X--X-"};
    validare(p2);
    p2.set_coloane(9);
    try{
        validare(p2);
        assert(false);
    }catch(exception){
        assert(true);
    }
    p2.set_linii(9);
    try{
        validare(p2);
        assert(false);
    }catch(exception){
        assert(true);
    }
    p2.set_stare("fas");
    try{
        validare(p2);
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