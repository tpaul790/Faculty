//
// Created by Titieni Paul on 24.06.2024.
//

#include "validare.h"

string validare(const Produs& p){
    string ers;
    if(p.get_nume().empty())
        ers.append("Nume Invalid! ");
    if(p.get_tip().empty())
        ers.append("Tip Invalid! ");
    if(p.get_id()<=0)
        ers.append("Id invalid! ");
    if(p.get_pret()<1.0 or p.get_pret()>100.0)
        ers.append("Pret invalid! ");
    return ers;
}