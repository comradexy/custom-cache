package cn.comradexy.demo.cache;

import cn.comradexy.demo.model.dto.UserDTO;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU 缓存
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: LRU 缓存
 */
public class LRUCache<K, V> {
    private final LinkedHashMap<K, V> cache;
    private final int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }

    public V get(K key) {
        return cache.getOrDefault(key, null);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

}
