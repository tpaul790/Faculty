#include "RepoFile.h"

//
//#include "RepoFile.h"
//
//
void RepoFile::load_from_file() {
    ifstream fin(file_name);
    while(!fin.eof()){
        string adr,st;
        int nrl,nrc;
        fin>>adr>>nrl>>nrc>>st;
        if(fin.eof())
            break;
        parcari.push_back(Parcare{adr,nrl,nrc,st});
    }
    fin.close();
}

void RepoFile::save_to_file() {
    ofstream fout(file_name);
    for(const auto& p : parcari){
        string ad = p.get_adresa();
        string st = p.get_stare();
        int nrl = p.get_linii();
        int nrc = p.get_coloane();
        fout<<ad<<' '<<nrl<<' '<<nrc<<' '<<st<<endl;
    }
    fout.close();
}

void RepoFile::store(const Parcare &p) {
    for(const auto& el : parcari)
        if(el.get_adresa() == p.get_adresa())
            throw exception();
    parcari.push_back(p);
    save_to_file();
}

void RepoFile::modifica(Parcare p, int nrl, int nrc, const std::string &str) {
    auto it = std::find(parcari.begin(),parcari.end(),p);
    if(it<parcari.end()) {
        parcari.erase(it);
        p.set_linii(nrl);
        p.set_coloane(nrc);
        p.set_stare(str);
        parcari.push_back(p);
        save_to_file();
    }else{
        throw exception();
    }
}

const vector<Parcare> &RepoFile::get_all() {
    sort(parcari.begin(),parcari.end(),[](const Parcare& p1, const Parcare& p2){
        return (p1.get_linii()*p1.get_coloane())<(p2.get_linii()*p2.get_coloane());
    });
    return parcari;
}
