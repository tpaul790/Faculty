#include <iostream>
#include <QApplication>
#include "Teste/teste.h"
#include "Repo/RepoFile.h"
#include "Service/Service.h"
#include "GUI/GUI.h"
int main(int argc, char* argv[]) {
    test_all();
    QApplication a{argc,argv};

    RepoFile repo{"tractoare.txt"};
    Service service{repo};
    GUI gui{service};
    gui.show();
    return QApplication::exec();
}
