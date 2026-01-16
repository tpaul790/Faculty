cmake_minimum_required(VERSION 4.0)

set(CMAKE_CXX_SCAN_FOR_MODULES FALSE)

project(sincron)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED True)

set(CMAKE_C_COMPILER mpicc)
set(CMAKE_CXX_COMPILER mpicxx)

find_package(MPI REQUIRED)

add_executable(sincron main.cpp)

target_link_libraries(sincron PRIVATE MPI::MPI_CXX)