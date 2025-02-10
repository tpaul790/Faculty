cmake_minimum_required(VERSION 3.27)
project(lab3)

set(CMAKE_CXX_STANDARD 17)

add_executable(lab3 main.cpp
        "TAD Multime/Proiect_C++/Multime.h"
        "TAD Multime/Proiect_C++/IteratorMultime.h"
        "TAD Multime/Proiect_C++/IteratorMultime.cpp"
        "TAD Multime/Proiect_C++/Multime.cpp"
        "TAD Multime/Teste/TestExtins.cpp"
        "TAD Multime/Teste/TestScurt.h"
        "TAD Multime/Teste/TestScurt.cpp")
