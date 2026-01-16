package ppd.ts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SymbolTable {
    private final List<LinkedList<SymbolEntry>> buckets;
    private final int bucketCount;
    private int nextId = 1;

    private final List<SymbolEntry> entries;

    public SymbolTable(int bucketCount) {
        this.bucketCount = bucketCount;
        this.buckets = new ArrayList<>(bucketCount);
        this.entries = new ArrayList<>();

        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new LinkedList<>());
        }
    }

    private int hash(String value) {
        return Math.floorMod(value.hashCode(), bucketCount);
    }

    public int add(String value, SymbolType type) {
        int bucketIndex = hash(value);
        LinkedList<SymbolEntry> bucket = buckets.get(bucketIndex);

        for (SymbolEntry entry : bucket) {
            if (entry.getValue().equals(value)) {
                return entry.getId(); // deja există
            }
        }

        int id = nextId++;
        SymbolEntry newEntry = new SymbolEntry(id, value, type);
        bucket.add(newEntry);
        entries.add(newEntry);
        return id;
    }


    public SymbolEntry getById(int id) {
        int index = id - 1;
        if (index < 0 || index >= entries.size()) {
            return null;
        }
        return entries.get(index);
    }


    public List<SymbolEntry> getEntriesInOrder() {
        return entries;
    }
}
