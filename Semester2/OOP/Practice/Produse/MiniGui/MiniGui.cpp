//
// Created by Titieni Paul on 24.06.2024.
//

#include "MiniGui.h"

void MiniGui::init_gui() {
    this->setLayout(main);
    this->setWindowTitle(QString::fromStdString(tip));
    this->resize(220,20);
    calcul();
    string nr = to_string(cnt);
    label->setText(QString::fromStdString(nr));
    main->addWidget(label);
}

void MiniGui::calcul(){
    cnt=0;
    for(const auto& e : service.get_all()){
        if(e.get_tip()==tip)
            cnt++;
    }
    string nr = to_string(cnt);
    label->setText(QString::fromStdString(nr));
}

void MiniGui::update() {
    calcul();
}