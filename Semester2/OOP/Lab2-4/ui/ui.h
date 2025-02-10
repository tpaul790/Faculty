//
// Created by Titieni Paul on 11.03.2024.
//

#ifndef LAB2_4_UI_H
#define LAB2_4_UI_H


#include "../domain/domain.h"
#include "../repo/store.h"


void print_m();

void afisare_produse(Lista* list);

void adaugare_produs_ui(Lista* list);

void actualizare_produs_ui(Lista* list);

void sterge_produs_ui(Lista* list);

void sortare_ui(Lista* list);

void filtrare_ui(Lista* list);

void start_program(Lista* list);

#endif //LAB2_4_UI_H
