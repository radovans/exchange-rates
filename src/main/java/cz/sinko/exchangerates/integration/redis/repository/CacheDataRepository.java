package cz.sinko.exchangerates.integration.redis.repository;

import cz.sinko.exchangerates.service.dto.CacheData;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link CacheData} entity.
 *
 * @author Radovan Šinko
 */
@Repository
public interface CacheDataRepository extends KeyValueRepository<CacheData, String> {

}
