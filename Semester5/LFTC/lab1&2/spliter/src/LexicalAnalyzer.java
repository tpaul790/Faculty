import fip.FIPEntry;
import ts.SymbolEntry;
import ts.SymbolTable;
import ts.SymbolType;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class LexicalAnalyzer {

    private static final Map<String, Integer> TOKEN_TABLE = createTokenTable();

    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "#include|<[^>]+>" +
                    "|\\b(using namespace std|int main|int|char|float|struct|if|else|while|return|cin|cout|main)\\b" +
                    "|\\b\\d+\\.\\d+|\\d+\\b" +             // numbers (float then int)
                    "|\"(\\\\.|[^\\\\\"])*\"" +             // string literal (supports escaped chars)
                    "|\\b[a-zA-Z_][a-zA-Z0-9_]*\\b" +       // identifiers
                    "|<<|>>|<=|>=|==|!=|&&|\\|\\|" +        // multi-char operators
                    "|[=+\\-*/%<>{}();,]"                   // single-char operators and punctuation
    );

    private static final Pattern LINE_COMMENT = Pattern.compile("//.*");
    private static final Pattern BLOCK_COMMENT = Pattern.compile("/\\*(?:.|\\R)*?\\*/", Pattern.DOTALL);

    private final SymbolTable ts = new SymbolTable(10);

    private final List<FIPEntry> fip = new ArrayList<>();

    private final List<String> lexicalErrors = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java LexicalAnalyzer <source_file>");
            return;
        }
        String fileName = args[0];
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        try {
            analyzer.processFile(fileName);
            analyzer.saveOutputs("FIP.txt", "TS.txt");
            analyzer.reportErrors();
            System.out.println("Done. FIP.txt and TS.txt generated.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Integer> createTokenTable() {
        Map<String, Integer> m = new LinkedHashMap<>();
        int code = 4;

        String[] tokens = {
                "#include", "<library>", "int main", "using namespace std",
                "int", "char", "float", "struct",
                "if", "else", "while", "return",
                "cin", "cout",
                "(", ")", "{", "}", ";", ",",
                "=", "+", "-", "*", "/", "%", "<<", ">>",
                "<", ">", "<=", ">=", "==", "!=", "&&", "||"
        };

        for (String t : tokens) m.put(t, code++);
        return m;
    }

    private void processFile(String fileName) throws IOException {
        String content = readAll(fileName);
        content = removeBlockCommentsPreserveLines(content);
        BufferedReader br = new BufferedReader(new StringReader(content));
        String line;
        int lineNo = 0;
        while ((line = br.readLine()) != null) {
            lineNo++;
            line = LINE_COMMENT.matcher(line).replaceAll("");
            tokenizeLine(line, lineNo);
        }
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

    private String removeBlockCommentsPreserveLines(String s) {
        Matcher m = BLOCK_COMMENT.matcher(s);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            String match = m.group();
            int newlines = countNewlines(match);
            StringBuilder repl = new StringBuilder();
            repl.append("\n".repeat(Math.max(0, newlines)));
            if (newlines == 0) repl.append(" ");
            m.appendReplacement(sb, Matcher.quoteReplacement(repl.toString()));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private int countNewlines(String s) {
        int c = 0;
        for (char ch : s.toCharArray()) if (ch == '\n') c++;
        return c;
    }

    private void tokenizeLine(String line, int lineNo) {
        Matcher matcher = TOKEN_PATTERN.matcher(line);
        int idx = 0;
        while (idx < line.length()) {
            if (matcher.find(idx)) {
                if (matcher.start() != idx) {
                    String bad = line.substring(idx, matcher.start());
                    if (!bad.trim().isEmpty()) {
                        int col = idx + 1;
                        lexicalErrors.add("Unrecognized token '" + bad.trim() + "' at line " + lineNo + ", col " + col);
                    }
                }
                String token = matcher.group();
                int col = matcher.start() + 1;
                processToken(token, lineNo, col);
                idx = matcher.end();
            } else {
                String tail = line.substring(idx);
                if (!tail.trim().isEmpty()) {
                    lexicalErrors.add("Unrecognized token '" + tail.trim() + "' at line " + lineNo + ", col " + (idx+1));
                }
                break;
            }
        }
    }

    private void processToken(String token, int lineNo, int col) {
        token = token.trim();
        if (token.isEmpty()) return;

        Integer code = TOKEN_TABLE.get(token);
        if (code != null) {
            fip.add(new FIPEntry(code, 0));
            return;
        }

        if (token.startsWith("<") && token.endsWith(">")) {
            int c = getTokenCode("<library>");
            fip.add(new FIPEntry(c, 0));
            return;
        }

        if (token.startsWith("\"") && token.endsWith("\"")) {
            int id = ts.add(token, SymbolType.CONSTANT);
            fip.add(new FIPEntry(getTokenCode("STRING_LITERAL"), id));
            return;
        }

        if (isNumber(token)) {
            int id = ts.add(token, SymbolType.CONSTANT);
            fip.add(new FIPEntry(getTokenCode("CONST_NUMBER"), id));
            return;
        }

        if (isIdentifier(token)) {
            int id = ts.add(token, SymbolType.IDENTIFIER);
            fip.add(new FIPEntry(getTokenCode("IDENT"), id));
            return;
        }

        for (int i = 0; i < token.length(); i++) {
            String t = token.substring(i, i+1);
            Integer c2 = TOKEN_TABLE.get(t);
            if (c2 != null) {
                fip.add(new FIPEntry(c2, 0));
            } else {
                lexicalErrors.add("Unknown symbol '" + t + "' at line " + lineNo + ", col " + (col + i));
            }
        }
    }

    private boolean isNumber(String s) {
        return s.matches("\\d+(\\.\\d+|\\d+)?");
    }

    private boolean isIdentifier(String s) {
        return s.matches("[a-zA-Z_][a-zA-Z0-9_]*");
    }

    private int getTokenCode(String token) {
        Integer c = TOKEN_TABLE.get(token);
        if (c != null) return c;
        if ("IDENT".equals(token)) return 1;
        if ("CONST_NUMBER".equals(token)) return 2;
        if ("STRING_LITERAL".equals(token)) return 3;
        return -1;
    }

    private void saveOutputs(String fipFile, String tsFile) throws IOException {
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

    private void reportErrors() {
        if (lexicalErrors.isEmpty()) {
            System.out.println("No lexical errors found.");
        } else {
            System.out.println("Lexical errors:");
            for (String err : lexicalErrors) {
                System.out.println("  " + err);
            }
        }
    }
}
