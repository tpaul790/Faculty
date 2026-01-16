package ppd;

import lombok.Data;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//ll(1)

@Getter
@Data
public class Gramatica {

    private String startSymbol;
    private final Map<String, List<List<String>>> productionRules;
    private Set<String> nonTerminals;
    private Set<String> terminals;

    public Gramatica() {
        this.productionRules = new HashMap<>();
        this.nonTerminals = new HashSet<>();
        this.terminals = new HashSet<>();
    }

    public void readFromFile(String filepath) {
        List<String> lines = new ArrayList<>();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filepath)) {
            assert is != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la citirea fișierului: " + filepath);
            return;
        }

        if (lines.isEmpty()) {
            System.out.println("Fișierul este gol.");
            return;
        }

        this.startSymbol = lines.get(lines.size() - 1);
        lines.remove(lines.size() - 1);

        for (String line : lines) {
            if (line.isEmpty()) continue;

            String[] parts = line.split(" -> ");
            if (parts.length != 2) continue;

            String head = parts[0].trim();
            String allBodies = parts[1].trim();

            String[] bodyParts = allBodies.split(" \\| ");

            List<List<String>> bodiesList = new ArrayList<>();
            for (String bodyStr : bodyParts) {
                if (bodyStr.equalsIgnoreCase("epsilon") || bodyStr.isEmpty()) {
                    bodiesList.add(new ArrayList<>());
                } else {
                    List<String> symbols = new ArrayList<>();

                    String[] symbolArray = bodyStr.split(" ");
                    for (String symbol : symbolArray) {
                        if (!symbol.isEmpty()) {
                            symbols.add(symbol);
                        }
                    }
                    bodiesList.add(symbols);
                }
            }
            this.productionRules.put(head, bodiesList);
        }

        determineSymbols();
    }


    private void determineSymbols() {
        this.nonTerminals.addAll(this.productionRules.keySet());
        if (this.startSymbol != null) {
            this.nonTerminals.add(this.startSymbol);
        }

        Set<String> allSymbols = new HashSet<>();
        for (List<List<String>> bodies : this.productionRules.values()) {
            for (List<String> body : bodies) {
                allSymbols.addAll(body);
            }
        }

        this.terminals.addAll(allSymbols);
        this.terminals.removeAll(this.nonTerminals);
    }


    public void printRules() {
        for (Map.Entry<String, List<List<String>>> entry : productionRules.entrySet()) {
            String head = entry.getKey();
            List<String> bodyStrings = new ArrayList<>();
            for (List<String> body : entry.getValue()) {
                if (body.isEmpty()) {
                    bodyStrings.add("epsilon");
                } else {
                    bodyStrings.add(String.join("", body));
                }
            }
            System.out.println("  " + head + " -> " + String.join(" | ", bodyStrings));
        }
    }


    public void displayLeftRecursiveRules() {
        System.out.println("\n--- Reguli cu Recursivitate Directă la Stânga ---");
        boolean found = false;

        for (Map.Entry<String, List<List<String>>> entry : this.productionRules.entrySet()) {
            String head = entry.getKey();
            List<List<String>> bodies = entry.getValue();

            List<String> leftRecursiveBodies = new ArrayList<>();

            for (List<String> body : bodies) {
                if (!body.isEmpty() && body.get(0).equals(head)) {
                    leftRecursiveBodies.add(String.join("", body));
                }
            }

            if (!leftRecursiveBodies.isEmpty()) {
                System.out.println(
                         head + " este recursiv la stânga prin: " +
                                head + " -> " + String.join(" | " + head + " -> ", leftRecursiveBodies)
                );
                found = true;
            }
        }
        if (!found) {
            System.out.println("  Nu s-a găsit nicio regulă cu recursivitate directă la stânga.");
        }
        System.out.println("---------------------------------------------------------");
    }


    public static void main(String[] args) {
        final String FILE_PATH = "gramatica.txt";

        System.out.println("Încărcarea gramaticii din fișierul: " + FILE_PATH);
        Gramatica g = new Gramatica();

        g.readFromFile(FILE_PATH);

        if (g.getStartSymbol() == null) {
            System.out.println("\nEroare: Inițializarea gramaticii a eșuat sau fișierul este gol.");
            return;
        }


        System.out.println("\n--- Sumar Gramatică ---");
        System.out.println("Simbolul de Start (S): " + g.getStartSymbol());
        System.out.println("Neterminale (N): " + g.getNonTerminals());
        System.out.println("Terminale (T): " + g.getTerminals());

        System.out.println("\n--- Toate Regulile de Producție ---");
        g.printRules();

        g.displayLeftRecursiveRules();

    }
}