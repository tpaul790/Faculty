//
// Created by Titieni Paul on 27.06.2024.
//

#ifndef PRACTIC_MINIGUI_H
#define PRACTIC_MINIGUI_H
#include "../Service/Service.h"
#include "../TableModel/TableModel.h"
#include "../Observer/Observer.h"
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
#include <QGridLayout>
#include <QString>
#include <QChar>
#include <utility>

class MiniGUI : public QWidget,public Observer{
public:
    MiniGUI(Service& s,int nrl,int nrc,string  st) : service{s}, nrl{nrl}, nrc{nrc}, stare{std::move(st)} {
        init_gui();
        service.adauga(this);
    };

private:
    Service& service;
    QHBoxLayout* main_ly = new QHBoxLayout;
    int nrl;
    int nrc;
    string stare;
    vector<QPushButton*> butoane;
    QGridLayout* matrice = new QGridLayout;
    void init_gui();
    void connect();
    void update() override;
};


#endif //PRACTIC_MINIGUI_H
