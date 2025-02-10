cmake_minimum_required(VERSION 3.27)
project(Practic)

set(CMAKE_CXX_STANDARD 17)

add_executable(Practic main.cpp
        Repo/RepoFile.h
        Repo/RepoFile.cpp
        Service/Service.h
        Service/Service.cpp
        TableModel/TableModel.h
        TableModel/TableModel.cpp
        Teste/teste.h
        Teste/teste.cpp
        GUI/GUI.h
        GUI/GUI.cpp
        Domain/Parcare.h
        Domain/Parcare.cpp
        MiniGUI/MiniGUI.h
        MiniGUI/MiniGUI.cpp
        Observer/Observer.h
        Observer/Observer.cpp
)

set(CMAKE_PREFIX_PATH "/Users/titienipaul/Downloads/Qt/6.8.0/macos/lib/cmake")

find_package(Qt6 COMPONENTS
        Core
        Gui
        Widgets
        REQUIRED)

target_link_libraries(Practic
        Qt::Core
        Qt::Gui
        Qt::Widgets
)