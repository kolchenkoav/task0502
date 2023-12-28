package com.example.service.integrationapp.model;

import com.example.service.integrationapp.entity.DatabaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("EntityModel")
public class EntityModel implements Serializable {
    private UUID id;
    private String name;
    private Instant date;

    public static EntityModel from(DatabaseEntity entity) {
        return new EntityModel(entity.getId(), entity.getName(), entity.getDate());
    }
}
