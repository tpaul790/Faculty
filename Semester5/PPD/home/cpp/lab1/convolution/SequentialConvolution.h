#ifndef SEQUENTIALCONVOLUTION_H
#define SEQUENTIALCONVOLUTION_H

#include "Convolution.h"
#include "../matrix/Matrix.h"

class SequentialConvolution: public Convolution {
public:
    using Convolution::Convolution;

    Matrix* convolve() override;
};

#endif //SEQUENTIALCONVOLUTION_H
