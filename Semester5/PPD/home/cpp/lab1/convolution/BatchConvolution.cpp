#include "BatchConvolution.h"

#include <iostream>

BatchConvolution::BatchConvolution(Matrix* matrix, Matrix* kernelMatrix, int P, BatchType batchType)
    : Convolution(matrix, kernelMatrix, P), type(batchType) {}

Matrix* BatchConvolution::convolve() {
    Matrix* convolvedMatrix = matrix->createSameSize();
    std::vector<std::thread> threads(P);

    if (type == BatchType::ROWS) {
        applyRowBatching(convolvedMatrix, threads);
    } else if (type == BatchType::COLUMNS) {
        applyColumnBatching(convolvedMatrix, threads);
    } else {
        throw std::runtime_error("Invalid batch type provided");
    }

    for (auto& thread : threads) {
        try {
            if (thread.joinable()) {
                thread.join();
            }
        } catch (const std::exception& e) {
            std::cerr << e.what() << std::endl;
        }
    }
    return convolvedMatrix;
}

void BatchConvolution::applyRowBatching(Matrix * convolvedMatrix, std::vector<std::thread> & threads) const {
    const int rowsPerThread = N / P;
    int remainingRows = N % P;
    int start, end = 0;

    for (int id = 0; id < P; ++id) {
        start = end;
        end = start + rowsPerThread;
        if (remainingRows > 0) {
            ++end;
            --remainingRows;
        }

        int threadStart = start, threadEnd = end;
        threads[id] = std::thread([=] {
            for (int i = threadStart; i < threadEnd; ++i) {
                for (int j = 0; j < M; ++j) {
                    convolvedMatrix->set(i, j, convolvePixel(i, j));
                }
            }
        });
    }
}

void BatchConvolution::applyColumnBatching(Matrix * convolvedMatrix, std::vector<std::thread> & threads) const {
    const int colsPerThread = M / P;
    int remainingCols = M % P;
    int start, end = 0;

    for (int id = 0; id < P; ++id) {
        start = end;
        end = start + colsPerThread;
        if (remainingCols > 0) {
            ++end;
            --remainingCols;
        }

        int threadStart = start, threadEnd = end;
        threads[id] = std::thread([=] {
            for (int j = threadStart; j < threadEnd; ++j) {
                for (int i = 0; i < N; ++i) {
                        convolvedMatrix->set(i, j, convolvePixel(i, j));
                }
            }
        });
    }
}
