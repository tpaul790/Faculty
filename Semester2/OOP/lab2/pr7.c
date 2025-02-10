#include <stdio.h>

float modul(float n){
    /*
     * Funcita returneaza modulul unui numar transmis ca parametru
     * param n: numarul real pentru care se va returna modulul
     * preconditii: n numar real
     * return: modulul unui numar dat
     * posconditii: rezultatul intors de functie este un numar real pozitiv
     */
    if(n<0)
        return -n;
    else
        return n;
}

float aprox_rad(float n, float precizie){
    /*
     * Functia calculeaza o valoare aproximativa a radacinii patrate a unui numar
   real pozitiv, cu o precizie data folosind algoritmul lui Newton-Rhapson.
     * param n: numarul pentru care se va calcula valoarea aproximativa a radacinii sale patrate
     * param prcizie: precizia cu, care se va calcula valoarea aproximativa a radacinii patrate a lui n
     * preconditii: n si precizie sunt doua numere reale
     * return: valoarea aproximativa a radacinii patrate a lui n cu precizia precizie
     * postconditii: rezultatul intors de functie este un numar real
     */
    float x=n/2;
    while(modul(x*x-n)>precizie){
        x=(x+n/x)/2;
    }
    return x;
}

int main(){
    /*
     * Functia citeste perechi de numere reale, de forma x,y unde x este numarul pentru care
     se calculeaza valoarea aproximativa a radacinii sale patrate cu precizie y, pana cand se citeste 0.
     */
    float n=-1;
    while(1){
        printf("Introduceti un numar: " );
        scanf("%f",&n);
        if(n==0)
            break;
        printf("Introduceti o precizie: ");
        float precizie=0;
        scanf("%f",&precizie);
        printf("Aproximarea radacinii patrate este: %0.3f \n", aprox_rad(n,precizie));
    }
    return 0;
}