//
// Created by Titieni Paul on 21.05.2024.
//

#ifndef LAB6_OBSERVER_H
#define LAB6_OBSERVER_H

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
    void add(Observer* cos){
        inscrisi.push_back(cos);
    }

    void remove(Observer* cos){
        auto it = std::find(inscrisi.begin(),inscrisi.end(),cos);
        inscrisi.erase(it);
    }
protected:
    void notify(){
        for(const auto& u : inscrisi)
            u->update();
    }
};

#endif //LAB6_OBSERVER_H
