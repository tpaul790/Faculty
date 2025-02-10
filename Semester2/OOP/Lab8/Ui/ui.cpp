#include "ui.h"
#include "string"
#include "../Exceptii/exceptions.h"
#include "../utils/utils.h"
#include <iostream>

using std::cin;
using std::cout;
using std::string;

void Ui::afisare(const vector<Produs>& prods) {
    if(!prods.empty()) {
        for (int i=0;i<prods.size();i++) {
            string name = prods[i].get_nume();
            string tip = prods[i].get_tip();
            string producator = prods[i].get_producator();
            int pret = prods[i].get_pret();
            cout << "Nume: " << name << " | Tip: " << tip << " | Pret: " << pret << " | Producator: " << producator<<"\n";
        }
    }else
        cout<<"Nu exista produse\n";
}

void Ui::print_menu() {
    cout<<"m. Afisare meniu\n";
    cout<<"a. Afisare lista de produse\n";
    cout<<"u. Efectueaza undo pe lista de produse\n";
    cout<<"c. Afisare cos de cuparaturi\n";
    cout<<"r. Adauga produse random in cos\n";
    cout<<"s. Salveaza cosul in fisier\n";
    cout<<"t. Total pret produse din cos\n";
    cout<<"g. Goleste cos\n";
    cout<<"1. Adaugare produs\n";
    cout<<"2. Sterge produs\n";
    cout<<"3. Modifica produs\n";
    cout<<"4. Cauta produs\n";
    cout<<"5. Filtrare produse\n";
    cout<<"6. Sortare produse\n";
    cout<<"7. Adauga produs in cos\n";
    cout<<"e. Exit\n";
}

void Ui::add_ui() {
    string name;
    string tip;
    string producator;
    int pret;
    cout<<"Introduceti numele: ";
    cin>>name;
    cout<<"Introduceti tipul: ";
    cin>>tip;
    cout<<"Introduceti pretul: ";
    cin>>pret;
    cout<<"Introduceti producatorul: ";
    cin>>producator;
    try{
        service.add(name,tip,pret,producator);
        cout<<"Produs adaugat!\n";
    }catch (Exception & ex){
        cout<<ex.get_mesaj()<<"\n";
    }
}

void Ui::sterge_ui() {
    string name;
    string tip;
    string producator;
    int pret;
    cout<<"Introduceti numele: ";
    cin>>name;
    cout<<"Introduceti tipul: ";
    cin>>tip;
    cout<<"Introduceti pretul: ";
    cin>>pret;
    cout<<"Introduceti producatorul: ";
    cin>>producator;
    try{
        service.sterge_service(name,tip,pret,producator);
        cout<<"Produs sters!\n";
    }catch (Exception & ex){
        cout<<ex.get_mesaj()<<"\n";
    }
}

void Ui::modifica_ui() {
    string name;
    string tip;
    string producator;
    int pret;
    cout<<"Introduceti numele: ";
    cin>>name;
    cout<<"Introduceti tipul: ";
    cin>>tip;
    cout<<"Introduceti pretul: ";
    cin>>pret;
    cout<<"Introduceti producatorul: ";
    cin>>producator;
    try{
        service.cauta_service(name,tip,pret,producator);
        cout<<"Acest produs exista, va rugam sa introduceti noile sale caracteristici!\n";
        string name_nou;
        string tip_nou;
        string producator_nou;
        int pret_nou;
        cout<<"Introduceti noul nume: ";
        cin>>name_nou;
        cout<<"Introduceti noul tip: ";
        cin>>tip_nou;
        cout<<"Introduceti noul pret: ";
        cin>>pret_nou;
        cout<<"Introduceti noul producator: ";
        cin>>producator_nou;
        service.modifica_service(name,tip,pret,producator,name_nou,tip_nou,pret_nou,producator_nou);
    }catch (Exception & ex){
        cout<<ex.get_mesaj()<<"\n";
    }
}

void Ui::cauta_ui() {
    string name;
    string tip;
    string producator;
    int pret;
    cout<<"Introduceti numele: ";
    cin>>name;
    cout<<"Introduceti tipul: ";
    cin>>tip;
    cout<<"Introduceti pretul: ";
    cin>>pret;
    cout<<"Introduceti producatorul: ";
    cin>>producator;
    try{
        service.cauta_service(name,tip,pret,producator);
        cout<<"Acest produs exista!\n";
    }catch (Exception& e){
        cout<<e.get_mesaj()<<"\n";
    }
}

void Ui::filtrare_pret_ui() {
    int pret;
    cout<<"Introduceti pretul pentru filtrare: ";
    cin>>pret;
    if(!service.filtrare_pret(pret).empty()){
        for (const auto &produs: service.filtrare_pret(pret)) {
            string name = produs.get_nume();
            string tip = produs.get_tip();
            string producator = produs.get_producator();
            int pret_p = produs.get_pret();
            cout << "Nume: " << name << " | Tip: " << tip << " | Pret: " << pret_p << " | Producator: " << producator<<"\n";
        }
    }else
        cout<<"Nu exista produse cu acest pret\n";
}

void Ui::filtrare_nume_ui() {
    string nume;
    cout<<"Introduceti numele pentru filtrare: ";
    cin>>nume;
    if(!service.filtrare_nume(nume).empty()){
        for (const auto &produs: service.filtrare_nume(nume)) {
            string name = produs.get_nume();
            string tip = produs.get_tip();
            string producator = produs.get_producator();
            int pret_p = produs.get_pret();
            cout << "Nume: " << name << " | Tip: " << tip << " | Pret: " << pret_p << " | Producator: " << producator<<"\n";
        }
    }else
        cout<<"Nu exista produse cu acest nume\n";
}

void Ui::filtrare_producator_ui() {
    string producator;
    cout<<"Introduceti producatorul pentru filtrare: ";
    cin>>producator;
    if(!service.filtrare_producator(producator).empty()){
        for (const auto &produs: service.filtrare_producator(producator)) {
            string name = produs.get_nume();
            string tip = produs.get_tip();
            string producator_p = produs.get_producator();
            int pret_p = produs.get_pret();
            cout << "Nume: " << name << " | Tip: " << tip << " | Pret: " << pret_p << " | Producator: " << producator_p<<"\n";
        }
    }else
        cout<<"Nu exista produse cu acest producator\n";
}

void Ui::filtrare_ui(){
    if(!service.get_all().empty()) {
        string optiune;
        cout << "Alegeti optiunea de filtrare(1-pret,2-nume,3-producaror): ";
        cin >> optiune;
        if (optiune == "1")
            filtrare_pret_ui();
        else if (optiune == "2")
            filtrare_nume_ui();
        else if (optiune == "3")
            filtrare_producator_ui();
        else
            cout << "Optiune invalida!\n";
    }else
        cout<<"Nu se poate face filtrare, lista este goala!\n";
}

void Ui::sortare_ui() {
    if(!service.get_all().empty()) {
        string crit;
        cout << "Introduceti criteriul dupa care doriti sa faceti sortarea(1-pret,2-nume,3-nume+tip): ";
        cin >> crit;
        if (crit == "1") {
            int ord;
            cout << "Introduceti ordinea de sortare(1-crescator,2-descrescator): ";
            cin >> ord;
            if (ord == 1) {
                service.sortare_generica(cmp_pret_cresc);
                afisare(service.get_all());
            }else if(ord == 2){
                service.sortare_generica(cmp_pret_desc);
                afisare(service.get_all());
            }
            else
                cout << "Optiune invalida pentru ordinea de sortare\n";
        } else if (crit == "2") {
            int ord;
            cout << "Introduceti ordinea de sortare(1-crescator,2-descrescator): ";
            cin >> ord;
            if (ord == 1) {
                service.sortare_generica(cmp_nume_cresc);
                afisare(service.get_all());
            }else if(ord == 2){
                service.sortare_generica(cmp_nume_desc);
                afisare(service.get_all());
            }
            else
                cout << "Optiune invalida pentru ordinea de sortare\n";
       }else if (crit == "3") {
            int ord;
            cout << "Introduceti ordinea de sortare(1-crescator,2-descrescator): ";
            cin >> ord;
            if (ord == 1) {
                service.sortare_generica(cmp_nume_tip_cresc);
                afisare(service.get_all());
            }else if(ord == 2){
                service.sortare_generica(cmp_nume_tip_desc);
                afisare(service.get_all());
            }
            else
                cout << "Optiune invalida pentru ordinea de sortare\n";
        } else
            cout << "Optiune invalida pentru criteriul de sortare\n";
    }else
        cout<<"Nu se poate face sortare, lista este goala!\n";
}


void Ui::adauga_cos_ui() {
    string nume;
    cout<<"Introduceti numele produsului pe care doriti sa-l adaugati in cos: ";
    cin>>nume;
    try{
        service.adauga_cos(nume);
        cout<<"Produs adaugat in cos!\n";
    }catch (Exception& e){
        cout<<e.get_mesaj()<<"\n";
    }
}

void Ui::goleste_cos_ui() {
    service.goleste_cos();
    cout<<"Cosul a fost golit\n";
}

void Ui::populeaza_ui() {
    if(!service.get_all().empty()) {
        int dim;
        cout << "Introduceti numarul de produse pe care doriti sa-l adaugati: ";
        cin >> dim;
        service.populare_cos(dim);
        cout<<"Produse adaugate!\n";
    }else
        cout<<"Nu exista produse!\n";
}

void Ui::total_ui() {
    cout<<"Total: "<<service.get_total()<<"\n";
}


void Ui::start() {
    int close=1;
    print_menu();
    while(close){
        string cmd;
        cout<<"Introduceti o optiune: ";
        cin>>cmd;
        if(cmd=="e")
            close=0;
        else if(cmd=="m")
            print_menu();
        else if(cmd=="a")
            afisare(service.get_all());
        else if(cmd=="c")
            afisare(service.get_cos());
        else if(cmd=="r")
            populeaza_ui();
        else if(cmd=="s")
            save_to_file_ui();
        else if(cmd=="t")
            total_ui();
        else if(cmd=="g")
            goleste_cos_ui();
        else if(cmd=="u")
            undo_ui();
        else if(cmd=="1")
            add_ui();
        else if(cmd=="2")
            sterge_ui();
        else if(cmd=="3")
            modifica_ui();
        else if(cmd=="4")
            cauta_ui();
        else if(cmd=="5")
            filtrare_ui();
        else if(cmd=="6")
            sortare_ui();
        else if(cmd=="7")
            adauga_cos_ui();
        else
            cout<<"Optiune invalida\n";
    }
}

void Ui::save_to_file_ui() {
    cout<<"Introduceti numele fisierului: ";
    string file_name;
    cin>>file_name;
    try{
        service.save_to_file(file_name);
    }catch (Exception& e){
        cout<<e.get_mesaj()<<"\n";
    }
}

void Ui::undo_ui() {
    try{
        service.undo();
        cout<<"Undo realizat cu succes!\n";
    }catch (Exception& e){
        cout<<e.get_mesaj();
    }
}
