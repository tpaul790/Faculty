cmake_minimum_required(VERSION 3.27)
project(Lab2_4 C)

set(CMAKE_C_STANDARD 11)

add_executable(Lab2_4 main.c
        domain/domain.c
        repo/store.c
        service/manager.c
        teste/teste.c
        ui/ui.c
        validari/validator.c
        teste/teste.h)
