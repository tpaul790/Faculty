//
// Created by Titieni Paul on 27.06.2024.
//

#ifndef PRACTIC_OBSERVER_H
#define PRACTIC_OBSERVER_H

#include <vector>

using namespace std;

class Observer {
public:
    virtual void update() = 0;
};

class Observable{
private:
    vector<Observer*> inscrisi;
public:
    void adauga(Observer* obs){
        inscrisi.push_back(obs);
    }

    void remove(Observer* obs){
        auto it = std::find(inscrisi.begin(),inscrisi.end(),obs);
        inscrisi.erase(it);
    }

    void notify(){
        for(const auto& u : inscrisi)
            u->update();
    }
};

#endif //PRACTIC_OBSERVER_H
