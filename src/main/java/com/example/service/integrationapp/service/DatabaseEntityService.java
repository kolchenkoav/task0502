package com.example.service.integrationapp.service;

import com.example.service.integrationapp.configuration.properties.AppCacheProperties;
import com.example.service.integrationapp.entity.DatabaseEntity;
import com.example.service.integrationapp.repository.DatabaseEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheManager = "redisCacheManager")
public class DatabaseEntityService {
    private final DatabaseEntityRepository repository;

    @Cacheable(AppCacheProperties.CacheNames.DATABASE_ENTITIES)
    public List<DatabaseEntity> findAll() {
        log.info("findAll");
        return repository.findAll();
    }

    @Cacheable(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_ID, key = "#id")
    public DatabaseEntity findById(UUID id) {
        log.info("findById {}", id);
        return repository.findById(id).orElseThrow();
    }

    @Cacheable(AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_NAME)
    public DatabaseEntity findByName(String name) {
        DatabaseEntity probe = new DatabaseEntity();
        probe.setName(name);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "date");
        Example<DatabaseEntity> example = Example.of(probe, matcher);
        log.info("findByName {}", name);
        return repository.findOne(example).orElseThrow();
    }

    @CacheEvict(value = "databaseEntities", allEntries = true)
    public DatabaseEntity create(DatabaseEntity entity) {
        DatabaseEntity forSave = new DatabaseEntity();
        forSave.setName(entity.getName());
        forSave.setDate(entity.getDate());
        return repository.save(forSave);
    }

    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_ID, key = "#id", beforeInvocation = true)
    public DatabaseEntity update(UUID id, DatabaseEntity entity) {
        DatabaseEntity entityForUpdate = findById(id);

        entityForUpdate.setName(entity.getName());
        entityForUpdate.setDate(entity.getDate());
        return repository.save(entityForUpdate);
    }

    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_ID, key = "#id", beforeInvocation = true)
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

}
