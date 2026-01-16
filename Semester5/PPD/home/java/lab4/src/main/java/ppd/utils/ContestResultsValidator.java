package ppd.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;

public class ContestResultsValidator {

    public static boolean validateRanking(String validSequentialPath, String parallelPath) {
        try (var sequentialScanner = new Scanner(new File(validSequentialPath));
             var parallelScanner = new Scanner(new File(parallelPath))) {
            while (sequentialScanner.hasNextLine() && parallelScanner.hasNextLine()) {
                var sequentialLine = sequentialScanner.nextLine();
                var parallelLine = parallelScanner.nextLine();
                if (!sequentialLine.equals(parallelLine)) {
                    return false;
                }
            }
            return !sequentialScanner.hasNextLine() && !parallelScanner.hasNextLine();
        } catch (FileNotFoundException e) {
            return false;
        }
    }
}
