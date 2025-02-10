
#ifndef PRACTIC_GUI_H
#define PRACTIC_GUI_H
#include "../Service/Service.h"
#include "../TableModel/TableModel.h"
#include "../MiniGUI/MiniGUI.h"
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

class GUI : public QWidget{
public:
    GUI(Service& s) : service{s} {
        init_gui();
        connect();
    };
private:
    Service& service;
    QHBoxLayout* main_ly = new QHBoxLayout;
    QTableView* vw = new QTableView;
    TableModel* tabel = new TableModel{service.get_all()};
    QLineEdit* adr_txt = new QLineEdit;
    QLineEdit* nrl_txt = new QLineEdit;
    QLineEdit* nrc_txt = new QLineEdit;
    QLineEdit* stare_txt = new QLineEdit;
    QPushButton* adauga = new QPushButton{"Adauga"};
    QPushButton* modifica = new QPushButton{"Modifica"};
    QPushButton* generare = new QPushButton{"Genereaza"};
    QPushButton* deschide = new QPushButton{"Deschide"};
    void init_gui();
    void connect();
};


#endif //PRACTIC_GUI_H
