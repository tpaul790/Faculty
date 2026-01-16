cmake_minimum_required(VERSION 3.27)
project(ppd_lab2_in_place_convolution)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O3")
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/convolution)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/utils)

add_executable(lab2 main.cpp
        convolution/InlineConvolution.h
        convolution/InlineSequentialConvolution.cpp
        convolution/InlineSequentialConvolution.h
        convolution/InlineParallelConvolution.cpp
        convolution/InlineParallelConvolution.h
        utils/MatrixFileManager.cpp
        utils/MatrixFileManager.h
        utils/MatrixValidator.cpp
        utils/MatrixValidator.h
)
