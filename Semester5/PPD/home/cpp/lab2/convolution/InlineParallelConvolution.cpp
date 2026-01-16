#include "InlineParallelConvolution.h"

#include <algorithm>

std::vector<std::vector<int> > InlineParallelConvolution::initTopCache(int start) {
    std::vector topCache(halfK + 1, std::vector(M, 0));
    for (int i = start - halfK; i < start; ++i) {
        if (i < 0) continue;
        topCache[i - start + halfK + 1] = matrix[i];
    }
    return topCache;
}

std::vector<std::vector<int> > InlineParallelConvolution::initBottomCache(int end) {
    std::vector bottomCache(std::min(halfK, N - end), std::vector(M, 0));
    for (int i = end; i < end + halfK && i < N; ++i) {
        if (i >= N) break;
        bottomCache[i - end] = matrix[i];
    }
    return bottomCache;
}

void InlineParallelConvolution::shiftTopCache(std::vector<std::vector<int> > &topCache, int newLine) {
    for (int i = 0; i < halfK; ++i) {
        topCache[i] = topCache[i + 1];
    }
    if (newLine < N) {
        topCache[halfK] = matrix[newLine];
    }
}

int InlineParallelConvolution::getPixelValue(const std::vector<std::vector<int> > &topCache,
                                             const std::vector<std::vector<int> > &bottomCache,
                                             int i, int j, int rowIndex, bool withinCacheBoundary) {
    int row = std::clamp(i, 0, N - 1);
    int col = std::clamp(j, 0, M - 1);
    if (row > rowIndex) {
        return withinCacheBoundary ? matrix[row][col] : bottomCache[row - rowIndex - 1][col];
    }
    return topCache[row - rowIndex + halfK][col];
}

int InlineParallelConvolution::convolvePixel(const std::vector<std::vector<int> > &topCache,
                                             const std::vector<std::vector<int> > &bottomCache,
                                             int i, int j, bool withinCacheBoundary) {
    int sum = 0;
    for (int ki = 0; ki < K; ++ki) {
        for (int kj = 0; kj < K; ++kj) {
            int matrixRow = i + ki - halfK;
            int matrixCol = j + kj - halfK;
            sum += getPixelValue(topCache, bottomCache, matrixRow, matrixCol, i, withinCacheBoundary) * kernel[ki][kj];
        }
    }
    return sum;
}

void InlineParallelConvolution::convolveLinesBarrier(int start, int end) {
    auto topCache = initTopCache(start);
    auto bottomCache = initBottomCache(end);
    int cacheBoundaryRow = end - halfK;

    barrier.arrive_and_wait();

    for (int i = start; i < end; ++i) {
        shiftTopCache(topCache, i);
        bool rowWithinCacheBoundary = i < cacheBoundaryRow; // <= (?)
        for (int j = 0; j < M; ++j) {
            matrix[i][j] = convolvePixel(topCache, bottomCache, i, j, rowWithinCacheBoundary);
        }
    }
}

void InlineParallelConvolution::convolveLinesFlag(int start, int end, int threadId) {
    auto topCache = initTopCache(start);
    auto bottomCache = initBottomCache(end);

    readyFlags[threadId].test_and_set();
    for (auto &flag: readyFlags) {
        while (!flag.test()) std::this_thread::yield();
    }

    int cacheBoundaryRow = end - halfK;
    for (int i = start; i < end; ++i) {
        shiftTopCache(topCache, i);
        bool withinCacheBoundary = i < cacheBoundaryRow;
        for (int j = 0; j < M; ++j) {
            matrix[i][j] = convolvePixel(topCache, bottomCache, i, j, withinCacheBoundary);
        }
    }
}

void InlineParallelConvolution::convolveWithBarrier(std::vector<std::thread> &threads) {
    int linesPerThread = N / P, remainingLines = N % P;
    int start, end = 0;

    for (int i = 0; i < P; ++i) {
        start = end;
        end += linesPerThread;
        if (remainingLines > 0) {
            end++;
            remainingLines--;
        }
        threads[i] = std::thread([this, start, end] { convolveLinesBarrier(start, end); });
    }
}

void InlineParallelConvolution::convolveWithFlag(std::vector<std::thread> &threads) {
    int linesPerThread = N / P, remainingLines = N % P;
    int start, end = 0;

    for (int i = 0; i < P; ++i) {
        start = end;
        end += linesPerThread;
        if (remainingLines > 0) {
            end++;
            remainingLines--;
        }
        threads[i] = std::thread([this, start, end, i] { convolveLinesFlag(start, end, i); });
    }
}

std::vector<std::vector<int> > InlineParallelConvolution::convolve() {
    std::vector<std::thread> threads(P);
    if (type == SynchronizationType::BARRIER) {
        convolveWithBarrier(threads);
    } else if (type == SynchronizationType::FLAG) {
        convolveWithFlag(threads);
    } else {
        throw std::runtime_error("Invalid synchronization type");
    }

    for (auto &thread: threads) {
        thread.join();
    }
    return matrix;
}
