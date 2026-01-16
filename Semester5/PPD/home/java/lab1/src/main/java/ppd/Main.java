package ppd;

import ppd.constants.BatchType;
import ppd.constants.ComputationMethod;
import ppd.constants.Constants;
import ppd.constants.DistributionType;
import ppd.utils.MatrixFileManager;
import ppd.utils.MatrixValidator;

import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {

        if (args.length < 3 || args.length > 4) {
            System.out.println("Usage: java Main <input_file> <num_threads> <method> <{type}>");
            System.exit(1);
        }

        try {
            String outputPath;
            String inputPath = Constants.INPUT_DIR + args[0];
            int threadCount = Integer.parseInt(args[1]);
            String method = args[2];
            int[][][] input = MatrixFileManager.readInput(inputPath);
            int[][] matrix = input[0], kernelMatrix = input[1];
            int N = matrix.length, M = matrix[0].length, K = kernelMatrix.length;
            Convolution convolution;

            if (method.equals(ComputationMethod.SEQUENTIAL.toString())) {
                outputPath = Constants.OUTPUT_DIR + "output_" + method + "_" + N + "_" + M + "_" + K + ".txt";
                convolution = new SequentialConvolution(matrix, kernelMatrix);
            }
            else if (method.equals(ComputationMethod.BATCH.toString())) {
                BatchType type = BatchType.valueOf(args[3]);
                outputPath = Constants.OUTPUT_DIR + "output_" + method + "_" + type + "_" + N + "_" + M + "_" + K + "_" + threadCount + ".txt";
                convolution = new BatchConvolution(matrix, kernelMatrix, threadCount, type);
            }
            else if (method.equals(ComputationMethod.BLOCK.toString())) {
                outputPath = Constants.OUTPUT_DIR + "output_" + method + "_" + N + "_" + M + "_" + K + "_" + threadCount + ".txt";
                convolution = new BlockConvolution(matrix, kernelMatrix, threadCount);
            }
            else if (method.equals(ComputationMethod.DISTRIBUTION.toString())) {
                DistributionType type = DistributionType.valueOf(args[3]);
                outputPath = Constants.OUTPUT_DIR + "output_" + method + "_" + type + "_" + N + "_" + M + "_" + K + "_" + threadCount + ".txt";
                convolution = new DistributionConvolution(matrix, kernelMatrix, threadCount, type);
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

            if (!method.equals(ComputationMethod.SEQUENTIAL.toString())) {
                String validSequentialPath = Constants.OUTPUT_DIR + "output_" + ComputationMethod.SEQUENTIAL + "_" + N + "_" + M + "_" + K + ".txt";
                if (!MatrixValidator.validate(validSequentialPath, outputPath)) {
                    throw new RuntimeException("Invalid output: Results do not match!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}