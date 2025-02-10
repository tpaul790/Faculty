#ifndef LAB6_UTILS_H
#define LAB6_UTILS_H

#include "../Domain/produs.h"
/*
 *Functia compara pretul a doua produse date
 * param:
 *  -1: p1,primul produs pentru din comparare
 *  -2: p2, al doilea produs din comparare
 * return: 1 daca pretul lui p1>p2 si 0 altfel
 */
int cmp_pret_desc(const Produs& p1,const Produs& p2);

int cmp_pret_cresc(const Produs& p1,const Produs& p2);

/*
 *Functia compara numele a doua produse date
 * param:
 *  -1: p1,primul produs pentru din comparare
 *  -2: p2, al doilea produs din comparare
 * return: 1 daca numele lui p1>p2,2 daca numele sunt egale si 0 altfel
 */
int cmp_nume_cresc(const Produs& p1,const Produs& p2);

int cmp_nume_desc(const Produs& p1,const Produs& p2);

/*
 *Functia compara tipul a doua produse date
 * param:
 *  -1: p1,primul produs pentru din comparare
 *  -2: p2, al doilea produs din comparare
 * return: 1 daca tipul lui p1>p2 si 0 altfel
 */
int cmp_nume_tip_desc(const Produs& p1,const Produs& p2);

int cmp_nume_tip_cresc(const Produs& p1,const Produs& p2);

#endif //LAB6_UTILS_H
