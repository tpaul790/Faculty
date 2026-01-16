package ppd;

import java.io.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Grammar g = new Grammar();
        g.readFromFile("grammar.txt");

        if (g.getStartSymbol() == null) {
            System.err.println("Eroare: Gramatica nu a putut fi incarcata.");
            return;
        }

        LL1Parser parser = new LL1Parser(g);

        parser.computeFirst();
        parser.computeFollow();
        parser.createParsingTable();

        String sourceCode = readFullFileFromResources("input.txt");

        if (sourceCode != null && !sourceCode.isEmpty()) {
            Scanner scanner = new Scanner();
            List<Token> tokens = scanner.tokenize(sourceCode);

            parser.parse(tokens);
        } else {
            System.out.println("Codul sursa este gol sau fisierul test.cpp nu exista.");
        }
    }

    private static String readFullFileFromResources(String filename) {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream(filename)) {
            if (is == null) throw new FileNotFoundException(filename);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la citirea fisierului: " + filename);
            return null;
        }
        return sb.toString();
    }
}