cmake_minimum_required(VERSION 3.27)
project(lab1)

set(CMAKE_CXX_STANDARD 17)

add_executable(lab1 main.cpp
        constants/BatchType.h
        constants/ComputationMethod.h
        constants/DistributionType.h
        constants/MatrixType.h
        convolution/BatchConvolution.h
        matrix/Matrix.h
        matrix/DynamicMatrix.h
        matrix/StaticMatrix.h
        convolution/BatchConvolution.cpp
        convolution/BlockConvolution.h
        convolution/BlockConvolution.cpp
        convolution/DistributionConvolution.h
        convolution/DistributionConvolution.cpp
        convolution/SequentialConvolution.h
        convolution/SequentialConvolution.cpp
        utils/MatrixFileManager.h
        utils/MatrixFileManager.cpp
        utils/MatrixValidator.h
        utils/MatrixValidator.cpp
        utils/Mapper.h
        utils/Mapper.cpp)
