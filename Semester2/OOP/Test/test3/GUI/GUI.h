//
// Created by Titieni Paul on 19.05.2024.
//

#ifndef TEST3_GUI_H
#define TEST3_GUI_H

#include "../Service/Service.h"
#include <qwidget.h>
#include <qlistwidget.h>
#include <qpushbutton.h>
#include <qlabel.h>
#include <qlayout.h>
#include <qlineedit.h>
#include <qformlayout.h>
#include <qstring.h>
#include <qtablewidget.h>

class GUI : public QWidget{
public:
    GUI(Service s) : service(s){
        init();
        connect();
        load_data();
    }
private:
    Service service;
    QHBoxLayout* main_ly = new QHBoxLayout;
    QListWidget* lista = new QListWidget;
    QTableWidget* tabel = new QTableWidget;
    QPushButton* exit = new QPushButton{"Exit"};
    QPushButton* sterge = new QPushButton{"Sterge"};
    QPushButton* modifica = new QPushButton{"Modifica"};
    QPushButton* filtrare = new QPushButton{"FiltrareProducator"};
    QPushButton* sortare = new QPushButton{"SortarePretCresc"};
    QLineEdit* filtru_producator_txt = new QLineEdit;
    QLineEdit* new_pret_txt = new QLineEdit;
    QLineEdit* producator_txt = new QLineEdit;
    QLineEdit* pret_txt = new QLineEdit;
    QLineEdit* an_txt = new QLineEdit;
    QLineEdit* nume_txt = new QLineEdit;

    void init();
    void connect();
    void load_data();
    void load_tabel();
};


#endif //TEST3_GUI_H
