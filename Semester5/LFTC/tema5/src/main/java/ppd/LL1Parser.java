package ppd;

import java.util.*;

public class LL1Parser {
    private Grammar Grammar;
    private Map<String, Set<String>> first = new HashMap<>();
    private Map<String, Set<String>> follow = new HashMap<>();
    private Map<String, Map<String, List<String>>> parsingTable = new HashMap<>();

    public LL1Parser(Grammar g) {
        this.Grammar = g;
        for (String nt : Grammar.getNonTerminals()) {
            first.put(nt, new HashSet<>());
            follow.put(nt, new HashSet<>());
        }
    }

    public void computeFirst() {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Map.Entry<String, List<List<String>>> entry : Grammar.getProductionRules().entrySet()) {
                String nt = entry.getKey();
                for (List<String> production : entry.getValue()) {
                    Set<String> productionFirst = getFirstOfSequence(production);
                    if (first.get(nt).addAll(productionFirst)) {
                        changed = true;
                    }
                }
            }
        }
    }

    private Set<String> getFirstOfSequence(List<String> sequence) {
        Set<String> result = new HashSet<>();
        if (sequence.isEmpty()) { // Epsilon
            result.add("epsilon");
            return result;
        }

        boolean allCanBeEpsilon = true;
        for (String symbol : sequence) {
            if (Grammar.isTerminal(symbol)) {
                result.add(symbol);
                allCanBeEpsilon = false;
                break;
            } else {
                // E neterminal
                Set<String> f = first.get(symbol);
                result.addAll(f);
                if (!f.contains("epsilon")) {
                    result.remove("epsilon");
                    allCanBeEpsilon = false;
                    break;
                }
                // Dacă conține epsilon, continuăm la următorul simbol, dar scoatem epsilon temporar
                result.remove("epsilon");
            }
        }
        if (allCanBeEpsilon) result.add("epsilon");
        return result;
    }

    public void computeFollow() {
        follow.get(Grammar.getStartSymbol()).add("$"); // $ = marcaj sfârșit
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Map.Entry<String, List<List<String>>> entry : Grammar.getProductionRules().entrySet()) {
                String lhs = entry.getKey(); // Partea stângă (A)

                for (List<String> rhs : entry.getValue()) { // Partea dreaptă
                    for (int i = 0; i < rhs.size(); i++) {
                        String B = rhs.get(i);
                        if (!Grammar.isNonTerminal(B)) continue;

                        // A -> alpha B beta
                        List<String> beta = rhs.subList(i + 1, rhs.size());
                        Set<String> firstBeta = getFirstOfSequence(beta);

                        // 1. FOLLOW(B) += FIRST(beta) \ {epsilon}
                        Set<String> target = follow.get(B);
                        int initialSize = target.size();

                        for (String s : firstBeta) {
                            if (!s.equals("epsilon")) target.add(s);
                        }

                        // 2. Dacă epsilon e în FIRST(beta) (sau beta e gol), FOLLOW(B) += FOLLOW(A)
                        if (firstBeta.contains("epsilon") || beta.isEmpty()) {
                            target.addAll(follow.get(lhs));
                        }

                        if (target.size() > initialSize) changed = true;
                    }
                }
            }
        }
    }

    public void createParsingTable() {
        for (String nt : Grammar.getNonTerminals()) {
            for (List<String> rule : Grammar.getProductionRules().get(nt)) {
                Set<String> firstAlpha = getFirstOfSequence(rule);

                for (String terminal : firstAlpha) {
                    if (!terminal.equals("epsilon")) {
                        addRuleToTable(nt, terminal, rule);
                    } else {
                        // Dacă regula poate fi epsilon, punem regula în tabel pentru tot ce e în FOLLOW(nt)
                        for (String f : follow.get(nt)) {
                            addRuleToTable(nt, f, rule); // Regula devine -> epsilon
                        }
                    }
                }
            }
        }
    }

    private void addRuleToTable(String nt, String term, List<String> rule) {
        parsingTable.putIfAbsent(nt, new HashMap<>());
        if (parsingTable.get(nt).containsKey(term)) {
            System.err.println("CONFLICT la [" + nt + ", " + term + "]! Grammar nu este LL(1).");
        }
        parsingTable.get(nt).put(term, rule);
    }

    public void parse(List<Token> inputTokens) {
        Stack<String> stack = new Stack<>();

        // Adăugăm token-ul de final '$' păstrând linia ultimului token real
        int lastLine = inputTokens.isEmpty() ? 1 : inputTokens.get(inputTokens.size()-1).line;
        List<Token> tokensWithEnd = new ArrayList<>(inputTokens);
        tokensWithEnd.add(new Token("$", "$", lastLine));

        stack.push("$");
        stack.push(Grammar.getStartSymbol());

        int index = 0;
        Token previousToken = null;

        while (!stack.isEmpty()) {
            String top = stack.peek();
            Token currentToken = tokensWithEnd.get(index);
            String lookahead = currentToken.type;

            if (top.equals(lookahead)) {
                if (top.equals("$")) {
                    System.out.println("\n[SUCCES] Programul este corect sintactic!");
                    return;
                }
                previousToken = currentToken;
                stack.pop();
                index++;

            } else if (Grammar.isTerminal(top)) {
                int errorLine = currentToken.line;
                boolean probableMissingChar = false;

                if (previousToken != null && currentToken.line > previousToken.line) {
                    if (top.equals("SEMI") || top.equals("RPAREN") || top.equals("RBRACE")) {
                        errorLine = previousToken.line;
                        probableMissingChar = true;
                    }
                }

                System.err.println("\n[EROARE COMPILARE]");
                System.err.println("Locatie: linia " + errorLine);

                String expected = getFriendlyName(top);
                String found = currentToken.value;

                if (probableMissingChar && top.equals("SEMI")) {
                    System.err.println("Detalii: Probabil lipsește " + expected + " la sfârșitul liniei " + errorLine);
                } else {
                    System.err.println("Detalii: Se astepta simbolul " + expected + " dar s-a gasit '" + found + "'");
                }
                return;

            } else {
                Map<String, List<String>> row = parsingTable.get(top);

                if (row != null && row.containsKey(lookahead)) {
                    stack.pop();
                    List<String> production = row.get(lookahead);
                    for (int i = production.size() - 1; i >= 0; i--) {
                        if (!production.get(i).equals("epsilon")) {
                            stack.push(production.get(i));
                        }
                    }
                } else {
                    int errLine = currentToken.line;
                    if (previousToken != null && currentToken.line > previousToken.line) {
                        errLine = previousToken.line;
                    }

                    System.err.printf("\n[EROARE SINTACTICA] la linia %d: Structura invalida (token neasteptat: '%s').%n",
                            errLine, currentToken.value);

                    if (row != null) {
                        List<String> friendlyExpectations = new ArrayList<>();
                        for (String key : row.keySet()) {
                            friendlyExpectations.add(getFriendlyName(key));
                        }
                        System.err.println("Se aștepta să înceapă una dintre structurile: " + friendlyExpectations);
                    } else {
                        System.err.println("Nu există nicio regulă validă pentru neterminalul intern: " + top);
                    }
                    return;
                }
            }
        }
    }

    private String getFriendlyName(String terminal) {
        switch (terminal) {
            case "SEMI":    return "';'";
            case "ASSIGN":  return "'='";
            case "LPAREN":  return "'('";
            case "RPAREN":  return "')'";
            case "LBRACE":  return "'{'";
            case "RBRACE":  return "'}'";
            case "ID":      return "identificator";
            case "CONST":   return "constanta";
            case "HASH_INCLUDE": return "'#include'";
            case "IOSTREAM": return "'iostream'";
            case "USING":   return "'using'";
            case "NAMESPACE": return "'namespace'";
            case "STD":     return "'std'";
            case "INT":     return "'int'";
            case "MAIN":    return "'main'";
            case "RETURN":  return "'return'";
            case "CIN":     return "'cin'";
            case "COUT":    return "'cout'";
            case "SHIFT_L": return "'<<'";
            case "SHIFT_R": return "'>>'";
            case "PLUS":    return "'+'";
            case "MINUS":   return "'-'";
            case "MUL":     return "'*'";
            case "DIV":     return "'/'";
            case "MOD":     return "'%'";
            case "LT":      return "'<'";
            case "GT":      return "'>'";
            case "LE":      return "'<='";
            case "GE":      return "'>='";
            case "EQ":      return "'=='";
            case "NE":      return "'!='";
            case "AND":     return "'&&'";
            case "OR":      return "'||'";
            case "IF":      return "'if'";
            case "ELSE":    return "'else'";
            case "WHILE":   return "'while'";
            case "STRUCT":  return "'struct'";
            case "MSG":     return "mesaj (text)";
            case "$":       return "sfarsit de fisier";
            default:        return "'" + terminal + "'";
        }
    }
}
