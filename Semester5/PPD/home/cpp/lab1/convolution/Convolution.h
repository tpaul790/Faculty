#ifndef CONVOLUTION_H
#define CONVOLUTION_H

#include "./../matrix/Matrix.h"

class Convolution {
protected:
    int N, M, K, P;
    Matrix* matrix;
    Matrix* kernelMatrix;

public:
    Convolution(Matrix* matrix, Matrix* kernelMatrix, const int P): P(P), matrix(matrix), kernelMatrix(kernelMatrix) {
        N = matrix->getRows();
        M = matrix->getCols();
        K = kernelMatrix->getRows();
    }

    int computeBorderedPixelValue(int i, int j) const {
        if (i < 0) i = 0;
        else if (i >= N) i = N - 1;
        if (j < 0) j = 0;
        else if (j >= M) j = M - 1;
        return matrix->get(i, j);
    }

    int convolvePixel(int i, int j) const {
        const int pad = (K - 1) / 2;
        int sum = 0;
        for (int ki = 0; ki < K; ki++) {
            for (int kj = 0; kj < K; kj++) {
                const int matrixRow = i + ki - pad;
                const int matrixCol = j + kj - pad;
                sum += computeBorderedPixelValue(matrixRow, matrixCol) * kernelMatrix->get(ki, kj);
            }
        }
        return sum;
    }

    virtual Matrix* convolve() = 0;

    virtual ~Convolution() = default;
};

#endif //CONVOLUTION_H
