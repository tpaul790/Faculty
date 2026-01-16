#ifndef FILEMANAGER_H
#define FILEMANAGER_H

#include <fstream>

class FileManager {
public:
    static std::pair<int*, int> readInput(const std::string& filePath);

    static void writeResult(const std::string& filePath, const int* resultNumber, const int& numDigits, int carry, bool reverse = true);

    static bool compareResults(const std::string& sequentialPath, const std::string& mpiPath);

    static void printBigInteger(const int* numberArray, int numDigits, bool reverse = false);
};

#endif //FILEMANAGER_H