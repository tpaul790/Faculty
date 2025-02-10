cmake_minimum_required(VERSION 3.27)
project(Melodii1)

set(CMAKE_CXX_STANDARD 14)

include_directories(Domain)

set(CMAKE_PREFIX_PATH "/Users/titienipaul/Downloads/Qt/6.8.0/macos/lib/cmake")

find_package(Qt6 COMPONENTS
        Core
        Gui
        Widgets
        REQUIRED)

add_executable(Melodii1
        Domain/Melodie.cpp
        Domain/Melodie.h
        Teste/teste.h
        Teste/teste.cpp
        main.cpp
        Repo/RepoFile.h
        Repo/RepoFile.cpp
        Service/Service.h
        Service/Service.cpp
        TabelModel/TabelModel.h
        TabelModel/TabelModel.cpp
        GUI/MelodieGui.h
        GUI/MelodieGui.cpp)

target_link_libraries(Melodii1
        Qt::Core
        Qt::Gui
        Qt::Widgets
)