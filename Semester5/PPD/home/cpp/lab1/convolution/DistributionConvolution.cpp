#include "DistributionConvolution.h"

#include <iostream>

DistributionConvolution::DistributionConvolution(Matrix* matrix, Matrix* kernelMatrix, int P, DistributionType distributionType)
        : Convolution(matrix, kernelMatrix, P), type(distributionType) {}

Matrix* DistributionConvolution::convolve() {
        Matrix* convolvedMatrix = matrix->createSameSize();
        std::vector<std::thread> threads(P);

        if (type == DistributionType::LINEAR) {
                applyLinearDistribution(convolvedMatrix, threads);
        } else if (type ==  DistributionType::CYCLIC) {
                applyCyclicDistribution(convolvedMatrix, threads);
        } else {
                throw std::runtime_error("Invalid distribution type provided");
        }

        for (auto& thread: threads) {
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

void DistributionConvolution::applyLinearDistribution(Matrix *convolvedMatrix, std::vector<std::thread> &threads) const {
        int totalPixels = N * M;
        int pixelsPerThread = totalPixels / P, remainingPixels = totalPixels % P;
        int start, end = 0;

        for (int id = 0; id < P; id++) {
                start = end;
                end = start + pixelsPerThread;

                if (remainingPixels > 0) {
                        end++;
                        remainingPixels--;
                }

                int threadStart = start, threadEnd = end;
                threads[id] = std::thread([=] {
                        for (int pixel = threadStart; pixel < threadEnd; pixel++) {
                                int i = pixel / M;
                                int j = pixel % M;
                                convolvedMatrix->set(i, j, convolvePixel(i, j));
                        }
                });
        }
}

void DistributionConvolution::applyCyclicDistribution(Matrix *convolvedMatrix, std::vector<std::thread> &threads) const {
        for (int id = 0; id < P; id++) {
                int threadId = id;
                threads[id] = std::thread([=] {
                        for (int pixel = threadId; pixel < N * M; pixel += P) {
                                int i = pixel / M;
                                int j = pixel % M;
                                convolvedMatrix->set(i, j, convolvePixel(i, j));
                        }
                });
        }
}


