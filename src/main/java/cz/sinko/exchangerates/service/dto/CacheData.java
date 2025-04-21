package cz.sinko.exchangerates.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

/**
 * Cache data entity.
 *
 * @author Radovan Šinko
 */
@AllArgsConstructor
@Getter
@RedisHash("data")
@ToString
public class CacheData {

    @Id
    private String key;

    private String value;

    @TimeToLive
    private Long expirationInSeconds;
}
