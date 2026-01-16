#include <iostream>
#include <fstream>
#include <string>

#include "./utils/FileManager.h"

int* readBigInteger(std::ifstream& inputFile, const int& numDigits) {
    auto numberArray = new int[numDigits];

    for (int i = numDigits - 1; i >= 0; i--) {
        char digitChar;
        inputFile >> digitChar;
        numberArray[i] = digitChar - '0';
    }
    return numberArray;
}

void readInput(const std::string& filePath, int* &N, int& numDigits) {
    std::ifstream inputFile(filePath);

    if (!inputFile.is_open()) {
        std::cerr << "Error: Could not open file " << filePath << std::endl;
        return;
    }

    inputFile >> numDigits;
    N = readBigInteger(inputFile, numDigits);
}

int main(const int argc, char** argv) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " <input_file1> <input_file2> <output_file>" << std::endl;
        return 1;
    }

    const std::string inputFilePath1 = "./inputs/" + std::string(argv[1]) + ".txt";
    const std::string inputFilePath2 = "./inputs/" + std::string(argv[2]) + ".txt";
    int *N1, *N2;
    int numDigits1, numDigits2;

    auto startTime = std::chrono::high_resolution_clock::now();
    readInput(inputFilePath1, N1, numDigits1);
    readInput(inputFilePath2, N2, numDigits2);

    int carry = 0;
    const int numDigitsResult = std::max(numDigits1, numDigits2);
    auto result = new int[numDigitsResult];

    for (int i = 0; i < numDigitsResult; i++) {
        int sum = carry;
        if (i < numDigits1) {
            sum += N1[i];
        }
        if (i < numDigits2) {
            sum += N2[i];
        }

        result[i] = sum % 10;
        carry = sum / 10;
    }
    auto endTime = std::chrono::high_resolution_clock::now();
    auto elapsedTime = std::chrono::duration<double, std::milli>(endTime - startTime).count();
    std::cout << elapsedTime << std::endl;

    const std::string outputPath = "./outputs/" + std::string(argv[3]) + "_SEQUENTIAL.txt";
    FileManager::writeResult(outputPath, result, numDigitsResult, carry, true);

    delete[] N1;
    delete[] N2;
    delete[] result;
    return 0;
}
