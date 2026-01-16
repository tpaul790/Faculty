#include <iostream>
#include <fstream>
#include <string>
#include <algorithm>
#include <mpi.h>
#include <vector>
#include "utils/FileManager.h"

int calculateChunkSize(const int rank, const int workersCount, const int numDigits) {
    if (workersCount <= 0) return 0;
    int chunkSize = numDigits / workersCount;
    if (rank <= (numDigits % workersCount)) {
        chunkSize++;
    }
    return chunkSize;
}

void reverse(int* digits, const int& len){
    for(int i = 0; i < len / 2; i++){
        int aux = digits[i];
        digits[i] = digits[len - i - 1];
        digits[len - i - 1] = aux;
    }
}

void readChunkAndReverse(const std::string &filePath, const int offset, const int& chunkSize, int* digits) {
    std::ifstream inputFile(filePath);
    if (!inputFile.is_open()) {
        std::cerr << "Eroare: Nu s-a putut deschide fisierul " << filePath << std::endl;
        MPI_Abort(MPI_COMM_WORLD, 1);
        return;
    }
    inputFile.seekg(offset, std::ios::beg);
    for (int i = 0; i < chunkSize; i++) {
        char digitChar;
        if (inputFile >> digitChar) {
            digits[i] = digitChar - '0';
        } else {
            digits[i] = 0;
        }
    }
    reverse(digits, chunkSize);
}

int addChunks(const int* digitChunk1, const int* digitChunk2, int* resultChunk, const int& chunkSize, int carryIn) {
    for (int i = 0; i < chunkSize; ++i) {
        int sum = digitChunk1[i] + digitChunk2[i] + carryIn;
        resultChunk[i] = sum % 10;
        carryIn = sum / 10;
    }
    return carryIn;
}


int main(int argc, char** argv) {
    if (argc < 4) {
        std::cerr << "Utilizare: " << argv[0] << " <input_file1> <input_file2> <output_file>" << std::endl;
        return 1;
    }

    MPI_Init(&argc, &argv);

    int CHUNK1_TAG = 0, CHUNK2_TAG = 1, CARRY_TAG = 2, RESULT_TAG = 3;

    int size, rank;
    MPI_Status status;
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    int workersCount = size - 1;

    auto startTime = std::chrono::high_resolution_clock::now();
    if (rank == 0) {
        std::string filePath1 = "./inputs/" + std::string(argv[1]) + ".txt";
        std::string filePath2 = "./inputs/" + std::string(argv[2]) + ".txt";

        std::ifstream inputFile1(filePath1);
        std::ifstream inputFile2(filePath2);

        if (!inputFile1 || !inputFile2) {
            std::cerr << "Eroare: Nu s-au putut deschide fisierele input." << std::endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
            return 1;
        }

        int numDigits1, numDigits2;
        std::string numDigits1Str, numDigits2Str;

        inputFile1 >> numDigits1Str;
        inputFile2 >> numDigits2Str;
        numDigits1 = std::stoi(numDigits1Str);
        numDigits2 = std::stoi(numDigits2Str);
        inputFile1.close(); inputFile2.close();

        int maxDigits = std::max(numDigits1, numDigits2);

        int initialOffset1 = static_cast<int>(numDigits1Str.length()) + 1;
        int initialOffset2 = static_cast<int>(numDigits2Str.length()) + 1;
        int currentOffset1 = initialOffset1, currentOffset2 = initialOffset2;

        MPI_Bcast(&numDigits1, 1, MPI_INT, 0, MPI_COMM_WORLD);
        MPI_Bcast(&numDigits2, 1, MPI_INT, 0, MPI_COMM_WORLD);

        std::vector<int*> sentBuffers;
        auto* sendRequests = new MPI_Request[workersCount * 2];
        int reqIdx = 0;

        int* chunkSizes1 = new int[workersCount + 1]();
        int* chunkSizes2 = new int[workersCount + 1]();
        int realDigitsRemainingN1 = numDigits1;
        int realDigitsRemainingN2 = numDigits2;
        for (int i = 1; i <= workersCount; i++) {
            int logicChunkSize = calculateChunkSize(i, workersCount, maxDigits);
            int readSize1 = std::min(logicChunkSize, realDigitsRemainingN1);
            chunkSizes1[i] = readSize1;
            realDigitsRemainingN1 -= readSize1;
            int readSize2 = std::min(logicChunkSize, realDigitsRemainingN2);
            chunkSizes2[i] = readSize2;
            realDigitsRemainingN2 -= readSize2;
        }

        for (int i = workersCount; i >= 1; i--) {
            int destRank = i;
            int readSize1 = chunkSizes1[destRank];
            int readSize2 = chunkSizes2[destRank];
            int logicChunkSize = calculateChunkSize(destRank, workersCount, maxDigits);

            auto digitChunk1 = new int[logicChunkSize]();
            auto digitChunk2 = new int[logicChunkSize]();

            readChunkAndReverse(filePath1, currentOffset1, readSize1, digitChunk1);
            readChunkAndReverse(filePath2, currentOffset2, readSize2, digitChunk2);

            MPI_Isend(digitChunk1, logicChunkSize, MPI_INT, destRank, CHUNK1_TAG, MPI_COMM_WORLD, &sendRequests[reqIdx++]);
            MPI_Isend(digitChunk2, logicChunkSize, MPI_INT, destRank, CHUNK2_TAG, MPI_COMM_WORLD, &sendRequests[reqIdx++]);

            // Salvam pointerii pentru a-i sterge mai tarziu
            sentBuffers.push_back(digitChunk1);
            sentBuffers.push_back(digitChunk2);

            currentOffset1 += readSize1;
            currentOffset2 += readSize2;
        }

        int* fullResultDigitsPtr = new int[maxDigits > 0 ? maxDigits : 1]();
        int resultOffset = 0;

        auto* recvRequests = new MPI_Request[workersCount];

        for (int i = 1; i <= workersCount; i++) {
            int logicChunkSize = calculateChunkSize(i, workersCount, maxDigits);

            MPI_Irecv(fullResultDigitsPtr + resultOffset, logicChunkSize, MPI_INT, i, RESULT_TAG, MPI_COMM_WORLD, &recvRequests[i - 1]);
            resultOffset += logicChunkSize;
        }

        MPI_Waitall(workersCount * 2, sendRequests, MPI_STATUSES_IGNORE);

        for (auto ptr : sentBuffers) {
            delete[] ptr;
        }

        MPI_Waitall(workersCount, recvRequests, MPI_STATUSES_IGNORE);

        int finalCarry = 0;
        MPI_Recv(&finalCarry, 1, MPI_INT, workersCount, CARRY_TAG, MPI_COMM_WORLD, &status);

        const auto endTime = std::chrono::high_resolution_clock::now();
        const auto elapsedTime = std::chrono::duration<double, std::milli>(endTime - startTime).count();
        std::cout << elapsedTime << std::endl;

        const std::string outputPath = "./outputs/" + std::string(argv[3]) + ".txt";
        const std::string validSeqPath = "./outputs/" + std::string(argv[3]) + "_SEQUENTIAL.txt";

        std::ofstream fout(outputPath);
        if (finalCarry > 0) {
            fout << finalCarry;
        } else if (resultOffset == 0) {
            fout << 0;
        }
        for (int i = resultOffset - 1; i >= 0; i--) {
            fout << fullResultDigitsPtr[i];
        }
        fout.close();


        if (!FileManager::compareResults(validSeqPath, outputPath)) {
             throw std::runtime_error("Invalid output: Results do not match!");
         }

        delete[] chunkSizes1;
        delete[] chunkSizes2;
        delete[] fullResultDigitsPtr;
        delete[] sendRequests;
        delete[] recvRequests;
    }
    else {
        int numDigits1, numDigits2;
        MPI_Bcast(&numDigits1, 1, MPI_INT, 0, MPI_COMM_WORLD);
        MPI_Bcast(&numDigits2, 1, MPI_INT, 0, MPI_COMM_WORLD);

        int maxDigits = std::max(numDigits1, numDigits2);
        int logicRank = rank;
        int logicChunkSize = calculateChunkSize(logicRank, workersCount, maxDigits);

        auto digitChunk1 = new int[logicChunkSize]();
        auto digitChunk2 = new int[logicChunkSize]();
        auto resultChunk = new int[logicChunkSize]();

        MPI_Request requests[3];
        MPI_Status statuses[3];
        int numRequests = 0;
        int carryIn = 0;

        MPI_Irecv(digitChunk1, logicChunkSize, MPI_INT, 0, CHUNK1_TAG, MPI_COMM_WORLD, &requests[numRequests++]); // req[0]
        MPI_Irecv(digitChunk2, logicChunkSize, MPI_INT, 0, CHUNK2_TAG, MPI_COMM_WORLD, &requests[numRequests++]); // req[1]

        if (rank > 1) {
            MPI_Irecv(&carryIn, 1, MPI_INT, rank - 1, CARRY_TAG, MPI_COMM_WORLD, &requests[numRequests++]); // req[2]
        }

        MPI_Waitall(numRequests, requests, statuses);

        int carryOut = addChunks(digitChunk1, digitChunk2, resultChunk, logicChunkSize, carryIn);

        if (rank < workersCount) {
            MPI_Send(&carryOut, 1, MPI_INT, rank + 1, CARRY_TAG, MPI_COMM_WORLD);
        } else {
            MPI_Send(&carryOut, 1, MPI_INT, 0, CARRY_TAG, MPI_COMM_WORLD);
        }

        MPI_Request resultSendRequest;
        MPI_Isend(resultChunk, logicChunkSize, MPI_INT, 0, RESULT_TAG, MPI_COMM_WORLD, &resultSendRequest);

        MPI_Wait(&resultSendRequest, MPI_STATUS_IGNORE);

        delete[] digitChunk1;
        delete[] digitChunk2;
        delete[] resultChunk;
    }

    MPI_Finalize();
    return 0;
}