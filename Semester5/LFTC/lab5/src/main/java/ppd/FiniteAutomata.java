package ppd;

import java.io.*;
import java.util.*;

public class FiniteAutomata {
    private Set<String> states = new HashSet<>();
    private Set<String> alphabet = new HashSet<>();
    private String startState;
    private Set<String> finalStates = new HashSet<>();
    private Map<String, Map<String, String>> transitions = new HashMap<>();

    public void readFromFile(String fileName) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            throw new FileNotFoundException("Fișierul " + fileName + " nu a fost găsit în resources!");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            parseInput(br);
        }
    }

    private void parseInput(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
            line = line.trim();
            if (line.startsWith("states:")) {
                states.addAll(Arrays.asList(line.substring(7).trim().split(",")));
            } else if (line.startsWith("alphabet:")) {
                alphabet.addAll(Arrays.asList(line.substring(9).trim().split(",")));
            } else if (line.startsWith("start:")) {
                startState = line.substring(6).trim();
            } else if (line.startsWith("finals:")) {
                finalStates.addAll(Arrays.asList(line.substring(7).trim().split(",")));
            } else if (line.startsWith("transitions:")) {
                StringBuilder sb = new StringBuilder(line.substring(12).trim());
                // Citim toate liniile rămase pentru lista de tranziții
                String next;
                while ((next = br.readLine()) != null && !next.trim().isEmpty()) {
                    sb.append(next.trim());
                }
                parseTransitions(sb.toString());
                break;
            }
        }
    }

    private void parseTransitions(String text) {
        String[] parts = text.split(";");
        for (String part : parts) {
            part = part.trim();
            if (part.isEmpty()) continue;

            // Format general: (q0-0,1,2,3->q8)
            part = part.replace("(", "").replace(")", "");
            String[] p1 = part.split(">");
            if (p1.length != 2) continue;
            String to = p1[1].trim();
            String[] p2 = p1[0].split("<");
            if (p2.length != 2) continue;
            String from = p2[0].trim();
            String symbolList = p2[1].trim();

            // simbolurile pot fi separate prin virgule
            String[] symbols = symbolList.split(",");

            transitions.putIfAbsent(from, new HashMap<>());
            for (String sym : symbols) {
                sym = sym.trim();
                if (!sym.isEmpty()) {
                    transitions.get(from).put(sym, to);
                }
            }
        }
    }

    public boolean accept(String sequence) {
        String current = startState;
        for (char c : sequence.toCharArray()) {
            String sym = String.valueOf(c);
            if (!alphabet.contains(sym)) return false;
            Map<String, String> trans = transitions.get(current);
            if (trans == null || !trans.containsKey(sym)) return false;
            current = trans.get(sym);
        }
        return finalStates.contains(current);
    }

    public String longestAcceptedPrefix(String sequence) {
        String current = startState;
        StringBuilder prefix = new StringBuilder();
        String longest = "";

        for (char c : sequence.toCharArray()) {
            String sym = String.valueOf(c);
            if (!alphabet.contains(sym)) break;
            Map<String, String> trans = transitions.get(current);
            if (trans == null || !trans.containsKey(sym)) break;
            current = trans.get(sym);
            prefix.append(c);
            if (finalStates.contains(current)) {
                longest = prefix.toString();
            }
        }
        return longest;
    }
}

