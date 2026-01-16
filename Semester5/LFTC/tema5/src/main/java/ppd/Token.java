package ppd;

public class Token {
    public final String type;  // (ex: "INT", "ID", "ASSIGN")
    public final String value; // Textul propriu-zis (ex: "int", "x", "=")
    public final int line;     // Linia din fișier

    public Token(String type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    @Override
    public String toString() {
        return String.format("[%s, '%s', linia %d]", type, value, line);
    }
}