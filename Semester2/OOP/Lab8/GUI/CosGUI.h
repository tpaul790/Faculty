#ifndef LAB6_COSGUI_H
#define LAB6_COSGUI_H

#include "../Cos/cos.h"
#include "CosGUIRO.h"
#include "../Service/service.h"
#include "../Observer/Observer.h"
#include <qwidget.h>
#include <qlayout.h>
#include <qpushbutton.h>
#include <qlineedit.h>
#include <qboxlayout.h>
#include <qformlayout.h>
#include <qlistwidget.h>
#include <qstring.h>
#include <qobject.h>
#include <qdebug.h>
#include <qmessagebox.h>
#include <string>
#include "../utils/utils.h"
#include <qlabel.h>
#include <QTableView>
#include <QListView>
#include <utility>
#include <qtablewidget.h>
#include "../TableModel/TableModel.h"
#include "../ListModel/ListModel.h"

class CosGui : public QWidget, public Observer{
public:
    CosGui(Service& service, CosProduse& cos) : service { service }, cos(cos){
        init_gui();
        connect_signals_slots();
    }

private:
    Service& service;
    CosProduse& cos;
    QVBoxLayout* main_ly=new QVBoxLayout;
    QPushButton* store=new QPushButton{ "Adauga"};
    QPushButton* exit=new QPushButton{"Exit"};
    QPushButton* goleste=new QPushButton{"Goleste"};
    QPushButton* populare=new QPushButton{"Populare"};
    QPushButton* save_to_file=new QPushButton{"Salveaza"};
    QPushButton* cos_nou=new QPushButton{ "CosNou"};
    QPushButton* cos_rdo=new QPushButton{ "CosReadOnly"};
    QListView* vw2 = new QListView;
    ListModel* lista_cos=new ListModel{cos.get_all()};
//    QTableWidget* tabel_cos=new QTableWidget;
    TableModel* tabel_cos = new TableModel{cos.get_all()};
    QTableView* vw = new QTableView;
    QLineEdit* nume_txt=new QLineEdit;
    QLineEdit* fisier_txt=new QLineEdit;
    QLineEdit* numar_txt=new QLineEdit;

    void init_gui(){
        cos.add(this);
        this->setLayout(main_ly);
        auto butoane_ly=new QVBoxLayout;
        butoane_ly->addWidget(store);
        butoane_ly->addWidget(goleste);
        butoane_ly->addWidget(populare);
        butoane_ly->addWidget(save_to_file);
        butoane_ly->addWidget(exit);

        auto drept_ly=new QVBoxLayout;
        auto form_ly=new QFormLayout;
        form_ly->addRow("Nume->",nume_txt);
        form_ly->addRow("Fisier->",fisier_txt);
        form_ly->addRow("Numar->",numar_txt);

        drept_ly->addLayout(form_ly);
        drept_ly->addLayout(butoane_ly);

        auto lista_butoane_ly=new QHBoxLayout;
        auto bt_cos = new QHBoxLayout;
        auto tabel_btNou = new QVBoxLayout;
        bt_cos->addWidget(cos_nou);
        bt_cos->addWidget(cos_rdo);
        tabel_btNou->addLayout(bt_cos);
        vw->setModel(tabel_cos);
        vw2->setModel(lista_cos);
        tabel_btNou->addWidget(vw);
        lista_butoane_ly->addLayout(tabel_btNou);
        lista_butoane_ly->addLayout(drept_ly);

        main_ly->addLayout(lista_butoane_ly);
    }

    void load_tabel(){
//        tabel_cos->clear();
//        QStringList  header;
//        header<<"Nume"<<"Tip"<<"Pret"<<"Producator";
//        tabel_cos->setHorizontalHeaderLabels(header);
//        tabel_cos->setRowCount(service.get_cos().size());
//        tabel_cos->setColumnCount(4);
//        int nr_l=0;
//        for (const auto &produs: service.get_cos()) {
//            string nume = produs.get_nume();
//            string tip = produs.get_tip();
//            string pret = to_string(produs.get_pret());
//            string producator = produs.get_producator();
//            auto* item1=new QTableWidgetItem(QString::fromStdString(nume));
//            auto* item2=new QTableWidgetItem(QString::fromStdString(tip));
//            auto* item3=new QTableWidgetItem(QString::fromStdString(pret));
//            auto* item4=new QTableWidgetItem(QString::fromStdString(producator));
//            tabel_cos->setItem(nr_l,0,item1);
//            tabel_cos->setItem(nr_l,1,item2);
//            tabel_cos->setItem(nr_l,2,item3);
//            tabel_cos->setItem(nr_l,3,item4);
//            nr_l++;
//        }
    }
    void reload(){
        tabel_cos->setProduse(cos.get_all());
//        lista_cos->setProduse(cos.get_all());
        load_tabel();
    }

    void update() override{
        reload();
    }

    void load_data() {
//        lista_cos->clear();
//        for (const auto &produs: service.get_cos()) {
//            string nume = produs.get_nume();
//            string tip = produs.get_tip();
//            string pret = to_string(produs.get_pret());
//            string producator = produs.get_producator();
//            string afis;
//            afis.append(nume);
//            afis.append(" | ");
//            afis.append(tip);
//            afis.append(" | ");
//            afis.append(pret);
//            afis.append(" | ");
//            afis.append(producator);
//            afis.append(" ");
//            lista_cos->addItem(QString::fromStdString(afis));
//        }
    }

    void connect_signals_slots(){
        QObject::connect(store,&QPushButton::clicked,[this](){
            try {
                service.adauga_cos(nume_txt->text().toStdString());
            }catch(Exception& e){
                QMessageBox::warning(this,"Error",QString::fromStdString(e.get_mesaj()));
            }
        });

        QObject::connect(goleste,&QPushButton::clicked,[this](){
            try {
                service.goleste_cos();
            }catch(Exception& e){
                QMessageBox::warning(this,"Error",QString::fromStdString(e.get_mesaj()));
            }
        });

        QObject::connect(populare,&QPushButton::clicked,[this](){
            try {
                if(numar_txt->text().isEmpty())
                    throw Exception("Introduceti un numar!");
                service.populare_cos(numar_txt->text().toInt());
            }catch(Exception& e){
                QMessageBox::warning(this,"Error",QString::fromStdString(e.get_mesaj()));
            }
        });

        QObject::connect(save_to_file,&QPushButton::clicked,[this](){
            try {
                if(fisier_txt->text().isEmpty())
                    throw Exception("Introduceti un nume de fisier!");
                service.save_to_file(fisier_txt->text().toStdString());
                QMessageBox::information(this,"Succes","Produsele din cos au fost salvate in fisier!");
            }catch(Exception& e){
                QMessageBox::warning(this,"Error",QString::fromStdString(e.get_mesaj()));
            }
        });

        QObject::connect(exit,&QPushButton::clicked,[this](){
           this->close();
           //cos.remove(this);
        });

        QObject::connect(cos_nou,&QPushButton::clicked,[this](){
           auto cos_gui = new CosGui{service,cos};
           cos_gui->show();
        });

        QObject::connect(cos_rdo,&QPushButton::clicked,[this](){
            auto cos_nou_rdo = new CosGUIRO{cos};
            cos_nou_rdo->show();
        });
    }
};


#endif //LAB6_COSGUI_H
