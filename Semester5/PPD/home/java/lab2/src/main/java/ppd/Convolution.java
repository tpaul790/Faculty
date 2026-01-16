package ppd;

public abstract class Convolution {
    protected final int N, M, K, P;
    protected final int[][] matrix, kernel;

    public Convolution(int[][] matrix, int[][] kernel, int P) {
        this.N = matrix.length;
        this.M = N > 0 ? matrix[0].length : 0;
        this.K = kernel.length;
        this.P = P;
        this.matrix = matrix;
        this.kernel = kernel;
    }

    private int getPixelValue(int i, int j) {
        int row = Math.max(0, Math.min(i, N - 1));
        int col = Math.max(0, Math.min(j, M - 1));
        return matrix[row][col];
    }

    protected int convolvePixel(int i, int j) {
        int pad = (K - 1) / 2;
        int sum = 0;
        for (int ki = 0; ki < K; ki++) {
            for (int kj = 0; kj < K; kj++) {
                int matrixRow = i + ki - pad;
                int matrixCol = j + kj - pad;
                sum += getPixelValue(matrixRow, matrixCol) * kernel[ki][kj];
            }
        }
        return sum;
    }

    public abstract int[][] convolve();
}

class SequentialConvolution extends Convolution {
    public SequentialConvolution(int[][] matrix, int[][] kernel) {
        super(matrix, kernel, 1);
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
