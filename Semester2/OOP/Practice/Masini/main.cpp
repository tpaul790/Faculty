#include <iostream>
#include <QApplication>
#include "Teste/teste.h"
#include "Repo/RepoFile.h"
#include "Service/Service.h"
#include "GUI/MasinaGui.h"

int main(int argc, char* argv[]) {
    test_all();
    QApplication a{argc,argv};
    RepoFile repo{"file.txt"};
    Service service{repo};
    MasinaGui gui{service};
    gui.show();
    return QApplication::exec();
}
