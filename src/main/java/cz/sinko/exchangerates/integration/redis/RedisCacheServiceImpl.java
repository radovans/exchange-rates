package cz.sinko.exchangerates.integration.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.sinko.exchangerates.integration.redis.repository.CacheDataRepository;
import cz.sinko.exchangerates.service.CacheService;
import cz.sinko.exchangerates.service.dto.CacheData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Redis implementation of {@link CacheService}.
 *
 * @author Radovan Šinko
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisCacheServiceImpl implements CacheService {

    private final CacheDataRepository cacheDataRepository;

    private final ObjectMapper objectMapper;

    @Override
    @Async
    public void save(final CacheData cacheData) {
        log.info("Saving cache data to redis: {}", cacheData);
        cacheDataRepository.save(cacheData);
    }

    @Override
    public Optional<CacheData> getKey(final String key) {
        log.info("Fetching cached data for key: {}", key);
        final Optional<CacheData> cacheData = cacheDataRepository.findById(key);
        if (cacheData.isEmpty()) {
            log.info("Cache miss for key: {}", key);
        }
        return cacheData;
    }

    @Override
    public <T> void cacheData(final String key, final T object) {
        save(new CacheData(key, objectToJsonString(object), null));
    }

    @Override
    public <T> void cacheData(final String key, final T object, final Long timeToLive) {
        save(new CacheData(key, objectToJsonString(object), timeToLive));
    }

    @Override
    public <T> T getCachedDataOrSave(final String key, final Supplier<T> dataSupplier, final Class<T> type) {
        return getCachedDataOrSaveWithTtl(key, dataSupplier, type, null);
    }

    @Override
    public <T> T getCachedDataOrSaveWithTtl(final String key, final Supplier<T> dataSupplier, final Class<T> type,
                                            final Long ttl) {
        return getKey(key)
                .map(CacheData::getValue)
                .map(value -> jsonStringToObject(value, type))
                .orElseGet(() -> {
                    final T data = dataSupplier.get();
                    cacheData(key, data, ttl);
                    return data;
                });
    }

    private <T> T jsonStringToObject(final String content, final Class<T> clazz) {
        try {
            return objectMapper.readValue(content, clazz);
        } catch (JsonProcessingException e) {
            log.error("Error while parsing json string from cache", e);
            return null;
        }
    }

    private <T> String objectToJsonString(final T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error while writing object to json", e);
            return null;
        }
    }
}
