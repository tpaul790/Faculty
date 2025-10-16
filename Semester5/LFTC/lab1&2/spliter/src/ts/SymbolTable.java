package ts;

import lombok.Data;

import java.util.*;

public class SymbolTable {
    private final List<LinkedList<SymbolNode>> buckets;
    private final int bucketCount;
    private final Map<String, SymbolNode> quickMap; // optional quick lookup
    private final List<SymbolNode> insertionOrder;
    private int nextId = 1;

    public SymbolTable(int bucketCount) {
        this.bucketCount = bucketCount;
        buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) buckets.add(new LinkedList<>());
        quickMap = new HashMap<>();
        insertionOrder = new ArrayList<>();
    }

    public int add(String value, SymbolType type) {
        SymbolNode node = quickMap.get(value);
        if (node != null) return node.entry.getId();
        int id = nextId++;
        SymbolEntry e = new SymbolEntry(id, value, type);
        SymbolNode newNode = new SymbolNode(value, e);
        int h = Math.floorMod(value.hashCode(), bucketCount);
        buckets.get(h).add(newNode);
        quickMap.put(value, newNode);
        insertionOrder.add(newNode);
        return id;
    }

    int lookup(String value) {
        SymbolNode n = quickMap.get(value);
        return n == null ? -1 : n.entry.getId();
    }

    public List<SymbolEntry> getEntriesInOrder() {
        List<SymbolEntry> res = new ArrayList<>();
        for (SymbolNode n : insertionOrder) res.add(n.entry);
        return res;
    }

    private static class SymbolNode {
        final String key;
        final SymbolEntry entry;
        SymbolNode(String key, SymbolEntry entry) { this.key = key; this.entry = entry; }
    }


    public List<LinkedList<SymbolNode>> getBuckets() {
        return buckets;
    }

    public int getBucketCount() {
        return bucketCount;
    }

    public Map<String, SymbolNode> getQuickMap() {
        return quickMap;
    }

    public List<SymbolNode> getInsertionOrder() {
        return insertionOrder;
    }

    public int getNextId() {
        return nextId;
    }
}
