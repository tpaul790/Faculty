//
// Created by Titieni Paul on 27.06.2024.
//

#include "MiniGUI.h"

void MiniGUI::init_gui() {
    this->setLayout(main_ly);
    main_ly->addLayout(matrice);
    int cnt = 0;
    for(int i=0;i<nrl;i++) {
        for (int j = 0; j < nrc; j++) {
            auto c = QChar::fromLatin1(stare[cnt]);
            auto *btn = new QPushButton{c};
            butoane.push_back(btn);
            QObject::connect(btn,&QPushButton::clicked,[cnt,btn,this](){
                if(btn->text().toStdString()=="X") {
                    btn->setText("-");
                    stare[cnt]='-';
                }
                else{
                    btn->setText("X");
                    stare[cnt]='X';
                }
                service.notify();
            });
            matrice->addWidget(btn,i,j);
            cnt++;
        }
    }
}

void MiniGUI::update() {
    int cnt=0;
    for(auto& bt : butoane) {
        auto c = QChar::fromLatin1(stare[cnt]);
        bt->setText(c);
        cnt++;
    }
}

