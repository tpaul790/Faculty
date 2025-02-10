#include "produs.h"

#include <utility>

int Produs::cnt_copieri=0;

string Produs::get_nume() const{
    return nume;
}

string Produs::get_tip() const{
    return tip;
}

int Produs::get_pret() const{
    return pret;
}

string Produs::get_producator() const{
    return producator;
}

void Produs::set_nume(string nou){
    nume=std::move(nou);
}

void Produs::set_tip(string nou) {
    tip=std::move(nou);
}

void Produs::set_pret(int nou) {
    pret=nou;
}

void Produs::set_producator(string nou) {
    producator=std::move(nou);
}

bool Produs::operator==(const Produs& ot) const{
    if(producator==ot.producator and  tip==ot.tip and pret==ot.pret and nume==ot.nume)
        return true;
    return false;

}

bool Produs::operator!=(const Produs &ot) {
    if(*this==ot)
        return false;
    return true;
}

