#include <stdio.h>

void intersectie(float x1, float y1, float x2, float y2, float x3, float y3,
                          float x4, float y4, float *x_intersec, float *y_intersec){
    /*
 Functia calculeaza punctul de intersectie intre doua segmente daca exista.
 Preconditii: coordonatele date trebuie sa fie reale
 Param:x1, y1, x2, y2: reprezinta coordonatele primului segment
 Param:x3, y3, x4, y4: reprezinta coordonatele celui de-al doilea segment
 Param:x_intersec: Pointer catre variabila pentru coordonata x a punctului de intersectie.
 Param:y_intersec: Pointer către variabila pentru coordonata y a punctului de intersectie.
 Postconditii: variabilele x_intersec,respectiv y_intersesc primesc coordonatele carteziene
    ale intersectiei celor doua segmente
     */
    float dif = (x4-x3)*(y2-y1)-(x2-x1)*(y4-y3);
    if (dif == 0) {
        // Segmentele sunt paralele
        printf("Segmentele sunt paralele deci nu se intersecteaza");
    } 
    else {
        *x_intersec = ((x1 - x2)*(x3*y4 - y3*x4)-(x1*y2 - y1*x2)*(x3 - x4)) / dif;
        *y_intersec = ((y1 - y2)*(x3*y4 - y3*x4)-(x1*y2 - y1*x2)*(y3 - y4)) / dif;
    }
}

int main() {
    /*
    Functia citeste perechi de coordonate pentru doua segmente pana cand coordonatele
    primului segment sunt (0,0),(0,0) si calculeaza intersectia celor doua segmente daca
    exista.
     */
    while (1){
        float x1, y1, x2, y2; // Coordonatele primului segment
        float x3, y3, x4, y4; // Coordonatele celui de-al doilea segment
        float x_intersec, y_intersec;

        printf("Introduceti coordonatele primului segment:\n");
        printf("Introduceti coordonatele: ");
        scanf("%f %f", &x1, &y1);
        printf("Introduceti coordonatele: ");
        scanf("%f %f", &x2, &y2);
        if(x1==0 && y1==0 && x2==0 && y2==0)
            break;
        printf("Introduceti coordonatele celui de-al doilea segment:\n");
        printf("Introduceti coordonatele: ");
        scanf("%f %f", &x3, &y3);
        printf("Introduceti coordonatele: ");
        scanf("%f %f", &x4, &y4);

        intersectie(x1, y1, x2, y2, x3, y3, x4, y4, &x_intersec, &y_intersec);

        printf("Segmentele se intersecteaza in punctul (%.1f, %.1f).\n", x_intersec, y_intersec);
        
    }
    return 0;
}