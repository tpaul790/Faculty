cmake_minimum_required(VERSION 3.27)
project(lab10)

add_executable(lab10 main.cpp)

find_package(OpenMP REQUIRED)
if (OpenMP_FOUND)
    message(STATUS "OpenMP found and will be used.")
    target_link_libraries(lab10 PRIVATE OpenMP::OpenMP_CXX)
else()
    message(FATAL_ERROR "OpenMP not found.")
endif()

set(CMAKE_CXX_STANDARD 17)