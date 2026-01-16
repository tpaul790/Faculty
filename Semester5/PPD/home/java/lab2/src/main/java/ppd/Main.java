package ppd;

import ppd.config.ComputationMethod;
import ppd.config.SynchronizationType;
import ppd.utils.Constants;
import ppd.utils.MatrixFileManager;
import ppd.utils.MatrixValidator;

public class Main {
    private static void applyGroundTruthConvolution(String inputPath) {
        try {
            int[][][] input = MatrixFileManager.readInput(inputPath);
            int[][] matrix = input[0], kernelMatrix = input[1];
            int N = matrix.length, M = matrix[0].length, K = kernelMatrix.length;
            String outputPath = Constants.OUTPUT_DIR + "output_" + ComputationMethod.SEQUENTIAL + "_" + N + "_" + M + "_" + K + ".txt";
            Convolution convolution = new SequentialConvolution(matrix, kernelMatrix);

            double startTime = System.nanoTime();
            int[][] result = convolution.convolve();
            double endTime = System.nanoTime();

            MatrixFileManager.writeOutput(result, outputPath);
            double executionTime = (endTime - startTime) / 1e6;
            System.out.println(executionTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1 || args.length > 4) {
            System.out.println("Usage: java Main <input_file> <num_threads> <method> <{type}>");
            System.exit(1);
        }

        try {
            String inputPath = Constants.INPUT_DIR + args[0];
            String method = args[2];

            if (method.equals(ComputationMethod.SEQUENTIAL.toString())) {
                applyGroundTruthConvolution(inputPath);
                return;
            }

            String outputPath;
            int threadCount = Integer.parseInt(args[1]);
            int[][][] input = MatrixFileManager.readInput(inputPath);
            int[][] matrix = input[0], kernelMatrix = input[1];
            int N = matrix.length, M = matrix[0].length, K = kernelMatrix.length;
            InlineConvolution convolution;

            if (method.equals(ComputationMethod.INLINE_SEQUENTIAL.toString())) {
                outputPath = Constants.OUTPUT_DIR + "output_" + method + "_" + N + "_" + M + "_" + K + ".txt";
                convolution = new InlineSequentialConvolution(matrix, kernelMatrix);
            }
            else if (method.equals(ComputationMethod.INLINE_PARALLEL.toString())) {
                SynchronizationType type = SynchronizationType.valueOf(args[3]);
                outputPath = Constants.OUTPUT_DIR + "output_" + method + "_" + type + "_" + N + "_" + M + "_" + K + "_" + threadCount + ".txt";
                convolution = new InlineParallelConvolution(matrix, kernelMatrix, threadCount, type);
            }
            else {
                throw new IllegalArgumentException("Invalid method: " + method);
            }

            double startTime = System.nanoTime();
            int[][] result = convolution.convolve();
            double endTime = System.nanoTime();

            MatrixFileManager.writeOutput(result, outputPath);
            double executionTime = (endTime - startTime) / 1e6;
            System.out.println(executionTime);

            String validSequentialPath = Constants.OUTPUT_DIR + "output_" + ComputationMethod.SEQUENTIAL + "_" + N + "_" + M + "_" + K + ".txt";
            if (!MatrixValidator.validate(validSequentialPath, outputPath)) {
                throw new RuntimeException("Invalid output: Results do not match!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}