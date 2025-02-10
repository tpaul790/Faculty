#include "Teste/teste.h"
#include "Repo/PlacaInFileRepo.h"
#include "Repo/ProcesorInFileRepo.h"
#include "Service/ProcesorService.h"
#include "Service/PlacaService.h"
#include "GUI/GUI.h"
#include <qapplication.h>

int main(int  argc,char* argv[]) {
    test_all();
    PlacaInFileRepo placa_repo{"placi"};
    PlacaService placa_service{placa_repo};

    ProcesorInFileRepo procesor_repo{"procesoare"};
    ProcesorService procesor_service{procesor_repo};

    QApplication app(argc,argv);
    GUI gui{placa_service,procesor_service};
    gui.show();
    return QApplication::exec();
}
