#ifndef INLINEPARALLELCONVOLUTION_H
#define INLINEPARALLELCONVOLUTION_H

#include <barrier>
#include <thread>

#include "InlineConvolution.h"


class InlineParallelConvolution : public InlineConvolution {
private:
    SynchronizationType type;
    std::barrier<> barrier;
    std::vector<std::atomic_flag> readyFlags;

public:
    InlineParallelConvolution(const std::vector<std::vector<int>>& matrix, const std::vector<std::vector<int>>& kernel, int P, SynchronizationType type)
        : InlineConvolution(matrix, kernel, P), type(type), barrier(P), readyFlags(P) {
        for (auto& flag: readyFlags) {
            flag.clear();
        }
    }

    std::vector<std::vector<int>> initTopCache(int start);
    std::vector<std::vector<int>> initBottomCache(int end);
    void shiftTopCache(std::vector<std::vector<int>>& topCache, int newLine);
    int getPixelValue(const std::vector<std::vector<int>>& topCache, const std::vector<std::vector<int>>& bottomCache,
                      int i, int j, int rowIndex, bool withinCacheBoundary);
    int convolvePixel(const std::vector<std::vector<int>>& topCache, const std::vector<std::vector<int>>& bottomCache,
                      int i, int j, bool withinCacheBoundary);
    void convolveLinesBarrier(int start, int end);
    void convolveLinesFlag(int start, int end, int threadId);
    void convolveWithBarrier(std::vector<std::thread>& threads);
    void convolveWithFlag(std::vector<std::thread>& threads);
    std::vector<std::vector<int>> convolve() override;
};



#endif //INLINEPARALLELCONVOLUTION_H
