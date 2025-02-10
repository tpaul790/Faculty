cmake_minimum_required(VERSION 3.27)
project(Masini)

set(CMAKE_CXX_STANDARD 17)

add_executable(Masini main.cpp
        Domain/Masina.h
        Domain/Masina.cpp
        Teste/teste.h
        Teste/teste.cpp
        Repo/RepoFile.h
        Repo/RepoFile.cpp
        Service/Service.h
        Service/Service.cpp
        GUI/MasinaGui.h
        GUI/MasinaGui.cpp
        TableModel/TabelModel.h
        TableModel/TabelModel.cpp
)

set(CMAKE_PREFIX_PATH "/Users/titienipaul/Downloads/Qt/6.8.0/macos/lib/cmake")

find_package(Qt6 COMPONENTS
        Core
        Gui
        Widgets
        REQUIRED)

target_link_libraries(Masini
        Qt::Core
        Qt::Gui
        Qt::Widgets
)