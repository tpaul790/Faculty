//
// Created by Titieni Paul on 24.06.2024.
//

#ifndef PRODUSE_MINIGUI_H
#define PRODUSE_MINIGUI_H
#include "../Service/Service.h"
#include <QLabel>
#include "../Observer/Observer.h"
#include <QBoxLayout>
#include <utility>

class MiniGui : public Observer, public QWidget{
public:
    MiniGui(Service& s, string t) : service(s), tip{std::move(t)}{
        service.adauga(this);
        init_gui();
    };
private:
    Service& service;
    string tip;
    int cnt;
    QLabel* label = new QLabel;
    QVBoxLayout* main = new QVBoxLayout;
    void init_gui();
    void update() override;
    void calcul();
};


#endif //PRODUSE_MINIGUI_H
