cmake_minimum_required(VERSION 3.27)
project(lab6)

set(CMAKE_CXX_STANDARD 14)

set(CMAKE_PREFIX_PATH "/Users/titienipaul/Downloads/Qt/6.8.0/macos/lib/cmake")

find_package(Qt6 COMPONENTS
        Core
        Gui
        Widgets
        REQUIRED)

add_executable(lab6
        Domain/produs.cpp
        Domain/produs.h
        Teste/tests.cpp
        Teste/tests.h
        main.cpp
        Repo/repo.cpp
        Repo/repo.h
        Exceptii/exceptions.h
        Service/service.cpp
        Service/service.h
        Ui/ui.cpp
        Ui/ui.h
        utils/utils.cpp
        utils/utils.h
        validari/validari.cpp
        validari/validari.h
        Cos/cos.h
        Cos/cos.cpp
        Undo/undo.h
        Undo/undo.cpp
        GUI/ProdusGUI.h
        GUI/CosGUI.h
        Observer/Observer.h
        Observer/Observer.cpp
        GUI/CosGUIRO.h
        ListModel/ListModel.h
        ListModel/ListModel.cpp
        TableModel/TableModel.h
        TableModel/TableModel.cpp
)

target_link_libraries(lab6
        Qt::Core
        Qt::Gui
        Qt::Widgets
)