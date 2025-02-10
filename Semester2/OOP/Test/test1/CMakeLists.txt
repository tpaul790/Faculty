cmake_minimum_required(VERSION 3.27)
project(test1)

set(CMAKE_CXX_STANDARD 17)

set(CMAKE_PREFIX_PATH "/Users/titienipaul/Downloads/Qt/6.8.0/macos/lib/cmake")

find_package(Qt6 COMPONENTS
        Core
        Gui
        Widgets
        REQUIRED)

add_executable(test1 main.cpp
        Domain/Procesor.h
        Domain/Procesor.cpp
        Teste/teste.cpp
        Teste/teste.h
        Domain/PlacaDeBaza.h
        Domain/PlacaDeBaza.cpp
        Repo/ProcesorInFileRepo.h
        Repo/ProcesorInFileRepo.cpp
        Repo/PlacaInFileRepo.h
        Repo/PlacaInFileRepo.cpp
        Service/ProcesorService.h
        Service/PlacaService.h
        Service/PlacaService.cpp
        Service/ProcesorService.cpp
        GUI/GUI.h
        GUI/GUI.cpp)

target_link_libraries(test1
        Qt::Core
        Qt::Gui
        Qt::Widgets
)