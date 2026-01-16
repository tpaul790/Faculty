#include <iostream>
#include <string>

#include "./convolution/BatchConvolution.h"
#include "./constants/MatrixType.h"
#include "./constants/DistributionType.h"
#include "./constants/ComputationMethod.h"
#include "./convolution/SequentialConvolution.h"
#include "./convolution/BlockConvolution.h"
#include "./convolution/DistributionConvolution.h"
#include "./utils/MatrixFileManager.h"
#include "./utils/MatrixValidator.h"
#include "utils/Mapper.h"

const std::string INPUT_DIR = "./inputs/";
const std::string OUTPUT_DIR = "./outputs/";
constexpr int TEST_CASES_COUNT = 5;
const int TEST_CASES[][TEST_CASES_COUNT] = {
        {10, 10, 3},
        {1000, 1000, 5},
        {10, 10000, 5},
        {10000, 10, 5},
        {10000, 10000, 5}
};

void generateTestCases() {
    try {
        for (const auto & i : TEST_CASES) {
            const int N = i[0];
            const int M = i[1];
            const int K = i[2];

            std::string fileName = INPUT_DIR + "input_" + std::to_string(N) + "_" + std::to_string(M) + "_" + std::to_string(K) + ".txt";
            std::cout << "Generating matrix and kernel for: " << fileName << std::endl;
            MatrixFileManager::generateInputMatrix(N, M, fileName);
            MatrixFileManager::generateInputKernel(K, fileName);
        }
    } catch (std::exception& e) {
        std::cerr << e.what() << std::endl;
    }
}

int main(const int argc, char* argv []) {
    // std::cout << "Current working directory: " << std::filesystem::current_path() << std::endl;
//     generateTestCases();

    if (argc < 5 || argc > 6) {
        std::cerr << "Usage: " << argv[0] << " <input_file> <matrix_type> <num_threads> <method> <type>" << std::endl;
        return 1;
    }
    try {
        const std::string inputFilePath = INPUT_DIR + std::string(argv[1]);
        const MatrixType matrixType = Mapper::stringToMatrixType(std::string(argv[2]));
        const std::string matrixTypeStr = std::string(argv[2]);
        int threadCount = std::stoi(argv[3]);
        const ComputationMethod method = Mapper::stringToComputationMethod(std::string(argv[4]));
        const auto methodStr = std::string(argv[4]);

        auto matrixies = MatrixFileManager::readInput(inputFilePath, matrixType);
        auto matrix = matrixies.first;
        auto kernel = matrixies.second;

        const int N = matrix->getRows();
        const int M = matrix->getCols();
        const int K = kernel->getRows();

        std::string outputPath;

        Convolution* convolution;
        if (method == ComputationMethod::SEQUENTIAL) {
            outputPath = OUTPUT_DIR + "output_" + methodStr + "_" + std::to_string(N) + "_" + std::to_string(M) + "_" + std::to_string(K) + "_" + matrixTypeStr + ".txt";
            convolution = new SequentialConvolution(matrix, kernel, 0);
        } else if (method == ComputationMethod::BATCH) {
            BatchType type = Mapper::stringToBatchType(std::string(argv[5]));
            const auto typeStr = std::string(argv[5]);
            outputPath = OUTPUT_DIR + "output_" + methodStr + "_" + typeStr + "_" + std::to_string(N) + "_" + std::to_string(M) + "_" + std::to_string(K) + "_" + matrixTypeStr + ".txt";
            convolution = new BatchConvolution(matrix, kernel, threadCount, type);
        } else if (method == ComputationMethod::BLOCK) {
            outputPath = OUTPUT_DIR + "output_" + methodStr + "_" + std::to_string(N) + "_" + std::to_string(M) + "_" + std::to_string(K) + "_" + matrixTypeStr + ".txt";
            convolution = new BlockConvolution(matrix, kernel, threadCount);
        } else {
            // ComputationMethod::DISTRIBUTION
            DistributionType type = Mapper::stringToDistributionType(std::string(argv[5]));
            const auto typeStr = std::string(argv[5]);
            outputPath = OUTPUT_DIR + "output_" + methodStr + "_" + typeStr + "_" + std::to_string(N) + "_" + std::to_string(M) + "_" + std::to_string(K) + "_" + matrixTypeStr + ".txt";
            convolution = new DistributionConvolution(matrix, kernel, threadCount, type);
        }

        auto startTime = std::chrono::high_resolution_clock::now();
        Matrix* outputMatrix = convolution->convolve();
        auto endTime = std::chrono::high_resolution_clock::now();

        MatrixFileManager::writeOutput(outputMatrix, outputPath);
        auto duration = std::chrono::duration<double, std::milli>(endTime - startTime).count();
        std::cout << duration << std::endl;

        if (method != ComputationMethod::SEQUENTIAL) {
            std::string validSequentialPath = OUTPUT_DIR + "output_" + "SEQUENTIAL" + "_" + std::to_string(N) + "_" + std::to_string(M) + "_" + std::to_string(K) + "_" + matrixTypeStr + ".txt";
            if (!MatrixValidator::validate(validSequentialPath, outputPath)) {
                throw std::runtime_error("Invalid output: Results do not match!");
            }
        }
        delete convolution;
    } catch (const std::exception& e) {
        std::cerr << e.what() << std::endl;
        return 1;
    }
    return 0;
}
