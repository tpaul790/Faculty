//
// Created by Titieni Paul on 09.05.2024.
//

#include "GUI.h"

void GUI::init() {
    setLayout(main_ly);
    auto l_proc=new QVBoxLayout;

    l_proc->addWidget(new QLabel{"Lista de Procesoare"});
    l_proc->addWidget(lista_procesoare);

    main_ly->addLayout(l_proc);

    auto l_placi=new QVBoxLayout;

    l_placi->addWidget(new QLabel{"Lista Placilor de Baza"});
    l_placi->addWidget(lista_placi);

    main_ly->addLayout(l_placi);

    auto drept=new QVBoxLayout;
    auto form=new QFormLayout;
    form->addRow("NumePlaca->",nume_txt);
    form->addRow("SocluPlaca->",sP_txt);
    form->addRow("PretPlaca->",pret_placa_txt);
    form->addRow("PretProcesor->",pret_procesor_txt);
    drept->addLayout(form);

    auto bt=new QVBoxLayout;
    bt->addWidget(add_btn);
    bt->addWidget(filtare_btn);
    bt->addWidget(pret_total_btn);
    bt->addWidget(exit_btn);

    drept->addLayout(bt);

    main_ly->addLayout(drept);
}

void GUI::load_procesoare() {
    lista_procesoare->clear();
    for(auto p : procesor_service.get_all()){
        string afis;
        auto nume = p.get_nume();
        auto nrT = p.get_nrTreduri();
        afis.append(nume);
        afis.append(" | ");
        afis.append(std::to_string(nrT));
        afis.append(" ");
        lista_procesoare->addItem(QString::fromStdString(afis));
    }
}

void GUI::load_placi() {
    lista_placi->clear();
    for(const auto& p : placa_service.get_all()){
        string afis;
        auto nume = p.get_nume();
        afis.append(nume);
        afis.append(" ");
        lista_placi->addItem(QString::fromStdString(afis));
    }
}

void GUI::connect() {
    QObject::connect(exit_btn,&QPushButton::clicked,[this](){
        this->close();
    });

    QObject::connect(add_btn,&QPushButton::clicked,[this](){
       try{
           auto nume=nume_txt->text().toStdString();
           auto sP=sP_txt->text().toInt();
           auto pret=pret_placa_txt->text().toInt();
           placa_service.add(nume,sP,pret);
           load_placi();
       } catch (exception&){
           QMessageBox::warning(this,"Error","Exista deja acest produs!");
       }
    });

    QObject::connect(filtare_btn,&QPushButton::clicked,[this](){
        auto local_w = new QWidget;
        auto ly=new QVBoxLayout;
        auto list=new QListWidget;
        auto close=new QPushButton{"Close"};
        ly->addWidget(new QLabel{"Placile filtrate dupa soclu"});
        ly->addWidget(list);
        ly->addWidget(close);
        local_w->setLayout(ly);
        for(const auto& p : placa_service.filtrare_sP(sP_txt->text().toInt())){
            list->addItem(QString::fromStdString(p.get_nume()+" "));
        }
        QObject::connect(close,&QPushButton::clicked,[local_w](){
            local_w->close();
        });
        local_w->show();
    });

    QObject::connect(lista_procesoare,&QListWidget::itemClicked,[this](){
        auto afis = lista_procesoare->selectedItems().at(0)->text().toStdString();
        vector<string> campuri;
        string sep= " ";
        while(afis.find(sep) != string::npos){
            auto poz=afis.find(sep);
            auto camp=afis.substr(0,poz);
            campuri.push_back(camp);
            afis.erase(0,poz+3);
        }
        auto nume=campuri[0];
        auto p = procesor_service.cauta(nume);
        pret_procesor_txt->setText(QString::fromStdString(std::to_string(p.get_pret())));

    });

    QObject::connect(lista_placi,&QListWidget::itemClicked,[this](){
        auto afis = lista_placi->selectedItems().at(0)->text().toStdString();
        vector<string> campuri;
        string sep= " ";
        while(afis.find(sep) != string::npos){
            auto poz=afis.find(sep);
            auto camp=afis.substr(0,poz);
            campuri.push_back(camp);
            afis.erase(0,poz+1);
        }
        auto nume=campuri[0];
        auto p=placa_service.cauta(nume);
        nume_txt->setText(QString::fromStdString(nume));
        sP_txt->setText(QString::fromStdString(std::to_string(p.get_socluProcesor())));
        pret_placa_txt->setText(QString::fromStdString(std::to_string(p.get_pret())));
    });

    QObject::connect(pret_total_btn,&QPushButton::clicked,[this](){
       auto pret_placa=pret_placa_txt->text().toInt();
       auto pret_proc=pret_procesor_txt->text().toInt();
       auto sum=pret_placa+pret_proc;
       auto afis ="Pretul total este: "+std::to_string(sum);
       QMessageBox::information(this,"Total",QString::fromStdString(afis));
    });
}
