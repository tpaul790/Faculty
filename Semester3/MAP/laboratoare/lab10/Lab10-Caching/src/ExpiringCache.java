import java.util.*;
import java.util.concurrent.*;

public class ExpiringCache<K, V> {
    private final Map<K, CacheItem<V>> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();

    // Constructor to start the cleanup thread
    public ExpiringCache(long cleanupIntervalMillis) {
        cleaner.scheduleAtFixedRate(this::cleanup, cleanupIntervalMillis, cleanupIntervalMillis, TimeUnit.MILLISECONDS);
    }

    // Method to add items to the cache with an expiry time
    public void put(K key, V value, long expiryMillis) {
        long expiryTime = System.currentTimeMillis() + expiryMillis;
       //TODO add a cache item to the cache
        CacheItem<V> cacheItem = new CacheItem<>(value, expiryTime);
        cache.put(key, cacheItem);
    }

    // Method to get items from the cache
    public V get(K key) {
        CacheItem<V> item = cache.get(key);
        //TODO remove the key from cache if the item is null or has expired
        if(item == null || item.isExpired()) {
            cache.remove(key);
            return null;
        }
        return item.getValue();
    }

    // Cleanup method to remove expired entries
    private void cleanup() {
       //TODO implement it
        cache.forEach((key, value) -> {
            if (value.isExpired()) {
                cache.remove(key);
            }
        });
    }

    // Method to shut down the cleaner thread
    public void shutdown() {
       //TODO use the shutdown from the cleaner thread
        cleaner.shutdown();
    }

}
