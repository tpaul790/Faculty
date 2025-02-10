//
// Created by Titieni Paul on 17.05.2024.
//

#include "GUI.h"
#include "qdebug.h"

void GUI::init() {
    this->setLayout(main_ly);
    main_ly->addWidget(lista);
    auto drept_ly = new QVBoxLayout;
    auto butoane = new QHBoxLayout;
    butoane->addWidget(sterge);
    butoane->addWidget(filtrare_pret);
    butoane->addWidget(filtrare_sup);
    butoane->addWidget(exit);

    auto form = new QFormLayout;
    form->addRow("Pret minim->",pret_min_txt);
    form->addRow("Pret maxim->",pret_max_txt);
    form->addRow("Suprafata minima->",sup_min_txt);
    form->addRow("Suprafata maxima->",sup_max_txt);

    drept_ly->addLayout(form);
    drept_ly->addLayout(butoane);

    main_ly->addLayout(drept_ly);
}

void GUI::load_data() {
    lista->clear();
    for(auto ap : service.get_all()){
        auto strada = ap.get_strada();
        auto sup = ap.get_suprafata();
        auto pret = ap.get_pret();
        string afis;
        afis.append(strada);
        afis.append(" | ");
        afis.append(sup);
        afis.append(" | ");
        afis.append(pret);
        afis.append(" | ");
        lista->addItem(QString::fromStdString(afis));
    }
}

void GUI::connect() {
    QObject::connect(exit,&QPushButton::clicked,[this](){
        this->close();
    });

    QObject::connect(sterge,&QPushButton::clicked,[this](){
        auto strada = strada_txt->text().toStdString();
        auto sup = suprafata_txt->text().toStdString();
        auto pret = pret_txt->text().toStdString();
        service.sterge(sup,strada,pret);
        load_data();
    });

    QObject::connect(filtrare_pret,&QPushButton::clicked,[this](){
        auto main = new QWidget;
        auto iesire = new QPushButton{"Exit"};

        auto ly = new QVBoxLayout;
        auto filtrate = new QListWidget;
        ly->addWidget(new QLabel{"Produsele cu preturi cuprinse in limita data!"});
        ly->addWidget(filtrate);
        ly->addWidget(iesire);
        main->setLayout(ly);

        for(auto ap : service.filtrare_pret(pret_min_txt->text().toStdString(),pret_max_txt->text().toStdString())){
            auto strada = ap.get_strada();
            auto sup = ap.get_suprafata();
            auto pret = ap.get_pret();
            string afis;
            afis.append(strada);
            afis.append(" | ");
            afis.append(sup);
            afis.append(" | ");
            afis.append(pret);
            afis.append(" ");
            filtrate->addItem(QString::fromStdString(afis));
        }
        main->show();
        QObject::connect(iesire,&QPushButton::clicked,[main](){
            main->close();
        });
    });

    QObject::connect(filtrare_sup,&QPushButton::clicked,[this](){
        auto main = new QWidget;
        auto iesire = new QPushButton{"Exit"};

        auto ly = new QVBoxLayout;
        auto filtrate = new QListWidget;
        ly->addWidget(new QLabel{"Produsele cu suprafata cuprinsa in limita data!"});
        ly->addWidget(filtrate);
        ly->addWidget(iesire);
        main->setLayout(ly);

        for(auto ap : service.filtrare_suprafata(sup_min_txt->text().toStdString(),sup_max_txt->text().toStdString())){
            auto strada = ap.get_strada();
            auto sup = ap.get_suprafata();
            auto pret = ap.get_pret();
            string afis;
            afis.append(strada);
            afis.append(" | ");
            afis.append(sup);
            afis.append(" | ");
            afis.append(pret);
            afis.append(" ");
            filtrate->addItem(QString::fromStdString(afis));
        }
        main->show();
        QObject::connect(iesire,&QPushButton::clicked,[main](){
            main->close();
        });
    });

    QObject::connect(this->lista,&QListWidget::itemClicked,[this](){
        auto item = lista->selectedItems().at(0)->text().toStdString();
        string sep = " | ";
        vector<string> campuri;
        while(item.find(sep) != string::npos){
            auto poz = item.find(sep);
            auto camp = item.substr(0,poz);
            campuri.push_back(camp);
            item.erase(0,poz+3);
        }
        strada_txt->setText(QString::fromStdString(campuri[0]));
        suprafata_txt->setText(QString::fromStdString(campuri[1]));
        pret_txt->setText(QString::fromStdString(campuri[2]));
    });
}



