//
// Created by Titieni Paul on 24.06.2024.
//

#ifndef PRODUSE_OBSERVABLE_H
#define PRODUSE_OBSERVABLE_H

#include "Observer.h"
#include <vector>
using std::vector;
class Observable {
private:
    vector<Observer*> inscrisi;
public:
    void adauga(Observer* obs){
        inscrisi.push_back(obs);
    }

    void notify(){
        for(const auto& obs : inscrisi)
            obs->update();
    }

    void sterge(Observer* obs){
        auto it = std::find(inscrisi.begin(),inscrisi.end(),obs);
        inscrisi.erase(it);
    }
};


#endif //PRODUSE_OBSERVABLE_H
