//
// Created by Titieni Paul on 25.06.2024.
//

#include "MasinaGui.h"

void MasinaGui::init_gui() {
    this->setLayout(main_ly);
    vw->setModel(tabel);
    vw->setSelectionMode(QAbstractItemView::MultiSelection);
    vw->setSelectionBehavior(QAbstractItemView::SelectRows);
    main_ly->addWidget(vw);

    auto* btn_ly = new QHBoxLayout;
    auto* btn2_ly = new QHBoxLayout;
    btn_ly->addWidget(minus);
    btn_ly->addWidget(sterge);
    btn_ly->addWidget(plus);
    btn2_ly->addWidget(undo);
    btn2_ly->addWidget(redo);
    main_ly->addLayout(btn_ly);
    main_ly->addLayout(btn2_ly);
}

void MasinaGui::connect() {
    QObject::connect(minus,&QPushButton::clicked,[this](){
       service.inc_an(-1);
       tabel->setMasini(service.get_all());
    });
    QObject::connect(plus,&QPushButton::clicked,[this](){
        service.inc_an(1);
        tabel->setMasini(service.get_all());
    });

    QObject::connect(undo,&QPushButton::clicked,[this](){
        try {
            service.do_undo();
            tabel->setMasini(service.get_all());
        }catch(exception){
            QMessageBox::warning(this,"Error","Nu se poate face undo!");
        }
    });
    QObject::connect(redo,&QPushButton::clicked,[this](){
        try {
            service.do_redo();
            tabel->setMasini(service.get_all());
        }catch(exception){
            QMessageBox::warning(this,"Error","Nu se poate face redo!");
        }
    });


    QObject::connect(sterge,&QPushButton::clicked,[this](){
        if(vw->selectionModel()->selectedIndexes().isEmpty())
            QMessageBox::warning(this,"Error","Nu s-a facut nicio selectie!");
        else{
            auto selectate = vw->selectionModel()->selectedIndexes();
            qDebug()<<selectate.size();
            for(int i=0;i<selectate.size();i+=4) {  //selecteaza fiecare casusa asa ca merg din 4 in 4
                auto index = selectate[i];
                if (index.isValid()) {
                    auto row = index.row();
                    auto cell1 = vw->model()->index(row, 0);
                    auto cell2 = vw->model()->index(row, 1);
                    auto cell3 = vw->model()->index(row, 2);
                    auto cell4 = vw->model()->index(row, 3);
                    auto id = vw->model()->data(cell1, Qt::DisplayRole).toString().toStdString();
                    auto denumire = vw->model()->data(cell2, Qt::DisplayRole).toString().toStdString();
                    auto an = vw->model()->data(cell3, Qt::DisplayRole).toString().toInt();
                    auto tip = vw->model()->data(cell4, Qt::DisplayRole).toString().toStdString();
                    if(!id.empty())
                        service.sterge(id,denumire,an,tip);
                }
            }
            tabel->setMasini(service.get_all());
        }
    });
}