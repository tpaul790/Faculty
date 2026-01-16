cmake_minimum_required(VERSION 3.27)
project(lab3)

set(CMAKE_CXX_STANDARD 17)

set(CMAKE_C_COMPILER mpicc)
set(CMAKE_CXX_COMPILER mpicxx)

find_package(MPI REQUIRED)

add_executable(lab3
        sequential.cpp
        utils/FileManager.h
        utils/FileManager.cpp
        mpi_send_recv_postcompute.cpp
        mpi_scatter_gather.cpp
        mpi_isend_irecv.cpp
        mpi_send_recv_precomputation.cpp)
