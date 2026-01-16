#ifndef DYNAMICMATRIX_H
#define DYNAMICMATRIX_H

#include "Matrix.h"

class DynamicMatrix: public Matrix {
    int** data;
    int rows, cols;

public:
    DynamicMatrix(int r, int c): rows(r), cols(c) {
        data = new int*[rows];
        for (int i = 0; i < rows; i++) {
            data[i] = new int[cols];
            for(int j = 0; j < cols; j++) {
                data[i][j] = 0;
            }
        }
    }

    int get(const int row, const int col) const override {
        return data[row][col];
    }

    void set(const int row, const int col, const int value) override {
        data[row][col] = value;
    }

    int getRows() const override {
        return rows;
    }

    int getCols() const override {
        return cols;
    }

    Matrix* createSameSize() const override {
        return new DynamicMatrix(rows, cols);
    }

    ~DynamicMatrix() override {
        for (int i = 0; i < rows; i++) {
            delete[] data[i];
        }
        delete[] data;
    }
};

#endif //DYNAMICMATRIX_H
