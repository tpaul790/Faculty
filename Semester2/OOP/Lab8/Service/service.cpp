#include "../validari/validari.h"
#include "../Exceptii/exceptions.h"
#include "service.h"
#include <utility>

void Service::add(const string &nume, const string &tip, int pret, const string &producator) {
    Produs p{nume, tip, pret,producator};
    string ers= validare_produs(p);
    if(!ers.empty())
        throw Exception(ers);
    repo.store(p);
    undo_list.push_back(std::make_unique<AdaugaUndo>(p, this->repo));
}

const vector<Produs>& Service::get_all() const {
    return repo.get_all();
}

void Service::sterge_service(const string &nume, const string &tip, int pret, const string &producator) {
    Produs p{nume,tip,pret,producator};
    string ers= validare_produs(p);
    if(!ers.empty())
        throw Exception(ers);
    const auto& prods=repo.get_all();
    repo.sterge(p);
    undo_list.push_back(std::make_unique<StergeUndo>(p, this->repo));
}

void Service::modifica_service(const string &nume, const string &tip, int pret, const string &producator,
                               const string &nume_nou, const string &tip_nou, int pret_nou,
                               const string &producator_nou) {
    Produs p{nume,tip,pret,producator};
    Produs nou{nume_nou,tip_nou,pret_nou,producator_nou};
    string ers = validare_produs(nou);
    if(!ers.empty())
        throw Exception(ers);
    repo.modifica(p,nou);
    undo_list.push_back(std::make_unique<ModificaUndo>(p, nou, this->repo));
}

void Service::cauta_service(const string &nume, const string &tip, int pret, const string &producator) const {
    Produs p{nume,tip,pret,producator};
    string ers= validare_produs(p);
    if(!ers.empty())
        throw Exception(ers);
    repo.cauta(p);
}

vector<Produs> Service::filtrare_pret(const int& pret) const {
    vector<Produs> f;
    const vector<Produs>& v=get_all();
    std::copy_if(v.begin(),v.end(),std::back_inserter(f),[&pret](const Produs& p1){return p1.get_pret()==pret;});
    return f;
}

vector<Produs> Service::filtrare_nume(const string& nume) const {
    vector<Produs> f;
    const vector<Produs>& v=get_all();
    std::copy_if(v.begin(),v.end(),std::back_inserter(f),[&nume](const Produs& p1){return p1.get_nume()==nume;});
    return f;
}

vector<Produs> Service::filtrare_producator(const std::string &producator) const {
    vector<Produs> f;
    const vector<Produs>& v=get_all();
    std::copy_if(v.begin(),v.end(),std::back_inserter(f),[&producator](const Produs& p1){return p1.get_producator()==producator;});
    return f;
}

void Service::sortare_generica(std::function<bool(const Produs&, const Produs&)> cmp) {
    auto all=repo.get_all();
    std::sort(all.begin(), all.end(), std::move(cmp));
    repo.set_all(all);
}

//cos
void Service::adauga_cos(const string& nume){
    auto prods=repo.get_all();
    auto it=std::find_if(prods.begin(),prods.end(),[&nume](const Produs& p){return p.get_nume()==nume;});
    if(it==prods.end())
        throw Exception("Nu exista niciun produs cu acest nume!");
    cos.store(*it);
}

void Service::goleste_cos(){
    cos.goleste();
}

void Service::populare_cos(int dim){
    cos.populare(dim,repo.get_all());
}

const vector<Produs>& Service::get_cos(){
    return cos.get_all();
}

void Service::save_to_file(string file_name){
    cos.save_to_file(std::move(file_name));
}

unordered_map<string, int> Service::get_map() const {
    std::unordered_map<string,int> map;
    for(const auto& el : get_all()){
        auto it=map.find(el.get_tip());
        if(it!=map.end()){
            it->second++;
        }else{
            map.insert(make_pair(el.get_tip(),1));
        }
    }
    return map;
}

void Service::undo() {
    if(undo_list.empty())
        throw Exception("Nu se poate face undo!\n");
    undo_list.back()->do_undo();
    undo_list.pop_back();
}
