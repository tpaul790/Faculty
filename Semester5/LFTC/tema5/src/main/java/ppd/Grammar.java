package ppd;

import java.io.*;
import java.util.*;

public class Grammar {
    private String startSymbol;
    private final Map<String, List<List<String>>> productionRules;
    private final Set<String> nonTerminals;
    private final Set<String> terminals;

    public Grammar() {
        this.productionRules = new HashMap<>();
        this.nonTerminals = new HashSet<>();
        this.terminals = new HashSet<>();
    }

    public void readFromFile(String filename) {
        List<String> lines = new ArrayList<>();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (is == null) {
                System.err.println("Fișierul " + filename + " nu a fost găsit în resources!");
                return;
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        lines.add(line.trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (lines.isEmpty()) return;

        this.startSymbol = lines.get(lines.size() - 1);
        lines.remove(lines.size() - 1);

        for (String line : lines) {
            String[] parts = line.split(" -> ");
            if (parts.length != 2) continue;
            String head = parts[0].trim();
            String[] bodyParts = parts[1].split(" \\| ");
            List<List<String>> bodiesList = new ArrayList<>();
            for (String bodyStr : bodyParts) {
                if (bodyStr.equalsIgnoreCase("epsilon")) {
                    bodiesList.add(new ArrayList<>());
                } else {
                    bodiesList.add(new ArrayList<>(Arrays.asList(bodyStr.trim().split(" "))));
                }
            }
            this.productionRules.put(head, bodiesList);
        }
        determineSymbols();
    }

    private void determineSymbols() {
        this.nonTerminals.addAll(this.productionRules.keySet());
        this.nonTerminals.add(this.startSymbol);

        for (List<List<String>> bodies : this.productionRules.values()) {
            for (List<String> body : bodies) {
                for (String symbol : body) {
                    if (!productionRules.containsKey(symbol)) {
                        terminals.add(symbol);
                    }
                }
            }
        }
    }

    public String getStartSymbol() { return startSymbol; }
    public Map<String, List<List<String>>> getProductionRules() { return productionRules; }
    public Set<String> getNonTerminals() { return nonTerminals; }
    public Set<String> getTerminals() { return terminals; }
    public boolean isNonTerminal(String s) { return nonTerminals.contains(s); }
    public boolean isTerminal(String s) { return terminals.contains(s); }
}
