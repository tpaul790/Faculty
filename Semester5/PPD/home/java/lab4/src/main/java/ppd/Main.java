package ppd;

import ppd.utils.ComputationMethod;
import ppd.utils.ContestFileManager;
import ppd.utils.ContestResultsValidator;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static ppd.utils.Constants.OUTPUT_DIR;

public class Main {
    private static final SyncRankingLinkedList rankingList = new SyncRankingLinkedList();
    private static final ScoreProcessingQueue queue = new ScoreProcessingQueue();
    private static final AtomicInteger activeReaders = new AtomicInteger(0);
    private static int numReaders = 0, numWorkers = 0, numThreads = 1;

    public static class Reader extends Thread {
        private final List<String> files;

        public Reader(List<String> files) {
            this.files = files;
            activeReaders.incrementAndGet();
        }

        @Override
        public void run() {
            try {
                for (var file: files) {
                    processFile(file);
                }
            } finally {
                activeReaders.decrementAndGet();
                synchronized (queue) {
                    queue.notifyAll();
                }
            }
        }

        private void processFile(String filePath) {
            try (var scanner = new Scanner(new FileReader(filePath))) {
                while (scanner.hasNextLine()) {
                    var line = scanner.nextLine();
                    var tokens = line.split(",");
                    var id = Integer.parseInt(tokens[0]);
                    var score = Integer.parseInt(tokens[1]);
                    var record = new ScoreRecord(id, score);
                    queue.enqueue(record);
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Error reading from file: " + filePath);
                e.printStackTrace();
            }
        }


    }

    public static class Worker extends Thread {

        @Override
        public void run() {
            try {
                while (true) {
                    ScoreRecord record;

                    synchronized (queue) {
                        while (queue.isEmpty() && activeReaders.get() > 0) {
                            queue.wait();
                        }

                        if (queue.isEmpty() && activeReaders.get() == 0) {
                            break;
                        }

                        record = queue.dequeue();
                    }

                    if (record != null) {
                        rankingList.save(record.id(), record.score());
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    private static List<List<String>> splitFiles(String[] inputFiles, int numReaders) {
        List<List<String>> fileAssignments = new ArrayList<>();
        for (int i = 0; i < numReaders; i++) {
            fileAssignments.add(new ArrayList<>());
        }
        for (int i = 0; i < inputFiles.length; i++) {
            fileAssignments.get(i % numReaders).add(inputFiles[i]);
        }
        return fileAssignments;
    }

    private static void computeRankingSequentially(String[] inputFiles) {
        double startTime = System.nanoTime();
        var rankingList = new SyncRankingLinkedList();

        for (var file : inputFiles) {
            try (var scanner = new Scanner(new FileReader(file))) {
                while (scanner.hasNextLine()) {
                    var line = scanner.nextLine();
                    var tokens = line.split(",");
                    var id = Integer.parseInt(tokens[0]);
                    var score = Integer.parseInt(tokens[1]);
                    rankingList.save(id, score);
                }
            } catch (IOException e) {
                System.err.println("Error reading from file: " + file);
                e.printStackTrace();
            }
        }

        String outputPath = OUTPUT_DIR + "/ranking_sequential.txt";
        ContestFileManager.outputFinalRanking(rankingList.getRanking(), outputPath);

        double endTime = System.nanoTime();
        double executionTime = (endTime - startTime) / 1e6;
        System.out.println(executionTime);
    }

    private static void computeRankingParallel(String[] inputFiles, int numReaders, int numWorkers) {
        var fileAssignments = splitFiles(inputFiles, numReaders);
        List<Thread> readers = new ArrayList<>();
        List<Thread> workers = new ArrayList<>();

        for (int i = 0; i < numReaders; i++) {
            readers.add(new Reader(fileAssignments.get(i)));
        }

        for (int i = 0; i < numWorkers; i++) {
            workers.add(new Worker());
        }

        double startTime = System.nanoTime();
        readers.forEach(Thread::start);
        workers.forEach(Thread::start);

        readers.forEach(reader -> {
            try {
                reader.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        workers.forEach(worker -> {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        double endTime = System.nanoTime();
        double executionTime = (endTime - startTime) / 1e6;
        System.out.println(executionTime);
    }

    public static void main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            System.out.println("Usage: java Main <method> <num_threads> <num_readers>");
            System.exit(1);
        }

        String[] inputFiles = ContestFileManager.getInputFileNames();
        ComputationMethod method = ComputationMethod.valueOf(args[0]);

        if (method == ComputationMethod.SEQUENTIAL) {
            computeRankingSequentially(inputFiles);
        } else if (method == ComputationMethod.PARALLEL) {
            numThreads = Integer.parseInt(args[1]);
            numReaders = Integer.parseInt(args[2]);
            numWorkers = numThreads - numReaders;
            computeRankingParallel(inputFiles, numReaders, numWorkers);
        } else {
            System.out.println("Invalid method: " + method);
            System.exit(1);
        }

        if(method != ComputationMethod.SEQUENTIAL) {
            String outputPath = OUTPUT_DIR + "/ranking_" + numReaders + "_" + numThreads + ".txt";
            String validRankingPath = OUTPUT_DIR + "/ranking_sequential.txt";
            outputAndValidateRanking(outputPath, validRankingPath);
        }
    }

    private static void outputAndValidateRanking(String outputPath, String validRankingPath) {
        ContestFileManager.outputFinalRanking(rankingList.getRanking(), outputPath);

        if (!ContestResultsValidator.validateRanking(validRankingPath, outputPath)) {
            throw new IllegalStateException("Invalid ranking!");
        }
    }
}