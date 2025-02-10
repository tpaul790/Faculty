//
// Created by Titieni Paul on 24.06.2024.
//

#include "ProdusGui.h"

void ProdusGui::init_gui() {
    this->setLayout(main_ly);
    vw->setModel(tabel);
    main_ly->addWidget(vw);
    //layout uri
    auto* left_ly = new QVBoxLayout;
    auto* form = new QFormLayout;
    auto* slider_ly = new QHBoxLayout;
    form->addRow("Id->",id_txt);
    form->addRow("Nume->",nume_txt);
    form->addRow("Tip->",tip_txt);
    form->addRow("Pret->",pret_txt);
    //layout stang
    left_ly->addLayout(form);
    left_ly->addLayout(slider_ly);
    left_ly->addWidget(adauga);
    //slider
    slider->setMaximum(100);
    slider->setMinimum(1);
    slider_ly->addWidget(slider);
    slider_ly->addWidget(slider_lb);
    slider_lb->setText(QString::fromStdString(to_string(slider->value())));
    main_ly->addLayout(left_ly);
    vw->setSelectionBehavior(QAbstractItemView::SelectRows);
}

void ProdusGui::connect() {
    QObject::connect(adauga,&QPushButton::clicked,[this](){
       auto id = id_txt->text().toInt();
       auto nume = nume_txt->text().toStdString();
       auto tip = tip_txt->text().toStdString();
       auto pret = pret_txt->text().toDouble();
       try{
           service.add(id,nume,tip,pret);
           reload();
       }catch (exception){
           QMessageBox::warning(this,"Error","Exista deja un produs cu id ul dat!!");
       }catch (Exception& e){
           QMessageBox::warning(this,"Error",QString::fromStdString(e.get_ers()));
       }
    });

    QObject::connect(vw->selectionModel(),&QItemSelectionModel::selectionChanged,[this](){
        auto row = vw->selectionModel()->selectedIndexes().at(0).row();
        auto cell1 = vw->model()->index(row,0);
        auto cell2 = vw->model()->index(row,1);
        auto cell3 = vw->model()->index(row,2);
        auto cell4 = vw->model()->index(row,3);
        auto id = vw->model()->data(cell1,Qt::DisplayRole).toString();
        auto nume = vw->model()->data(cell2,Qt::DisplayRole).toString();
        auto tip = vw->model()->data(cell3,Qt::DisplayRole).toString();
        auto pret = vw->model()->data(cell4,Qt::DisplayRole).toString();
        id_txt->setText(id);
        nume_txt->setText(nume);
        tip_txt->setText(tip);
        pret_txt->setText(pret);
    });

    QObject::connect(slider,&QSlider::valueChanged,[this](){
        tabel->setSlider(slider->value());
        slider_lb->setText(QString::fromStdString(to_string(slider->value())));
    });
}
