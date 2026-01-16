#ifndef MATRIX_H
#define MATRIX_H

class Matrix {
public:
    virtual void set(int row, int col, int value) = 0;
    virtual int get(int row, int col) const = 0;
    virtual int getRows() const = 0;
    virtual int getCols() const = 0;
    virtual Matrix* createSameSize() const = 0;
    virtual ~Matrix() = default;
};

#endif //MATRIX_H
