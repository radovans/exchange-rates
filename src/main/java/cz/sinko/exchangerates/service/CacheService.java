package cz.sinko.exchangerates.service;

import cz.sinko.exchangerates.service.dto.CacheData;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Service for caching data.
 *
 * @author Radovan Šinko
 */
public interface CacheService {

    /**
     * Saves cache data.
     *
     * @param cacheData cache data
     */
    void save(CacheData cacheData);

    /**
     * Gets cache data by key.
     *
     * @param key cache key
     * @return cache data
     */
    Optional<CacheData> getKey(String key);

    /**
     * Creates cache data from object and saves it.
     *
     * @param key    cache key
     * @param object object to cache
     * @param <T>    type of the object
     */
    <T> void cacheData(String key, T object);

    /**
     * Creates cache data from object and saves it with time to live.
     *
     * @param key        cache key
     * @param object     object to cache
     * @param timeToLive time to live in seconds
     * @param <T>        type of the object
     */
    <T> void cacheData(String key, T object, Long timeToLive);

    /**
     * Gets cached data or saves it if not present.
     *
     * @param key          cache key
     * @param dataSupplier supplier for data to cache
     * @param type         type of the object
     * @param <T>          type of the object
     * @return cached data
     */
    <T> T getCachedDataOrSave(String key, Supplier<T> dataSupplier, Class<T> type);

    /**
     * Gets cached data or saves it if not present with time to live.
     *
     * @param key          cache key
     * @param dataSupplier supplier for data to cache
     * @param type         type of the object
     * @param ttl          time to live in seconds
     * @param <T>          type of the object
     * @return cached data
     */
    <T> T getCachedDataOrSaveWithTtl(String key, Supplier<T> dataSupplier, Class<T> type, Long ttl);
}
