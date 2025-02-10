#include <iostream>
#include <QApplication>
#include "Teste/teste.h"
#include "Repo/RepoFile.h"
#include "Service/Service.h"
#include "GUI/ProdusGui.h"
#include "MiniGui/MiniGui.h"
#include <map>

int main(int argc, char* argv[]) {
    test_all();
    QApplication a{argc,argv};
    RepoFile repo{"file.txt"};
    Service service{repo};
    ProdusGui gui{service};
    gui.show();
    map<string, int> map;
    for(const auto& p : service.get_all())
        map[p.get_tip()]++;
    vector<MiniGui*> vect;
    int i=0;
    for(const auto& p : map){
        vect.push_back(new MiniGui{service,p.first});
        vect.at(i)->show();
        i++;
    }
    return QApplication::exec();
}
