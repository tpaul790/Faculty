//
// Created by Titieni Paul on 26.06.2024.
//

#include "TractorGui.h"

void TractorGui::init_gui() {
    this->setLayout(main_ly);
    vw->setModel(tabel);
    vw->setSelectionBehavior(QAbstractItemView::SelectRows);
    auto* smain_ly = new QHBoxLayout;
    smain_ly->addWidget(vw);
    auto* btn_ly = new QHBoxLayout;
    auto* form_btn_ly = new QVBoxLayout;
    btn_ly->addWidget(adauga);
    btn_ly->addWidget(box);

    auto* form = new QFormLayout;
    form->addRow("Id->",id_txt);
    form->addRow("Denumire->",denumire_txt);
    form->addRow("Tip->",tip_txt);
    form->addRow("NrRoti->",nrRoti_txt);
    form_btn_ly->addLayout(form);
    form_btn_ly->addLayout(btn_ly);

    smain_ly->addLayout(form_btn_ly);

    main_ly->addLayout(smain_ly);
    main_ly->addItem(spacer);

    set<string> tipuri;
    for(const auto& tr : service.get_all())
        tipuri.insert(tr.get_tip());

    for(const auto& el : tipuri)
        box->addItem(QString::fromStdString(el));

    tabel->setTip(box->currentText().toStdString());
}

void TractorGui::connect() {
    QObject::connect(adauga,&QPushButton::clicked,[this](){
        if(id_txt->text().isEmpty() or denumire_txt->text().isEmpty() or tip_txt->text().isEmpty() or nrRoti_txt->text().isEmpty()) {
            QMessageBox::warning(this,"Error","Va rugam completati toate text field urile!!");
        }else{
            auto id = id_txt->text().toInt();
            auto denumire = denumire_txt->text().toStdString();
            auto tip = tip_txt->text().toStdString();
            auto nr = nrRoti_txt->text().toInt();
            try{
                service.add(id,denumire,tip,nr);
                tabel->setTractoare(service.get_all());
            }catch (exception){
                QMessageBox::warning(this,"Invalid","Exista deja un tractor cu acest id!!");
            }catch(Exception& e){
                QMessageBox::warning(this,"Invalid",QString::fromStdString(e.get_ers()));
            }
        }
    });

    QObject::connect(box,&QComboBox::currentIndexChanged,[this](){
        auto tip = box->currentText().toStdString();
        tabel->setTip(tip);
    });

    QObject::connect(vw->selectionModel(),&QItemSelectionModel::selectionChanged,[this](){
        auto row = vw->selectionModel()->selectedIndexes().at(0).row();
        auto cell0 = vw->model()->index(row,0);
        auto cell3 = vw->model()->index(row,3);
        NrRoti = vw->model()->data(cell3,Qt::DisplayRole).toInt();
        id_txt->setText(vw->model()->data(cell0,Qt::DisplayRole).toString());
        repaint();
    });
}

void TractorGui::paintEvent(QPaintEvent *ev) {
    QPainter p{this};
    cercuri.clear();
    for(int i=0;i<NrRoti;i++){
        cercuri.push_back(std::make_pair(QPoint{(i+1)*37,p.device()->height()-70},30));
        p.drawEllipse((i+1)*37,p.device()->height()-70,10*NrRoti,10*NrRoti);
    }

}

bool TractorGui::isInCircle(const QPoint& point, const pair<QPoint,int>& cerc){
    auto dx = (point.x()-cerc.first.x())*(point.x()-cerc.first.x());
    auto dy = (point.y()-cerc.first.y())*(point.y()-cerc.first.y());
    auto ds = dx+dy;
    return ds<=cerc.second*cerc.second;  //(x-x0)^2-(y-y0)^2<r^2
}

void TractorGui::mousePressEvent(QMouseEvent *ev) {
    for(const auto & cerc : cercuri)
        if(isInCircle(ev->pos(),cerc)){
            service.dec_nrRoti(id_txt->text().toInt());
            tabel->setTractoare(service.get_all());
            NrRoti-=2;
            repaint();
        }
}

