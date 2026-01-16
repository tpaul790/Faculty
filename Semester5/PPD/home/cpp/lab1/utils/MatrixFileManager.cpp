
#include <iostream>
#include <random>
#include <string>
#include <filesystem>
#include <stdexcept>

#include "MatrixFileManager.h"

namespace fs = std::filesystem;

bool MatrixFileManager::fileExists(const std::string& filePath) {
    return fs::exists(filePath);
}

void MatrixFileManager::generateInputMatrix(int N, int M, const std::string& filePath, int pixelValueBound) {
    std::ofstream outFile(filePath);
    if (!outFile) {
        throw std::runtime_error("Cannot open file: " + filePath);
    }

    outFile << N << " " << M << std::endl;
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> dist(0, pixelValueBound);

    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j) {
            outFile << dist(gen) << " ";
        }
        outFile << std::endl;
    }

    outFile.close();
}

void MatrixFileManager::generateInputKernel(int K, const std::string& filePath, int kernelValueBound) {
    std::ofstream outFile(filePath, std::ios_base::app); // append to the existing file
    if (!outFile) {
        throw std::runtime_error("Cannot open file: " + filePath);
    }

    outFile << K << std::endl;
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> dist(0, kernelValueBound);

    for (int i = 0; i < K; ++i) {
        for (int j = 0; j < K; ++j) {
            outFile << dist(gen) << " ";
        }
        outFile << std::endl;
    }

    outFile.close();
}

std::pair<Matrix*, Matrix*> MatrixFileManager::readInput(const std::string& filePath, MatrixType matrixType) {
    std::ifstream inFile(filePath);
    if (!inFile) {
        throw std::runtime_error("Cannot open file: " + filePath);
    }

    int N, M;
    inFile >> N >> M;
    Matrix* valuesMatrix;
    if (matrixType == MatrixType::DYNAMIC) {
        valuesMatrix = new DynamicMatrix(N, M);
    } else {
        valuesMatrix = new StaticMatrix(N, M);
    }

    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j) {
            int value;
            inFile >> value;
            valuesMatrix->set(i, j, value);
        }
    }

    int K;
    inFile >> K;
    Matrix* kernelMatrix;
    if (matrixType == MatrixType::DYNAMIC) {
        kernelMatrix = new DynamicMatrix(K, K);
    } else {
        kernelMatrix = new StaticMatrix(K, K);
    }

    for (int i = 0; i < K; ++i) {
        for (int j = 0; j < K; ++j) {
            int value;
            inFile >> value;
            kernelMatrix->set(i, j, value);
        }
    }

    inFile.close();
    return {valuesMatrix, kernelMatrix};
}

void MatrixFileManager::writeOutput(const Matrix* outputMatrix, const std::string& filePath) {
    std::ofstream outFile(filePath);
    if (!outFile) {
        throw std::runtime_error("Cannot open file: " + filePath);
    }

    for (int i = 0; i < outputMatrix->getRows(); ++i) {
        for (int j = 0; j < outputMatrix->getCols(); ++j) {
            outFile << outputMatrix->get(i, j) << " ";
        }
        outFile << std::endl;
    }

    outFile.close();
}