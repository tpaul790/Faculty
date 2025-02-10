//
// Created by Titieni Paul on 19.05.2024.
//

#include "GUI.h"
#include <QDebug>

void GUI::init() {
    this->setLayout(main_ly);
    main_ly->addWidget(lista);
    auto drept = new QVBoxLayout;
    auto butoane = new QVBoxLayout;
    auto form = new QFormLayout;
    form->addRow("Ateliere->",ateliere);
    butoane->addWidget(sortare);
    butoane->addWidget(info);
    butoane->addWidget(muzica);
    butoane->addWidget(fotografie);
    butoane->addWidget(jurnalism);
    butoane->addWidget(desen_pictura);
    drept->addLayout(form);
    drept->addLayout(butoane);
    main_ly->addLayout(drept);
}

void GUI::load_data() {
    lista->clear();
    for(auto el : service.get_all()){
        if(!el.get_nume().empty()) {
            string afis;
            afis.append(el.get_nume());
            afis.append(" | ");
            afis.append(el.get_scoala());
            afis.append(" | ");
            auto ats = el.get_ateliere();
            string at_str;
            for(auto at : ats)
                at_str.append(at),at_str.append(",");

            auto it = new QListWidgetItem;
            it->setText(QString::fromStdString(afis));
            it->setData(Qt::UserRole,QString::fromStdString(at_str));
            lista->addItem(it);
            afis.clear();
        }
    }
}

void GUI::connect() {
    QObject::connect(lista,&QListWidget::itemClicked,[this](){
        auto sel_it = lista->selectedItems().at(0);
        ateliere->setText(sel_it->data(Qt::UserRole).toString());
    });

    QObject::connect(sortare,&QPushButton::clicked,[this](){
        auto sortat = service.sortare_nume();
        service.incarca(sortat);
        load_data();
    });

    QObject::connect(info,&QPushButton::clicked,[this](){
        auto tabel = new QTableWidget;
        auto main = new QWidget;
        auto ly = new QVBoxLayout;
        auto local_exit = new QPushButton{"Exit"};
        main->setLayout(ly);
        ly->addWidget(new QLabel{"Lista elevilor filtrare dupa atelier!"});
        ly->addWidget(tabel);
        ly->addWidget(local_exit);
        tabel->setColumnCount(2);
        tabel->setRowCount(service.elevi_inscrisi("informatica").size());
        int i = 0;
        qDebug()<<service.elevi_inscrisi("informatica").size();
        for(auto el : service.elevi_inscrisi("informatica")){
            qDebug()<<el.get_nume()<<' ';
            auto it1 = new QTableWidgetItem(QString::fromStdString(el.get_nume()));
            auto it2 = new QTableWidgetItem(QString::fromStdString(el.get_scoala()));

            tabel->setItem(i,0,it1);
            tabel->setItem(i,1,it2);
            i++;
        }
        main->show();
        QObject::connect(local_exit,&QPushButton::clicked,[main](){
            main->close();
        });
    });

    QObject::connect(desen_pictura,&QPushButton::clicked,[this](){
        auto tabel = new QTableWidget;
        auto main = new QWidget;
        auto ly = new QVBoxLayout;
        auto local_exit = new QPushButton{"Exit"};
        main->setLayout(ly);
        ly->addWidget(new QLabel{"Lista elevilor filtrare dupa atelier!"});
        ly->addWidget(tabel);
        ly->addWidget(local_exit);
        tabel->setColumnCount(2);
        tabel->setRowCount(service.elevi_inscrisi("desen").size());
        int i = 0;
        for(auto el : service.elevi_inscrisi("desen")){
            auto it1 = new QTableWidgetItem(QString::fromStdString(el.get_nume()));
            auto it2 = new QTableWidgetItem(QString::fromStdString(el.get_scoala()));

            tabel->setItem(i,0,it1);
            tabel->setItem(i,1,it2);
            i++;
        }
        main->show();
        QObject::connect(local_exit,&QPushButton::clicked,[main](){
            main->close();
        });
    });

    QObject::connect(fotografie,&QPushButton::clicked,[this](){
        auto tabel = new QTableWidget;
        auto main = new QWidget;
        auto ly = new QVBoxLayout;
        auto local_exit = new QPushButton{"Exit"};
        main->setLayout(ly);
        ly->addWidget(new QLabel{"Lista elevilor filtrare dupa atelier!"});
        ly->addWidget(tabel);
        ly->addWidget(local_exit);
        tabel->setColumnCount(2);
        tabel->setRowCount(service.elevi_inscrisi("fotografie").size());
        int i = 0;
        for(auto el : service.elevi_inscrisi("fotografie")){
            auto it1 = new QTableWidgetItem(QString::fromStdString(el.get_nume()));
            auto it2 = new QTableWidgetItem(QString::fromStdString(el.get_scoala()));

            tabel->setItem(i,0,it1);
            tabel->setItem(i,1,it2);
            i++;
        }
        main->show();
        QObject::connect(local_exit,&QPushButton::clicked,[main](){
            main->close();
        });
    });

    QObject::connect(muzica,&QPushButton::clicked,[this](){
        auto tabel = new QTableWidget;
        auto main = new QWidget;
        auto ly = new QVBoxLayout;
        auto local_exit = new QPushButton{"Exit"};
        main->setLayout(ly);
        ly->addWidget(new QLabel{"Lista elevilor filtrare dupa atelier!"});
        ly->addWidget(tabel);
        ly->addWidget(local_exit);
        tabel->setColumnCount(2);
        tabel->setRowCount(service.elevi_inscrisi("muzica").size());
        int i = 0;
        for(auto el : service.elevi_inscrisi("muzica")){
            auto it1 = new QTableWidgetItem(QString::fromStdString(el.get_nume()));
            auto it2 = new QTableWidgetItem(QString::fromStdString(el.get_scoala()));

            tabel->setItem(i,0,it1);
            tabel->setItem(i,1,it2);
            i++;
        }
        main->show();
        QObject::connect(local_exit,&QPushButton::clicked,[main](){
            main->close();
        });
    });

    QObject::connect(jurnalism,&QPushButton::clicked,[this](){
        auto tabel = new QTableWidget;
        auto main = new QWidget;
        auto ly = new QVBoxLayout;
        auto local_exit = new QPushButton{"Exit"};
        main->setLayout(ly);
        ly->addWidget(new QLabel{"Lista elevilor filtrare dupa atelier!"});
        ly->addWidget(tabel);
        ly->addWidget(local_exit);
        tabel->setColumnCount(2);
        tabel->setRowCount(service.elevi_inscrisi("jurnalism").size());
        int i = 0;
        for(auto el : service.elevi_inscrisi("jurnalism")){
            auto it1 = new QTableWidgetItem(QString::fromStdString(el.get_nume()));
            auto it2 = new QTableWidgetItem(QString::fromStdString(el.get_scoala()));

            tabel->setItem(i,0,it1);
            tabel->setItem(i,1,it2);
            i++;
        }
        main->show();
        QObject::connect(local_exit,&QPushButton::clicked,[main](){
            main->close();
        });
    });

    QObject::connect(desen_pictura,&QPushButton::clicked,[this](){
        auto tabel = new QTableWidget;
        auto main = new QWidget;
        auto ly = new QVBoxLayout;
        auto local_exit = new QPushButton{"Exit"};
        main->setLayout(ly);
        ly->addWidget(new QLabel{"Lista elevilor filtrare dupa atelier!"});
        ly->addWidget(tabel);
        ly->addWidget(local_exit);
        tabel->setColumnCount(2);
        tabel->setRowCount(service.elevi_inscrisi("pictura").size());
        int i = 0;
        for(auto el : service.elevi_inscrisi("pictura")){
            auto it1 = new QTableWidgetItem(QString::fromStdString(el.get_nume()));
            auto it2 = new QTableWidgetItem(QString::fromStdString(el.get_scoala()));

            tabel->setItem(i,0,it1);
            tabel->setItem(i,1,it2);
            i++;
        }
        main->show();
        QObject::connect(local_exit,&QPushButton::clicked,[main](){
            main->close();
        });
    });
}