#ifndef DISTRIBUTIONCONVOLUTION_H
#define DISTRIBUTIONCONVOLUTION_H

#include <vector>
#include <thread>

#include "Convolution.h"
#include "../constants/DistributionType.h"

class DistributionConvolution: public Convolution {
    DistributionType type;
public:
    DistributionConvolution(Matrix* matrix, Matrix* kernelMatrix, int P, DistributionType distributionType);

    Matrix* convolve() override;

private:
    void applyLinearDistribution(Matrix* convolvedMatrix, std::vector<std::thread>& threads) const;
    void applyCyclicDistribution(Matrix* convolvedMatrix, std::vector<std::thread>& threads) const;
};

#endif //DISTRIBUTIONCONVOLUTION_H
