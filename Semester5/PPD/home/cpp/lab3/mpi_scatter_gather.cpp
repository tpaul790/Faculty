#include <iostream>
#include <fstream>
#include <string>
#include <algorithm>
#include <cmath>
#include <mpi.h>
#include "utils/FileManager.h"
#include <chrono>

int CARRY_TAG = 1;
int FINAL_CARRY_TAG = 2;

bool readNumberFile(const std::string& filePath, int& N, std::string& numberStr) {
    std::ifstream file(filePath);
    if (!file.is_open()) {
        std::cerr << "Eroare: Nu s-a putut deschide fisierul " << filePath << std::endl;
        return false;
    }
    std::string nStr;
    if (!(file >> nStr)) return false;
    N = std::stoi(nStr);

    numberStr.resize(N);
    char digitChar;
    for(int i=0; i < N; ++i) {
        if (!(file >> digitChar)) {
            N = i;
            numberStr.resize(N);
            break;
        }
        numberStr[i] = digitChar;
    }
    return true;
}

int addChunks(const int* digitChunk1, const int* digitChunk2, int* resultChunk, const int& chunkSize, int carryIn) {
    for (int i = 0; i < chunkSize; ++i) {
        int sum = digitChunk1[i] + digitChunk2[i] + carryIn;
        resultChunk[i] = sum % 10;
        carryIn = sum / 10;
    }
    return carryIn;
}

void writeResult(const std::string& file_path, int* fullResultDigitsPtr, int maxDigits, int finalCarry) {
    std::ofstream fout(file_path);
    if (!fout.is_open()) {
        std::cerr << "Eroare: Nu s-a putut scrie in fisierul " << file_path << std::endl;
        return;
    }
    if (finalCarry > 0) {
        fout << finalCarry;
    }
    int i = maxDigits - 1;
    while (i > 0 && fullResultDigitsPtr[i] == 0) {
        i--;
    }

    if (finalCarry == 0 && i == 0 && fullResultDigitsPtr[0] == 0) {
        fout << 0;
    } else {
        for (; i >= 0; i--) {
            fout << fullResultDigitsPtr[i];
        }
    }
    fout.close();
}


int main(int argc, char** argv) {
    if (argc < 4) {
        std::cerr << "Utilizare: " << argv[0] << " <input_file1> <input_file2> <output_file>" << std::endl;
        return 1;
    }

    MPI_Init(&argc, &argv);

    int size, rank;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    MPI_Status status;

    int maxDigits = 0;
    int chunkSize = 0;
    int finalCarry = 0;

    int* sendBuffer1 = nullptr;
    int* sendBuffer2 = nullptr;
    int* fullResultDigitsPtr = nullptr;

    std::string filePath1 = "./inputs/" + std::string(argv[1]) + ".txt";
    std::string filePath2 = "./inputs/" + std::string(argv[2]) + ".txt";
    const std::string outputPath = "./outputs/" + std::string(argv[3]) + ".txt";
    const std::string validSeqPath = "./outputs/" + std::string(argv[3]) + "_SEQUENTIAL.txt";

    auto startTime = std::chrono::high_resolution_clock::now();
    if (rank == 0) {
        int numDigits1, numDigits2;
        std::string str1, str2;

        if (!readNumberFile(filePath1, numDigits1, str1) || !readNumberFile(filePath2, numDigits2, str2)) {
            MPI_Abort(MPI_COMM_WORLD, 1);
            return 1;
        }

        int maxN = std::max(numDigits1, numDigits2);

        chunkSize = (int)std::ceil((double)maxN / size);
        if (chunkSize == 0) chunkSize = 1;
        maxDigits = chunkSize * size;

        sendBuffer1 = new int[maxDigits]();
        sendBuffer2 = new int[maxDigits]();
        fullResultDigitsPtr = new int[maxDigits]();

        for (int i = 0; i < numDigits1; ++i) {
            sendBuffer1[i] = str1[numDigits1 - 1 - i] - '0';
        }
        for (int i = 0; i < numDigits2; ++i) {
            sendBuffer2[i] = str2[numDigits2 - 1 - i] - '0';
        }
    }

    MPI_Bcast(&chunkSize, 1, MPI_INT, 0, MPI_COMM_WORLD);

    int logicChunkSize = chunkSize;
    auto digitChunk1 = new int[logicChunkSize]();
    auto digitChunk2 = new int[logicChunkSize]();
    auto resultChunk = new int[logicChunkSize]();

    MPI_Scatter(sendBuffer1, logicChunkSize, MPI_INT,
                digitChunk1, logicChunkSize, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Scatter(sendBuffer2, logicChunkSize, MPI_INT,
                digitChunk2, logicChunkSize, MPI_INT, 0, MPI_COMM_WORLD);

    int carryIn = 0;
    int carryOut = 0;

    if (rank > 0) {
        MPI_Recv(&carryIn, 1, MPI_INT, rank - 1, CARRY_TAG, MPI_COMM_WORLD, &status);
    }

    carryOut = addChunks(digitChunk1, digitChunk2, resultChunk, logicChunkSize, carryIn);

    if (rank < size - 1) {
        MPI_Send(&carryOut, 1, MPI_INT, rank + 1, CARRY_TAG, MPI_COMM_WORLD);
    }
    else {
        MPI_Send(&carryOut, 1, MPI_INT, 0, FINAL_CARRY_TAG, MPI_COMM_WORLD);
    }

    if( rank == 0 ){
        if (size > 1) {
            MPI_Recv(&finalCarry, 1, MPI_INT, size - 1, FINAL_CARRY_TAG, MPI_COMM_WORLD, &status);
        } else {
            finalCarry = carryOut;
        }
    }

    MPI_Gather(resultChunk, logicChunkSize, MPI_INT,
               fullResultDigitsPtr, logicChunkSize, MPI_INT, 0, MPI_COMM_WORLD);

    if (rank == 0) {
        const auto endTime = std::chrono::high_resolution_clock::now();
        const auto elapsedTime = std::chrono::duration<double, std::milli>(endTime - startTime).count();
        std::cout << elapsedTime << std::endl;

        writeResult(outputPath, fullResultDigitsPtr, maxDigits, finalCarry);

        std::cout << "Rezultatul a fost scris in " << outputPath << std::endl;

        if (!FileManager::compareResults(validSeqPath, outputPath)) {
            throw std::runtime_error("Invalid output: Results do not match!");
        }

        delete[] sendBuffer1;
        delete[] sendBuffer2;
        delete[] fullResultDigitsPtr;
    }

    delete[] digitChunk1;
    delete[] digitChunk2;
    delete[] resultChunk;

    MPI_Finalize();
    return 0;
}