package ppd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RezultatValidator {

    public static void main(String[] args) {
        String fileParalel = "rezultate.txt";
        String fileSecvential = "rezultate_secv.txt";

        try {
            System.out.println("=== Inceput Validare Rezultate ===");
            List<String> linesParalel = readLines(fileParalel);
            List<String> linesSecvential = readLines(fileSecvential);

            if (linesParalel.size() != linesSecvential.size()) {
                System.err.println("ESEC: Numar diferit de linii!");
                System.err.println("Paralel: " + linesParalel.size() + " linii");
                System.err.println("Secvential: " + linesSecvential.size() + " linii");
            } else {
                boolean identical = true;
                for (int i = 0; i < linesParalel.size(); i++) {
                    if (!linesParalel.get(i).trim().equals(linesSecvential.get(i).trim())) {
                        System.err.println("DSEC la linia " + (i + 1));
                        System.err.println("  Asteptat (Secv): " + linesSecvential.get(i));
                        System.err.println("  Gasit (Paralel): " + linesParalel.get(i));
                        identical = false;
                        break; // Ne oprim la prima eroare pentru claritate
                    }
                }

                if (identical) {
                    System.out.println("SUCCES: Fisierele sunt identice!");
                    System.out.println("Numar total studenti validati: " + linesParalel.size());
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la citirea fisierelor: " + e.getMessage());
        }
    }

    private static List<String> readLines(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        }
        return lines;
    }
}
