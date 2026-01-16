#ifndef STATICMATRIX_H
#define STATICMATRIX_H

#include "Matrix.h"

class StaticMatrix : public Matrix {
private:
    static const int LIMIT = 10000;
    int data[LIMIT][LIMIT] = {};
    int rows, cols;

public:
    StaticMatrix(const int rows, const int cols): rows(rows), cols(cols) {};

    int get(const int row, const int col) const override {
        return data[row][col];
    }

    void set(int row, int col, int value) override {
        data[row][col] = value;
    }

    int getRows() const override {
        return rows;
    }

    int getCols() const override {
        return cols;
    }

    Matrix* createSameSize() const override {
        return new StaticMatrix(rows, cols);
    }
};

#endif //STATICMATRIX_H
