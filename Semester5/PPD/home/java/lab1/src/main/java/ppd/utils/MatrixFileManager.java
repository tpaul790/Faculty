package ppd.utils;

import ppd.constants.Constants;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class MatrixFileManager {
    private static final int PIXEL_VALUE_BOUND = 255;
    private static final int KERNEL_VALUE_BOUND = 3;

    public static void generateInputMatrix(int N, int M, Random random, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
            writer.println(N + " " + M);
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    writer.print(random.nextInt(PIXEL_VALUE_BOUND) + " ");
                }
                writer.println();
            }
        }
    }

    public static void generateInputKernel(int K, Random random, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            writer.println(K);
            for (int i = 0; i < K; i++) {
                for (int j = 0; j < K; j++) {
                    writer.print(random.nextInt(KERNEL_VALUE_BOUND) + " ");
                }
                writer.println();
            }
        }
    }

    public static int[][][] readInput(String filePath) throws IOException {
        try (Scanner scanner = new Scanner(new FileReader(filePath))) {
            String dimensions = scanner.nextLine();
            String[] tokens = dimensions.split(" ");
            int N = Integer.parseInt(tokens[0]);
            int M = Integer.parseInt(tokens[1]);
            int[][] matrix = new int[N][M];
            for (int i = 0; i < N; i++) {
                String line =  scanner.nextLine();
                String[] values = line.split(" ");
                for (int j = 0; j < M; j++) {
                    matrix[i][j] = Integer.parseInt(values[j]);
                }
            }

            String kernelDimensions = scanner.nextLine();
            int K = Integer.parseInt(kernelDimensions);
            int[][] kernelMatrix = new int[K][K];
            for (int i = 0; i < K; i++) {
                String line =  scanner.nextLine();
                String[] values = line.split(" ");
                for (int j = 0; j < K; j++) {
                    kernelMatrix[i][j] = Integer.parseInt(values[j]);
                }
            }

            return new int[][][] {matrix, kernelMatrix};
        }
    }

    public static void writeOutput(int[][] outputMatrix, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
            for (int[] row: outputMatrix) {
                for (int value: row) {
                    writer.print(value + " ");
                }
                writer.println();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        for (int[] testCase: Constants.TEST_CASES) {
            int N = testCase[0], M = testCase[1], K = testCase[2];
            Random random = new Random();
            String fileName = Constants.INPUT_DIR + "input_" + N + "_" + M + "_" + K + ".txt";
            generateInputMatrix(N, M, random, fileName);
            generateInputKernel(K, random, fileName);
        }
    }

}
