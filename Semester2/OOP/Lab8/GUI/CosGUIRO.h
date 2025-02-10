//
// Created by Titieni Paul on 21.05.2024.
//

#ifndef LAB6_COSGUIRO_H
#define LAB6_COSGUIRO_H

#include "../Observer/Observer.h"
#include "../Cos/cos.h"
#include <qwidget.h>
#include <qboxlayout.h>
#include <qpainter.h>
#include <random>

class CosGUIRO : public QWidget, public Observer{
public:
    CosGUIRO(CosProduse& c) : cos(c){
        init();
    };
private:
    CosProduse& cos;
    QHBoxLayout* main_ly = new QHBoxLayout;
    void init(){
        cos.add(this);
        this->setLayout(main_ly);
    }

    void paintEvent(QPaintEvent* ev) override{
        QPainter p{this};
        for(const auto& prod : cos.get_all()){
            auto x = rand()%150+20;
            auto y = rand()%150+20;
            auto w = rand()%150+20;
            auto h = rand()%150+20;
            p.drawRect(x,y,w,h);
        }

    }

    void update() override{
        repaint();
    }
};


#endif //LAB6_COSGUIRO_H
