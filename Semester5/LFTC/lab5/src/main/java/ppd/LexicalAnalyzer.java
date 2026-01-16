package ppd;

import ppd.fip.FIPEntry;
import ppd.ts.SymbolEntry;
import ppd.ts.SymbolTable;
import ppd.ts.SymbolType;

import java.io.*;
import java.util.*;

public class LexicalAnalyzer {

    private static final int CODE_IDENTIFIER = 1;
    private static final int CODE_CONST_NUMBER = 2;
    private static final int CODE_STRING_LITERAL = 3;

    private static final Map<String, Integer> TOKEN_TABLE = createTokenTable();

    private final FiniteAutomata identAF;
    private final FiniteAutomata intAF;
    private final FiniteAutomata realAF;

    private final SymbolTable ts = new SymbolTable(10);
    private final List<FIPEntry> fip = new ArrayList<>();
    private final List<String> lexicalErrors = new ArrayList<>();

    private String content;
    private int cursor;

    public LexicalAnalyzer() {
        try {
            identAF = new FiniteAutomata();
            identAF.readFromFile("af/af_id.txt");

            intAF = new FiniteAutomata();
            intAF.readFromFile("af/af_int.txt");

            realAF = new FiniteAutomata();
            realAF.readFromFile("af/af_real.txt");

        } catch (IOException e) {
            System.err.println(" Eroare la încărcarea AF-urilor. Verificați calea (e.g., af/af_id.txt) și existența fișierelor: " + e.getMessage());
            throw new RuntimeException("Eroare inițializare AF", e);
        }
    }


    private static Map<String, Integer> createTokenTable() {
        Map<String, Integer> m = new LinkedHashMap<>();
        m.put("IDENT", CODE_IDENTIFIER);
        m.put("CONST_NUMBER", CODE_CONST_NUMBER);
        m.put("STRING_LITERAL", CODE_STRING_LITERAL);

        int code = 4;

        String[] tokens = {
                "#include",
                "<library>",

                "using", "namespace", "std",
                "int", "char", "float", "struct",
                "if", "else", "while", "return", "main",
                "cin", "cout",

                "(", ")", "{", "}", ";", ",",
                "=", "+", "-", "*", "/", "%",
                "<<", ">>",
                "<", ">", "<=", ">=", "==", "!=", "&&", "||"
        };

        for (String t : tokens) m.put(t, code++);
        return m;
    }

    public void processFile(String fileName) throws IOException {
        content = readAll(fileName);
        content = removeComments(content);

        cursor = 0;
        while (cursor < content.length()) {
            skipWhitespace();
            if (cursor >= content.length()) break;

            int startCursor = cursor;
            Map.Entry<String, Integer> tokenResult = scanNextToken(startCursor);

            if (tokenResult != null) {
                processToken(tokenResult.getKey(), tokenResult.getValue());
            } else {
                if (cursor == startCursor) {
                    char badChar = content.charAt(cursor);
                    int lineNo = getLineNumber(startCursor);
                    int colNo = getColNumber(startCursor);

                    lexicalErrors.add("Unrecognized symbol '" + badChar + "' at line " + lineNo + ", col " + colNo);
                    cursor++;
                }
            }
        }
    }

    private Map.Entry<String, Integer> scanNextToken(int start) {
        if (start >= content.length()) return null;
        String remainingContent = content.substring(start);
        char currentChar = content.charAt(start);

        for (int len = Math.min(10, content.length() - start); len >= 2; len--) {
            String potentialToken = content.substring(start, start + len);
            if (TOKEN_TABLE.containsKey(potentialToken)) {
                cursor = start + len;
                return new AbstractMap.SimpleEntry<>(potentialToken, TOKEN_TABLE.get(potentialToken));
            }
        }

        if (currentChar == '<') {
            int end = start + 1;
            while (end < content.length()) {
                char c = content.charAt(end);
                if (c == '>') {
                    String token = content.substring(start, end + 1);
                    cursor = end + 1;
                    return new AbstractMap.SimpleEntry<>("<library>", TOKEN_TABLE.get("<library>"));
                }
                if (c == '\n' || c == ' ') break;
                end++;
            }
        }

        String singleCharToken = content.substring(start, start + 1);
        if (TOKEN_TABLE.containsKey(singleCharToken)) {
            cursor = start + 1;
            return new AbstractMap.SimpleEntry<>(singleCharToken, TOKEN_TABLE.get(singleCharToken));
        }

        if (isDigit(currentChar) || currentChar == '.') {
            String realPrefix = realAF.longestAcceptedPrefix(remainingContent);
            String intPrefix = intAF.longestAcceptedPrefix(remainingContent);

            String token = null;
            int length = 0;

            if (realPrefix.length() > intPrefix.length()) {
                token = realPrefix;
                length = realPrefix.length();
            } else if (!intPrefix.isEmpty()) {
                token = intPrefix;
                length = intPrefix.length();
            }

            if (token != null) {
                int nextCharIndex = start + length;
                if (nextCharIndex < content.length()) {
                    char nextChar = content.charAt(nextCharIndex);

                    // Verifică dacă după număr (ex: 123) urmează o literă (ex: a)
                    if (isLetter(nextChar) || nextChar == '_') {
                        int errEnd = nextCharIndex + 1;
                        while (errEnd < content.length() && (isLetter(content.charAt(errEnd)) || isDigit(content.charAt(errEnd)) || content.charAt(errEnd) == '_')) {
                            errEnd++;
                        }
                        String errorToken = content.substring(start, errEnd);
                        int lineNo = getLineNumber(start);
                        int colNo = getColNumber(start);
                        lexicalErrors.add("Invalid token (bad numeric/identifier) '" + errorToken + "' at line " + lineNo + ", col " + colNo);

                        cursor = errEnd;
                        return null;
                    }
                }
                cursor = start + length;
                return new AbstractMap.SimpleEntry<>(token, CODE_CONST_NUMBER);
            }
        }

        if (isLetter(currentChar) || currentChar == '_') {
            String token = identAF.longestAcceptedPrefix(remainingContent);
            if (!token.isEmpty()) {
                cursor = start + token.length();
                return new AbstractMap.SimpleEntry<>(token, CODE_IDENTIFIER);
            }
        }

        if (currentChar == '"') {
            int end = start + 1;
            boolean escaped = false;
            while (end < content.length()) {
                char c = content.charAt(end);
                if (c == '"' && !escaped) {
                    String token = content.substring(start, end + 1);
                    cursor = end + 1;
                    return new AbstractMap.SimpleEntry<>(token, CODE_STRING_LITERAL);
                }
                escaped = (c == '\\' && !escaped);
                if (c == '\n') return null;
                end++;
            }
        }
        return null;
    }

    private void processToken(String token, int tokenTypeOrCode) {

        if (tokenTypeOrCode > 3) {
            fip.add(new FIPEntry(tokenTypeOrCode, 0));
            return;
        }

        if (tokenTypeOrCode == CODE_STRING_LITERAL) {
            int id = ts.add(token, SymbolType.CONSTANT);
            fip.add(new FIPEntry(CODE_STRING_LITERAL, id));
            return;
        }

        if (tokenTypeOrCode == CODE_CONST_NUMBER) {
            int id = ts.add(token, SymbolType.CONSTANT);
            fip.add(new FIPEntry(CODE_CONST_NUMBER, id));
            return;
        }

        if (tokenTypeOrCode == CODE_IDENTIFIER) {
            int id = ts.add(token, SymbolType.IDENTIFIER);
            fip.add(new FIPEntry(CODE_IDENTIFIER, id));
        }
    }

    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void skipWhitespace() {
        while (cursor < content.length() && Character.isWhitespace(content.charAt(cursor))) {
            cursor++;
        }
    }

    private int getLineNumber(int position) {
        int line = 1;
        for (int i = 0; i < position && i < content.length(); i++) {
            if (content.charAt(i) == '\n') {
                line++;
            }
        }
        return line;
    }

    private int getColNumber(int position) {
        int col = 1;
        for (int i = position - 1; i >= 0 && content.charAt(i) != '\n'; i--) {
            col++;
        }
        return col;
    }

    private String readAll(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new FileReader(fileName))) {
            String l;
            while ((l = r.readLine()) != null) {
                sb.append(l).append("\n");
            }
        }
        return sb.toString();
    }

    private String removeComments(String s) {
        StringBuilder result = new StringBuilder();
        int current = 0;

        while (current < s.length()) {
            char c = s.charAt(current);

            if (c == '/') {
                if (current + 1 < s.length()) {
                    char next = s.charAt(current + 1);

                    if (next == '/') {
                        while (current < s.length() && s.charAt(current) != '\n') {
                            result.append(' ');
                            current++;
                        }
                    } else if (next == '*') {
                        int startBlock = current;
                        current += 2;

                        while (current < s.length()) {
                            if (s.charAt(current) == '*' && current + 1 < s.length() && s.charAt(current + 1) == '/') {
                                current += 2;
                                break;
                            }
                            current++;
                        }

                        for (int i = startBlock; i < current; i++) {
                            if (s.charAt(i) == '\n') {
                                result.append('\n');
                            } else {
                                result.append(' ');
                            }
                        }
                        continue;
                    }
                }
            }
            result.append(c);
            current++;
        }
        return result.toString();
    }


    public void saveOutputs(String fipFile, String tsFile) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(tsFile))) {
            for (SymbolEntry e : ts.getEntriesInOrder()) {
                pw.println(e.getId() + " | " + e.getType() + " | " + e.getValue());
            }
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(fipFile))) {
            for (FIPEntry entry : fip) {
                pw.println("(" + entry.getTokenCode() + "," + entry.getTsPosition() + ")");
            }
        }
    }

    public void reportErrors() {
        if (lexicalErrors.isEmpty()) {
            System.out.println("No lexical errors found.");
        } else {
            System.out.println("Lexical errors (" + lexicalErrors.size() + "):");
            for (String err : lexicalErrors) {
                System.out.println("  " + err);
            }
        }
    }
}