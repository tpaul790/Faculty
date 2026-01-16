package ppd;

import ppd.constants.BatchType;
import ppd.constants.DistributionType;

public abstract class Convolution {
    protected final int N, M, K, P;
    protected final int[][] matrix, kernelMatrix;

    public Convolution(int[][] matrix, int[][] kernelMatrix, int P) {
        N = matrix.length;
        M = matrix[0].length;
        K = kernelMatrix.length;
        this.P = P;
        this.matrix = matrix;
        this.kernelMatrix = kernelMatrix;
    }

    private int computeBorderedPixelValue(int i, int j) {
        if (i < 0) {
            i = 0;
        } else if (i >= N) {
            i = N - 1;
        }
        if (j < 0) {
            j = 0;
        } else if (j >= M) {
            j = M - 1;
        }
        return matrix[i][j];
    }

    protected int convolvePixel(int i, int j) {
        int pad = (K - 1) / 2;
        int sum = 0;
        for (int ki = 0; ki < K; ki++) {
            for(int kj = 0; kj < K; kj++) {
                int matrixRow = i + ki - pad;
                int matrixCol = j + kj - pad;
                sum += computeBorderedPixelValue(matrixRow, matrixCol) * kernelMatrix[ki][kj];
            }
        }
        return sum;
    }
    
    public abstract int[][] convolve();
}

class SequentialConvolution extends Convolution {
    public SequentialConvolution(int[][] matrix, int[][] kernelMatrix) {
        super(matrix, kernelMatrix, 0);
    }

    @Override
    public int[][] convolve() {
        int[][] result = new int[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                result[i][j] = convolvePixel(i, j);
            }
        }
        return result;
    }
}

class BatchConvolution extends Convolution {
    private final BatchType type;

    public BatchConvolution(int[][] matrix, int[][] kernelMatrix, int P, BatchType type) {
        super(matrix, kernelMatrix, P);
        this.type = type;
    }

    @Override
    public int[][] convolve() {
        int[][] convolvedMatrix = new int[N][M];
        Thread[] threads = new Thread[P];

        if (type == BatchType.ROWS) {
            applyRowBatching(convolvedMatrix, threads);
        }
        else if (type == BatchType.COLUMNS) {
            applyColumnBatching(convolvedMatrix, threads);
        }
        else {
            throw new RuntimeException("Invalid batch convolution type provided: " + type);
        }

        for (Thread thread: threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return convolvedMatrix;
    }

    private void applyRowBatching(int[][] convolvedMatrix, Thread[] threads) {
        int rowsPerThread = N / P;
        int remainingRows = N % P;
        int start, end = 0;

        for (int id = 0; id < P; id++) {
            start = end;
            end = start + rowsPerThread;
            if (remainingRows > 0) {
                end++;
                remainingRows--;
            }

            int threadStart = start, threadEnd = end;
            threads[id] = new Thread(() -> {
                for (int i = threadStart; i < threadEnd; i++) {
                    for (int j = 0; j < M; j++) {
                        convolvedMatrix[i][j] = convolvePixel(i, j);
                    }
                }
            });
            threads[id].start();
        }
    }

    private void applyColumnBatching(int[][] convolvedMatrix, Thread[] threads) {
        int columnsPerThread = M / P;
        int remainingColumns = M % P;
        int start, end = 0;

        for (int id = 0; id < P; id++) {
            start = end;
            end = start + columnsPerThread;
            if (remainingColumns > 0) {
                end++;
                remainingColumns--;
            }

            int threadStart = start, threadEnd = end;
            threads[id] = new Thread(() -> {
                for (int j = threadStart; j < threadEnd; j++) {
                    for (int i = 0; i < N; i++) {
                        convolvedMatrix[i][j] = convolvePixel(i, j);
                    }
                }
            });
            threads[id].start();
        }
    }
}

class BlockConvolution extends Convolution {
    public BlockConvolution(int[][] matrix, int[][] kernelMatrix, int P) {
        super(matrix, kernelMatrix, P);
    }

    @Override
    public int[][] convolve() {
        int[][] convolvedMatrix =  new int[N][M];
        int Px = (int)Math.sqrt(P);
        while (P % Px != 0) Px--;
        int Py = P / Px;

        int baseRows = N / Px;
        int extraRows = N % Px;

        int baseCols = M / Py;
        int extraCols = M % Py;

        Thread[] threads = new Thread[P];

        int threadId = 0;

        int rowStart = 0;
        for (int bx = 0; bx < Px; bx++) {
            int rows = baseRows + (bx < extraRows ? 1 : 0);         // daca am de ex 2 linii extra, la primele 2 seturi de
            int rowEnd = rowStart + rows - 1;                       // blocuri pe orizontala mai adaug cate o linie

            int colStart = 0;
            for (int by = 0; by < Py; by++) {
                int cols = baseCols + (by < extraCols ? 1 : 0);
                int colEnd = colStart + cols - 1;

                int finalRowStart = rowStart;
                int finalColStart = colStart;
                threads[threadId] = new Thread(() -> {
                    for (int i = finalRowStart; i <= rowEnd; i++) {
                        for (int j = finalColStart; j <= colEnd; j++) {
                            convolvedMatrix[i][j] = convolvePixel(i, j);
                        }
                    }
                });

                threads[threadId].start();
                threadId++;

                colStart += cols;
            }
            rowStart += rows;
        }

        for (Thread thread: threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return convolvedMatrix;
    }
}

class DistributionConvolution extends Convolution {
    private final DistributionType type;

    public DistributionConvolution(int[][] matrix, int[][] kernelMatrix, int P, DistributionType type) {
        super(matrix, kernelMatrix, P);
        this.type = type;
    }

    @Override
    public int[][] convolve() {
        int[][] convolvedMatrix = new int[N][M];
        Thread[] threads =  new Thread[P];

        if (type == DistributionType.LINEAR) {
            applyLinearDistribution(convolvedMatrix, threads);
        }
        else if (type == DistributionType.CYCLIC) {
            applyCyclicDistribution(convolvedMatrix, threads);
        }
        else {
            throw new RuntimeException("Invalid distribution convolution type provided: " + type);
        }

        for (Thread thread: threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return convolvedMatrix;
    }

    private void applyLinearDistribution(int[][] convolvedMatrix, Thread[] threads) {
        int totalPixels = N * M;
        int pixelsPerThread = totalPixels / P, remainingPixels = totalPixels % P;
        int start, end = 0;

        for (int id = 0; id < P; id++) {
            start = end;
            end = start + pixelsPerThread;

            if (remainingPixels > 0) {
                end++;
                remainingPixels--;
            }

            int threadStart = start, threadEnd = end;
            threads[id] = new Thread(() -> {
                for (int pixel = threadStart; pixel < threadEnd; pixel++) {
                    int i = pixel / M;
                    int j = pixel % M;
                    convolvedMatrix[i][j] = convolvePixel(i, j);
                }
            });
            threads[id].start();
        }
    }

    private void applyCyclicDistribution(int[][] convolvedMatrix, Thread[] threads) {
        for (int id = 0; id < P; id++) {
            int threadId = id;
            threads[threadId] = new Thread(() -> {
                for (int pixel = threadId; pixel < N * M; pixel += P) {
                    int i = pixel / M;
                    int j = pixel % M;
                    convolvedMatrix[i][j] = convolvePixel(i, j);
                }
            });
            threads[threadId].start();
        }
    }
}