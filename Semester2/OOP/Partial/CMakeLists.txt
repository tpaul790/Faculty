cmake_minimum_required(VERSION 3.27)
project(Partial)

set(CMAKE_CXX_STANDARD 17)

set(CMAKE_PREFIX_PATH "/Users/titienipaul/Downloads/Qt/6.8.0/macos/lib/cmake")

find_package(Qt6 COMPONENTS
        Core
        Gui
        Widgets
        REQUIRED)

add_executable(Partial main.cpp
        GUI/GUI.h
        Repo/RepoFile.h
        Repo/RepoFile.cpp
        GUI/GUI.cpp
        Service/Service.h
        Service/Service.cpp
        Teste/teste.h
        Teste/teste.cpp
        Domain/Elev.h
        Domain/Elev.cpp
        Utils/utils.cpp)

target_link_libraries(Partial
        Qt::Core
        Qt::Gui
        Qt::Widgets
)
