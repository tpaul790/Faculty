//
// Created by Titieni Paul on 25.06.2024.
//

#ifndef MASINI_MASINAGUI_H
#define MASINI_MASINAGUI_H
#include "../Service/Service.h"
#include "../TableModel/TabelModel.h"
#include <QTableView>
#include <QPushButton>
#include <QLabel>
#include <QLineEdit>
#include <QWidget>
#include <QPushButton>
#include <QBoxLayout>
#include <QSlider>
#include <QFormLayout>
#include <QMessageBox>

class MasinaGui : public QWidget{
public:
    MasinaGui(Service& s) : service{s} {
        init_gui();
        connect();
    }
private:
    Service& service;
    QVBoxLayout* main_ly = new QVBoxLayout;
    QPushButton* sterge = new QPushButton{"Sterge"};
    QTableView* vw = new QTableView;
    TableModel* tabel = new TableModel{service.get_all()};
    QPushButton* plus = new QPushButton{"+"};
    QPushButton* minus = new QPushButton{"-"};
    QPushButton* undo = new QPushButton{"Undo"};
    QPushButton* redo = new QPushButton{"Redo"};
    void init_gui();
    void connect();
};


#endif //MASINI_MASINAGUI_H
