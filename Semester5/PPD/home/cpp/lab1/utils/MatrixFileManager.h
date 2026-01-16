#ifndef MATRIXFILEMANAGER_H
#define MATRIXFILEMANAGER_H

#include <fstream>
#include <vector>

#include "../matrix/DynamicMatrix.h"
#include "../matrix/Matrix.h"
#include "../constants/MatrixType.h"
#include "../matrix/StaticMatrix.h"

class MatrixFileManager {
public:
    static bool fileExists(const std::string& filePath);

    static void generateInputMatrix(int N, int M, const std::string& filePath, int pixelValueBound = 255);

    static void generateInputKernel(int K, const std::string& filePath, int kernelValueBound = 3);

    static std::pair<Matrix*, Matrix*> readInput(const std::string& filePath, MatrixType matrixType);

    static void writeOutput(const Matrix* outputMatrix, const std::string& filePath);
};

#endif //MATRIXFILEMANAGER_H
