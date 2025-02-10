cmake_minimum_required(VERSION 3.27)
project(lab4)

set(CMAKE_CXX_STANDARD 17)

add_executable(lab4 main.cpp
        "TAD Multi-dictionar/Proiect c++/IteratorMD.h"
        "TAD Multi-dictionar/Proiect c++/MD.h"
        "TAD Multi-dictionar/Proiect c++/MD.cpp"
        "TAD Multi-dictionar/Proiect c++/IteratorMD.cpp"
        "TAD Multi-dictionar/Teste/TestExtins.h"
        "TAD Multi-dictionar/Teste/TestExtins.cpp"
        "TAD Multi-dictionar/Teste/TestScurt.cpp")
