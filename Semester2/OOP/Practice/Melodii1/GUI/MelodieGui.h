//
// Created by Titieni Paul on 23.06.2024.
//

#ifndef MELODII1_MELODIEGUI_H
#define MELODII1_MELODIEGUI_H
#include "../Service/Service.h"
#include <QWidget>
#include <QLabel>
#include <QTableView>
#include <QTableWidget>
#include <QTableWidgetItem>
#include <QBoxLayout>
#include <QLineEdit>
#include <QPushButton>
#include <QSlider>
#include <QString>
#include <QStringList>
#include <QMessageBox>
#include <QPainter>
#include "../TabelModel/TabelModel.h"

class MelodieGui : public QWidget {
public:
    MelodieGui(Service& serv) : service{serv}{
        init_gui();
        load_data();
        connect();
    }

    void paintEvent(QPaintEvent* ev) override;

private:
    Service& service;
    QVBoxLayout* main_ly = new QVBoxLayout;
    TableModel* tabel = new TableModel{service.get_all()};
    QTableView* vw = new QTableView;
    QLineEdit* titlu_txt = new QLineEdit;
    QLineEdit* id_txt = new QLineEdit;
    QSlider* rank_txt = new QSlider{Qt::Horizontal};
    QPushButton* modifica = new QPushButton{"Modifica"};
    QPushButton* sterge = new QPushButton{"Sterge"};
    QSpacerItem *spacer = new QSpacerItem(420, 50, QSizePolicy::Minimum, QSizePolicy::Expanding);
    void init_gui();
    void load_data();
    void connect();
};


#endif //MELODII1_MELODIEGUI_H
