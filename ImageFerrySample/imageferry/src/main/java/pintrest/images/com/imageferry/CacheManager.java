package pintrest.images.com.imageferry;

import android.util.LruCache;

/**
 * Created by Ishtiaq on Dec , 01, 2018.
 */

public class CacheManager {
    private LruCache<String, byte[]> mMemoryCache;
    private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private final int CACHE_SIZE = maxMemory / 8;

    private static final CacheManager ourInstance = new CacheManager();

    public static CacheManager getInstance() {
        return ourInstance;
    }

    private CacheManager() {
        mMemoryCache = new LruCache<String, byte[]>(CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, byte[] data) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return data.length / 1024;
            }
        };
    }

    public void addDataMemoryCache(String key, byte[] data) {
        if (getObjectFromMemCache(key) == null) {
            mMemoryCache.put(key, data);
        }
    }

    public byte[] getObjectFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}