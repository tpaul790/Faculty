cmake_minimum_required(VERSION 3.27)
project(test2)

set(CMAKE_CXX_STANDARD 17)

set(CMAKE_PREFIX_PATH "/Users/titienipaul/Downloads/Qt/6.8.0/macos/lib/cmake")

find_package(Qt6 COMPONENTS
        Core
        Gui
        Widgets
        REQUIRED)

add_executable(test2 main.cpp
        Domain/Apartament.h
        Teste/teste.cpp
        Teste/teste.h
        Repo/RepoFile.h
        Repo/RepoFile.cpp
        Domain/Apartament.cpp
        Service/Service.h
        Service/Service.cpp
        GUI/GUI.h
        GUI/GUI.cpp)

target_link_libraries(test2
        Qt::Core
        Qt::Gui
        Qt::Widgets
)