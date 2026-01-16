package ppd;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {

    public List<Token> tokenize(String sourceCode) {
        List<Token> tokens = new ArrayList<>();

        String tokenPatterns =
                "(?<INCLUDE>#include)|(?<IOSTREAM>iostream)|(?<USING>using)|(?<NAMESPACE>namespace)|(?<STD>std)|" +
                        "(?<MAIN>main)|(?<RETURN>return)|" +

                        "(?<INT>int)|(?<CHAR>char)|(?<FLOAT>float)|(?<STRUCT>struct)|" +

                        "(?<IF>if)|(?<ELSE>else)|(?<WHILE>while)|" +

                        "(?<CIN>cin)|(?<COUT>cout)|" +

                        "(?<AND>&&)|(?<OR>\\|\\|)|" +

                        "(?<SHIFTL>" + Pattern.quote("<<") + ")|" +
                        "(?<SHIFTR>" + Pattern.quote(">>") + ")|" +

                        "(?<EQ>==)|(?<NE>!=)|(?<LE><=)|(?<GE>>=)|" +

                        "(?<ASSIGN>=)|(?<SEMI>;)|" +
                        "(?<LPAREN>\\()|(?<RPAREN>\\))|(?<LBRACE>\\{)|(?<RBRACE>\\})|" +
                        "(?<LT>" + Pattern.quote("<") + ")|" +
                        "(?<GT>" + Pattern.quote(">") + ")|" +
                        "(?<PLUS>\\+)|(?<MINUS>-)|(?<MUL>\\*)|(?<DIV>/)|(?<MOD>%)|" +

                        "(?<MSG>\"[^\"]*\")|" +
                        "(?<ID>[a-zA-Z_][a-zA-Z0-9_]*)|(?<CONST>[0-9]+)|" +

                        "(?<NEWLINE>\\n)|(?<WHITESPACE>[ \\t\\r]+)";

        Pattern pattern = Pattern.compile(tokenPatterns);
        Matcher matcher = pattern.matcher(sourceCode);
        int currentLine = 1;

        String[] groupNames = {
                "INCLUDE", "IOSTREAM", "USING", "NAMESPACE", "STD", "MAIN", "RETURN",
                "INT", "CHAR", "FLOAT", "STRUCT",
                "IF", "ELSE", "WHILE",
                "CIN", "COUT",
                "AND", "OR",
                "SHIFTL", "SHIFTR",
                "EQ", "NE", "LE", "GE",
                "ASSIGN", "SEMI",
                "LPAREN", "RPAREN", "LBRACE", "RBRACE", "LT", "GT",
                "PLUS", "MINUS", "MUL", "DIV", "MOD",
                "MSG", "ID", "CONST"
        };

        while (matcher.find()) {
            if (matcher.group("NEWLINE") != null) {
                currentLine++;
                continue;
            }
            if (matcher.group("WHITESPACE") != null) continue;

            for (String name : groupNames) {
                if (matcher.group(name) != null) {
                    String typeToSend = name;

                    if (name.equals("INCLUDE")) typeToSend = "HASH_INCLUDE";
                    if (name.equals("SHIFTL"))  typeToSend = "SHIFT_L";
                    if (name.equals("SHIFTR"))  typeToSend = "SHIFT_R";

                    if (name.equals("MSG")) typeToSend = "MSG";

                    tokens.add(new Token(typeToSend, matcher.group(name), currentLine));
                    break;
                }
            }
        }
        return tokens;
    }
}