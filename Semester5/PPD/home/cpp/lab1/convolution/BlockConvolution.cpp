
#include "BlockConvolution.h"

#include <iostream>
#include <vector>
#include <thread>

Matrix* BlockConvolution::convolve() {
    Matrix* convolvedMatrix = matrix->createSameSize();

    int Px = (int)std::sqrt(P);     //numar blocuri orizontala
    while (P % Px != 0) Px--;
    int Py = P / Px;                   //numar blocuri verticala (Px*Py=P=nr blocuri)

    int baseRows = N / Px;
    int extraRows = N % Px;

    int baseCols = M / Py;
    int extraCols = M % Py;

    std::vector<std::thread> threads(P);
    int threadId = 0;

    int rowStart = 0;
    for (int bx = 0; bx < Px; bx++) {
        int rows = baseRows + (bx < extraRows ? 1 : 0);         // daca am de ex 2 linii extra, la primele 2 seturi de
        int rowEnd = rowStart + rows - 1;                       // blocuri pe orizontala mai adaug cate o linie

        int colStart = 0;
        for (int by = 0; by < Py; by++) {
            int cols = baseCols + (by < extraCols ? 1 : 0);
            int colEnd = colStart + cols - 1;

            threads[threadId++] = std::thread([=] {
                for (int i = rowStart; i <= rowEnd; i++) {
                    for (int j = colStart; j <= colEnd; j++) {
                        convolvedMatrix->set(i, j, convolvePixel(i, j));
                    }
                }
            });
            colStart += cols;
        }
        rowStart += rows;
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
