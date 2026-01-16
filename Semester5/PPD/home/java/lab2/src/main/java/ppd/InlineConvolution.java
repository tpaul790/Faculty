package ppd;

import ppd.config.SynchronizationType;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class InlineConvolution {
    protected final int N, M, K, P, halfK;
    protected final int[][] matrix, kernel;

    public InlineConvolution(int[][] matrix, int[][] kernel, int P) {
        this.N = matrix.length;
        this.M = N > 0 ? matrix[0].length : 0;
        this.K = kernel.length;
        this.P = P;
        this.halfK = K / 2;
        this.matrix = matrix;
        this.kernel = kernel;
    }

    public abstract int[][] convolve();
}

class InlineSequentialConvolution extends InlineConvolution {
    public InlineSequentialConvolution(int[][] matrix, int[][] kernel) {
        super(matrix, kernel, 1);
    }

    private void shiftCache(int[][] cache, int newLine) {
        for (int i = 0; i < halfK; i++) {
            System.arraycopy(cache[i+1], 0, cache[i],0, M);
        }
        if (newLine < N) {
            System.arraycopy(matrix[newLine], 0, cache[halfK], 0, M);
        }
    }

    private int getPixelValue(int[][] cache, int i, int j, int rowIndex) {
        int row = Math.max(0, Math.min(i, N - 1));
        int col = Math.max(0, Math.min(j, M - 1));
        if (row > rowIndex) {
            return matrix[row][col];
        }
        return cache[row - rowIndex + halfK][col];
    }

    private int convolvePixel(int[][] cache, int i, int j) {
        int sum = 0;
        for (int ki = 0; ki < K; ki++) {
            for (int kj = 0; kj < K; kj++) {
                int matrixRow = i + ki - halfK;
                int matrixCol = j + kj - halfK;
                sum += getPixelValue(cache, matrixRow, matrixCol, i) * kernel[ki][kj];
            }
        }
        return sum;
    }

    @Override
    public int[][] convolve() {
        int[][] cache = new int[halfK + 1][M];
        for (int i = 0; i < N; i++) {
            shiftCache(cache, i);
            for (int j = 0; j < M; j++) {
                matrix[i][j] = convolvePixel(cache, i, j);
            }
        }
        return matrix;
    }
}

class InlineParallelConvolution extends InlineConvolution {
    private final SynchronizationType type;

    public InlineParallelConvolution(int[][] matrix, int[][] kernel, int P, SynchronizationType type) {
        super(matrix, kernel, P);
        this.type = type;
    }

    private int[][] initTopCache(int start) {
        int[][] topCache = new int[halfK + 1][M];
        for (int i = start - halfK; i < start; i++) {
            if (i < 0) continue;
            System.arraycopy(matrix[i], 0, topCache[i - start + halfK + 1], 0, M);
        }
        return topCache;
    }

    private int[][] initBottomCache(int end) {
        int[][] bottomCache = new int[Math.min(halfK, N - end)][M];
        for (int i = end; i < end + halfK; i++) {
            if (i >= N) break;
            System.arraycopy(matrix[i], 0, bottomCache[i - end], 0, M);
        }
        return bottomCache;
    }

    private void shiftTopCache(int[][] topCache, int newLine) {
        for (int i = 0; i < halfK; i++) {
            System.arraycopy(topCache[i+1], 0, topCache[i], 0, M);
        }
        if (newLine < N) {
            System.arraycopy(matrix[newLine], 0, topCache[halfK], 0, M);
        }
    }

    private int getPixelValue(int[][] topCache, int[][] bottomCache, int i, int j, int rowIndex, boolean withinCacheBoundary) {
        int row = Math.max(0, Math.min(i, N - 1));
        int col = Math.max(0, Math.min(j, M - 1));
        if (row > rowIndex) {
            if (withinCacheBoundary) {
                return matrix[row][col];
            }
            return bottomCache[row - rowIndex - 1][col];
        }
        return topCache[row - rowIndex + halfK][col];
    }

    private int convolvePixel(int[][] topCache, int[][] bottomCache, int i, int j, boolean withinCacheBoundary) {
        int sum = 0;
        for (int ki = 0; ki < K; ki++) {
            for (int kj = 0; kj < K; kj++) {
                int matrixRow = i + ki - halfK;
                int matrixCol = j + kj - halfK;
                sum += getPixelValue(topCache, bottomCache, matrixRow, matrixCol, i, withinCacheBoundary) * kernel[ki][kj];
            }
        }
        return sum;
    }

    private void convolveLinesBarrier(int start, int end, CyclicBarrier barrier) {
        int[][] topCache = initTopCache(start);
        int[][] bottomCache = initBottomCache(end);
        int cacheBoundaryRow = end - halfK;
        try {
            barrier.await();
        } catch ( InterruptedException | BrokenBarrierException exception) {
            throw new RuntimeException(exception);
        }
        for (int i = start; i < end; i++) {
            shiftTopCache(topCache, i);
            boolean rowWithinCacheBoundary = i < cacheBoundaryRow;
            for (int j = 0; j < M; j++) {
                matrix[i][j] = convolvePixel(topCache, bottomCache, i, j, rowWithinCacheBoundary);
            }
        }
    }

    private void convolveLinesFlag(int start, int end, int threadId, AtomicBoolean[] readyFlags) {
        int[][] topCache = initTopCache(start);
        int[][] bottomCache = initBottomCache(end);

        readyFlags[threadId].set(true);
        for (AtomicBoolean flag: readyFlags) {
            while (!flag.get()) {
                Thread.onSpinWait();
            }
        }

        int bottomThreshold = end - halfK;
        for (int i = start; i < end; i++) {
            shiftTopCache(topCache, i);
            boolean outOfBounds = i < bottomThreshold;
            for (int j = 0; j < M; j++) {
                matrix[i][j] = convolvePixel(topCache, bottomCache, i, j, outOfBounds);
            }
        }
    }

    private void convolveWithBarrier(Thread[] threads) {
        CyclicBarrier barrier = new CyclicBarrier(P);
        int linesPerThread = N / P, remainingLines = N % P;
        int threadId = 0, start, end = 0;
        for (int i = 0; i < P; i++) {
            start = end;
            end = start + linesPerThread;
            if (remainingLines > 0) {
                end++;
                remainingLines--;
            }
            int threadStart = start, threadEnd = end;
            threads[threadId] = new Thread(() -> convolveLinesBarrier(threadStart, threadEnd, barrier));
            threads[threadId++].start();
        }
    }

    private void convolveWithFlag(Thread[] threads) {
        AtomicBoolean[] readyFlags = new AtomicBoolean[P];
        for (int i = 0; i < P; i++) {
            readyFlags[i] = new AtomicBoolean(false);
        }

        int linesPerThread = N / P, remainingLines = N % P;
        int threadId = 0, start, end = 0;
        for (int i = 0; i < P; i++) {
            start = end;
            end = start + linesPerThread;
            if (remainingLines > 0) {
                end++;
                remainingLines--;
            }
            int threadStart = start, threadEnd = end;
            int finalThreadId = threadId;
            threads[threadId] = new Thread(() -> convolveLinesFlag(threadStart, threadEnd, finalThreadId, readyFlags));
            threads[threadId++].start();
        }
    }

    @Override
    public int[][] convolve() {
        Thread[] threads = new Thread[P];

        if (type == SynchronizationType.BARRIER) {
            convolveWithBarrier(threads);
        }
        else if (type == SynchronizationType.FLAG) {
            convolveWithFlag(threads);
        }
        else {
            throw new RuntimeException("Invalid synchronization type provided: " + type);
        }

        for (var thread: threads) {
            try {
                thread.join();
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }
        return matrix;
    }
}
