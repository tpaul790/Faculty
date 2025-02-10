#include "../Domain/produs.h"

int cmp_pret_cresc(const Produs& p1,const Produs& p2){
    return p1.get_pret()<p2.get_pret();
}

int cmp_pret_desc(const Produs& p1,const Produs& p2){
    return p1.get_pret()>p2.get_pret();
}

int cmp_nume_cresc(const Produs& p1,const Produs& p2){
    return p1.get_nume()<p2.get_nume();
}

int cmp_nume_desc(const Produs& p1,const Produs& p2){
    return p1.get_nume()>p2.get_nume();
}

int cmp_nume_tip_cresc(const Produs& p1,const Produs& p2){
    if(p1.get_nume() != p2.get_nume())
        return p1.get_nume()<p2.get_nume();
    else
        return p1.get_tip()<p2.get_tip();
}

int cmp_nume_tip_desc(const Produs& p1,const Produs& p2){
    if(p1.get_nume() != p2.get_nume())
        return p1.get_nume()>p2.get_nume();
    else
        return p1.get_tip()>p2.get_tip();
}



