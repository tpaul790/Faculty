#include "Ui/ui.h"
#include "Service/service.h"
#include "Repo/repo.h"
#include "Cos/cos.h"
#include "Teste/tests.h"
#include "GUI/ProdusGUI.h"
#include <QApplication>
int main(int argc, char** argv){
    //test_all();
    ProdusRepo repo;
    ProdusRepoFile repo_file("repo");
    CosProduse cos;
    Service service{ repo_file ,cos };
//    Ui ui{ service };
//    ui.start();
    QApplication a(argc,argv);
    ProdusGui gui{service,cos};
    gui.show();
    return QApplication::exec();
}