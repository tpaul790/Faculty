
#ifndef LAB6_PRODUSGUI_H
#define LAB6_PRODUSGUI_H
#include "../Service/service.h"
#include "../ListModel/ListModel.h"
#include "CosGUI.h"
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
#include <qlistview.h>

using namespace std;

class ProdusGui : public QWidget{
public:
    explicit ProdusGui(Service& serv,CosProduse& cos) : service { serv }, cos(cos){
        init_gui();
        load_data();
        connect_signals_slots();
    }

private:
    Service& service;
    CosProduse& cos;
    CosGui gui_cos{service,cos};
    vector<std::pair<QPushButton*,string>> butoane_lab;
    //QHBoxLayout* lab_ly=new QHBoxLayout;
//    QListWidget* lista_produse=new QListWidget;
    //--------------------lab14
    ListModel* lista_produse=new ListModel{service.get_all()};
    QListView* vw = new QListView;
    //----------------------
    QPushButton* cos_btn=new QPushButton("AccesareCos");
    QPushButton* store_button=new QPushButton("Adauga");
    QPushButton* delete_button=new QPushButton("Sterge");
    QPushButton* modify_button=new QPushButton("Modifica");
    QPushButton* undo_button=new QPushButton("Undo");
    QPushButton* exit_button=new QPushButton("Exit");
    QPushButton* sort_pret_cresc=new QPushButton("SortPretCresc");
    QPushButton* sort_pret_desc=new QPushButton("SortPretDesc");
    QPushButton* sort_name_cresc=new QPushButton("SortNumeCresc");
    QPushButton* sort_name_desc=new QPushButton("SortNumeDesc");
    QPushButton* sort_name_tip_cresc=new QPushButton("SortNumeTipCresc");
    QPushButton* sort_name_tip_desc=new QPushButton("SortNumeTipDesc");
    QPushButton* filtru_pret=new QPushButton("FiltruPret");
    QPushButton* filtru_nume=new QPushButton("FiltruNume");
    QPushButton* filtru_prod=new QPushButton("FiltruProducator");
    QLineEdit* nume_txt=new QLineEdit;
    QLineEdit* numar_txt=new QLineEdit;
    QLineEdit* tip_txt=new QLineEdit;
    QLineEdit* pret_txt=new QLineEdit;
    QLineEdit* producator_txt=new QLineEdit;
    QVBoxLayout* main_ly=new QVBoxLayout;
    QPushButton* adauga_cos=new QPushButton("AdaugaCos");
    QPushButton* goleste_cos=new QPushButton("GolesteCos");
    QPushButton* populare_cos=new QPushButton("PopulareCos");

    void init_gui(){
        auto lista_butoate1_form=new QHBoxLayout;
        this->setLayout(main_ly);

        auto ly_drept=new QVBoxLayout;
        auto form_campuri=new QFormLayout;
        form_campuri->addRow("Nume->",nume_txt);
        form_campuri->addRow("Tip->",tip_txt);
        form_campuri->addRow("Pret->",pret_txt);
        form_campuri->addRow("Producator->",producator_txt);
        form_campuri->addRow("Numar->",numar_txt);

        auto ly_butoane1=new QHBoxLayout;
        ly_butoane1->addWidget(store_button);
        ly_butoane1->addWidget(delete_button);
        ly_butoane1->addWidget(modify_button);
        ly_butoane1->addWidget(undo_button);
        ly_butoane1->addWidget(exit_button);

        ly_drept->addLayout(form_campuri);
        ly_drept->addLayout(ly_butoane1);

        auto cos_lista_ly=new QVBoxLayout;
        cos_lista_ly->addWidget(cos_btn);
//        cos_lista_ly->addWidget(lista_produse);
        //-----------lab14
        vw->setModel(lista_produse);
        cos_lista_ly->addWidget(vw);
        //-------------
        lista_butoate1_form->addLayout(cos_lista_ly);
        lista_butoate1_form->addLayout(ly_drept);

        auto butoane_service=new QVBoxLayout;
        auto ly_sort=new QHBoxLayout;
        ly_sort->addWidget(sort_pret_cresc);
        ly_sort->addWidget(sort_pret_desc);
        ly_sort->addWidget(sort_name_cresc);
        ly_sort->addWidget(sort_name_desc);
        ly_sort->addWidget(sort_name_tip_cresc);
        ly_sort->addWidget(sort_name_tip_desc);

        auto ly_filtru=new QHBoxLayout;
        ly_filtru->addWidget(filtru_pret);
        ly_filtru->addWidget(filtru_nume);
        ly_filtru->addWidget(filtru_prod);
        butoane_service->addLayout(ly_sort);
        ly_drept->addLayout(ly_filtru);

        auto ly_cos = new QHBoxLayout;
        ly_cos->addWidget(adauga_cos);
        ly_cos->addWidget(goleste_cos);
        ly_cos->addWidget(populare_cos);


        main_ly->addLayout(lista_butoate1_form);
        main_ly->addLayout(butoane_service);
        main_ly->addLayout(ly_cos);

        //main_ly->addLayout(lab_ly);

        //butoane_laborator();
    }

    void load_data(){
//        for(const auto& produs : service.get_all()){
//            string nume=produs.get_nume();
//            string tip=produs.get_tip();
//            string pret=to_string(produs.get_pret());
//            string producator=produs.get_producator();
//            string afis;
//            afis.append(nume);
//            afis.append(" | ");
//            afis.append(tip);
//            afis.append(" | ");
//            afis.append(pret);
//            afis.append(" | ");
//            afis.append(producator);
//            afis.append(" ");
//            lista_produse->addItem(QString::fromStdString(afis));
//        }
    }

    void connect_signals_slots() {
        QObject::connect(adauga_cos,&QPushButton::clicked,[this](){
            try {
                service.adauga_cos(nume_txt->text().toStdString());
            }catch(Exception& e){
                QMessageBox::warning(this,"Error",QString::fromStdString(e.get_mesaj()));
            }
        });

        QObject::connect(goleste_cos,&QPushButton::clicked,[this](){
            try {
                service.goleste_cos();
            }catch(Exception& e){
                QMessageBox::warning(this,"Error",QString::fromStdString(e.get_mesaj()));
            }
        });

        QObject::connect(populare_cos,&QPushButton::clicked,[this](){
            try {
                if(numar_txt->text().isEmpty())
                    throw Exception("Introduceti un numar!");
                service.populare_cos(numar_txt->text().toInt());
            }catch(Exception& e){
                QMessageBox::warning(this,"Error",QString::fromStdString(e.get_mesaj()));
            }
        });

        QObject::connect(store_button, &QPushButton::clicked, [this]() {
            auto nume = nume_txt->text().toStdString();
            auto tip = tip_txt->text().toStdString();
            auto pret = pret_txt->text().toInt();
            auto producator = producator_txt->text().toStdString();
            try {
                service.add(nume, tip, pret, producator);
                reload();
            } catch (Exception &e) {
                QMessageBox::warning(this, "Error", QString::fromStdString(e.get_mesaj()));
            }
        });

        QObject::connect(delete_button, &QPushButton::clicked, [this]() {
            auto nume = nume_txt->text().toStdString();
            auto tip = tip_txt->text().toStdString();
            auto pret = pret_txt->text().toInt();
            auto producator = producator_txt->text().toStdString();
            try {
                service.sterge_service(nume, tip, pret, producator);
                reload();
            } catch (Exception &e) {
                QMessageBox::warning(this, "Error", QString::fromStdString(e.get_mesaj()));
            }
        });

        QObject::connect(modify_button, &QPushButton::clicked, [this]() {
            static bool click = true;
            string nume = nume_txt->text().toStdString();
            string tip = tip_txt->text().toStdString();
            int pret = pret_txt->text().toInt();
            string producator = producator_txt->text().toStdString();
            try {
                service.cauta_service(nume, tip, pret, producator);
                auto local_w = new QWidget;
                auto new_form = new QFormLayout;
                auto *new_name = new QLineEdit;
                auto *new_tip = new QLineEdit;
                auto *new_pret = new QLineEdit;
                auto *new_prod = new QLineEdit;
                auto mesaj = new QVBoxLayout;
                new_form->addRow("Nume nou->", new_name);
                new_form->addRow("Tip nou->", new_tip);
                new_form->addRow("Pret nou->", new_pret);
                new_form->addRow("Producator nou->", new_prod);
                mesaj->addWidget(new QLabel{"Acest produs exista. Va rugam sa introduceti noile sale date!"});
                mesaj->addLayout(new_form);
                auto *confirm_button = new QPushButton{"Confirm"};
                mesaj->addWidget(confirm_button);
                local_w->setLayout(mesaj);
                local_w->show();
                QObject::connect(confirm_button, &QPushButton::clicked,
                                 [this, local_w, nume, tip, pret, producator, new_name, new_pret, new_prod, new_tip]() {
                                    try {
                                        service.modifica_service(nume, tip, pret, producator,
                                                                 new_name->text().toStdString(),
                                                                 new_tip->text().toStdString(),
                                                                 new_pret->text().toInt(),
                                                                 new_prod->text().toStdString());
                                        local_w->close();
                                        QMessageBox::information(this, "Succes", "Produs modificat cu succes!");
                                        reload();
                                    }catch (Exception& e){
                                        QMessageBox::warning(this, "Error", QString::fromStdString(e.get_mesaj()));
                                    }
                                 });

            } catch (Exception &e) {
                QMessageBox::warning(this, "Error", QString::fromStdString(e.get_mesaj()));
            }
        });

        QObject::connect(undo_button, &QPushButton::clicked, [this]() {
            try {
                service.undo();
                reload();
            } catch (Exception &e) {
                QMessageBox::warning(this, "Error", QString::fromStdString(e.get_mesaj()));
            }
        });

        QObject::connect(exit_button, &QPushButton::clicked, [this]() {
            this->close();
        });

//        QObject::connect(lista_produse, &QListWidget::itemClicked, [this]() {
//            if (lista_produse->selectedItems().empty()) {
//                nume_txt->setText("");
//                tip_txt->setText("");
//                pret_txt->setText("");
//                producator_txt->setText("");
//            }
//            auto item = lista_produse->selectedItems().at(0);
//            auto all = item->text().toStdString();
//            vector<string> campuri;
//            string sep = " ";
//            size_t poz;
//            while (all.find(sep) != string::npos) {
//                poz = all.find(sep);
//                auto camp = all.substr(0, poz);
//                campuri.push_back(camp);
//                all.erase(0, poz + sep.length() + 2);
//            }
//            auto nume = campuri[0];
//            auto tip = campuri[1];
//            auto pret = campuri[2];
//            auto producator = campuri[3];
//
//            nume_txt->setText(QString::fromStdString(nume));
//            tip_txt->setText(QString::fromStdString(tip));
//            pret_txt->setText(QString::fromStdString(pret));
//            producator_txt->setText(QString::fromStdString(producator));
//        });

        //lab14
        QObject::connect(vw->selectionModel(), &QItemSelectionModel::selectionChanged, [this]() {
            if (vw->selectionModel()->selectedIndexes().isEmpty()) return;
            auto item = vw->selectionModel()->selectedIndexes().at(0);
            auto all = item.data(Qt::DisplayRole).toString().toStdString();
            vector<string> campuri;
            string sep = " ";
            size_t poz;
            while (all.find(sep) != string::npos) {
                poz = all.find(sep);
                auto camp = all.substr(0, poz);
                campuri.push_back(camp);
                all.erase(0, poz + sep.length() + 2);
            }
            auto nume = campuri[0];
            auto tip = campuri[1];
            auto pret = campuri[2];
            auto producator = campuri[3];

            nume_txt->setText(QString::fromStdString(nume));
            tip_txt->setText(QString::fromStdString(tip));
            pret_txt->setText(QString::fromStdString(pret));
            producator_txt->setText(QString::fromStdString(producator));
        });

        QObject::connect(sort_pret_cresc, &QPushButton::clicked, [this]() {
            service.sortare_generica(cmp_pret_cresc);
            reload();
        });
        QObject::connect(sort_pret_desc, &QPushButton::clicked, [this]() {
            service.sortare_generica(cmp_pret_desc);
            reload();
        });
        QObject::connect(sort_name_cresc, &QPushButton::clicked, [this]() {
            service.sortare_generica(cmp_nume_cresc);
            reload();
        });
        QObject::connect(sort_name_desc, &QPushButton::clicked, [this]() {
            service.sortare_generica(cmp_nume_desc);
            reload();
        });
        QObject::connect(sort_name_tip_cresc, &QPushButton::clicked, [this]() {
            service.sortare_generica(cmp_nume_tip_cresc);
            reload();
        });
        QObject::connect(sort_name_tip_desc, &QPushButton::clicked, [this]() {
            service.sortare_generica(cmp_nume_tip_desc);
            reload();
        });

        QObject::connect(filtru_pret, &QPushButton::clicked, [this]() {
            auto w = new QWidget;
            auto local_ly = new QVBoxLayout;
            local_ly->addWidget(new QLabel{"Produele filtrate dupa pret!"});
            auto local_list = new QListWidget;
            local_ly->addWidget(local_list);
            auto close_btn = new QPushButton{"Close"};
            local_ly->addWidget(close_btn);
            w->setLayout(local_ly);
            try {
                vector<Produs> vect = service.filtrare_pret(pret_txt->text().toInt());
                for (const auto &produs: vect) {
                    string nume = produs.get_nume();
                    string tip = produs.get_tip();
                    string pret = to_string(produs.get_pret());
                    string producator = produs.get_producator();
                    string afis;
                    afis.append(nume);
                    afis.append(" | ");
                    afis.append(tip);
                    afis.append(" | ");
                    afis.append(pret);
                    afis.append(" | ");
                    afis.append(producator);
                    afis.append(" ");
                    local_list->addItem(QString::fromStdString(afis));
                }
                QObject::connect(close_btn, &QPushButton::clicked, [w]() {
                    w->close();
                });
                w->show();
            } catch (Exception &e) {
                QMessageBox::warning(this, "Error", QString::fromStdString(e.get_mesaj()));
            }
        });
        QObject::connect(filtru_nume, &QPushButton::clicked, [this]() {
            auto w = new QWidget;
            auto local_ly = new QVBoxLayout;
            local_ly->addWidget(new QLabel{"Produsele filtrate dupa nume!"});
            auto local_list = new QListWidget;
            local_ly->addWidget(local_list);
            auto close_btn = new QPushButton{"Close"};
            local_ly->addWidget(close_btn);
            w->setLayout(local_ly);
            try {
                vector<Produs> vect = service.filtrare_nume(nume_txt->text().toStdString());
                for (const auto &produs: vect) {
                    string nume = produs.get_nume();
                    string tip = produs.get_tip();
                    string pret = to_string(produs.get_pret());
                    string producator = produs.get_producator();
                    string afis;
                    afis.append(nume);
                    afis.append(" | ");
                    afis.append(tip);
                    afis.append(" | ");
                    afis.append(pret);
                    afis.append(" | ");
                    afis.append(producator);
                    afis.append(" ");
                    local_list->addItem(QString::fromStdString(afis));
                }
                QObject::connect(close_btn, &QPushButton::clicked, [w]() {
                    w->close();
                });
                w->show();
            } catch (Exception &e) {
                QMessageBox::warning(this, "Error", QString::fromStdString(e.get_mesaj()));
            }
        });
        QObject::connect(filtru_prod, &QPushButton::clicked, [this]() {
            auto w = new QWidget;
            auto local_ly = new QVBoxLayout;
            local_ly->addWidget(new QLabel{"Produele filtrate dupa producator!"});
            auto local_list = new QListWidget;
            local_ly->addWidget(local_list);
            auto close_btn = new QPushButton{"Close"};
            local_ly->addWidget(close_btn);
            w->setLayout(local_ly);
            try {
                vector<Produs> vect = service.filtrare_producator(producator_txt->text().toStdString());
                for (const auto &produs: vect) {
                    string nume = produs.get_nume();
                    string tip = produs.get_tip();
                    string pret = to_string(produs.get_pret());
                    string producator = produs.get_producator();
                    string afis;
                    afis.append(nume);
                    afis.append(" | ");
                    afis.append(tip);
                    afis.append(" | ");
                    afis.append(pret);
                    afis.append(" | ");
                    afis.append(producator);
                    afis.append(" ");
                    local_list->addItem(QString::fromStdString(afis));
                }
                QObject::connect(close_btn, &QPushButton::clicked, [w]() {
                    w->close();
                });
                w->show();
            } catch (Exception &e) {
                QMessageBox::warning(this, "Error", QString::fromStdString(e.get_mesaj()));
            }
        });

        QObject::connect(cos_btn,&QPushButton::clicked,[this](){
            gui_cos.show();
        });

//        for (const auto &bt: butoane_lab) {
//            QObject::connect(bt.first, &QPushButton::clicked, [this, bt]() {
//                auto nr = service.get_map()[bt.second];
//                string str = std::to_string(nr);
//                QMessageBox::information(this, "Info", QString::fromStdString(str));
//            });
//        }
    }

//    void butoane_laborator(){
//        for(const auto& val : service.get_map()){
//            auto* bt = new QPushButton(QString::fromStdString(val.first));
//            butoane_lab.push_back(make_pair(bt,val.first));
//            lab_ly->addWidget(bt);
//        }
//    }

    void reload(){
//        lista_produse->clear();
//        load_data();
        //----------lab14
        lista_produse->setProduse(service.get_all());
    }
};

#endif //LAB6_PRODUSGUI_H
