package app.funfinder.roomservice.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisCache {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void save(String name, Object obj) throws JsonProcessingException {
        String objString = new ObjectMapper().writeValueAsString(obj);
        redisTemplate.opsForValue().set(name, objString);
    }

    public <T> T findById(String id, Class<T> clazz) throws JsonProcessingException {
        String objString = redisTemplate.opsForValue().get(id);
        return new ObjectMapper().readValue(objString, clazz);
    }
}
