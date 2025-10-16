package ts;

import lombok.Data;

public class SymbolEntry {
    private final int id;
    private final String value;
    private final SymbolType type;

    public SymbolEntry(int id, String value, SymbolType type) {
        this.id = id; this.value = value; this.type = type;
    }

    public int getId() {
        return id;
    }

    public SymbolType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
