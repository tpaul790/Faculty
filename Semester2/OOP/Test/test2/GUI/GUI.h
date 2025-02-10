//
// Created by Titieni Paul on 17.05.2024.
//

#ifndef TEST2_GUI_H
#define TEST2_GUI_H

#include "../Service/Service.h"
#include <qwidget>
#include <qlistwidget.h>
#include <qlayout.h>
#include "qlabel.h"
#include "qformlayout.h"
#include "qpushbutton.h"
#include "qlineedit.h"

class GUI : public QWidget{
public:
    GUI(Service serv) : service(serv){
        init();
        load_data();
        connect();
    }
private:
    Service service;
    QHBoxLayout* main_ly = new QHBoxLayout;
    QListWidget* lista = new QListWidget;
    QPushButton* sterge = new QPushButton{"Sterge"};
    QPushButton* filtrare_sup = new QPushButton{"FiltrareSuprafata"};
    QPushButton* filtrare_pret = new QPushButton{"FiltrarePret"};
    QPushButton* exit = new QPushButton{"Exit"};
    QLineEdit* suprafata_txt = new QLineEdit;
    QLineEdit* strada_txt = new QLineEdit;
    QLineEdit* pret_txt = new QLineEdit;
    QLineEdit* sup_min_txt = new QLineEdit;
    QLineEdit* sup_max_txt = new QLineEdit;
    QLineEdit* pret_min_txt = new QLineEdit;
    QLineEdit* pret_max_txt = new QLineEdit;
    void init();
    void load_data();
    void connect();
};


#endif //TEST2_GUI_H
