cmake_minimum_required(VERSION 3.27)
project(Set)

set(CMAKE_CXX_STANDARD 17)

add_executable(Set main.cpp
        Project/Iterator.cpp
        Project/Set.cpp
        Tests/LargeTest.cpp
        Tests/ShortTest.cpp)
