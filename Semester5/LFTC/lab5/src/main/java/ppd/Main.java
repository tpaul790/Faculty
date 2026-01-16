package ppd;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java LexicalAnalyzer <code_source_file>");
            return;
        }
        String fileName = args[0];
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        try {
            analyzer.processFile(fileName);
            analyzer.saveOutputs("FIP.txt", "TS.txt");
            analyzer.reportErrors();
            System.out.println("\nAnaliză Lexicală finalizată (cu AF). FIP.txt și TS.txt generate.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}