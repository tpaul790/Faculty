#include "validari.h"

bool validare_nume(const string& nume){
    if(nume=="iphone" or nume=="mac" or nume=="imac" or nume=="airpods" or nume=="lapte" or nume=="ceafa" or nume=="covrigi" or nume=="branza" or nume=="telemea" or nume=="iaurt")
        return true;
    return false;
}

bool validare_tip(const string& tip){
    if(tip=="telefon" or tip=="laptop" or tip=="casti" or tip=="pc" or tip=="vision" or tip=="carne" or tip=="lactate" or tip=="patiserie" or tip=="electronic")
        return true;
    return false;
}

bool validare_producator(const string& prod){
    if(prod=="apple" or prod=="samsung" or prod=="hp" or prod=="lenovo" or prod=="ardeal" or prod=="monor" or prod=="panemar")
        return true;
    return false;
}

bool validare_pret(int pret){
    return pret>0;
}

string validare_produs(const Produs& p){
    string ers;
    if(!validare_nume(p.get_nume()) or !validare_tip(p.get_tip()) or !validare_pret(p.get_pret()) or !validare_producator(p.get_producator()))
        ers.append("PRODUS INVALID: ");
    if(!validare_nume(p.get_nume()))
        ers.append("Nume invalid, ");
    if(!validare_tip(p.get_tip()))
        ers.append("Tip invalid, ");
    if(!validare_pret(p.get_pret()))
        ers.append("Pret invalid, ");
    if(!validare_producator(p.get_producator()))
        ers.append("Producator invalid");
    return ers;
}