#include <iostream>
#include "Teste/teste.h"
#include <qapplication.h>
#include "GUI/GUI.h"
#include "Repo/RepoFile.h"
#include "Service/Service.h"

int main(int argc, char* argv[]) {
    test_all();
    RepoFile repo{"apartamente.txt"};
    Service service{repo};
    QApplication a(argc,argv);
    GUI gui{service};
    gui.show();
    return QApplication::exec();
}
