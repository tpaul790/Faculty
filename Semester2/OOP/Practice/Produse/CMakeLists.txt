cmake_minimum_required(VERSION 3.27)
project(Produse)

set(CMAKE_CXX_STANDARD 17)

add_executable(Produse main.cpp
        Domain/Produs.h
        Domain/Produs.cpp
        Teste/teste.h
        Teste/teste.cpp
        Repo/RepoFile.h
        Repo/RepoFile.cpp
        Service/Service.h
        Service/Service.cpp
        Validari/validare.h
        Validari/validare.cpp
        Exception/Exception.h
        TabelModel/TableModel.h
        TabelModel/TableModel.cpp
        GUI/ProdusGui.h
        GUI/ProdusGui.cpp
        Observer/Observer.h
        Observer/Observable.h
        MiniGui/MiniGui.h
        MiniGui/MiniGui.cpp
)

set(CMAKE_PREFIX_PATH "/Users/titienipaul/Downloads/Qt/6.8.0/macos/lib/cmake")

find_package(Qt6 COMPONENTS
        Core
        Gui
        Widgets
        REQUIRED)

target_link_libraries(Produse
        Qt::Core
        Qt::Gui
        Qt::Widgets
)