//
// Created by Titieni Paul on 09.05.2024.
//

#ifndef TEST1_PLACAINFILEREPO_H
#define TEST1_PLACAINFILEREPO_H

#include "../Domain/PlacaDeBaza.h"
#include <vector>
#include <fstream>
#include <exception>

class PlacaInFileRepo {
private:
    vector<PlacaDeBaza> lista_placi;
    string file_name;
    void load_form_file();
public:
    explicit PlacaInFileRepo(string fn) : file_name(std::move(fn)){
        load_form_file();
    };

    void store(const PlacaDeBaza& p){
        for(const auto& placa : lista_placi){
            if(placa.get_nume()==p.get_nume())
                throw exception();
        }
        lista_placi.push_back(p);
    }

    PlacaDeBaza cauta(const string& nume){
        for(const auto& placa : lista_placi)
            if(placa.get_nume()==nume)
                return placa;
        throw exception();
    }

    const vector<PlacaDeBaza>& get_all(){
        return lista_placi;
    }
};


#endif //TEST1_PLACAINFILEREPO_H
