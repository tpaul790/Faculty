#ifndef BLOCKCONVOLUTION_H
#define BLOCKCONVOLUTION_H

#include "Convolution.h"

class BlockConvolution: public Convolution {
    using Convolution::Convolution;

    Matrix* convolve() override;
};

#endif //BLOCKCONVOLUTION_H
