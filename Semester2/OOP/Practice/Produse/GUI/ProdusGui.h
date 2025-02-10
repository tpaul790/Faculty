//
// Created by Titieni Paul on 24.06.2024.
//

#ifndef PRODUSE_PRODUSGUI_H
#define PRODUSE_PRODUSGUI_H
#include "../Service/Service.h"
#include "../TabelModel/TableModel.h"
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

class ProdusGui : public QWidget{
public:
    ProdusGui(Service& ser) : service(ser) {
        init_gui();
        connect();
    };
private:
    Service& service;
    QHBoxLayout* main_ly = new QHBoxLayout;
    QTableView* vw = new QTableView;
    TableModel* tabel = new TableModel{service.get_all()};
    QLineEdit* id_txt = new QLineEdit;
    QLineEdit* nume_txt = new QLineEdit;
    QLineEdit* tip_txt = new QLineEdit;
    QLineEdit* pret_txt = new QLineEdit;
    QPushButton* adauga = new QPushButton{"Adauga"};
    QSlider* slider = new QSlider{Qt::Horizontal};
    QLabel* slider_lb = new QLabel;

    void init_gui();
    void reload(){
        tabel->setProduse(service.get_all());
    }
    void connect();
};


#endif //PRODUSE_PRODUSGUI_H
