
#include "GUI.h"

void GUI::init_gui() {
    this->setLayout(main_ly);
    vw->setModel(tabel);
    vw->setSelectionBehavior(QAbstractItemView::SelectRows);
    main_ly->addWidget(vw);
    //layout uri
    auto* left_ly = new QVBoxLayout;
    auto* form = new QFormLayout;
    form->addRow("Adresa->",adr_txt);
    form->addRow("NrLinii->",nrl_txt);
    form->addRow("NrColoane->",nrc_txt);
    form->addRow("Stare->",stare_txt);
    //layout drept
    left_ly->addLayout(form);
    left_ly->addWidget(adauga);
    left_ly->addWidget(modifica);
    left_ly->addWidget(generare);
    left_ly->addWidget(deschide);
    main_ly->addLayout(left_ly);
}

void GUI::connect() {
    QObject::connect(vw->selectionModel(),&QItemSelectionModel::selectionChanged,[this](){
        auto row = vw->selectionModel()->selectedIndexes().at(0).row();
        auto cell1 = vw->model()->index(row,0);
        auto cell2 = vw->model()->index(row,1);
        auto cell3 = vw->model()->index(row,2);
        auto cell4 = vw->model()->index(row,3);
        auto adr = vw->model()->data(cell1,Qt::DisplayRole).toString();
        auto nrl = vw->model()->data(cell2,Qt::DisplayRole).toString();
        auto nrc = vw->model()->data(cell3,Qt::DisplayRole).toString();
        auto stare = vw->model()->data(cell4,Qt::DisplayRole).toString();
        adr_txt->setText(adr);
        nrl_txt->setText(nrl);
        nrc_txt->setText(nrc);
        stare_txt->setText(stare);
    });

    QObject::connect(adauga,&QPushButton::clicked,[this](){
        if(adr_txt->text().isEmpty() or nrc_txt->text().isEmpty() or nrl_txt->text().isEmpty() or stare_txt->text().isEmpty())
            QMessageBox::warning(this,"Error","Va rog sa completati toate text field urile!!");
        else{
            auto adr = adr_txt->text().toStdString();
            auto nrl = nrl_txt->text().toInt();
            auto nrc = nrc_txt->text().toInt();
            auto st = stare_txt->text().toStdString();
            try{
                service.add(adr,nrl,nrc,st);
                tabel->setParcari(service.get_all());
            }catch(exception){
                QMessageBox::warning(this,"Error","Date sunt invalide sau exista deja aceasta parcare!!");
            }
        }
    });

    QObject::connect(modifica,&QPushButton::clicked,[this](){
        if(adr_txt->text().isEmpty() or nrc_txt->text().isEmpty() or nrl_txt->text().isEmpty() or stare_txt->text().isEmpty())
            QMessageBox::warning(this,"Error","Va rog sa completati toate text field urile!!");
        else{
            auto adr = adr_txt->text().toStdString();
            auto nrl = nrl_txt->text().toInt();
            auto nrc = nrc_txt->text().toInt();
            auto st = stare_txt->text().toStdString();
            try{
                service.modifica(adr,nrl,nrc,st);
                tabel->setParcari(service.get_all());
            }catch(exception){
                QMessageBox::warning(this,"Error","Date sunt invalide sau NU exista o parcare la adresa data!");
            }
        }
    });

    QObject::connect(generare,&QPushButton::clicked,[this](){
        if(nrc_txt->text().isEmpty() or nrl_txt->text().isEmpty())
            QMessageBox::warning(this,"Error","Va rog sa completati text field urile pentru linii si coloane!!");
        else{
            auto nrl = nrl_txt->text().toInt();
            auto nrc = nrc_txt->text().toInt();
            try{
                stare_txt->setText(QString::fromStdString(service.generare(nrl,nrc)));
            }catch(exception){
                QMessageBox::warning(this,"Error","Date sunt invalide!!!");
            }
        }
    });

    QObject::connect(deschide,&QPushButton::clicked,[this](){
        if(adr_txt->text().isEmpty() or nrc_txt->text().isEmpty() or nrl_txt->text().isEmpty() or stare_txt->text().isEmpty())
            QMessageBox::warning(this,"Error","Va rog sa completati toate text field urile!!");
        else {
            auto adr = adr_txt->text().toStdString();
            auto nrl = nrl_txt->text().toInt();
            auto nrc = nrc_txt->text().toInt();
            auto st = stare_txt->text().toStdString();
            MiniGUI *g1 = new MiniGUI{service, nrl, nrc, st};
            MiniGUI *g2 = new MiniGUI{service, nrl, nrc, st};
            g1->show();
            g2->show();
        }
    });
}