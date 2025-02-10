//
// Created by Titieni Paul on 23.06.2024.
//

#include "MelodieGui.h"

void MelodieGui::init_gui() {
    this->setLayout(main_ly);
    rank_txt->setRange(0,10);
    auto* text_bt_ly = new QVBoxLayout;
    auto* big_ly = new QHBoxLayout;
    vw->setModel(tabel);
    vw->setSelectionBehavior(QAbstractItemView::SelectRows);
    big_ly->addWidget(vw);
    text_bt_ly->addWidget(titlu_txt);
    text_bt_ly->addWidget(rank_txt);
    text_bt_ly->addWidget(modifica);
    text_bt_ly->addWidget(sterge);
    big_ly->addLayout(text_bt_ly);
    main_ly->addLayout(big_ly);
    main_ly->addItem(spacer);
}

void MelodieGui::load_data() {
    tabel->setProduse(service.get_all());
}

void MelodieGui::connect() {
    QObject::connect(vw->selectionModel(),&QItemSelectionModel::selectionChanged,[this](){
        auto sel_row = vw->selectionModel()->selectedIndexes().at(0).row();
        auto cell0 = vw->model()->index(sel_row,0);
        auto cell1 = vw->model()->index(sel_row,1);
        auto cell3=vw->model()->index(sel_row,3);
        auto id = vw->model()->data(cell0,Qt::DisplayRole).toString();
        auto titlu =vw->model()->data(cell1,Qt::DisplayRole).toString();
        auto rank = vw->model()->data(cell3,Qt::DisplayRole).toInt();
        id_txt->setText(id);
        titlu_txt->setText(titlu);
        rank_txt->setValue(rank);
    });

    QObject::connect(modifica,&QPushButton::clicked,[this](){
        try{
            service.modifica(id_txt->text().toInt(),titlu_txt->text().toStdString(),rank_txt->value());
            load_data();
            repaint();
        }catch (exception){
            QMessageBox::warning(this,"Error","Nu exista aceasta melodie!");
        }
    });

    QObject::connect(sterge,&QPushButton::clicked,[this](){
        try{
            service.sterge(id_txt->text().toInt());
            load_data();
            repaint();
        }catch (exception){
            QMessageBox::warning(this,"Error","Nu se poate sterge piesa!");
        }
    });
}

void MelodieGui::paintEvent(QPaintEvent* ev){
    QPainter p{ this };
    for(int i=0;i<=10;i++) {
        int x = (i + 1) * 40;
        p.drawLine(x, p.device()->height(), x, p.device()->height() - service.get_nr_rank(i) * 20);
    }
}
