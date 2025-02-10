#ifndef LAB6_UI_H
#define LAB6_UI_H

#include "../Service/service.h"

class Ui{
private:
    Service& service;
public:
    explicit Ui(Service& serv): service{serv}{}

    void start();

    void afisare(const vector<Produs>& prods);

    static void print_menu();

    void add_ui();

    void sterge_ui();

    void modifica_ui();

    void cauta_ui();

    void filtrare_pret_ui();

    void filtrare_nume_ui();

    void filtrare_producator_ui();

    void filtrare_ui();

    void sortare_ui();

    void adauga_cos_ui();

    void goleste_cos_ui();

    void populeaza_ui();

    void total_ui();

    void save_to_file_ui();

    void undo_ui();

};
#endif //LAB6_UI_H
