//
// Created by Titieni Paul on 19.05.2024.
//

#ifndef PARTIAL_GUI_H
#define PARTIAL_GUI_H
#include <qwidget.h>
#include "../Service/Service.h"

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
    GUI(Service serv) : service(serv){
        init();
        connect();
        load_data();
    }
private:
    Service service;
    QHBoxLayout* main_ly = new QHBoxLayout;
    QListWidget* lista = new QListWidget;
    QLineEdit* ateliere = new QLineEdit;
    QPushButton* sortare = new QPushButton{"SortareNume"};
    QPushButton* info = new QPushButton{"Informatica"};
    QPushButton* fotografie = new QPushButton{"Fotografie"};
    QPushButton* muzica = new QPushButton{"Muzica"};
    QPushButton* desen_pictura = new QPushButton{"Desen/Pictura"};
    QPushButton* jurnalism = new QPushButton{"Jurnalism"};


    void init();
    void connect();
    void load_data();
};


#endif //PARTIAL_GUI_H
