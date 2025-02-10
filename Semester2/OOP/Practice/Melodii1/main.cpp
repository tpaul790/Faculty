#include <iostream>
#include "../Teste/teste.h"
#include "../Repo/RepoFile.h"
#include "../Service/Service.h"
#include "../GUI/MelodieGui.h"
#include <QApplication>

int main(int argc, char* argv[]){
    test_all();
    QApplication a{argc,argv};
    RepoFile repo{"file.txt"};
    Service service{repo};
    MelodieGui gui{service};
    gui.show();
    return QApplication::exec();
}


