cmake_minimum_required(VERSION 4.0)

set(CMAKE_CXX_SCAN_FOR_MODULES FALSE)

project(Subiect3_Bogdan)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED True)

set(CMAKE_C_COMPILER mpicc)
set(CMAKE_CXX_COMPILER mpicxx)

find_package(MPI REQUIRED)

add_executable(Subiect3_Bogdan main.cpp)

target_link_libraries(Subiect3_Bogdan PRIVATE MPI::MPI_CXX)