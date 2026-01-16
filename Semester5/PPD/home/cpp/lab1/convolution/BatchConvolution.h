#ifndef BATCHCONVOLUTION_H
#define BATCHCONVOLUTION_H

#include <vector>
#include <thread>

#include "Convolution.h"
#include "../constants/BatchType.h"
#include "../matrix/Matrix.h"

class BatchConvolution: public Convolution {
    BatchType type;

public:
    BatchConvolution(Matrix* matrix, Matrix* kernelMatrix, int P, BatchType batchType);

    Matrix* convolve() override;

private:
    void applyRowBatching(Matrix* convolvedMatrix, std::vector<std::thread>& threads) const;
    void applyColumnBatching(Matrix* convolvedMatrix, std::vector<std::thread>& threads) const;
};

#endif //BATCHCONVOLUTION_H
