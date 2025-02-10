//
// Created by Titieni Paul on 09.05.2024.
//

#ifndef TEST1_PROCESORINFILEREPO_H
#define TEST1_PROCESORINFILEREPO_H
#include <utility>
#include <vector>
#include "../Domain/Procesor.h"
#include <fstream>

using namespace std;

class ProcesorInFileRepo {
private:
    vector<Procesor> lista_procesoare;
    string file_name;
    void load_form_file();
public:
    explicit ProcesorInFileRepo(string fn) : file_name(std::move(fn)){
        load_form_file();
    };

    Procesor cauta(const string& nume){
        for(auto p : lista_procesoare)
            if(p.get_nume()==nume)
                return p;
        throw exception();
    }

    const vector<Procesor>& get_all(){
        return lista_procesoare;
    }
};

#endif //TEST1_PROCESORINFILEREPO_H
