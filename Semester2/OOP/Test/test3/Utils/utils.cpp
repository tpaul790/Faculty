//
// Created by Titieni Paul on 19.05.2024.
//

#include "utils.h"

int to_int(const string& str){
    int nr = 0;
    int p = 10;
    for(const auto& bit : str){
        int cif = bit - '0';
        nr=nr*p+cif;
    }
    return nr;
}

bool cmp(const Tractor& t1,const Tractor& t2){
    return t1.get_pret()<t2.get_pret();
}