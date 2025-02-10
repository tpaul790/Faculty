//
// Created by Titieni Paul on 19.05.2024.
//

#include "GUI.h"

void GUI::init() {
    this->setLayout(main_ly);
    main_ly->addWidget(lista);
    auto drept_ly = new QVBoxLayout;
    auto butoane = new QHBoxLayout;
    auto form = new QFormLayout;
    form->addRow("Producator Filtrare->",filtru_producator_txt);
    form->addRow("Pret Nou->",new_pret_txt);
    butoane->addWidget(sterge);
    butoane->addWidget(modifica);
    butoane->addWidget(filtrare);
    butoane->addWidget(sortare);
    butoane->addWidget(exit);
    drept_ly->addLayout(form);
    drept_ly->addLayout(butoane);
    main_ly->addLayout(drept_ly);
}

void GUI::load_data() {
    lista->clear();
    for(const auto& tr : service.get_all()){
        string afis;
        afis.append(tr.get_nume());
        afis.append(" | ");
        afis.append(tr.get_producator());
        afis.append(" | ");
        afis.append(to_string(tr.get_an()));
        afis.append(" | ");
        auto pret = tr.get_pret();
        afis.append(to_string(tr.get_pret()));
        afis.append(" | ");
        auto it = new QListWidgetItem(QString::fromStdString(afis));
        if(pret >= 9000)
            it->setBackground(Qt::red);
        else
            it->setBackground(Qt::green);
        lista->addItem(it);
        afis.clear();
    }
    load_tabel();
}

void GUI::load_tabel() {
    tabel->setColumnCount(4);
    tabel->setRowCount(service.get_all().size());
    QStringList header;
    header<<"Nume"<<"Producator"<<"An"<<"Pret";
    tabel->setHorizontalHeaderLabels(header);
    int i = 0;
    for(const auto& tr : service.get_all()){
        auto nume = QString::fromStdString(tr.get_nume());
        auto prod = QString::fromStdString(tr.get_producator());
        auto an = QString::fromStdString(to_string(tr.get_an()));
        auto pret = QString::fromStdString(to_string(tr.get_pret()));

        auto it1 = new QTableWidgetItem(nume);
        auto it2 = new QTableWidgetItem(prod);
        auto it3 = new QTableWidgetItem(an);
        auto it4 = new QTableWidgetItem(pret);

        tabel->setItem(i,0,it1);
        tabel->setItem(i,1,it2);
        tabel->setItem(i,2,it3);
        tabel->setItem(i,3,it4);
        i++;
    }
}

void GUI::connect() {
    QObject::connect(exit,&QPushButton::clicked,[this](){
        this->close();
    });

    QObject::connect(lista,&QListWidget::itemClicked,[this](){
       auto str = lista->selectedItems().at(0)->text().toStdString();
       string sep = " | ";
       auto poz = str.find(sep);
       vector<string> campuri;
       while(poz != string::npos){
           poz = str.find(sep);
           auto camp = str.substr(0,poz);
           campuri.push_back(camp);
           str.erase(0,poz+sep.length());
       }
       nume_txt->setText(QString::fromStdString(campuri[0]));
       producator_txt->setText(QString::fromStdString(campuri[1]));
       an_txt->setText(QString::fromStdString(campuri[2]));
       pret_txt->setText(QString::fromStdString(campuri[3]));
    });

    QObject::connect(sterge,&QPushButton::clicked,[this](){
        if(nume_txt->text() != "" and producator_txt->text() != "" and an_txt->text() != "" and pret_txt->text() != "") {
            auto nume = nume_txt->text().toStdString();
            auto producator = producator_txt->text().toStdString();
            auto an = an_txt->text().toInt();
            auto pret = pret_txt->text().toInt();
            service.sterge(nume, producator, an, pret);
            load_data();
        }
    });

    QObject::connect(modifica,&QPushButton::clicked,[this](){
        if(nume_txt->text() != "" and producator_txt->text() != "" and an_txt->text() != "" and pret_txt->text() != "" and new_pret_txt->text() != ""){
            auto nume = nume_txt->text().toStdString();
            auto producator = producator_txt->text().toStdString();
            auto an = an_txt->text().toInt();
            auto pret = pret_txt->text().toInt();
            auto new_pret = new_pret_txt->text().toInt();
            service.modifica(nume, producator, an, pret,new_pret);
            load_data();
        }
    });

    QObject::connect(filtrare,&QPushButton::clicked,[this](){
       auto main = new QWidget;
       auto ly = new QVBoxLayout;
       auto local_exit = new QPushButton{"Exit"};
       auto local_list = new QListWidget;
       main->setLayout(ly);
       ly->addWidget(new QLabel{"Lista tractoarelor filtrare dupa producatorul dat!"});
       ly->addWidget(local_list);
       ly->addWidget(local_exit);
       for(const auto& tr : service.filtru_producator(filtru_producator_txt->text().toStdString())){
           auto nume = tr.get_nume();
           auto prod = tr.get_producator();
           auto an = to_string(tr.get_an());
           auto pret = to_string(tr.get_pret());
           string afis;
           afis.append(nume);
           afis.append(" | ");
           afis.append(prod);
           afis.append(" | ");
           afis.append(an);
           afis.append(" | ");
           afis.append(pret);
           afis.append(" | ");
           local_list->addItem(QString::fromStdString(afis));
           afis.clear();
       }
       main->show();
       QObject::connect(local_exit,&QPushButton::clicked,[main](){
           main->close();
       });
    });

    QObject::connect(sortare,&QPushButton::clicked,[this](){
       service.sortare_pret();
       load_data();
    });
}