#include <iostream>
#include <string>
#include "utils/MatrixFileManager.h"
#include "convolution/InlineConvolution.h"
#include "convolution/InlineSequentialConvolution.h"
#include "convolution/InlineParallelConvolution.h"
#include "utils/MatrixValidator.h"


const std::string INPUT_DIR = "../inputs/";
const std::string OUTPUT_DIR = "../outputs/";

enum class ComputationMethod {
    INLINE_SEQUENTIAL,
    INLINE_PARALLEL
};

const int TEST_CASES[][5] = {
        {10000, 10000, 3},
};

void generateTestCases() {
    try {
        for (const auto & i : TEST_CASES) {
            const int N = i[0];
            const int M = i[1];
            const int K = i[2];

            std::string fileName = INPUT_DIR + "input_" + std::to_string(N) + "_" + std::to_string(M) + "_" + std::to_string(K) + ".txt";
            std::cout << "Generating matrix and kernel for: " << fileName << std::endl;
            MatrixFileManager::generateInputMatrix(N, M, fileName, 255);
            MatrixFileManager::generateInputKernel(K, fileName, 10);
        }
    } catch (std::exception& e) {
        std::cerr << e.what() << std::endl;
    }
}

int main(const int argc, char *argv[]) {
    // generateTestCases();

    if (argc < 2 || argc > 5) {
        std::cerr << "Usage: " << argv[0] << " <input_file> <num_threads> <method> <{type}>" << std::endl;
        return 1;
    }

    try {
        const std::string inputFilePath = INPUT_DIR + std::string(argv[1]);
        const int threadCount = std::stoi(argv[2]);
        const std::string methodStr = std::string(argv[3]);
        const ComputationMethod method = methodStr == "INLINE_SEQUENTIAL"
                                         ? ComputationMethod::INLINE_SEQUENTIAL
                                         : ComputationMethod::INLINE_PARALLEL;

        auto [matrix, kernel] = MatrixFileManager::readInput(inputFilePath);
        const int N = matrix.size();
        const int M = matrix[0].size();
        const int K = kernel.size();

        std::string outputPath;
        InlineConvolution *convolution;
        if (method == ComputationMethod::INLINE_SEQUENTIAL) {
            outputPath = OUTPUT_DIR + "output_" + methodStr + "_" + std::to_string(N) + "_" + std::to_string(M) + "_" +
                         std::to_string(K) + ".txt";
            convolution = new InlineSequentialConvolution(matrix, kernel);
        } else {
            // ComputationMethod::INLINE_PARALLEL
            const std::string typeStr = std::string(argv[4]);
            const SynchronizationType syncType = typeStr == "BARRIER"
                                                 ? SynchronizationType::BARRIER
                                                 : SynchronizationType::FLAG;
            outputPath = OUTPUT_DIR + "output_" + methodStr + "_" + typeStr + "_" + std::to_string(N) + "_" +
                         std::to_string(M) + "_" + std::to_string(K) + "_" + std::to_string(threadCount) + ".txt";
            convolution = new InlineParallelConvolution(matrix, kernel, threadCount, syncType);
        }

        auto startTime = std::chrono::high_resolution_clock::now();
        auto outputMatrix = convolution->convolve();
        auto endTime = std::chrono::high_resolution_clock::now();

        MatrixFileManager::writeOutput(outputMatrix, outputPath);
        auto duration = std::chrono::duration<double, std::milli>(endTime - startTime).count();
        std::cout << duration << std::endl;

        std::string validSeqPath = OUTPUT_DIR + "output_SEQUENTIAL_" + std::to_string(N) + "_" + std::to_string(M) + "_"
                                   + std::to_string(K) + ".txt";
        if (!MatrixValidator::validate(validSeqPath, outputPath)) {
            throw std::runtime_error("Invalid output: Results do not match!");
        }
        delete convolution;
    } catch (const std::exception &e) {
        std::cerr << e.what() << std::endl;
        return 1;
    }
    return 0;
}
