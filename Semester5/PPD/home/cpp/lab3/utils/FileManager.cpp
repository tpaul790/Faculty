#include "FileManager.h"

#include <iostream>

std::pair<int *, int> FileManager::readInput(const std::string &filePath) {
    std::ifstream inputFile(filePath);
    if (!inputFile.is_open()) {
        std::cerr << "Error: Could not open file " << filePath << std::endl;
        return {nullptr, 0};
    }

    int numDigits;
    inputFile >> numDigits;

    auto N = new int[numDigits];

    for (int i = 0; i < numDigits; i++) {
        char digitChar;
        inputFile >> digitChar;
        N[i] = digitChar - '0';
    }

    inputFile.close();
    return {N, numDigits};
}

void FileManager::writeResult(const std::string &filePath, const int *resultNumber, const int &numDigits, int carry,
                              bool reverse) {
    std::ofstream outputFile(filePath);
    if (!outputFile.is_open()) {
        std::cerr << "Error: Could not open file " << filePath << std::endl;
        return;
    }

    if (reverse) {
        if (carry) {
            outputFile << carry;
        }
        for (int i = numDigits - 1; i >= 0; i--) {
            outputFile << resultNumber[i];
        }
    } else {
        if (carry) {
            outputFile << carry;
        }
        for (int i = 0; i < numDigits; i++) {
            outputFile << resultNumber[i];
        }
    }
    outputFile.close();
}

bool FileManager::compareResults(const std::string &sequentialPath, const std::string &mpiPath) {
    std::ifstream sequentialFile(sequentialPath);
    std::ifstream mpiFile(mpiPath);

    if (!sequentialFile.is_open() || !mpiFile.is_open()) {
        std::cerr << "Error: Could not open file" << std::endl;
        return false;
    }

    std::string sequentialLine, mpiLine;
    while (std::getline(sequentialFile, sequentialLine) && std::getline(mpiFile, mpiLine)) {
        if (sequentialLine != mpiLine) {
            std::cerr << "Error: Sequential and MPI results do not match" << std::endl;
            return false;
        }
    }

    return true;
}

void FileManager::printBigInteger(const int *numberArray, const int numDigits, bool reverse) {
    if (reverse) {
        for (int i = numDigits - 1; i >= 0; i--) {
            std::cout << numberArray[i];
        }
    } else {
        for (int i = 0; i < numDigits; i++) {
            std::cout << numberArray[i];
        }
    }
}
