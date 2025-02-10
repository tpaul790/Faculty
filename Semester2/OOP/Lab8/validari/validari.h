#ifndef LAB6_VALIDARI_H
#define LAB6_VALIDARI_H
#include "../Domain/produs.h"
#include <string>
using std::string;

bool validare_nume(const string& nume);

bool validare_tip(const string& tip);

bool validare_producator(const string& producator);

bool validare_pret(int pret);

string validare_produs(const Produs& p);

#endif //LAB6_VALIDARI_H
