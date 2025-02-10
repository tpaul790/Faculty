//
// Created by Titieni Paul on 09.05.2024.
//

#include "ProcesorInFileRepo.h"

void ProcesorInFileRepo::load_form_file() {
    ifstream fin(file_name);
    if(!fin.is_open())
        throw std::exception();
    while(!fin.eof()){
        string nume;
        int nrT,sP,pr;
        fin>>nume;
        fin>>nrT;
        fin>>sP;
        fin>>pr;
        if (fin.eof())
            break;
        Procesor p{nume,nrT,sP,pr};
        lista_procesoare.push_back(p);
    }
    fin.close();
}
