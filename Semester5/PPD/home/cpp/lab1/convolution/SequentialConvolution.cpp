
#include "SequentialConvolution.h"

Matrix* SequentialConvolution::convolve() {
    Matrix* result = matrix->createSameSize();
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            result->set(i, j, convolvePixel(i, j));
        }
    }
    return result;
}
