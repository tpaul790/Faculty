//
// Created by Titieni Paul on 09.05.2024.
//

#ifndef TEST1_GUI_H
#define TEST1_GUI_H

#include "../Service/PlacaService.h"
#include "../Service/ProcesorService.h"
#include <qwidget.h>
#include <qlineedit.h>
#include <qpushbutton.h>
#include <qlayout.h>
#include <qlabel.h>
#include <qlistwidget.h>
#include <qformlayout.h>
#include <qstring.h>
#include <qmessagebox.h>
#include <utility>

class GUI : public QWidget{
public:
    GUI(PlacaService ps,ProcesorService ps2) : placa_service(std::move(ps)), procesor_service(std::move(ps2)){
        init();
        load_procesoare();
        load_placi();
        connect();
    };
private:
    PlacaService placa_service;
    ProcesorService procesor_service;
    QListWidget* lista_procesoare=new QListWidget;
    QListWidget* lista_placi=new QListWidget;
    QLineEdit* nume_txt=new QLineEdit;
    QLineEdit* sP_txt=new QLineEdit;
    QLineEdit* pret_placa_txt=new QLineEdit;
    QLineEdit* pret_procesor_txt=new QLineEdit;
    QPushButton* add_btn=new QPushButton{"Adauga"};
    QPushButton* filtare_btn=new QPushButton{"FiltrareSoclu"};
    QPushButton* pret_total_btn=new QPushButton{"PretTotal"};
    QPushButton* exit_btn=new QPushButton{"Exit"};
    QHBoxLayout* main_ly=new QHBoxLayout;

    void init();

    void load_procesoare();

    void load_placi();

    void connect();
};


#endif //TEST1_GUI_H
