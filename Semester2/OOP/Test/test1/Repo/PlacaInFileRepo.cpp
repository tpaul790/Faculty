//
// Created by Titieni Paul on 09.05.2024.
//

#include "PlacaInFileRepo.h"

void PlacaInFileRepo::load_form_file() {
    ifstream fin(file_name);
    if(!fin.is_open())
        throw exception();
    while(!fin.eof()){
        string nume;
        int sP,pret;
        fin>>nume;
        fin>>sP;
        fin>>pret;
        PlacaDeBaza p{nume,sP,pret};
        this->store(p);
    }
    fin.close();
}
