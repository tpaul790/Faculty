//
// Created by Titieni Paul on 26.06.2024.
//

#ifndef TRACTOARE_TRACTORGUI_H
#define TRACTOARE_TRACTORGUI_H
#include "../Service/Service.h"
#include "../TableModel/TableModel.h"
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
#include <QComboBox>
#include <QPainter>
#include <set>
#include <QMouseEvent>

class TractorGui : public QWidget{
public:
    TractorGui(Service& s) : service{s} {
        init_gui();
        connect();
        NrRoti=0;
    }
private:
    Service& service;
    QTableView* vw = new QTableView;
    TableModel* tabel = new TableModel{service.get_all()};
    QVBoxLayout* main_ly = new QVBoxLayout;
    QLineEdit* id_txt = new QLineEdit;
    QLineEdit* denumire_txt = new QLineEdit;
    QLineEdit* tip_txt = new QLineEdit;
    QLineEdit* nrRoti_txt = new QLineEdit;
    QComboBox* box = new QComboBox;
    QPushButton* adauga = new QPushButton{"Adauga"};
    QSpacerItem* spacer = new QSpacerItem{100,100};
    vector<pair<QPoint,int>> cercuri; // coordonatele centrului si raza cercului
    int NrRoti;
    void init_gui();
    void connect();
    void paintEvent(QPaintEvent* ev) override;
    bool isInCircle(const QPoint& point, const pair<QPoint,int>& cerc);
    void mousePressEvent(QMouseEvent* ev) override;
};


#endif //TRACTOARE_TRACTORGUI_H
