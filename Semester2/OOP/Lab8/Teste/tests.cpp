#include "../Domain/produs.h"
#include "../Exceptii//exceptions.h"
#include "../Repo/repo.h"
#include "../Service/service.h"
#include "../utils/utils.h"
#include <cassert>


//repo
void test_domain(){
    Produs p1{ "airpods", "casti", 8000, "apple" };
    Produs p2{ "iphone", "telefon", 312, "apple" };
    assert(p1.get_pret()==8000);
    assert(p1.get_nume()=="airpods");
    assert(p1.get_tip()=="casti");
    assert(p1.get_producator()=="apple");
    p1.set_nume("iphone");
    assert(p1.get_nume()=="iphone");
    p1.set_tip("telefon");
    assert(p1.get_tip()=="telefon");
    p1.set_pret(500);
    assert(p1.get_pret()==500);
    p1.set_producator("hp");
    assert(p1.get_producator()=="hp");
    assert(p1!=p2);
    if(p1==p2)
        assert(false);
    else
        assert(true);

    if(p1!=p1)
        assert(false);
    else
        assert(true);
    assert(p1.get_cnt()==p2.get_cnt());
}

void test_store(){
    ProdusRepo repo;
    Produs p1{ "airpods", "casti", 8000, "apple" };
    Produs p2{ "iphone", "telefon", 47, "apple" };
    Produs p3{ "mac", "laptop", 65, "apple" };
    repo.store(p1);
    auto& prods=repo.get_all();
    assert(prods.size()==1);
    repo.store(p2);
    repo.store(p3);
    assert(prods.size()==3);
    try{
        repo.store(p1);
        assert(false);
    }catch (Exception& e){
        assert(true);
    }
}

void test_sterge(){
    Produs p1{ "airpods", "casti", 8000, "apple" };
    Produs p2{ "iphone", "telefon", 47, "apple" };
    Produs p3{ "iphone", "laptop", 65, "apple" };
    ProdusRepo repo;
    repo.store(p1);
    repo.store(p2);
    repo.store(p3);
    auto& prods=repo.get_all();
    assert(prods.size()==3);
    repo.sterge(p1);
    repo.sterge(p3);
    assert(prods.size()==1);
    assert(prods[0].get_nume()=="iphone");
    try{
        repo.sterge(p1);
        assert(false);
    }catch(Exception& ex){
        assert(true);
    }
}

void test_modifica(){
    Produs p1{ "airpods", "casti", 8000, "apple" };
    Produs p2{ "iphone", "telefon", 47, "apple" };
    ProdusRepo repo;
    repo.store(p1);
    repo.modifica(p1,p2);
    assert(repo.get_all()[0].get_nume()=="iphone");
    assert(repo.get_all()[0].get_tip()=="telefon");
    assert(repo.get_all()[0].get_pret()==47);
    assert(repo.get_all()[0].get_producator()=="apple");
    try{
        repo.modifica(p1,p2);
        assert(false);
    }catch(Exception& e){
        assert(true);
    }
}

void test_cauta(){
    Produs p1{ "iphone", "telefon", 47, "apple" };
    ProdusRepo repo;
    try {
        repo.cauta(p1);
        assert(false);
    }catch (Exception& e){
        assert(true);
    }
    repo.store(p1);
    repo.cauta(p1);
}

//repo file

void test_store_file(){
    string fn="test.txt";
    ProdusRepoFile repo_file(fn);
    Produs p1{ "airpods", "casti", 8000, "apple" };
    Produs p2{ "iphone", "telefon", 47, "apple" };
    repo_file.store(p1);
    repo_file.store(p2);
    assert(repo_file.get_all().size()==2);
    fn.clear();
}

void test_set_all(){
    string fn="test.txt";
    ProdusRepoFile repo_file(fn);
    assert(repo_file.get_all().size()==2);
    Produs p1{ "airpods", "casti", 8000, "apple" };
    Produs p2{ "iphone", "telefon", 47, "apple" };
    vector<Produs> vect;
    vect.push_back(p1);
    vect.push_back(p2);
    repo_file.set_all(vect);
}

void test_modifica_file(){
    string fn="test.txt";
    ProdusRepoFile repo_file(fn);
    assert(repo_file.get_all().size()==2);
    Produs p1{ "airpods", "casti", 8000, "apple" };
    Produs nou{ "mac", "laptop", 65, "apple" };
    repo_file.modifica(p1,nou);
}

void test_sterge_file(){
    string fn="test.txt";
    ProdusRepoFile repo_file(fn);
    assert(repo_file.get_all().size()==2);
    Produs nou{ "mac", "laptop", 65, "apple" };
    Produs p2{ "iphone", "telefon", 47, "apple" };
    repo_file.sterge(nou);
    assert(repo_file.get_all().size()==1);
    repo_file.sterge(p2);
    assert(repo_file.get_all().empty());
}


//cos
void test_adauga_cos(){
    Produs p1{ "airpods", "casti", 8000, "apple" };
    Produs p2{ "iphone", "telefon", 47, "apple" };
    CosProduse cos;
    cos.store(p1);
    assert(cos.get_all().size()==1);
    cos.store(p2);
    assert(cos.get_all().size()==2);
    cos.store(p1);
    cos.store(p2);
    assert(cos.get_all().size()==4);
}

void test_goleste_cos(){
    Produs p1{ "airpods", "casti", 8000, "apple" };
    Produs p2{ "iphone", "telefon", 47, "apple" };
    CosProduse cos;
    cos.store(p1);
    cos.store(p2);
    cos.store(p1);
    cos.store(p2);
    assert(cos.get_all().size()==4);
    cos.goleste();
    assert(cos.get_all().empty());
}

void test_populare() {
    ProdusRepo repo;
    CosProduse cos;
    Produs p1{"airpods", "casti", 8000, "apple"};
    Produs p2{"iphone", "telefon", 47, "apple"};
    repo.store(p1);
    repo.store(p2);
    cos.populare(5,repo.get_all());
    assert(cos.get_all().size()==5);
}

void test_adauga_cos_service(){
    Produs p1{ "airpods", "casti", 8000, "apple" };
    Produs p2{ "iphone", "telefon", 47, "apple" };
    CosProduse cos;
    ProdusRepo repo;
    Service serv{repo,cos};
    repo.store(p1);
    repo.store(p2);
    serv.adauga_cos("airpods");
    assert(serv.get_cos().size()==1);
    serv.adauga_cos("airpods");
    assert(serv.get_cos().size()==2);
    serv.adauga_cos("iphone");
    assert(serv.get_cos().size()==3);
    try{
        serv.adauga_cos("tractor");
        assert(false);
    }catch(Exception&){
        assert(true);
    }
    assert(cos.get_total()==16047);
}

void test_populare_cos_service(){
    Produs p1{ "airpods", "casti", 8000, "apple" };
    Produs p2{ "iphone", "telefon", 47, "apple" };
    CosProduse cos;
    ProdusRepo repo;
    Service serv{repo,cos};
    repo.store(p1);
    repo.store(p2);
    serv.populare_cos(3);
    assert(serv.get_cos().size()==3);
    serv.goleste_cos();
    assert(serv.get_cos().empty());
}

//exceptii
void test_get_mesaj(){
    try {
        throw Exception{"Exceptie"};
    }catch (Exception & ex){
        string mesaj=ex.get_mesaj();
        assert(mesaj.size()==8);
        assert(mesaj=="Exceptie");
    }
}

//service

void test_add(){
    ProdusRepo repo;
    CosProduse cos;
    Service serv { repo,cos };
    serv.add("iphone", "telefon", 47, "apple");
    serv.add("mac", "telefon", 47, "apple");
    serv.add("airpods", "telefon", 47, "apple");
    try{
        serv.add("a", "a", 0, "a");
        assert(false);
    }catch(Exception& e){
        assert(true);
    }
    const auto& prods=serv.get_all();
    assert(prods.size()==3);
    assert(prods[0].get_nume()=="iphone");
    assert(prods[1].get_nume()=="mac");
    assert(prods[2].get_nume()=="airpods");
    assert(serv.get_total()==0);
}

void test_sterge_service(){
    ProdusRepo repo;
    CosProduse cos;
    Service serv { repo,cos };
    serv.add("iphone", "telefon", 47, "apple");
    serv.add("mac", "telefon", 47, "apple");
    serv.add("airpods", "telefon", 47, "apple");
    try{
        serv.sterge_service("a", "a", 0, "a");
        assert(false);
    }catch(Exception& e){
        assert(true);
    }
    auto& prods=serv.get_all();
    assert(prods.size()==3);
    serv.sterge_service("iphone", "telefon", 47, "apple");
    assert(prods.size()==2);
    try{
        serv.sterge_service("iphone", "telefon", 47, "apple");
        assert(false);
    }catch(Exception& e){
        assert(true);
    }
    serv.sterge_service("mac", "telefon", 47, "apple");
    assert(prods.size()==1);
    assert(prods[0].get_nume()=="airpods");
}

void test_modifica_service(){
    ProdusRepo repo;
    CosProduse cos;
    Service serv { repo ,cos};
    serv.add("iphone", "telefon", 47, "apple");
    serv.modifica_service("iphone", "telefon", 47, "apple","airpods", "casti", 47, "apple");
    assert(serv.get_all()[0].get_nume()=="airpods");
    assert(serv.get_all()[0].get_tip()=="casti");
    assert(serv.get_all()[0].get_pret()==47);
    assert(serv.get_all()[0].get_producator()=="apple");
    try{
        serv.modifica_service("iphone", "telefon", 47, "apple","a", "a", -1, "a");
        assert(false);
    }catch(Exception& e){
        assert(true);
    }
    try{
        serv.modifica_service("iphone", "telefon", 47, "apple","airpods", "casti", 47, "apple");
        assert(false);
    }catch(Exception& e){
        assert(true);
    }
}

void test_save_to_file(){
    ProdusRepo repo;
    CosProduse cos;
    Service serv { repo,cos };
    serv.add("iphone", "telefon", 47, "apple");
    serv.add("mac", "telefon", 47, "apple");
    serv.add("airpods", "telefon", 47, "apple");
    cos.populare(2,repo.get_all());
    cos.save_to_file("cart");
    cos.goleste();
    serv.save_to_file("cart");
}

void test_cauta_service(){
    ProdusRepo repo;
    CosProduse cos;
    Service serv { repo ,cos};
    try{
        serv.cauta_service("iphone", "telefon", 47, "apple");
        assert(false);
    }catch (Exception&){
        assert(true);
    }
    serv.add("iphone", "telefon", 47, "apple");
    serv.cauta_service("iphone", "telefon", 47, "apple");
    try{
        serv.cauta_service("a", "a", -1, "a");
        assert(false);
    }catch (Exception&){
        assert(true);
    }
}

void test_filtrari(){
    ProdusRepo repo;
    CosProduse cos;
    Service serv { repo,cos };
    serv.add("iphone", "telefon", 47, "apple");
    serv.add("mac", "telefon", 47, "apple");
    serv.add("airpods", "telefon", 21, "apple");
    assert(serv.filtrare_pret(47).size()==2);
    assert(serv.filtrare_nume("iphone").size()==1);
    assert(serv.filtrare_producator("apple").size()==3);
}

void test_sortare_generica(){
    Produs p1{ "airpods", "casti", 1, "apple" };
    Produs p2{ "iphone", "telefon", 3, "apple" };
    Produs p3{ "mac", "laptop", 2, "apple" };
    ProdusRepo repo;
    CosProduse cos;
    repo.store(p1);
    repo.store(p2);
    repo.store(p3);
    Service service{ repo,cos };
    service.sortare_generica(cmp_pret_cresc);
    assert(service.get_all()[0].get_pret()==1);
    assert(service.get_all()[1].get_pret()==2);
    assert(service.get_all()[2].get_pret()==3);
    service.sortare_generica(cmp_pret_desc);
    assert(service.get_all()[0].get_pret()==3);
    assert(service.get_all()[1].get_pret()==2);
    assert(service.get_all()[2].get_pret()==1);

    service.sortare_generica(cmp_nume_cresc);
    assert(service.get_all()[0].get_nume()=="airpods");
    assert(service.get_all()[1].get_nume()=="iphone");
    assert(service.get_all()[2].get_nume()=="mac");

    service.sortare_generica(cmp_nume_desc);
    assert(service.get_all()[0].get_nume()=="mac");
    assert(service.get_all()[1].get_nume()=="iphone");
    assert(service.get_all()[2].get_nume()=="airpods");
}

void test_sortare_generica_2(){
    Produs p1{ "iphone", "laptop", 1, "apple" };
    Produs p2{ "airpods", "telefon", 3, "apple" };
    Produs p3{ "iphone", "vision", 2, "apple" };
    Produs p4{ "airpods", "casti", 2, "apple" };
    ProdusRepo repo;
    CosProduse cos;
    repo.store(p1);
    repo.store(p2);
    repo.store(p3);
    repo.store(p4);
    Service service{ repo,cos };
    service.sortare_generica(cmp_nume_tip_cresc);
    assert(service.get_all()[0].get_nume()=="airpods");
    assert(service.get_all()[0].get_tip()=="casti");
    assert(service.get_all()[1].get_nume()=="airpods");
    assert(service.get_all()[1].get_tip()=="telefon");
    assert(service.get_all()[2].get_nume()=="iphone");
    assert(service.get_all()[2].get_tip()=="laptop");
    assert(service.get_all()[3].get_nume()=="iphone");
    assert(service.get_all()[3].get_tip()=="vision");
    service.sortare_generica(cmp_nume_tip_desc);
    assert(service.get_all()[0].get_nume()=="iphone");
    assert(service.get_all()[0].get_tip()=="vision");
    assert(service.get_all()[1].get_nume()=="iphone");
    assert(service.get_all()[1].get_tip()=="laptop");
    assert(service.get_all()[2].get_nume()=="airpods");
    assert(service.get_all()[2].get_tip()=="telefon");
    assert(service.get_all()[3].get_nume()=="airpods");
    assert(service.get_all()[3].get_tip()=="casti");

}

void test_undo(){
    ProdusRepo repo;
    CosProduse cos;
    Service serv(repo,cos);
    try{
        serv.undo();
        assert(false);
    }catch (Exception&){
        assert(true);
    }
    serv.add("iphone", "laptop", 1, "apple");
    serv.add("mac", "laptop", 2, "apple" );
    serv.add("airpods", "casti", 2, "apple");
    assert(serv.get_all().size()==3);
    serv.undo();
    assert(serv.get_all().size()==2);
    try{
        serv.cauta_service("airpods", "casti", 2, "apple");
        assert(false);
    }catch (Exception&){
        assert(true);
    }

    serv.sterge_service("mac", "laptop", 2, "apple");
    assert(serv.get_all().size()==1);
    serv.undo();
    assert(serv.get_all().size()==2);
    try{
        serv.cauta_service("mac", "laptop", 2, "apple");
        assert(true);
    }catch (Exception&){
        assert(false);
    }
    serv.modifica_service("mac", "laptop", 2, "apple","airpods", "casti", 2, "apple");
    assert(serv.get_all().size()==2);
    try{
        serv.cauta_service("mac", "laptop", 2, "apple");
        assert(false);
    }catch (Exception&){
        assert(true);
    }
    serv.undo();
    try{
        serv.cauta_service("mac", "laptop", 2, "apple");
        assert(true);
    }catch (Exception&){
        assert(false);
    }
}

void test_cmp(){

    Produs p1{ "Someica", "tractor", 8000, "fiat" };
    Produs p2{ "Iphone", "telefon", 47, "apple" };
    Produs p3{ "Iphone", "laptop", 65, "apple" };
    assert(cmp_pret_desc(p1,p2)==1);
    assert(cmp_pret_cresc(p2,p1)==1);
    assert(cmp_nume_desc(p1,p2)==1);
    assert(cmp_nume_cresc(p2,p1)==1);
    assert(cmp_nume_tip_cresc(p3,p2)==1);
    assert(cmp_nume_tip_desc(p2,p3)==1);
}

void test_get_map(){
    Produs p1{ "Someica", "tractor", 8000, "fiat" };
    Produs p2{ "Iphone", "telefon", 47, "apple" };
    Produs p3{ "Iphone", "laptop", 65, "apple" };
    Produs p4{ "Mac", "laptop", 65, "apple" };
    ProdusRepo repo;
    CosProduse cos;
    Service serv{repo,cos};
    repo.store(p1);
    repo.store(p2);
    repo.store(p3);
    repo.store(p4);
    unordered_map<string,int> map=serv.get_map();
    assert(map.size()==3);
    auto it = map.find("laptop");
    assert(it->second==2);
    auto it2 = map.find("tractor");
    assert(it2->second==1);

}


void test_all(){
    //domain
    test_domain();
    //repo
    test_store();
    test_sterge();
    test_modifica();
    test_cauta();
    //repo file
    test_store_file();
    test_set_all();
    test_modifica_file();
    test_sterge_file();
    //cos
    test_adauga_cos();
    test_goleste_cos();
    test_populare();
    test_save_to_file();
    //exceptii
    test_get_mesaj();
    test_sterge_service();
    //service
    test_add();
    test_sterge_service();
    test_modifica_service();
    test_cauta_service();
    test_filtrari();
    test_sortare_generica();
    test_sortare_generica_2();
    test_adauga_cos_service();
    test_populare_cos_service();
    test_get_map();
    test_undo();
    //utils
    test_cmp();
}