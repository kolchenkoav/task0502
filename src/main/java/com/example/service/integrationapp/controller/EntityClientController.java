package com.example.service.integrationapp.controller;

import com.example.service.integrationapp.model.EntityModel;
import com.example.service.integrationapp.clients.OpenFeignClient;
import com.example.service.integrationapp.entity.DatabaseEntity;
import com.example.service.integrationapp.model.UpsertEntityRequest;
import com.example.service.integrationapp.service.DatabaseEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/client/entity")
@RequiredArgsConstructor
public class EntityClientController {
    private final OpenFeignClient client;
    private final DatabaseEntityService service;

    @GetMapping
    public ResponseEntity<List<EntityModel>> entityList() {
        return ResponseEntity.ok(
                service.findAll().stream().map(EntityModel::from).toList()
        );
    }

    @GetMapping("/{name}")
    public ResponseEntity<EntityModel> entityByName(@PathVariable String name) {
        return ResponseEntity.ok(
                EntityModel.from(service.findByName(name))
        );
    }

    @PostMapping
    public ResponseEntity<EntityModel> createEntity(@RequestBody UpsertEntityRequest request) {
        var newEntity = client.createEntity(request);
        var savedEntity = service.create(DatabaseEntity.from(newEntity));
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.from(savedEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel> updateEntity(@PathVariable UUID id, @RequestBody UpsertEntityRequest request) {
        var updatedEntity = client.updateEntity(id, request);
        var updatedDbEntity = service.update(id, DatabaseEntity.from(updatedEntity));
        return ResponseEntity.ok(EntityModel.from(updatedDbEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel> deleteEntityById(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
