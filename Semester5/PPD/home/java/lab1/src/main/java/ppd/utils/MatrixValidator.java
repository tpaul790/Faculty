package ppd.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MatrixValidator {
    public static boolean validate(String validSequentialPath, String parallelPath) throws FileNotFoundException {
        File validSequentialFile = new File(validSequentialPath);
        Scanner sequentialScanner = new Scanner(validSequentialFile);

        File parallelFile = new File(parallelPath);
        Scanner parallelScanner = new Scanner(parallelFile);

        while(sequentialScanner.hasNextLine() && parallelScanner.hasNextLine()) {
            String sequentialLine = sequentialScanner.nextLine();
            String parallelLine = parallelScanner.nextLine();

            if (!sequentialLine.equals(parallelLine)) {
                return false;
            }
        }

        if (sequentialScanner.hasNextLine() || parallelScanner.hasNextLine()) {
            return false;
        }

        return true;
    }
}
