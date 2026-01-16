cmake_minimum_required(VERSION 4.0)

set(CMAKE_CXX_SCAN_FOR_MODULES FALSE)

project(Subiect1_Vladiela)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED True)

set(CMAKE_C_COMPILER mpicc)
set(CMAKE_CXX_COMPILER mpicxx)

find_package(MPI REQUIRED)

add_executable(Subiect1_Vladiela main.cpp)

target_link_libraries(Subiect1_Vladiela PRIVATE MPI::MPI_CXX)