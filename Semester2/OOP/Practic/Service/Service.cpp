
#include "Service.h"


void validare(const Parcare& p){
    if(p.get_adresa().empty())
        throw exception();
    if(p.get_stare().size()!=p.get_linii()*p.get_coloane())
        throw exception();
    for(const auto& el : p.get_stare())
        if(el != 'X' and el != '-')
            throw exception();
    if(p.get_coloane()<1 or p.get_coloane()>5)
        throw exception();
    if(p.get_linii()<1 or p.get_linii()>5)
        throw exception();
}