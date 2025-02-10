cmake_minimum_required(VERSION 3.27)
project(Tractoare)

set(CMAKE_CXX_STANDARD 17)

add_executable(Tractoare main.cpp
        Domain/Tractor.h
        Domain/Tractor.cpp
        Teste/teste.h
        Teste/teste.cpp
        Repo/RepoFile.h
        Repo/RepoFile.cpp
        Service/Service.h
        Service/Service.cpp
        Exception.h
        TableModel/TableModel.h
        TableModel/TableModel.cpp
        GUI/TractorGui.h
        GUI/TractorGui.cpp)

set(CMAKE_PREFIX_PATH "/Users/titienipaul/Downloads/Qt/6.8.0/macos/lib/cmake")

find_package(Qt6 COMPONENTS
        Core
        Gui
        Widgets
        REQUIRED)

target_link_libraries(Tractoare
        Qt::Core
        Qt::Gui
        Qt::Widgets
)