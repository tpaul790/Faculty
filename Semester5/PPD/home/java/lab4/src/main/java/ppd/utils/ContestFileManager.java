package ppd.utils;

import ppd.Node;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ppd.utils.Constants.*;

public class ContestFileManager {
    private static final int TOTAL_STUDENTS = 200;

    public static void generateData(String inputDir) {
        Random random = new Random();
        List<Integer> allStudentIds = IntStream.rangeClosed(1, TOTAL_STUDENTS)
                .boxed().collect(Collectors.toList());

        for (int project = 1; project <= PROJECTS; project++) {
            Collections.shuffle(allStudentIds, random);
            int participantsCount = random.nextInt(MAX_PARTICIPANTS - MIN_PARTICIPANTS + 1) + MIN_PARTICIPANTS;
            List<Integer> participantsForProject = allStudentIds.subList(0, participantsCount);

            String fileName = String.format("%s/project_%d.txt", inputDir, project);

            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                for (int studentId : participantsForProject) {
                    int score = random.nextInt(10) + 1;

                    writer.write(studentId + "," + score);
                    writer.println();
                }
            } catch (IOException e) {
                System.err.println("Eroare la scrierea în fișier: " + fileName);
                e.printStackTrace();
            }
        }
    }

    public static void outputFinalRanking(LinkedList<Node> ranking, String outputPath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            for (var node: ranking) {
                writer.write(node.getId() + "," + node.getScore());
                writer.println();
            }
        } catch (IOException e) {
            System.err.println("Eroare la scrierea în fișier: " + outputPath);
            e.printStackTrace();
        }
    }

    public static String[] getInputFileNames() {
        String[] fileNames = new String[PROJECTS];
        for (int i = 0; i < PROJECTS; i++) {
            int projectNumber = i + 1;
            fileNames[i] = String.format("%s/project_%d.txt", INPUT_DIR, projectNumber);
        }
        return fileNames;
    }

    public static void main(String[] args) {
        ContestFileManager.generateData(INPUT_DIR);
        System.out.println("Contest files generated successfully!");
    }
}